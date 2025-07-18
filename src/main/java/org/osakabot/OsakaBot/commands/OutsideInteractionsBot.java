package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osakabot.OsakaBot.backend.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.List;

public class OutsideInteractionsBot extends ListenerAdapter implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutsideInteractionsBot.class);
    private static final String SERVER_URL = "http://localhost:4005/startDiscord";
    private static final String SERVER_SECRET = System.getenv("serverSecret");
    private final OkHttpClient http = new OkHttpClient();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (!event.getName().equals("server")) return;

        switch (event.getSubcommandName()) {
            case "anso" -> handleAddOperator(event);
            case "tso" -> handleTurnServerOn(event);
            case "players" -> handleGetListofPlayers(event);
            default -> event.reply("Unknown sub-command.").setEphemeral(true).queue();
        }
    }

    private void handleAddOperator(SlashCommandInteractionEvent event) {
        User invoker = event.getUser();
        User target = event.getOption("target").getAsUser();

        JSONObject json = new JSONObject()
                .put("invokerId", invoker.getId())
                .put("targetId", target.getId())
                .put("password", SERVER_SECRET);

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("http://localhost:4005/toggleOperator")
                .post(body)
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = http.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "{}";
            JSONObject replyJson = new JSONObject(responseBody);

            String replyMessage = replyJson.optString("message", "No response from server.");
            event.reply(replyMessage).queue();

        } catch (Exception e) {
            LOGGER.error("Error toggling operator", e);
            event.reply("❌ Server error: `" + e.getMessage() + "`").queue();
        }
    }

    private void handleGetListofPlayers(SlashCommandInteractionEvent event) {
        OkHttpClient http = new OkHttpClient();

        HttpUrl url = HttpUrl.parse("http://localhost:4005/listPlayers").newBuilder()
                .addQueryParameter("password", SERVER_SECRET)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json")
                .build();

        http.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                event.reply("❌ Couldn't get player list: " + e.getMessage()).queue();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body() != null ? response.body().string() : "{}";
                JSONObject json = new JSONObject(responseBody);

                if (!json.optBoolean("success", false)) {
                    event.reply("⚠️ Failed: " + json.optString("message", "Unknown error")).queue();
                    return;
                }
                LOGGER.error(json.toString());
                JSONArray players = json.optJSONArray("players");
                if (players == null || players.isEmpty()) {
                    event.reply("📭 No players online.").queue();
                    return;
                }

                StringBuilder msg = new StringBuilder("🧍 Players online:\n");
                for (int i = 0; i < players.length(); i++) {
                    msg.append("• ").append(players.getString(i)).append("\n");
                }

                event.reply(msg.toString()).queue();
            }
        }); //TODO: make code less unreadable
    }

    private void handleTurnServerOn(SlashCommandInteractionEvent event) {
        User user = event.getUser();

        try {
            String jsonBody = new JSONObject()
                    .put("userId", user.getId())
                    .put("username", user.getName())
                    .put("password", SERVER_SECRET)
                    .toString();

            RequestBody body = RequestBody.create(
                    jsonBody,
                    MediaType.get("application/json; charset=utf-8")
            );

            Request request = new Request.Builder()
                    .url(SERVER_URL)
                    .post(body)
                    .addHeader("Accept", "application/json")
                    .build();

            try (Response response = http.newCall(request).execute()) {
                String respText = response.body() != null ? response.body().string()
                        : "{\"message\":\"<no body>\"}";
                event.reply(mapServerMessage(respText)).queue();
            }

        } catch (Exception e) {
            LOGGER.error("tso error", e);
            event.reply("❌ Something went wrong: `" + e.getMessage() + "`").queue();
        }
    }

    private String mapServerMessage(String jsonText) {
        try {
            JSONObject json = new JSONObject(jsonText);
            boolean authorized = json.optBoolean("authorized", false);
            boolean success = json.optBoolean("success", false);
            String message = json.optString("message", "");

            if (!authorized && message.contains("Add As Operator"))
                return "❌ You are not an authorized operator!";
            if (!authorized && message.contains("Wrong Password"))
                return "🔐 Incorrect server password.";
            if (!success && message.contains("recently started"))
                return "🕒 Please wait before starting the server again.";
            if (authorized && success)
                return "✅ Server is starting up!";

            return "⚠️ " + message;
        } catch (Exception e) {
            return "⚠️ Unexpected server response: `" + jsonText + "`";
        }
    }

    private void sillyMarketNotification() {

    }



    @Override
    public void onSuccess() {
        LOGGER.debug("{} Successful!", getClass());
    }

    public void onFailure(String errorMessage, TextChannel channel) {
        LOGGER.debug("Error in InformationBot\n\n{}", errorMessage);
        channel.sendMessage("Command Failed. Shape up man.").queue();
    }

    @Override
    public boolean isFailed() {
        LOGGER.error("InformationBot Failed!");
        return false;
    }

    @Override
    public String getCommandBody(Message message) {
        return message.getContentRaw().replaceFirst(getIdentifier(), "").replaceAll(" ", "");
    }

    @Override
    public String getIdentifier() {
        return "info";
    }

    @Override
    public String display(Message message) {
        return message.getContentRaw();
    }

    @Override
    public String getDescription(boolean isSlash) {
        return "Gets your information about any guild that you want. Stuff like owners, etc.";
    }

    @Override
    public List<Role> permissionsList() {
        //List<Role> roles = guild.getRoles();
        //roles.removeIf(role -> !channel.getPermissionOverride(role).getAllowed().contains(Permission.MESSAGE_HISTORY));
        //return roles;
        return null;
    }
}

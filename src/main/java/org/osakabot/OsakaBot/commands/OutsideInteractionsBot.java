package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.json.JSONObject;
import org.osakabot.OsakaBot.backend.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

public class OutsideInteractionsBot extends ListenerAdapter implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutsideInteractionsBot.class);
    private static final String SERVER_URL = "http://localhost:4005/startDiscord";
    private static final String SERVER_SECRET = System.getenv("serverSecret");

    public OutsideInteractionsBot() {
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("server")) {
            String action = event.getOptions().get(0).getAsString();
            LOGGER.error(event.getOptions().toString());
            if (action.equals("anso")) {
                event.reply("Please provide the username to add as server operator.").queue();
            } else if (action.equals("tso")) {
                User user = event.getUser();

                try {
                    okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();

                    String jsonBody = String.format(
                            "{\"userId\":\"%s\",\"username\":\"%s\",\"password\":\"%s\"}",
                            user.getId(),
                            user.getName(),
                            SERVER_SECRET
                    );

                    LOGGER.debug(jsonBody);

                    okhttp3.RequestBody body = okhttp3.RequestBody.create(
                            jsonBody,
                            okhttp3.MediaType.get("application/json; charset=utf-8")
                    );

                    okhttp3.Request request = new okhttp3.Request.Builder()
                            .url(SERVER_URL)
                            .post(body)
                            .addHeader("Accept", "application/json")
                            .build();

                    okhttp3.Response response = client.newCall(request).execute();

                    String responseText = response.body() != null
                            ? response.body().string()
                            : "{\"message\": \"<no response>\"}";

                    String finalReply;

                    try {
                        JSONObject json = new JSONObject(responseText);
                        String message = json.optString("message", "No message.");
                        boolean authorized = json.optBoolean("authorized", false);
                        boolean success = json.optBoolean("success", false);

                        // Friendly user-facing message mapping
                        if (!authorized && message.contains("Add As Operator")) {
                            finalReply = "❌ Sorry, you're not an authorized operator!";
                        } else if (!authorized && message.contains("Wrong Password")) {
                            finalReply = "🔐 Incorrect server password.";
                        } else if (!success && message.contains("recently started")) {
                            finalReply = "🕒 Please wait a bit before starting the server again.";
                        } else if (authorized && success) {
                            finalReply = "✅ Server is starting up!";
                        } else {
                            finalReply = "⚠️ Something went wrong: `" + message + "`";
                        }

                    } catch (Exception e) {
                        LOGGER.error(e.getMessage());
                        finalReply = "⚠️ Unexpected server response: `" + responseText + "`";
                    }

                    event.reply(finalReply).queue();
                } catch (Exception e) {
                    LOGGER.error(e.getMessage());
                    event.reply("Something went wrong: `" + e.getMessage() + "`").queue();
                }
            } else {
                event.reply("...what?").setEphemeral(true).queue();
            }
        }
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

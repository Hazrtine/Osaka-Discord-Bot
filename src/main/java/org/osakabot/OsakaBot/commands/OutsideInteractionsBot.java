package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.osakabot.OsakaBot.backend.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class OutsideInteractionsBot extends ListenerAdapter implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutsideInteractionsBot.class);
    private static final String SERVER_URL = "localhost";
    private static final String SERVER_SECRET = System.getenv("serverSecret");

    public OutsideInteractionsBot() {
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("server")) {
            String action = event.getOption(event.getOptions().get(0).getAsString()).getAsString();
            if (action.equals("anso")) {
                event.reply("Please provide the username to add as server operator.").queue();
            } else if (action.equals("tso")) {
                event.reply("Turning the server on... 🔌🖥️").queue();
                User user = event.getUser();

                try {
                    String jsonBody = String.format(
                            "{\"userId\": %s, \"username\": \"%s\", \"password\": \"%s\"}",
                            user.getId(),
                            user.getName(),
                            SERVER_SECRET
                    );

                    URL url = new URL(SERVER_URL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json; utf-8");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);

                    try (OutputStream os = conn.getOutputStream()) {
                        byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                        os.write(input, 0, input.length);
                    }

                    int code = conn.getResponseCode();
                    InputStream responseStream = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();

                    String response;
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(responseStream, StandardCharsets.UTF_8))) {
                        StringBuilder responseBuilder = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            responseBuilder.append(line.trim());
                        }
                        response = responseBuilder.toString();
                    }

                    event.reply("Server response: `" + response + "`").queue();

                } catch (Exception e) {
                    e.printStackTrace();
                    event.reply("Something went wrong: `" + e.getMessage() + "`").setEphemeral(true).queue();
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

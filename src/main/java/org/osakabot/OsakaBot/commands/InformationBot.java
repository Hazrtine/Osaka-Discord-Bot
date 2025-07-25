package org.osakabot.OsakaBot.commands;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.osakabot.OsakaBot.backend.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class InformationBot extends ListenerAdapter implements Command {

    private static final Map<Long, CompletableFuture<String>> awaitingResponse = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(InformationBot.class);
    private static final String URL = "https://sillypost.net/games/sillyexchange";
    private static final String COOKIE = "lax_token=6063b4b0-2aa7-4f2c-af35-c3e2ad8ecea5; token=6063b4b0-2aa7-4f2c-af35-c3e2ad8ecea5";

    public InformationBot() {
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) { //what... the hell is this code? im looking at this round a year or two later and this just looks horrid! ughhhh!
        if (event.getName().equals("info")) {
            informationAboutOsaka(event);
        } else if (event.getName().equals("status")) {
            OptionMapping choiceOpt = event.getOption(event.getOptions().get(0).getAsString()); //expand this pattern to the rest of this godawful code
            String choice = choiceOpt.getAsString();
            event.deferReply().queue(hook -> {
                try {
                    HttpClient client = HttpClient.newHttpClient();
                    HttpRequest request = HttpRequest.newBuilder()
                            .uri(URI.create("https://api.mcsrvstat.us/2/" + choice + ".hazrtine.construction"))
                            .GET()
                            .build();

                    HttpResponse<String> resp = client.send(request, HttpResponse.BodyHandlers.ofString());

                    if (resp.statusCode() != 200) {
                        LOGGER.error("{}: FAILURE. BODY AS FOLLOWS:\n\n{}", resp.statusCode(), resp.body());
                    }

                    JsonNode root = new ObjectMapper().readTree(resp.body());
                    boolean isOnline = root.path("online").asBoolean(false);
                    int playersOnline = root.path("players").path("online").asInt();

                    hook.sendMessage(
                            "The " + choice + " server is " + (isOnline ? "online" : "offline")
                                    + " with " + playersOnline + " playing!"
                    ).queue();
                } catch (Exception e) {
                    LOGGER.error("Server status failure: {}", e.getMessage());
                }
            });
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) throws IllegalStateException {
        String message = event.getMessage().getContentRaw();
        Guild guild = event.getGuild();
        TextChannel channel = event.getChannel().asTextChannel(); //osaka can't really respond to NewsChannels, and im fine with that
        User user = event.getAuthor();
        if (user.isBot())
            return;

        //it's just here in case i want to do something with it.
    }

    public void informationAboutOsaka(SlashCommandInteractionEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("SAATAA ANDAGIIII");
        embed.setDescription("I... here.");
        embed.setColor(Color.BLUE); // You can set the color you want

        event.replyEmbeds(embed.build()).queue();
    }

    public static void futureMessage(MessageChannel channel, String message, long userId) {
        // Check if this user is awaiting a response
        if (awaitingResponse.containsKey(userId)) {
            CompletableFuture<String> future = awaitingResponse.get(userId);
            future.complete(message);
            awaitingResponse.remove(userId);
            LOGGER.debug(message);
        }

        awaitingResponse.get(userId).orTimeout(30, TimeUnit.SECONDS).whenComplete((response, throwable) -> {
            channel.sendMessage("You took too long to respond. Please try again.").queue();
            awaitingResponse.remove(userId);
        });
    }

    public void start(JDA jda, String channelId) {
        Timer timer = new Timer();
        timer.schedule(new PollTask(jda, channelId), 0, 10 * 60 * 900); // 9 minutes
    }

    static class PollTask extends TimerTask {
        private final JDA jda;
        private final String channelId;

        public PollTask(JDA jda, String channelId) {
            this.jda = jda;
            this.channelId = channelId;
        }

        @Override
        public void run() {
            try {
                HttpClient client = HttpClient.newHttpClient();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(URL))
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .header("Cookie", COOKIE)
                        .header("User-Agent", "Java SillyPoller/1.0")
                        .build();

                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                String body = response.body();
                int beans = Integer.parseInt(body.replaceAll("\\D", ""));


                TextChannel channel = jda.getTextChannelById(channelId);

                    if (beans < 20)
                        channel.sendMessage("Tha market is crashin' y'all! It's at **" + beans + "** beans per silly!!!").queue();
                    else if (beans > 190)
                        channel.sendMessage("Tha market has never been better y'all! It's at **" + beans + "** beans per silly!!!").queue();
            } catch (Exception e) {
                System.err.println("Polling failed: " + e.getMessage());
            }
        }
    }


    @Override
    public void onSuccess() {
        LOGGER.debug("HelperBot Successful!");
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
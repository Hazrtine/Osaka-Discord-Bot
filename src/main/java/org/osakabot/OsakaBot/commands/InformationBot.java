package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.osakabot.OsakaBot.Osaka;
import org.osakabot.OsakaBot.backend.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class InformationBot implements Command {

    private static Map<Long, CompletableFuture<String>> awaitingResponse;

    private static final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);

    public InformationBot(MessageReceivedEvent event) {
        onMessageReceived(event);
    }


    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();

        if (message.getContentRaw().contains("!info"))
            messageResponse(event);
    }

    public void messageResponse(MessageReceivedEvent event) {
        event.getChannel().sendMessage("What information are you lookin' for?\nSay Guild for Guild stuff\nSay User for info about other people\nSay Osaka for information about me!").queue();
        String requestedInfo = event.getMessage().getContentRaw();

        switch (requestedInfo.toLowerCase()) {
            case "guild":
                informationAboutGuild(event);
            case "user":
                informationAboutUser(event);
            case "osaka", "osaker":
        }       informationAboutOsaka(event);
    }

    public void informationAboutGuild(MessageReceivedEvent event) {
        TextChannel channel = event.getChannel().asTextChannel();

    }

    public void informationAboutUser(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Who're you talkin' about?").queue();
        //just get the next message that it
    }

    public void informationAboutOsaka(MessageReceivedEvent event) {
        //SAAATA ANDAGIIIIIIIII
    }

    public static void futureMessage(MessageReceivedEvent event) {
        MessageChannel channel = event.getChannel();
        String message = event.getMessage().getContentRaw();
        long userId = event.getAuthor().getIdLong();

        // Check if this user is awaiting a response
        if (awaitingResponse.containsKey(userId)) {
            CompletableFuture<String> future = awaitingResponse.get(userId);
            future.complete(message);
            awaitingResponse.remove(userId);
            return;
        }

        // If the user sends the "!info" command
        if (message.equalsIgnoreCase("!info")) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Information Request")
                    .setDescription("What kind of info do you want?")
                    .setColor(Color.BLUE);

            channel.sendMessageEmbeds(embedBuilder.build()).queue();

            CompletableFuture<String> responseFuture = new CompletableFuture<>();
            awaitingResponse.put(userId, responseFuture);

            responseFuture.orTimeout(30, TimeUnit.SECONDS).whenComplete((response, throwable) -> {
                if (throwable != null) {
                    channel.sendMessage("You took too long to respond. Please try again.").queue();
                } else {
                    handleUserResponse(channel, response);
                }
                awaitingResponse.remove(userId);
            });
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
    public String getDescription() {
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

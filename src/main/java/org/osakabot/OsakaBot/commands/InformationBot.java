package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.osakabot.OsakaBot.Osaka;
import org.osakabot.OsakaBot.backend.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class InformationBot extends ListenerAdapter implements Command {

    private static final Map<Long, CompletableFuture<String>> awaitingResponse = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);

    public InformationBot() {}

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        if (message.getContentRaw().equalsIgnoreCase("!info")) {
            if (awaitingResponse.isEmpty()) {
                switch (message.getContentRaw().toLowerCase()) {
                    case "guild":
                        informationAboutGuild(event);
                        awaitingResponse.put(message.getAuthor().getIdLong(), new CompletableFuture<>());
                        LOGGER.debug("this is the awaiting fellers, {}", awaitingResponse);
                    case "user":
                        informationAboutUser(event);
                    case "osaka", "osaker":
                        informationAboutOsaka(event);
                }
            } else {
                event.getChannel().sendMessage("What information are you lookin' for?\nSay Guild for Guild stuff\nSay User for info about other people\nSay Osaka for information about me!").queue();
            }
        }
    }

    public void informationAboutGuild(MessageReceivedEvent event) {
        TextChannel channel = event.getChannel().asTextChannel();

    }

    public void informationAboutUser(MessageReceivedEvent event) {
        event.getChannel().sendMessage("Who're you talkin' about?").queue();
        futureMessage(event);
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
            LOGGER.debug(message);
        }

        awaitingResponse.get(userId).orTimeout(30, TimeUnit.SECONDS).whenComplete((response, throwable) -> {
            channel.sendMessage("You took too long to respond. Please try again.").queue();
            awaitingResponse.remove(userId);
        });
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

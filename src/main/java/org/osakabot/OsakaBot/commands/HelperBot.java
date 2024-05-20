package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.osakabot.OsakaBot.Osaka;
import org.osakabot.OsakaBot.backend.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.lang.reflect.Constructor;
import java.util.List;

public class HelperBot extends ListenerAdapter implements Command {
    private final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        TextChannel channel = event.getChannel().asTextChannel();
        Message message = event.getMessage();
        Command command = new GenericCommand();
        try {
            command = (Command) Class.forName(getCommandBody(message)).getDeclaredConstructor(new Class[]{String.class}).newInstance();
        } catch (Exception e) {
            if (!getCommandBody(message).contains("help")) {
                LOGGER.error("No Such Command", e);
                return;
            }
            else
                getDescription();
        }
        if (event.getMessage().getContentRaw().startsWith("!help")) {
            if (getCommandBody(message).replaceAll(" ", "").length() > 5) {
                event.getChannel().sendTyping().queue();
                getDescription();
            } else {
                event.getChannel().sendTyping().queue();
                helpSpecific(event, command);
            }
        }
    }

    private void helpSpecific(MessageReceivedEvent event, Command command) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Command " + command.getIdentifier() + ":");
        String descriptionFormat = command.getDescription();
        String descriptionText = String.format(descriptionFormat);
        embedBuilder.setDescription(descriptionText);
    }

    @Override
    public void onSuccess() {
        LOGGER.debug("HelperBot Successful!");
    }

    @Override
    public void onFailure(String errorMessage, TextChannel channel) {
        LOGGER.error("HelperBot error. \n\n{}", errorMessage);
        channel.sendMessage("Command Failed. Shape up man.").queue();
        //i should make this mention me since this means there's a pretty bad error
    }

    @Override
    public boolean isFailed() {
        return false;
    }

    @Override
    public String getCommandBody(Message message) {
        return message.getContentRaw().replaceFirst(getIdentifier(), "").replaceAll(" ", "");
    }

    @Override
    public String getIdentifier() {
        return "help";
    }

    @Override
    public String display(Message message) {
        return message.getContentRaw();
    }

    @Override
    public String getDescription() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Osaka Bot");
        String descriptionFormat = "bawls"; //CHECK THIS
        String descriptionText = String.format(descriptionFormat);
        embedBuilder.setDescription(descriptionText);
        embedBuilder.setColor(Color.RED);

        return descriptionText;
    }

    @Override
    public List<Role> permissionsList() {
        //List<Role> roles = guild.getRoles();
        //roles.removeIf(role -> !channel.getPermissionOverride(role).getAllowed().contains(Permission.MESSAGE_HISTORY) && !channel.getPermissionOverride(role).getAllowed().contains(Permission.MESSAGE_SEND));
        //return roles;
        return null; //check for a different way of doing this, probably GenericCommand.java having it's own way of getting the roles idk
    }
}

/*
if (getCommandBody(message).equals("play"))
            command = new PlayBot();
        else if (getCommandBody(message).equals("help"))
            command = new HelperBot();
        else if (getCommandBody(message).equals("info") || getCommandBody(message).equals("information"))
            command = new InformationBot();
        else {
                onFailure("No Such Command Exists", channel);
                return;
            }
 */
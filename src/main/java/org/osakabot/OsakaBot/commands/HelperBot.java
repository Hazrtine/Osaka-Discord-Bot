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
import java.util.List;

public class HelperBot extends ListenerAdapter implements Command {
    private final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);
    private Guild guild;
    private TextChannel channel;
    private Message message;
    private Command command;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        guild = event.getGuild();
        channel = event.getChannel().asTextChannel();
        message = event.getMessage();
        while (command != null) { //this is an * immensely *  stupid solution but i have to start testing
            if (getCommandBody().equals("play"))
                command = new PlayBot();
            else if (getCommandBody().equals("help"))
                command = new HelperBot();
            else if (getCommandBody().equals("info") || getCommandBody().equals("information"))
                command = new InformationBot();
            else {
                onFailure("No Such Command Exists");
                return;
            }
        }
        if (event.getMessage().getContentRaw().startsWith("!help")) {
            if (event.getMessage().getContentRaw().replaceAll(" ", "").length() > 5) {
                event.getChannel().sendTyping().queue();
                getDescription();
            } else {
                event.getChannel().sendTyping().queue();
                helpSpecific(event);
            }
        }
    }

    private void helpSpecific(MessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Command " + command.getIdentifier() + ":");
        String descriptionFormat = command.getDescription();
        String descriptionText = String.format(descriptionFormat);
        embedBuilder.setDescription(descriptionText);
    }

    @Override
    public void doRun() throws Exception {
        //uses onMessageReceieved
    }

    @Override
    public void onSuccess() {
        LOGGER.debug("HelperBot Successful!");
    }

    @Override
    public void onFailure(String errorMessage) {
        LOGGER.debug(errorMessage);
        channel.sendMessage("Command Failed. Shape up man.").queue();
        //channel.sendMessage("and uh...").mention(member).queue();
    }

    @Override
    public boolean isFailed() {
        return false;
    }

    @Override
    public String getCommandBody() {
        return message.getContentRaw().replaceFirst(getIdentifier(), "").replaceAll(" ", "");
    }

    @Override
    public String getIdentifier() {
        return "!help";
    }

    @Override
    public String display() {
        return message.getContentRaw();
    }

    @Override
    public String getDescription() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Osaka Bot");
        String descriptionFormat = command.getDescription();
        String descriptionText = String.format(descriptionFormat);
        embedBuilder.setDescription(descriptionText);
        embedBuilder.setColor(Color.RED);

        return "CONSEQUENCES."; //make this the embedBuilder, it should be String-able
    }

    @Override
    public List<Role> permissionsList() {
        List<Role> roles = guild.getRoles();
        roles.removeIf(role -> !channel.getPermissionOverride(role).getAllowed().contains(Permission.MESSAGE_HISTORY) && !channel.getPermissionOverride(role).getAllowed().contains(Permission.MESSAGE_SEND));
        return roles;
    }
}
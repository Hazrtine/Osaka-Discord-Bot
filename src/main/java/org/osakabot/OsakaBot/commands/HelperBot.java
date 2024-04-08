package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.osakabot.OsakaBot.Osaka;
import org.osakabot.OsakaBot.backend.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HelperBot extends ListenerAdapter implements Command {
    private final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);
    private Guild guild;
    private TextChannel channel;
    private Message message;
    private Command command = null;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        guild = event.getGuild();
        channel = event.getChannel().asTextChannel();
        message = event.getMessage();
        while (command != null) { //this is an * immensely *  stupid solution but i have to start testing
            if (getCommandBody().equals(""))
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
        String descriptionText = String.format(descriptionFormat, prefix, argumentPrefix);
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
    public void onFailure() {
        LOGGER.debug("HelperBot Failed.");
        channel.sendMessage("That command doesn't exist buddy.").queue();
    }

    @Override
    public boolean isFailed() {
        return false;
    }

    @Override
    public void setFailed(boolean failed) {

    }

    @Override
    public String getCommandBody() {
        return message.getContentRaw().replaceFirst("!help", "");
    }

    @Override
    public String getIdentifier() {
        return "Help";
    }

    @Override
    public String display() {
        return message.getContentRaw();
    }

    @Override
    public void getDescription() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Command " + command.getIdentifier() + ":");
        String descriptionFormat = command.getDescription();
        String descriptionText = String.format(descriptionFormat, prefix, argumentPrefix);
        embedBuilder.setDescription(descriptionText);
        embedBuilder.setColor(ColorSchemeProperty.getColor());
    }

    @Override
    public List<Role> permissionsList() {
        List<Role> roles = guild.getRoles();
        roles.removeIf(role -> !channel.getPermissionOverride(role).getAllowed().contains(Permission.MESSAGE_HISTORY) && !channel.getPermissionOverride(role).getAllowed().contains(Permission.MESSAGE_SEND));
        return roles;
    }
}
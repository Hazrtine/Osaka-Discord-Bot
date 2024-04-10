package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.osakabot.OsakaBot.Osaka;
import org.osakabot.OsakaBot.backend.Command;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

public class InformationBot extends ListenerAdapter implements Command { //this will be a bot that is able to grab information about certain Guilds

    private static final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);
    private static Guild guild;
    private static TextChannel channel;
    private static Message message;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        guild = event.getGuild();
        channel = event.getChannel().asTextChannel();
        message = event.getMessage();
    }

    @Override
    public void doRun() throws Exception {
        //uses onMessageReceieved
    }

    @Override
    public void onSuccess() {
        LOGGER.debug("HelperBot Successful!");
    }

    public void onFailure(String errorMessage) {
        LOGGER.debug(errorMessage);
        channel.sendMessage("Command Failed. Shape up man.").queue();
        //channel.sendMessage("and uh...").mention(Osaka.getCreator()).queue();
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
        return "Gets your information about any guild that you want. Stuff like owners, etc.";
    }

    @Override
    public List<Role> permissionsList() {
        List<Role> roles = guild.getRoles();
        roles.removeIf(role -> !channel.getPermissionOverride(role).getAllowed().contains(Permission.MESSAGE_HISTORY));
        return roles;
    }
}

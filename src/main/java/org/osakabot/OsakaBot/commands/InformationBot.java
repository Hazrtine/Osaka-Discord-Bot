package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.osakabot.OsakaBot.backend.Command;
import net.dv8tion.jda.api.entities.Guild;
import org.slf4j.Logger;


import java.util.List;

public class InformationBot extends ListenerAdapter implements Command { //this will be a bot that is able to grab information about certain Guilds

    private static final Logger LOGGER =
    private static Guild guild;
    private static TextChannel channel;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        guild = event.getGuild();
        channel = event.getChannel().asTextChannel();
    }

    @Override
    public void doRun() throws Exception {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure() {

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
        return "";
    }

    @Override
    public String getIdentifier() {
        return "";
    }

    @Override
    public String display() {
        return "";
    }

    @Override
    public boolean abort() {
        return false;
    }

    @Override
    public boolean isAborted() {
        return false;
    }

    @Override
    public void getDescription() {

    }

    @Override
    public List<Role> permissionsList() {
        List<Role> roles = guild.getRoles();
        roles.removeIf(role -> !channel.getPermissionOverride(role).getAllowed().contains(Permission.MESSAGE_HISTORY));
        return roles;
    }
}

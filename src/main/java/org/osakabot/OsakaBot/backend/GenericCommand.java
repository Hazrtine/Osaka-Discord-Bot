package org.osakabot.OsakaBot.backend;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.List;

public class GenericCommand implements Command {
    public void doRun() throws Exception {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(String failureMessage, TextChannel channel) {

    }

    @Override
    public boolean isFailed() {
        return false;
    }

    @Override
    public String getCommandBody(Message message) {
        return "";
    }

    @Override
    public String getIdentifier() {
        return "";
    }

    @Override
    public String display(Message message) {
        return "";
    }

    @Override
    public String getDescription(boolean isSlash) {
        return "";
    }

    @Override
    public List<Role> permissionsList() {
        return List.of();
    }
}

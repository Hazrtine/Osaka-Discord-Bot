package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.osakabot.OsakaBot.backend.Command;

import java.util.List;

public class GenericCommand implements Command {
    @Override
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
    public String getDescription(Command command) {
        return "";
    }

    @Override
    public List<Role> permissionsList(Guild guild, TextChannel channel) {
        return List.of();
    }
}

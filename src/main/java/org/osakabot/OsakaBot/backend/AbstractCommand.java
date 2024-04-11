package org.osakabot.OsakaBot.backend;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.osakabot.OsakaBot.Osaka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class AbstractCommand implements Command {

    private final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);

    @Override
    public void doRun() throws Exception {
        //must be overridden
    }

    @Override
    public void onSuccess() {
        LOGGER.debug("HelperBot Successful!");
    }

    @Override
    public void onFailure(String failureMessage) {
        LOGGER.error("ERROR FOR {}\n\n{}", getIdentifier(), failureMessage);
    }

    @Override
    public void onFailure(String failureMessage, TextChannel channel) {
        LOGGER.debug("{} Failed. {}", getIdentifier(), failureMessage);
        channel.sendMessage("That command doesn't exist buddy.").queue();
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
        return "MUST BE OVERRIDDEN. MUST BE OVERRIDDEN.";
    }

    @Override
    public String display(Message message) {
        return message.getContentRaw();
    }

    @Override
    public String getDescription() {
        return "Use this command to play a song";
    }

    @Override
    public List<Role> permissionsList() {
        return List.of();
        //this must be overridden
    }
}

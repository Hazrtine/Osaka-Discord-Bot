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


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        TextChannel channel = event.getChannel().asTextChannel();
        Message message = event.getMessage();

        if (message.getContentRaw().contains("!info"))
            messageResponse(event);
    }

    public void messageResponse(MessageReceivedEvent event) {
        event.getChannel().sendMessage("What information are you lookin' for?\nSay Guild for Guild stuff\nSay User for info about other people\nSay Osaka for information about me!").queue();
        String requestedInfo = event.getMessage().getContentRaw();
        if (requestedInfo.contains("guild") || requestedInfo.contains("Guild"))
            informationAboutGuild(event);
    }

    public void informationAboutGuild(MessageReceivedEvent event) {
        TextChannel channel = event.getChannel().asTextChannel();
        Guild guild = null;
        for (Guild guilde : Osaka.getJDA().getGuilds()) {
            //first check if it was an ID given or a name, two different enhanced for-loops
        }
        //first of all, this bot needs to be having the ability to basically disguise itself as a player. could i make it that when a player speaks in this server
        //it outputs it as text from OsakaBot? interesting
        channel.sendMessage("Guild with name \"" + guild.getName() + "\" not found.").queue();
    }

    public void informationAboutUser(MessageReceivedEvent event) {

    }

    public void informationAboutOsaka(MessageReceivedEvent event) {
        //SAAATA ANDAGIIIIIIIII
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

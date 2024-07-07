package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.osakabot.OsakaBot.Osaka;
import org.osakabot.OsakaBot.backend.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Scanner;

public class PlayBot implements Command {

    private final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);
    private TextChannel channel;

    public PlayBot(MessageReceivedEvent event) {
        onMessageReceived(event);
        LOGGER.debug(event.getMessage().getContentRaw());
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        onEchoCommand(event, event.getGuild(), getCommandBody(event.getMessage()));
        System.out.println("Playing!");
    }

    private void onEchoCommand(MessageReceivedEvent event, Guild guild, String arg)
    {
        boolean isNumber = arg.matches("\\d+");
        VoiceChannel channel = null;
        if (isNumber)
        {
            channel = guild.getVoiceChannelById(arg);
        }
        if (channel == null)
        {
            List<VoiceChannel> channels = guild.getVoiceChannelsByName(arg, true);
            if (!channels.isEmpty())
                channel = channels.get(0);
        }

        MessageChannel messageChannel = event.getChannel();
        if (channel == null)
        {
            onFailure(arg, event.getGuildChannel().asTextChannel());
            return;
        }
        connectTo(channel);
        onConnecting(channel, messageChannel);
    }

    private void connectTo(AudioChannel channel)
    {
        Guild guild = channel.getGuild();
        // Get an audio manager for this guild, this will be created upon first use for each guild
        AudioManager audioManager = guild.getAudioManager();
        // Create our Send/Receive handler for the audio connection
        GuildPlayer handler = new GuildPlayer(guild, channel);

        // The order of the following instructions does not matter!

        // Set the sending handler to our echo system
        audioManager.setSendingHandler(handler);
        // Set the receiving handler to the same echo system, otherwise we can't echo anything
        audioManager.setReceivingHandler(handler);
        // Connect to the voice channel
        audioManager.openAudioConnection(channel);
    }

    private void onConnecting(AudioChannel channel, MessageChannel mc)
    {
        mc.sendMessage("Connecting to " + channel.getName() + "!").queue();
    }

    private void onUnknownChannel(MessageChannel mc, String arg) {
        mc.sendMessage("Can't find " + arg + ". You sure you're not just stupid?").queue();
    }


    public void onReady(ReadyEvent event) {
        //make an announcement of being alive
        }

    @Override
    public void onSuccess() {
        LOGGER.debug("HelperBot Successful!");
    }

    @Override
    public void onFailure(String failureMessage, TextChannel channel) {
        LOGGER.debug("HelperBot Failed.{}", failureMessage);
        channel.sendMessage("whar?").queue();
    }

    @Override
    public boolean isFailed() {
        LOGGER.error("HelperBot Failed!");
        return false;
    }

    @Override
    public String getCommandBody(Message message) {
        return message.getContentRaw().replaceFirst("!help", "").replaceAll(" ", "");

    }

    @Override
    public String getIdentifier() {
        return "play";
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
    }
}
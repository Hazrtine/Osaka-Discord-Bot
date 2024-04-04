package org.osakabot.OsakaBot.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import java.util.List;

public class PingBot extends ListenerAdapter {

    String flag = "test!";
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String authorName = event.getAuthor().getName();
        String messageContent = event.getMessage().getContentRaw();

        if (messageContent.contains(flag) && !event.getAuthor().isBot())
            onEchoCommand(event, event.getGuild(), messageContent.replaceFirst(flag, ""));
        else
            System.out.println("Message from " + authorName + ": " + messageContent.replaceFirst(flag, ""));

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
        if (channel == null)                    // I have no idea what you want mr user
        {
            onUnknownChannel(messageChannel, arg); // Let the user know about our failure
            return;
        }
        connectTo(channel);                     // We found a channel to connect to!
        onConnecting(channel, messageChannel);     // Let the user know, we were successful!
    }

    private void connectTo(AudioChannel channel)
    {
        Guild guild = channel.getGuild();
        // Get an audio manager for this guild, this will be created upon first use for each guild
        AudioManager audioManager = guild.getAudioManager();
        // Create our Send/Receive handler for the audio connection
        EchoHandler handler = new EchoHandler();

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
        event.getJDA().getTextChannels().forEach(channel -> {
            channel.sendMessage("saata andagi hours rn").queue();
        });
    }
}
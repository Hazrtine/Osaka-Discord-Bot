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

public class PlayBot extends ListenerAdapter implements Command {

    //private final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);
    private TextChannel channel;
    private Message message;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println("message receieved.");
        String authorName = event.getAuthor().getName();
        String messageContent = event.getMessage().getContentRaw();
        channel = event.getChannel().asTextChannel();
        message = event.getMessage();

        if (messageContent.equals("!play") && !event.getAuthor().isBot()) {
            onEchoCommand(event, event.getGuild(), getCommandBody());
            System.out.println("Caught!");
        }
        else
            System.out.println("Message from " + authorName + ": " + getCommandBody());

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
                channel = channels.getFirst();
        }

        MessageChannel messageChannel = event.getChannel();
        if (channel == null)
        {
            onFailure(arg);
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
        event.getJDA().getTextChannels().forEach(channel -> {
            channel.sendMessage("saata andagi hours rn").queue();
        });
            Scanner sc = new Scanner(System.in);
            System.out.println("Scanner Start!");
            String message = "";
            while (!message.equals("KILLBOTCONSOLE")) {
                //now sure, i should really not be just straight up using my guild id but this is for like one server i do NOT care
                message = sc.nextLine();
                System.out.println(message);
                channel.sendMessage(message).queue();
            }
    }

    @Override
    public void doRun() throws Exception {
        //uses onMessageReceieved
    }

    @Override
    public void onSuccess() {
        //LOGGER.debug("HelperBot Successful!");
    }

    @Override
    public void onFailure(String failureMessage) {
        //LOGGER.debug("HelperBot Failed.{}", failureMessage);
        channel.sendMessage("That command doesn't exist buddy.").queue();
    }

    @Override
    public boolean isFailed() {
        return false;
    }

    @Override
    public String getCommandBody() {
        return message.getContentRaw().replaceFirst("!help", "").replaceAll(" ", "");
    }

    @Override
    public String getIdentifier() {
        return "!play";
    }

    @Override
    public String display() {
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
package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GuildPlayer implements AudioSendHandler, AudioReceiveHandler {

    private final Queue<byte[]> queue = new ConcurrentLinkedQueue<>();
    private final AudioManager audioManager;
    private final AudioChannel channel;

    public GuildPlayer(Guild guild, AudioChannel channel) {
        this.channel = channel;
        audioManager = guild.getAudioManager();
        playAudio();
    }

    //TODO: put the thing to find the actual song that someone wants here

    public void playAudio() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("/resources/UndergroundTesting.mp3"));
            AudioFormat format = new AudioFormat(48000, 16, 2, true, false);
            AudioInputStream convertedInputStream = AudioSystem.getAudioInputStream(format, audioInputStream);
            audioManager.openAudioConnection(channel);
            SourceDataLine line = AudioSystem.getSourceDataLine(format);
            queue.add(audioInputStream.readAllBytes());
            line.open(format);
            line.start();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = convertedInputStream.read(buffer)) != -1) {
                line.write(buffer, 0, bytesRead);
            }

            line.drain();
            line.close();
            convertedInputStream.close();
            audioInputStream.close();
            audioManager.closeAudioConnection();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ByteBuffer provide20MsAudio()
    {
        byte[] data = queue.poll();
        return data == null ? null : ByteBuffer.wrap(data);
    }

    @Override
    public boolean canProvide()
    {
        return true;
    }


    @Override
    public boolean isOpus()
    {
        return true;
    }
}

package org.osakabot.OsakaBot.listeners;

import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EchoHandler implements AudioSendHandler, AudioReceiveHandler {
    private final Queue<byte[]> queue = new ConcurrentLinkedQueue<>();

    public EchoHandler() {
        playAudio();
    }
    public void playAudio() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("UndergroundTesting.mp3"));
            AudioFormat format = new AudioFormat(48000, 16, 2, true, false);
            AudioInputStream convertedInputStream = AudioSystem.getAudioInputStream(format, audioInputStream);
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

package org.osakabot.OsakaBot.backend;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

// 1) Create a monitor
public class ServerStatusMonitor {
    private final JDA jda;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private boolean lastOnline = false;
    private Instant lastChange = Instant.now();

    public ServerStatusMonitor(JDA jda) {
        this.jda = jda;
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::checkAndNotifyTavern, 0, 10, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::checkAndNotifyKyle, 0, 10, TimeUnit.SECONDS);
    }

    private void checkAndNotifyTavern() {
        boolean nowOnline = isServerAcceptingConnections("localhost", 25565, 2_000);

        if (nowOnline != lastOnline) {
            lastOnline = nowOnline;
            lastChange = Instant.now();
            announceTavern(nowOnline);
        }
    }

    private boolean isServerAcceptingConnections(String host, int port, int timeoutMs) {
        try (Socket s = new Socket()) {
            s.connect(new InetSocketAddress(host, port), timeoutMs);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private void announceTavern(boolean online) {
        String emoji = online ? "🟢" : "🔴";
        String text  = online
                ? String.format("%s **Minecraft server is now ONLINE** (at %s)", emoji, lastChange)
                : String.format("%s **Minecraft server is now OFFLINE** (at %s)", emoji, lastChange);

        TextChannel channel = jda.getTextChannelById("1331903557561221200");
        if (channel != null) {
            channel.sendMessage(text).queue();
        } else {
            System.err.println("No channel found with that ID!");
        }
    }

    private void checkAndNotifyKyle() {
        boolean nowOnline = isServerAcceptingConnections("localhost", 6942, 2_000);

        if (nowOnline != lastOnline) {
            lastOnline = nowOnline;
            lastChange = Instant.now();
            announceKyle(nowOnline);
        }
    }

    private void announceKyle(boolean online) {
        String emoji = online ? "🟢" : "🔴";
        String text  = online
                ? String.format("%s **Minecraft server is now ONLINE** (at %s)", emoji, lastChange)
                : String.format("%s **Minecraft server is now OFFLINE** (at %s)", emoji, lastChange);

        TextChannel channel = jda.getTextChannelById("1093702777328390216");
        if (channel != null) {
            channel.sendMessage(text).queue();
        } else {
            System.err.println("No channel found with that ID!");
        }
    }
}


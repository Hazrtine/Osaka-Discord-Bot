package org.osakabot.OsakaBot.backend;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
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
    private boolean lastOnlineTavern = false;
    private boolean lastOnlineKyle = false;
    private Instant lastChange = Instant.now();

    public ServerStatusMonitor(JDA jda) {
        this.jda = jda;
    }

    public static void sendIfStatusChanged(TextChannel channel, String newMessage) {
        if (newMessage.length() > 2000) newMessage = newMessage.substring(0, 1997) + "...";

        boolean isNewOnline = newMessage.toLowerCase().contains("online");
        boolean isNewOffline = newMessage.toLowerCase().contains("offline");

        String finalNewMessage = newMessage;
        channel.getHistory().retrievePast(1).queue(history -> {
            if (!history.isEmpty()) {
                Message last = history.get(0);
                if (last.getAuthor().isBot()) {
                    String lastContent = last.getContentRaw().toLowerCase();
                    boolean wasOnline = lastContent.contains("online");
                    boolean wasOffline = lastContent.contains("offline");

                    if ((isNewOnline && wasOnline) || (isNewOffline && wasOffline)) {
                        return;
                    }
                }
            }

            channel.sendMessage(finalNewMessage).queue();
        }, failure -> {
            System.err.println("Failed to get channel history: " + failure.getMessage());
            channel.sendMessage(finalNewMessage).queue();
        });
    }

    public void start() {
        scheduler.scheduleAtFixedRate(this::checkAndNotifyTavern, 0, 10, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(this::checkAndNotifyKyle, 0, 10, TimeUnit.SECONDS);
    }

    private void checkAndNotifyTavern() {
        boolean nowOnline = isServerAcceptingConnections("localhost", 25565, 2_000);

        if (nowOnline != lastOnlineTavern) {
            lastOnlineTavern = nowOnline;
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
        String text = online ? String.format("%s **Minecraft server is now ONLINE** (at %s)", emoji, lastChange) : String.format("%s **Minecraft server is now OFFLINE** (at %s)", emoji, lastChange);

        TextChannel channel = jda.getTextChannelById("1331903557561221200");
        if (channel != null) {
            sendIfStatusChanged(channel, text);
        } else {
            System.err.println("No channel found with that ID!");
        }
    }

    private void checkAndNotifyKyle() {
        boolean nowOnline = isServerAcceptingConnections("localhost", 6942, 2_000);

        if (nowOnline != lastOnlineKyle) {
            lastOnlineKyle = nowOnline;
            lastChange = Instant.now();
            announceKyle(nowOnline);
        }
    }

    private void announceKyle(boolean online) {
        String emoji = online ? "🟢" : "🔴";
        String text = online ? String.format("%s **Minecraft server is now ONLINE** (at %s)", emoji, lastChange) : String.format("%s **Minecraft server is now OFFLINE** (at %s)", emoji, lastChange);

        TextChannel channel = jda.getTextChannelById("1093702777328390216");
        if (channel != null) {
            sendIfStatusChanged(channel, text);
        } else {
            System.err.println("No channel found with that ID!");
        }
    }
}

//TODO: make this modular
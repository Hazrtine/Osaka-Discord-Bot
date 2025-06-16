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

    // Inject your JDA instance
    public ServerStatusMonitor(JDA jda) {
        this.jda = jda;
    }

    // 2) Start polling
    public void start() {
        // initial delay 0, then every 30 seconds
        scheduler.scheduleAtFixedRate(this::checkAndNotify, 0, 30, TimeUnit.SECONDS);
    }

    private void checkAndNotify() {
        boolean nowOnline = isServerAcceptingConnections("localhost", 25565, 2_000);

        // Only fire on state change
        if (nowOnline != lastOnline) {
            lastOnline = nowOnline;
            lastChange = Instant.now();
            announce(nowOnline);
        }
    }

    // 3) Ping via raw TCP connect (fast & dependency-free)
    private boolean isServerAcceptingConnections(String host, int port, int timeoutMs) {
        try (Socket s = new Socket()) {
            s.connect(new InetSocketAddress(host, port), timeoutMs);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // 4) Send a message to your hard-coded channel
    private void announce(boolean online) {
        String emoji = online ? "🟢" : "🔴";
        String text  = online
                ? String.format("%s **Minecraft server is now ONLINE** (at %s)", emoji, lastChange)
                : String.format("%s **Minecraft server is now OFFLINE** (at %s)", emoji, lastChange);

        TextChannel channel = jda.getTextChannelById("YOUR_TEXT_CHANNEL_ID");
        if (channel != null) {
            channel.sendMessage(text).queue();
        } else {
            System.err.println("No channel found with that ID!");
        }
    }

    public void stop() {
        scheduler.shutdownNow();
    }
}

//change this whole file later
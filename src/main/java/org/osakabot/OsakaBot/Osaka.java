package org.osakabot.OsakaBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.osakabot.OsakaBot.backend.ListenerIntersection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Stream;


public class Osaka {
    private static final String botName = "OsakaBot";
    public static final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);
    private static JDA jda;

    public static void main(String[] args) {
        jda = JDABuilder.createDefault("MTE3MjcxODA5NzEwMzMzOTY2MQ.GoJmdo.4Ee4WCXfAdnZvoXy4LjHYT5lSvawLaNnqZkhxQ")
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new ListenerIntersection())
                .build();
        //TODO: replace with system variable
        LOGGER.debug("Starting Bot!");
        System.out.println("Starting Bot!");
        System.out.println(listOfCommandIdentifiers());
    }

    public static String getBotName() { return botName; }

    public static List<String> listOfCommandIdentifiers() {
        return Stream.of(new File("src/main/java/org/osakabot/OsakaBot/commands").listFiles())
                .filter(f -> f.getName().contains("Bot"))
                .map(f -> {
                    try {
                        return Files.readString(f.toPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(f -> f.substring(f.lastIndexOf("getIdentifier() ") + 35, f.indexOf(";", f.lastIndexOf("getIdentifier()")) - 1))
                .toList();
    }

    public static JDA getJDA() { return jda; }
}

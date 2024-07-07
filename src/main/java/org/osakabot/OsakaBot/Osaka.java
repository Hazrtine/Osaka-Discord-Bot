package org.osakabot.OsakaBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.osakabot.OsakaBot.backend.ListenerIntersection;
import org.osakabot.OsakaBot.commands.InformationBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static java.nio.file.Files.readString;


public class Osaka {
    private static final String botName = "OsakaBot";
    public static final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);
    private static JDA jda;

    public static void main(String[] args) {
        jda = JDABuilder.createDefault(System.getenv("OsakaKey"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new ListenerIntersection())
                .addEventListeners(new InformationBot())
                .build();
        LOGGER.debug("Starting Bot!");
        System.out.println("Starting Bot!");
        System.out.println(listOfCommandIdentifiers());
        registerCommands();
    }

    public static String getBotName() {
        return botName;
    }

    public static List<String> listOfCommandIdentifiers() {
        return Stream.of(Objects.requireNonNull(new File("src/main/java/org/osakabot/OsakaBot/commands").listFiles()))
                .filter(f -> f.getName().contains("Bot"))
                .map(f -> {
                    try {
                        return readString(f.toPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(f -> f.substring(f.lastIndexOf("getIdentifier() ") + 35, f.indexOf(";", f.lastIndexOf("getIdentifier()")) - 1))
                .toList();
    }

    public static JDA getJDA() {
        return jda;
    }

    private static void registerCommands() {
        jda.updateCommands().addCommands(
                Commands.slash("ping", "Ping the bot."),
                Commands.slash("info", "Get Certain Information from the Bot")
                        .addOption(OptionType.STRING, "guild", "Do you want information about a certain guild? Type it in here.")
                        .addOption(OptionType.BOOLEAN, "osaka", "Do you want to know more about the bot?")
                        .addOption(OptionType.USER, "user", "You want to know more about a user? Input their username or their UserID!")
                ).queue();
    }
}
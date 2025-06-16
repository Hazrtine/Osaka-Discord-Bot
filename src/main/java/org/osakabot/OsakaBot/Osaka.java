package org.osakabot.OsakaBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.osakabot.OsakaBot.backend.ListenerIntersection;
import org.osakabot.OsakaBot.commands.FunThings;
import org.osakabot.OsakaBot.commands.InformationBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Osaka {
    private static final String botName = "OsakaBot";
    public static final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);
    private static JDA jda;

    public static void main(String[] args) throws InterruptedException {
        jda = JDABuilder.createDefault(System.getenv("OsakaKey"))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new ListenerIntersection())
                .addEventListeners(new InformationBot())
                .addEventListeners(new FunThings())
                .build();
        jda.awaitReady();
        jda.updateCommands().queue();
        LOGGER.debug("Starting Bot!");
        System.out.println("Starting Bot!");
        registerCommands();
    }

    public static String getBotName() {
        return botName;
    }

    public static JDA getJDA() {
        return jda;
    }

    static OptionData choiceOption = new OptionData(OptionType.STRING, "choice", "Pick A or B", true)
            .addChoice("Tavern", "Tavern")
            .addChoice("Kyle", "Kyle");

    private static void registerCommands() {
        jda.getGuildById("835385439112265759").updateCommands().addCommands(
                Commands.slash("ping", "Ping the bot."),
                Commands.slash("info", "Get Certain Information from the Bot")
                        .addOption(OptionType.STRING, "guild", "Do you want information about a certain guild? Type it in here. NOTE: I have to be in the guild.")
                        .addOption(OptionType.BOOLEAN, "osaka", "Do you want to know more about the bot?")
                        .addOption(OptionType.USER, "user", "You want to know more about a user? Input their username or their UserID!"),
                Commands.slash("help", "Get help from the bot")
                        .addOption(OptionType.STRING, "command", "What command would you like help with?"),
                Commands.slash("status", "Is your minecraft server up?")
                        .addOptions(choiceOption)
                ).queue();
    }
}
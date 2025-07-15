package org.osakabot.OsakaBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.osakabot.OsakaBot.backend.ListenerIntersection;
import org.osakabot.OsakaBot.backend.ServerStatusMonitor;
import org.osakabot.OsakaBot.commands.FunThings;
import org.osakabot.OsakaBot.commands.InformationBot;
import org.osakabot.OsakaBot.commands.OutsideInteractionsBot;
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
                .addEventListeners(new OutsideInteractionsBot())
                .build();
        jda.awaitReady();
        jda.updateCommands().queue();
        LOGGER.debug("Starting Bot!");
        System.out.println("Starting Bot!");
        registerCommands();

        ServerStatusMonitor monitor = new ServerStatusMonitor(jda);
        monitor.start();
    }

    public static String getBotName() {
        return botName;
    }

    public static JDA getJDA() {
        return jda;
    }

    static OptionData statusOptions = new OptionData(OptionType.STRING, "status", "Which server?", true)
            .addChoice("Tavern", "Tavern")
            .addChoice("Kyle", "Kyle");

    private static void registerCommands() {
        jda.getGuildById("1224454602473340958").updateCommands().addCommands(
                Commands.slash("ping", "Ping the bot."),
                Commands.slash("info", "Get Certain Information from the Bot"),
                Commands.slash("help", "Get help from the bot")
                        .addOption(OptionType.STRING, "command", "What command would you like help with?"),
                Commands.slash("status", "Is your minecraft server up?")
                        .addOptions(statusOptions),
                Commands.slash("server", "Server options").addSubcommands(
                        new SubcommandData("tso", "Turn on the server"),
                        new SubcommandData("anso", "Add a user as server operator"),
                        new SubcommandData("players", "Lists the players on the server.")
                                .addOption(OptionType.USER, "target", "User to promote", true)
                )).queue();
    }
}
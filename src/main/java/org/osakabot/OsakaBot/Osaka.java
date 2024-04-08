package org.osakabot.OsakaBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.osakabot.OsakaBot.commands.InformationBot;
import org.osakabot.OsakaBot.commands.PlayBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;

import java.util.Scanner;


public class Osaka {
    private static final String botName = "OsakaBot";
    private static JDA jda;
    public static final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);
    private static final Member haz = jda.getGuildById("835385439112265759").getMemberById("430373875549732874");


    public static void main(String[] arguments) throws Exception {
        jda = JDABuilder.createDefault("MTE3MjcxODA5NzEwMzMzOTY2MQ.GoJmdo.4Ee4WCXfAdnZvoXy4LjHYT5lSvawLaNnqZkhxQ")
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new PlayBot())
                .addEventListeners(new InformationBot())
                .build();
        LoggerFactoryBinder binding = LoggerFactoryBinder.
        //TODO: replace with system variable
        LOGGER.debug("Starting Bot!");
    }

    public static String getBotName() { return botName; }
    public static Member getCreator() { return haz; }

    public static void speakFromConsole() {
        Scanner sc = new Scanner(System.in);
        String message = "";
        while (!message.equals("KILLBOTCONSOLE")) {
            //now sure, i should really not be just straight up using my guild id but this is for like one server i do NOT care
            TextChannel channel = jda.getGuildById("835385439112265759").getTextChannelsByName(sc.nextLine(), true).getFirst();
            message = sc.nextLine();
            channel.sendMessage(message).queue();
        }
    }
}

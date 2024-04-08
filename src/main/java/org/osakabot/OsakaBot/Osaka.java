package org.osakabot.OsakaBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.osakabot.OsakaBot.commands.InformationBot;
import org.osakabot.OsakaBot.commands.PingBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;


public class Osaka {
    private static final String botName = "OsakaBot";
    private static JDA jda;
    public static final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);


    public static void main(String[] arguments) throws Exception
    {
        jda = JDABuilder.createDefault("MTE3MjcxODA5NzEwMzMzOTY2MQ.GoJmdo.4Ee4WCXfAdnZvoXy4LjHYT5lSvawLaNnqZkhxQ")
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new PingBot())
                .addEventListeners(new InformationBot())
                .build();
        //TODO: replace with system variable
        System.out.println("Starting!");
    }

    public static String getBotName() { return botName; }

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

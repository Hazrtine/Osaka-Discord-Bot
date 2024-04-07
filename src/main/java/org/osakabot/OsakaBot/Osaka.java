package org.osakabot.OsakaBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.osakabot.OsakaBot.commands.InformationBot;
import org.osakabot.OsakaBot.commands.PingBot;


public class Osaka {
    private static final String botName = "OsakaBot";

    public static void main(String[] arguments) throws Exception
    {
        JDA api = JDABuilder.createDefault("MTE3MjcxODA5NzEwMzMzOTY2MQ.GoJmdo.4Ee4WCXfAdnZvoXy4LjHYT5lSvawLaNnqZkhxQ")
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new PingBot())
                .addEventListeners(new InformationBot())
                .build();
        //TODO: replace with system variable
        System.out.println("Starting!");
    }

    public static String getBotName() { return botName; }
}

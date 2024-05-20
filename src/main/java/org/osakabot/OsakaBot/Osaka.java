package org.osakabot.OsakaBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.osakabot.OsakaBot.commands.HelperBot;
import org.osakabot.OsakaBot.commands.InformationBot;
import org.osakabot.OsakaBot.commands.PlayBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Osaka {
    private static final String botName = "OsakaBot";
    public static final Logger LOGGER = LoggerFactory.getLogger(Osaka.class.getName());
    private static JDA jda;

    public static void main(String[] arguments) throws Exception {
        jda = JDABuilder.createDefault("MTE3MjcxODA5NzEwMzMzOTY2MQ.GoJmdo.4Ee4WCXfAdnZvoXy4LjHYT5lSvawLaNnqZkhxQ")
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(new PlayBot())
                .addEventListeners(new InformationBot())
                .addEventListeners(new HelperBot())
                .build();
        //TODO: replace with system variable
        LOGGER.debug("Starting Bot!");
        System.out.println("Starting Bot!");
    }

    public static String getBotName() { return botName; }

    public static JDA getJDA() { return jda; }
}

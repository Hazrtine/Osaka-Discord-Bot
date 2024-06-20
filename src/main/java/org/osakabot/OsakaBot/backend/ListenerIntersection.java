package org.osakabot.OsakaBot.backend;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.osakabot.OsakaBot.Osaka;
import org.osakabot.OsakaBot.commands.FunThings;
import org.osakabot.OsakaBot.commands.HelperBot;
import org.osakabot.OsakaBot.commands.InformationBot;
import org.osakabot.OsakaBot.commands.PlayBot;

public class ListenerIntersection extends ListenerAdapter {

    private static MessageReceivedEvent event;

    @Override
    public void onMessageReceived(MessageReceivedEvent evente) {
        event = evente;
        MessagesToConsole(event);
        String message =  event.getMessage().getContentRaw();
        FunThings fT = new FunThings(event);
        if (message.charAt(0) == '!') { //TODO: this sucks, fix this into something dynamic
            if (message.contains("help"))

            }
        }

    private void MessagesToConsole(MessageReceivedEvent event) {
        System.out.println(event.getMessage().getContentRaw() + "\nFrom: " + event.getAuthor().getName());
    }

}


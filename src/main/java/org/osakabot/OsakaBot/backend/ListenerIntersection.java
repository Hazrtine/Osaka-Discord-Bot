package org.osakabot.OsakaBot.backend;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.osakabot.OsakaBot.commands.FunThings;
import org.osakabot.OsakaBot.commands.HelperBot;
import org.osakabot.OsakaBot.commands.InformationBot;
import org.osakabot.OsakaBot.commands.PlayBot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ListenerIntersection extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

    }

    private void MessagesToConsole(MessageReceivedEvent event) {
        System.out.println(event.getMessage().getContentRaw() + "\nFrom: " + event.getAuthor().getName());
    }

}


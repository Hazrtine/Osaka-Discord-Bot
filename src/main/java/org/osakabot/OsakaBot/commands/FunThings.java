package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class FunThings {


    public FunThings(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        switch (message) {
            case "h":
                event.getChannel().sendMessage("h").queue();
            case "i love this bot":
                event.getChannel().sendMessage("https://tenor.com/view/azumanga-osaka-wave-hi-gif-26415274").queue();
        }
    }
}

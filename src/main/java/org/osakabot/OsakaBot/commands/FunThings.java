package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class FunThings {

    private MessageReceivedEvent event;

    public FunThings(MessageReceivedEvent event) {
        this.event = event;
    }
}

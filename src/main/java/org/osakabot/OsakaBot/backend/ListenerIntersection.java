package org.osakabot.OsakaBot.backend;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class ListenerIntersection extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        getAllIdentifiers();

        String message = event.getMessage().getContentRaw();
        if (message.charAt(0) == '!') {

        }
    }

    private void getAllIdentifiers() {

    }
}

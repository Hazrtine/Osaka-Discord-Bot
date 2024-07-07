package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class FunThings extends ListenerAdapter {


    public FunThings(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (!event.getAuthor().isBot())
            switch (message) {
                case "h":
                    event.getChannel().sendMessage("h").queue();
                    break;
                case "i love this bot":
                    event.getChannel().sendMessage("fellow knucklehead").queue();
            }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")) {
            event.getChannel().asTextChannel().sendMessage("Pong!").queue();
        }
    }


}

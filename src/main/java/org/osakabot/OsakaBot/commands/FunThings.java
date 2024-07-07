package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.osakabot.OsakaBot.Osaka;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FunThings extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);

    public FunThings() {

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        LOGGER.debug("FunThings Amirite");
    }


    public FunThings(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        if (!event.getAuthor().isBot())
            switch (message) {
                case "h":
                    event.getChannel().sendMessage("h").queue();
                    break;
                case "i love this bot":
                    event.getChannel().sendMessage("fellow knucklehead").queue(); //it'd be kinda funny to have a database of all the lines that was said in azumanga and just choose from there
            }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")) {
            event.deferReply().queue(); // Tell discord we received the command, send a thinking... message to the user
            event.getHook().sendMessage("Pong!").queue();
        }
    }


}

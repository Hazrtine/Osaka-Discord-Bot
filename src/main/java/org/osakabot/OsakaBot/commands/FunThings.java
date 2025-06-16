package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.osakabot.OsakaBot.Osaka;
import org.osakabot.OsakaBot.backend.AzumangaQuoteGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class FunThings extends ListenerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(FunThings.class);

    public FunThings() {

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        LOGGER.debug("FunThings Amirite");
        String message = event.getMessage().getContentRaw();
        if (!event.getAuthor().isBot())
            switch (message.toLowerCase()) {
                case "h":
                    if ((int) (Math.random() * 3) == 2)
                        event.getChannel().sendMessage("h").queue();
                    break;
                case "i love this bot":
                    event.getChannel().sendMessage(Objects.requireNonNull(AzumangaQuoteGenerator.generate())).queue();
            }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("ping")) {
            event.deferReply().queue();
            event.getHook().sendMessage("Pong!").queue();
        }
    }


}

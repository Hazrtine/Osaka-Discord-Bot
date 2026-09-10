package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.entities.Message;
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
        Message message = event.getMessage();
        if (!event.getAuthor().isBot())
            if (message.getContentRaw().equals("h")) {
                if ((int) (Math.random() * 2) == 1)
                    event.getChannel().sendMessage("h").queue();
            } else if (message.getContentRaw().toLowerCase().contains("i love this bot") || (message.getContentRaw().contains("osaka") && message.getContentRaw().contains("love")) || message.getMentions().isMentioned(event.getJDA().getSelfUser())) {
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

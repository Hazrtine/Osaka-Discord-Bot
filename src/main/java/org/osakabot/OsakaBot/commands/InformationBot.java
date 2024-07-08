package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.osakabot.OsakaBot.Osaka;
import org.osakabot.OsakaBot.backend.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Text;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class InformationBot extends ListenerAdapter implements Command {

    private static final Map<Long, CompletableFuture<String>> awaitingResponse = new HashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);

    public InformationBot() {}

        @Override
        public void onSlashCommandInteraction (SlashCommandInteractionEvent event){
            if (event.getName().equals("info")) {
                Guild guild = event.getOption("guild").getAsChannel().asGuildMessageChannel().getGuild();
                User user = event.getOption("user").getAsUser();
                boolean osaka = event.getOption("osaka").getAsBoolean();

                if (guild.isLoaded())
                    informationAboutGuild(guild, event.getChannel().asTextChannel());
                else if (osaka)
                    informationAboutOsaka(event.getChannel().asTextChannel());
                else if (user.getIdLong() != 0 || user.getId().isBlank())
                    informationAboutUserSlashCommand(event.getChannel().asTextChannel(), user.getIdLong());
                else
                    LOGGER.error("There was nothing inputted in the slash command for /info");
            }
        }

        @Override
        public void onMessageReceived (MessageReceivedEvent event){
            String message = event.getMessage().getContentRaw();
            Guild guild = event.getGuild();
            TextChannel channel = event.getChannel().asTextChannel();
            User user = event.getAuthor();
            if (user.isBot())
                return;
            if (message.equalsIgnoreCase("!info"))
                if (awaitingResponse.isEmpty()) {
                    switch (message) {
                        case "guild":
                            informationAboutGuild(guild, channel);
                            awaitingResponse.put(user.getIdLong(), new CompletableFuture<>());
                            LOGGER.debug("this is the awaiting fellers, {}", awaitingResponse);
                        case "user":
                            informationAboutUser(channel, message,user.getIdLong());
                        case "osaka", "osaker":
                            informationAboutOsaka(channel);
                    }
                } else {
                    channel.sendMessage("What information are you lookin' for?\nSay Guild for Guild stuff\nSay User for info about other people\nSay Osaka for information about me!").queue();
                }
        }

        public void informationAboutGuild (Guild guild, TextChannel channel){

        }

        public void informationAboutUser (TextChannel channel, String message, long userId){
            channel.sendMessage("Who're you talkin' about?").queue();
            futureMessage(channel, message, userId);
        }

    public void informationAboutUserSlashCommand (TextChannel channel, long userId){

    }

        public void informationAboutOsaka (TextChannel channel){
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("SAATAA ANDAGIIII");
            embed.setDescription("Im! and! yheah!");
            embed.setColor(Color.BLUE); // You can set the color you want

            channel.sendMessageEmbeds(embed.build()).queue();
        }

        public static void futureMessage (MessageChannel channel, String message,long userId){
            // Check if this user is awaiting a response
            if (awaitingResponse.containsKey(userId)) {
                CompletableFuture<String> future = awaitingResponse.get(userId);
                future.complete(message);
                awaitingResponse.remove(userId);
                LOGGER.debug(message);
            }

            awaitingResponse.get(userId).orTimeout(30, TimeUnit.SECONDS).whenComplete((response, throwable) -> {
                channel.sendMessage("You took too long to respond. Please try again.").queue();
                awaitingResponse.remove(userId);
            });
        }


        @Override
        public void onSuccess () {
            LOGGER.debug("HelperBot Successful!");
        }

        public void onFailure (String errorMessage, TextChannel channel){
            LOGGER.debug("Error in InformationBot\n\n{}", errorMessage);
            channel.sendMessage("Command Failed. Shape up man.").queue();
        }

        @Override
        public boolean isFailed () {
            LOGGER.error("InformationBot Failed!");
            return false;
        }

        @Override
        public String getCommandBody (Message message){
            return message.getContentRaw().replaceFirst(getIdentifier(), "").replaceAll(" ", "");
        }

        @Override
        public String getIdentifier () {
            return "info";
        }

        @Override
        public String display (Message message){
            return message.getContentRaw();
        }

        @Override
        public String getDescription () {
            return "Gets your information about any guild that you want. Stuff like owners, etc.";
        }

        @Override
        public List<Role> permissionsList () {
            //List<Role> roles = guild.getRoles();
            //roles.removeIf(role -> !channel.getPermissionOverride(role).getAllowed().contains(Permission.MESSAGE_HISTORY));
            //return roles;
            return null;
        }
}
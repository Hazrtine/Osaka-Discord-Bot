package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.osakabot.OsakaBot.Osaka;
import org.osakabot.OsakaBot.backend.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;

public class HelperBot extends ListenerAdapter implements Command {
    private final Logger LOGGER = LoggerFactory.getLogger(Osaka.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        TextChannel channel = event.getChannel().asTextChannel();
        Message message = event.getMessage();
        Command command = new GenericCommand();
        if (event.getMessage().getContentRaw().startsWith("!help")) {
            if (getCommandBody(message).replaceAll(" ", "").length() > 5) { //what is the point of this
                event.getChannel().sendTyping().queue();
                getDescription(false);
            } else {
                event.getChannel().sendTyping().queue();
                helpSpecific(command, channel, hook, false);
            }
        }
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        event.deferReply().queue(hook -> {
            TextChannel channel = event.getChannel().asTextChannel();
            String content = event.getInteraction().getCommandString().replaceAll("/", "");
            if (event.getName().equals("help")) {
                try {
                    helpSpecific(new HelperBot(), channel, hook, true);
                    if (content.equals("info"))
                        helpSpecific(new InformationBot(), channel, hook, true);
                    if (content.equals("play"))
                        helpSpecific(new PlayBot(), channel, hook, true);
                    if (content.equals("piss"))
                        onFailure("willy", channel);
                } catch (Exception e) {
                    helpGeneral(event.getChannel().asTextChannel());
                }
            }
        });
    }

    private void helpSpecific(Command command, TextChannel channel, InteractionHook hook, boolean isSlash) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Command " + command.getIdentifier() + ":");
        String descriptionFormat = command.getDescription(isSlash);
        String descriptionText = String.format(descriptionFormat);
        embedBuilder.setDescription(descriptionText);

        hook.editOriginalEmbeds(embedBuilder.build());
    }

    private void helpGeneral(TextChannel channel) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Bot Commands")
                .setDescription("Here is a list of commands you can use:")
                .setColor(Color.BLUE);

        embedBuilder.addField("help", "Displays this help message.", false);
        embedBuilder.addField("info", "Shows information about a user or guild.\nOptions:\n- `guild`: The guild to get info about.\n- `user`: The user to get info about.\n- `osaka`: A boolean option.", false);
        embedBuilder.addField("play", "Description of another command.", false);

        embedBuilder.setFooter("Use /help [command] for more information about a specific command.");

        channel.sendMessageEmbeds(embedBuilder.build()).queue();
    }

    @Override
    public void onSuccess() {
        LOGGER.debug("HelperBot Successful!");
    }

    @Override
    public void onFailure(String errorMessage, TextChannel channel) {
        LOGGER.error("HelperBot error. \n\n{}", errorMessage);
        channel.sendMessage("Oh no! <@430373875549732874>! Help! It broke!").queue();
        //i should make this mention me since this means there's a pretty bad error
    }

    @Override
    public boolean isFailed() {
        return false;
    }

    @Override
    public String getCommandBody(Message message) {
        return message.getContentRaw().replaceFirst(getIdentifier(), "").replaceAll(" *!*", "");
    }

    @Override
    public String getIdentifier() {
        return "help";
    }

    @Override
    public String display(Message message) {
        return message.getContentRaw();
    }

    @Override
    public String getDescription(boolean isSlash) {
        return "Unused.";
    }

    @Override
    public List<Role> permissionsList() {
        //List<Role> roles = guild.getRoles();
        //roles.removeIf(role -> !channel.getPermissionOverride(role).getAllowed().contains(Permission.MESSAGE_HISTORY) && !channel.getPermissionOverride(role).getAllowed().contains(Permission.MESSAGE_SEND));
        //return roles;
        return null; //check for a different way of doing this, probably GenericCommand.java having it's own way of getting the roles idk
    }
}

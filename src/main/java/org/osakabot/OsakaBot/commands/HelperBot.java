package org.osakabot.OsakaBot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.osakabot.OsakaBot.Osaka;
import org.osakabot.OsakaBot.backend.AbstractCommand;
import org.osakabot.OsakaBot.backend.Command;
import org.osakabot.OsakaBot.paincheckers.AccessConfiguration;

import java.util.List;
import java.util.Optional;

public class HelperBot extends ListenerAdapter implements AbstractCommand {

    private Guild guild;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().startsWith("!help")) {
            if (event.getMessage().getContentRaw().replaceAll(" ", "").length() > 5) {
                event.getChannel().sendTyping().queue();
                showCommandHelp();
            } else {
                event.getChannel().sendTyping().queue();
                helpSpecific(event);
            }
            guild = event.getGuild();
        }
    }

    private void helpSpecific(MessageReceivedEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        Command command = new Command();
        embedBuilder.setTitle("Command " + command.getIdentifier() + ":");
        String descriptionFormat = command.getDescription();
        String descriptionText = String.format(descriptionFormat, prefix, argumentPrefix);
        embedBuilder.setDescription(descriptionText);
    }

    private void showCommandHelp() {
            String botName =  Osaka.getBotName();

            char argumentPrefix = '!';

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Command " + command.getIdentifier() + ":");
            String descriptionFormat = command.getDescription();
            String descriptionText = String.format(descriptionFormat, prefix, argumentPrefix);
            embedBuilder.setDescription(descriptionText);
            Optional<AccessConfiguration> accessConfiguration = Osaka.getSecurityManager().getAccessConfiguration(command.getPermissionTarget(), guild);
            if (accessConfiguration.isPresent()) {
                String title = "Available to roles: ";
                String text;
                List<Role> roles = accessConfiguration.get().getRoles(guild);
                if (!roles.isEmpty()) {
                    text = StringList.create(roles, Role::getName).toSeparatedString(", ");
                } else {
                    text = "Guild owner and administrator roles only";
                }

                embedBuilder.addField(title, text, false);
            }
            ArgumentController argumentController = command.getArgumentController();
            if (argumentController.hasArguments()) {
                embedBuilder.addField("__Arguments__", "Keywords that alter the command behavior or define a search scope.", false);

                argumentController
                        .getArguments()
                        .values()
                        .stream()
                        .sorted(Comparator.comparing(CommandArgument::getIdentifier))
                        .forEach(argument ->
                                embedBuilder.addField(
                                        argumentPrefix + argument.getIdentifier(),
                                        String.format(argument.getDescription(), prefix, argumentPrefix),
                                        false
                                )
                        );
            }

            List<XmlElement> examples = command.getCommandContribution().query(tagName("example")).collect();
            if (!examples.isEmpty()) {
                embedBuilder.addField("__Examples__", "Practical usage examples for this command.", false);
                for (XmlElement example : examples) {
                    String exampleText = String.format(example.getTextContent(), prefix, argumentPrefix);
                    String titleText = String.format(example.getAttribute("title").getValue(), prefix, argumentPrefix);
                    embedBuilder.addField(titleText, exampleText, false);
                }
            }

            embedBuilder.setColor(ColorSchemeProperty.getColor());
        }

    @Override
    public void doRun() throws Exception {

    }

    @Override
    public void onSuccess() {

    }
}
package org.osakabot.OsakaBot.backend;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.osakabot.OsakaBot.commands.HelperBot;

import java.util.List;

/**
 * Interface for all user triggered actions. See {@link AbstractWidgetAction} for commands that are widgets that are interacted with by adding reactions to a message.
 */
public interface Command {

    /**
     * Method called after {@link #doRun()} has run successfully. This is the case when no exception is thrown or,
     * depending on the implementation, other criteria, such as {@link #isFailed()} are met.
     */
    void onSuccess();

    /**
     * Method that is executed when {@link #doRun()} fails, either when an exception is thrown or some other custom
     * condition based in implementation. It is important that this method should not throw an exception, otherwise this
     * exception would override the original exception.
     */
    void onFailure(String failureMessage, TextChannel channel);

    /**
     * @return true to declare the command has failed, this is called after execution and can be used to mark the command
     * as failed even if no exception has been thrown.
     */
    boolean isFailed();

    /**
     * @return the body of the command, for text based commands this is the input following the command name before
     * parsing.
     */
    String getCommandBody(Message message);

    /**
     * @return the identifier, for text based commands this would be the name of the command, of this command. This
     * is relevant to check permissions
     */
    String getIdentifier();

    /**
     * @return a visual representation of this Command. For text based commands this is equal to the raw text entered by
     * the user
     */
    String display(Message message);

    /**
     * Method is to get the description of the actual command for the helpSpecific() method over in {@link HelperBot}
     */
    String getDescription();

    /**
     * @return list of roles this command is available to.
     */
    List<Role> permissionsList();
}
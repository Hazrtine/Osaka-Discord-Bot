package org.osakabot.OsakaBot.paincheckers;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.osakabot.OsakaBot.backend.Command;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class PermissionChecker {

    private Set<Role> listOfRolesApplicable = null;

    public PermissionChecker(Command command) {
        listOfRolesApplicable = Set.copyOf(command.permissionsList());
    }

    public List<Role> permsList(Guild guild) {
        List<Role> roles = guild.getRoles();

        System.out.println("Roles in " + guild.getName() + ":");
        for (Role role : roles) {
            System.out.println(role.getName());
        }
    return roles;
    }

    private List<Role> permissionCheckList(MessageReceivedEvent event) {
        Set<Role> roles = new HashSet<>(permsList(event.getGuild()));
        roles.retainAll(listOfRolesApplicable);
        return roles.stream().toList();
    }

    public boolean permissionCheck(@NotNull MessageReceivedEvent event) {
        List<Role> roles = permsList(event.getGuild());
        Member member = event.getMember();
        List<Role> listCheck = permissionCheckList(event);
        for (Role role : Objects.requireNonNull(member).getRoles()) {
            if (listCheck.contains(role))
                return true;
        }
        return false;
    }
}


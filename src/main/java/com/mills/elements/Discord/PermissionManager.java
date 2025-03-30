package com.mills.elements.Discord;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public class PermissionManager {

    private static final List<String> ALLOWED_ROLE_IDS = List.of(
            "1346548268766593183",
            "1346548268766593182",
            "1346548268766593181"
    );

     public static boolean hasStaffRoles(SlashCommandInteractionEvent e) {
         boolean hasRole = e.getMember().getRoles().stream().map(Role::getId).anyMatch(ALLOWED_ROLE_IDS::contains);

         if (!hasRole) {
             e.reply("You don't have permission to use this command!")
                     .setEphemeral(true)
                     .queue();
             return false;
         }
         return true;
     }
}

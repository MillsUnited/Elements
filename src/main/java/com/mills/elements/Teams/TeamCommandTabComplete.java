package com.mills.elements.Teams;

import com.mills.elements.Main;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TeamCommandTabComplete implements TabCompleter {

    private Main main;
    private TeamManager teamManager;

    public TeamCommandTabComplete(Main main) {
        this.main = main;
        this.teamManager = main.getTeamManager();
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], Arrays.asList("create", "delete", "invite", "kick", "info", "leave", "accept"), new ArrayList<>());
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("invite"))) {
            List<String> availablePlayers = new ArrayList<>();

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

                if (!teamManager.inATeam(onlinePlayer.getUniqueId())) {
                    availablePlayers.add(onlinePlayer.getName());
                }
            }
            return StringUtil.copyPartialMatches(args[1], availablePlayers, new ArrayList<>());
        } else if (args.length == 2 && (args[0].equalsIgnoreCase("kick"))) {
            Player player = (Player) commandSender;
            String teamName = teamManager.getTeamName(player.getUniqueId());
            if (teamName == null) {
                return new ArrayList<>();
            }

            List<String> teamMembers = new ArrayList<>();
            UUID ownerUUID = teamManager.getTeamOwner(teamName);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (teamName.equals(teamManager.getTeamName(onlinePlayer.getUniqueId())) && !onlinePlayer.getUniqueId().equals(ownerUUID)) {
                    teamMembers.add(onlinePlayer.getName());
                }
            }

            for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                if (teamName.equals(teamManager.getTeamName(offlinePlayer.getUniqueId())) && !offlinePlayer.getUniqueId().equals(ownerUUID)) {
                    teamMembers.add(offlinePlayer.getName());
                }
            }

            return StringUtil.copyPartialMatches(args[1], teamMembers, new ArrayList<>());
        }

        return List.of();
    }
}

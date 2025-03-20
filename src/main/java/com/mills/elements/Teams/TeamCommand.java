package com.mills.elements.Teams;

import com.mills.elements.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TeamCommand implements CommandExecutor {

    private Main main;
    private TeamManager teamManager;

    private final Map<UUID, UUID> pendingInvites = new ConcurrentHashMap<>();

    public TeamCommand(Main main) {
        this.main = main;
        this.teamManager = main.getTeamManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;


            if (args.length == 0) {
                player.sendMessage(Main.teamsPrefix + "invalid ussage. /team <command>");
                return false;
            }


            if (args[0].equalsIgnoreCase("create")) {
                UUID ownerUUID = player.getUniqueId();
                if (teamManager.inATeam(ownerUUID)) {
                    player.sendMessage(Main.teamsPrefix + "you are already in a team!");
                    return false;
                }
                String teamName = player.getName() + "'s Team";

                teamManager.createTeam(teamName, ownerUUID);
                player.sendMessage(Main.teamsPrefix + "Created a team!");

            } else if (args[0].equalsIgnoreCase("delete")) {
                UUID playerUUID = player.getUniqueId();

                if (!teamManager.inATeam(playerUUID)) {
                    player.sendMessage(Main.teamsPrefix + "you are not currently in a team!");
                    return false;
                }

                String teamName = teamManager.getTeamName(playerUUID);
                UUID ownerUUID = teamManager.getTeamOwner(teamName);

                if (!playerUUID.equals(ownerUUID)) {
                    player.sendMessage(Main.teamsPrefix + "you are not the team owner!");
                    return true;
                }

                teamManager.removeTeam(teamName);
                player.sendMessage(Main.teamsPrefix + "You have deleted your team!");

            } else if (args[0].equalsIgnoreCase("invite")) {

                if (args.length < 2) {
                    return false;
                }

                UUID playerUUID = player.getUniqueId();

                if (!teamManager.inATeam(playerUUID)) {
                    player.sendMessage(Main.teamsPrefix + "you are not currently in a team!");
                    return false;
                }

                String teamName = teamManager.getTeamName(playerUUID);
                UUID ownerUUID = teamManager.getTeamOwner(teamName);

                if (!playerUUID.equals(ownerUUID)) {
                    player.sendMessage(Main.teamsPrefix + "you are not the team owner!");
                    return true;
                }

                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);

                if (target != null) {

                    if (player.getUniqueId().equals(target.getUniqueId())) {
                        player.sendMessage(Main.teamsPrefix + "You can't add yourself!");
                        return false;
                    }

                    String targetTeamName = teamManager.getTeamName(target.getUniqueId());
                    if (targetTeamName != null && targetTeamName.equals(teamName)) {
                        player.sendMessage(Main.teamsPrefix + target.getName() + " is already a member of your team!");
                        return false;
                    }

                    int teamMemberPlayerCount = teamManager.getTeamMemberCount(teamName);
                    if (teamMemberPlayerCount >= 5) {
                        player.sendMessage(Main.teamsPrefix + "You have reached the max team member count, /team kick <player>!");
                        return false;
                    }

                    pendingInvites.put(target.getUniqueId(), player.getUniqueId());
                    target.sendMessage(Main.teamsPrefix + "You have been invited to join " + teamName + ". Type /team accept to join.");
                    player.sendMessage(Main.teamsPrefix + "Invitation sent to " + target.getName() + ".");
                } else {
                    player.sendMessage(Main.teamsPrefix + "Player not found!");
                }


            } else if (args[0].equalsIgnoreCase("accept")) {
                UUID playerUUID = player.getUniqueId();

                if (playerUUID == null || !pendingInvites.containsKey(playerUUID)) {
                    player.sendMessage(Main.teamsPrefix + "You don't have any pending invitations.");
                    return false;
                }

                UUID inviterUUID = pendingInvites.get(playerUUID);
                String teamName = teamManager.getTeamName(inviterUUID);

                if (teamName == null) {
                    player.sendMessage(Main.teamsPrefix + "This team is no longer available!");
                    return false;
                }

                if (teamManager.inATeam(playerUUID)) {
                    player.sendMessage(Main.teamsPrefix + "You are currently in a team, /team leave!");
                    return false;
                }

                player.sendMessage(Main.teamsPrefix + "You have joined " + teamName + "!");

                Player inviter = Bukkit.getPlayer(inviterUUID);
                if (inviter != null) {
                    inviter.sendMessage(Main.teamsPrefix + player.getName() + " joined your team!");
                }

                teamManager.addTeamMember(playerUUID, teamName);
                pendingInvites.remove(playerUUID);

            } else if (args[0].equalsIgnoreCase("kick")) {

                if (args.length < 2) {
                    return false;
                }

                UUID playerUUID = player.getUniqueId();

                if (!teamManager.inATeam(playerUUID)) {
                    player.sendMessage(Main.teamsPrefix + "You are not currently in a team!");
                    return false;
                }

                String teamName = teamManager.getTeamName(playerUUID);
                UUID ownerUUID = teamManager.getTeamOwner(teamName);

                if (!playerUUID.equals(ownerUUID)) {
                    player.sendMessage(Main.teamsPrefix + "You are not the team owner!");
                    return true;
                }

                String targetName = args[1];
                Player target = Bukkit.getPlayer(targetName);

                if (target != null && target.isOnline()) {
                    // If the target is online
                    if (player.getUniqueId().equals(target.getUniqueId())) {
                        player.sendMessage(Main.teamsPrefix + "You can't remove yourself!");
                        return false;
                    }

                    teamManager.removeTeamMember(target.getUniqueId(), teamName);
                    player.sendMessage(Main.teamsPrefix + "Kicked " + target.getName() + " from the team.");
                    target.sendMessage(Main.teamsPrefix + player.getName() + " kicked you from their team!");

                } else {
                    OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(targetName);

                    if (offlineTarget != null) {
                        String targetTeamName = teamManager.getTeamName(offlineTarget.getUniqueId());
                        if (targetTeamName == null || !targetTeamName.equals(teamName)) {
                            player.sendMessage(Main.teamsPrefix + offlineTarget.getName() + " is not in your team.");
                            return false;
                        }

                        if (player.getUniqueId().equals(offlineTarget.getUniqueId())) {
                            player.sendMessage(Main.teamsPrefix + "You can't remove yourself!");
                            return false;
                        }

                        teamManager.removeTeamMember(offlineTarget.getUniqueId(), teamName);
                        player.sendMessage(Main.teamsPrefix + "Kicked " + offlineTarget.getName() + " from the team.");
                    } else {
                        player.sendMessage(Main.teamsPrefix + "Player not found!");
                    }
                }

            } else if (args[0].equalsIgnoreCase("leave")) {
                UUID playerUUID = player.getUniqueId();

                if (!teamManager.inATeam(playerUUID)) {
                    player.sendMessage(Main.teamsPrefix + "you are not currently in a team!");
                    return false;
                }

                String teamName = teamManager.getTeamName(playerUUID);
                UUID ownerUUID = teamManager.getTeamOwner(teamName);
                Player ownerPlayer = Bukkit.getPlayer(ownerUUID);
                if (player.equals(ownerPlayer)) {
                    player.sendMessage(Main.teamsPrefix + "You cannot leave your own team, /team delete");
                    return true;
                }
                ownerPlayer.sendMessage(Main.teamsPrefix + player.getName() + " just left your team!");
                player.sendMessage(Main.teamsPrefix + "left " + teamName + "!");
                teamManager.removeTeamMember(playerUUID, teamName);

            } else if (args[0].equalsIgnoreCase("info")) {
                UUID playerUUID = player.getUniqueId();

                if (!teamManager.inATeam(playerUUID)) {
                    player.sendMessage(Main.teamsPrefix + "you are not currently in a team!");
                    return false;
                }
                String teamName = teamManager.getTeamName(playerUUID);
                UUID ownerUUID = teamManager.getTeamOwner(teamName);

                player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "Team Info:");
                Player ownerPlayer = Bukkit.getPlayer(ownerUUID);
                String ownerName = ownerPlayer.getName();
                player.sendMessage(ChatColor.YELLOW + "Owner: " + ChatColor.WHITE + ownerName);
                player.sendMessage(ChatColor.YELLOW + "Members:");

                List<UUID> teamMembers = teamManager.getTeamMembers(teamName);
                for (UUID member : teamMembers) {
                    OfflinePlayer memberPlayer = Bukkit.getOfflinePlayer(member);
                    String memberName = memberPlayer.getName() != null ? memberPlayer.getName() : "Unknown";
                    player.sendMessage(ChatColor.YELLOW + "- " + ChatColor.WHITE + memberName);
                }
            } else {
                player.sendMessage(Main.teamsPrefix + "invalid ussage. /team <command>");
            }
        }
        return false;
    }
}

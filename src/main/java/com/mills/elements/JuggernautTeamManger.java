package com.mills.elements;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class JuggernautTeamManger {

    private static final String TEAM_NAME = "glow_red";

    public static void addTeam(Player player) {
        if (player == null) return;

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        Scoreboard scoreboard = manager.getMainScoreboard();
        Team team = scoreboard.getTeam(TEAM_NAME);

        if (team == null) {
            team = scoreboard.registerNewTeam(TEAM_NAME);
            team.setColor(ChatColor.RED);
            team.setPrefix(ChatColor.DARK_RED + "");
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        }

        for (Team t : scoreboard.getTeams()) {
            if (t.hasEntry(player.getName())) {
                t.removeEntry(player.getName());
            }
        }

        team.addEntry(player.getName());

        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 2, 0, false, false, false));
        }, 1L);

        player.setDisplayName(ChatColor.DARK_RED + player.getName());
        player.setPlayerListName(ChatColor.DARK_RED + player.getName());

    }

    public static void removeTeam(Player player) {
        if (player == null) return;

        player.removePotionEffect(PotionEffectType.GLOWING);

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        Scoreboard scoreboard = manager.getMainScoreboard();
        Team team = scoreboard.getTeam(TEAM_NAME);

        if (team != null && team.hasEntry(player.getName())) {
            team.removeEntry(player.getName());
        }

        player.setDisplayName(player.getName());
        player.setPlayerListName(player.getName());
    }

}

package com.mills.elements;

import com.mills.elements.Teams.TeamCommand;
import com.mills.elements.Teams.TeamCommandTabComplete;
import com.mills.elements.Teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static String prefix = ChatColor.translateAlternateColorCodes('&', "&e&lElemental &r&8» &7");
    public static String teamsPrefix = ChatColor.translateAlternateColorCodes('&', "&e&lTeams &r&8» &7");
    private static Main instance;
    public static String adminPerm = "elemental.admin";

    private TeamManager teamManager;

    @Override
    public void onEnable() {
        instance = this;

        teamManager = new TeamManager(this.getDataFolder());

        PassiveAbilityManager passiveAbilityManager = new PassiveAbilityManager(this);
        getServer().getPluginManager().registerEvents(passiveAbilityManager, this);
        getServer().getPluginManager().registerEvents(new FallDamageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMovementListener(), this);
        getServer().getPluginManager().registerEvents(new WindChargeManager(this), this);
        Bukkit.getPluginManager().registerEvents(new AbilityInteractManager(), this);
        Bukkit.getPluginManager().registerEvents(new ItemDrop(), this);

        getCommand("elementaladmin").setExecutor(new ElementalAdminCommand());
        getCommand("elementaladmin").setTabCompleter(new ElementalAdminTabComplete());
        getCommand("team").setExecutor(new TeamCommand(this));
        getCommand("team").setTabCompleter(new TeamCommandTabComplete(this));

        RegisterItems registerItems = new RegisterItems(Main.getInstance());
        registerItems.registerElementalItems();
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public static Main getInstance() {
        return instance;
    }

}

package com.mills.elements;

import com.mills.elements.Discord.DiscordManager;
import com.mills.elements.Discord.DiscordVulcan;
import com.mills.elements.Teams.TeamCommand;
import com.mills.elements.Teams.TeamCommandTabComplete;
import com.mills.elements.Teams.TeamManager;
import net.dv8tion.jda.api.JDA;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {

    public static String prefix = ChatColor.translateAlternateColorCodes('&', "&e&lElemental &r&8» &7");
    public static String teamsPrefix = ChatColor.translateAlternateColorCodes('&', "&e&lTeams &r&8» &7");
    private static Main instance;
    public static String adminPerm = "elemental.admin";

    private TeamManager teamManager;
    private CombatLogManager combatLogManager;
    private JDA bot;

    @Override
    public void onEnable() {
        bot = DiscordManager.startBot();

        instance = this;
        combatLogManager = new CombatLogManager(bot);
        combatLogManager.startCooldownChecker();

        teamManager = new TeamManager(this.getDataFolder());

        PassiveAbilityManager passiveAbilityManager = new PassiveAbilityManager(this);
        getServer().getPluginManager().registerEvents(passiveAbilityManager, this);
        getServer().getPluginManager().registerEvents(new FallDamageListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMovementListener(), this);
        getServer().getPluginManager().registerEvents(new WindChargeManager(this), this);
        Bukkit.getPluginManager().registerEvents(new AbilityInteractManager(), this);
        Bukkit.getPluginManager().registerEvents(new EnderpearlCooldown(), this);
        Bukkit.getPluginManager().registerEvents(new ItemCaps(), this);
        Bukkit.getPluginManager().registerEvents(combatLogManager, this);
        Bukkit.getPluginManager().registerEvents(new DiscordVulcan(bot), this);
        Bukkit.getPluginManager().registerEvents(new AntiFireAspectBook(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerSkullHandler(), this);
        Bukkit.getPluginManager().registerEvents(new SmithingBlocker(), this);
        Bukkit.getPluginManager().registerEvents(new ChatManager(), this);

        getCommand("elementaladmin").setExecutor(new ElementalAdminCommand());
        getCommand("elementaladmin").setTabCompleter(new ElementalAdminTabComplete());
        getCommand("team").setExecutor(new TeamCommand(this));
        getCommand("team").setTabCompleter(new TeamCommandTabComplete(this));
        getCommand("skull").setExecutor(new SkullCommand());
        getCommand("string").setExecutor(new StringCommand());

        RegisterItems registerItems = new RegisterItems(Main.getInstance());
        registerItems.registerElementalItems();

        CraftingRecipeHandler.recipeChanger();
    }

    @Override
    public void onDisable() {
        try {
            DiscordManager.stopBot();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public static Main getInstance() {
        return instance;
    }
}
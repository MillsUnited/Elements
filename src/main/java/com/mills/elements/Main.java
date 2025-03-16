package com.mills.elements;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static String prefix = ChatColor.translateAlternateColorCodes('&', "&e&lElemental &r&8» &7");

    @Override
    public void onEnable() {
        PassiveAbilityManager passiveAbilityManager = new PassiveAbilityManager(this);
        getServer().getPluginManager().registerEvents(passiveAbilityManager, this);

        Bukkit.getPluginManager().registerEvents(new AbilityInteractManager(), this);
        Bukkit.getPluginManager().registerEvents(new ItemDrop(), this);

        getCommand("elementaladmin").setExecutor(new ElementalAdminCommand());
        getCommand("elementaladmin").setTabCompleter(new ElementalAdminTabComplete());

        RegisterItems.registerElementalItems();
    }

}

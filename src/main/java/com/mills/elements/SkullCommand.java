package com.mills.elements;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SkullCommand implements CommandExecutor {

    private final String prefix = ChatColor.translateAlternateColorCodes('&', "&e&lHeads &r&8» &7");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + "Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission(Main.adminPerm)) {
            player.sendMessage(prefix + "You don't have permission to use this command!");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage(prefix + "Usage: /skull <onlinePlayer>");
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayerExact(targetName);

        if (target == null || !target.isOnline()) {
            player.sendMessage(prefix + "That player is not online.");
            return true;
        }

        ItemStack skull = PlayerSkullHandler.getPlayerHead(target);

        if (skull == null) {
            player.sendMessage(prefix + "That player is not online.");
            return true;
        }

        player.getInventory().addItem(skull);
        player.sendMessage(prefix + "Gave you the skull of " + target.getName());
        return true;
    }
}

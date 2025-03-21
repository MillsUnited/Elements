package com.mills.elements;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ElementalAdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (player.hasPermission(Main.adminPerm)) {

                if (args.length == 1) {

                    String name;

                    if (args[0].equalsIgnoreCase("fire")) {
                        player.getInventory().addItem(Items.fire());
                        name = "Fire";
                        player.sendMessage(Main.prefix + "gave " + name + " element item!");
                    } else if (args[0].equalsIgnoreCase("water")) {
                        player.getInventory().addItem(Items.water());
                        name = "Water";
                        player.sendMessage(Main.prefix + "gave " + name + " element item!");
                    } else if (args[0].equalsIgnoreCase("ice")) {
                        player.getInventory().addItem(Items.ice());
                        name = "Ice";
                        player.sendMessage(Main.prefix + "gave " + name + " element item!");
                    } else if (args[0].equalsIgnoreCase("shadow")) {
                        player.getInventory().addItem(Items.shadow());
                        name = "Shadow";
                        player.sendMessage(Main.prefix + "gave " + name + " element item!");
                    } else if (args[0].equalsIgnoreCase("earth")) {
                        player.getInventory().addItem(Items.earth());
                        name = "Earth";
                        player.sendMessage(Main.prefix + "gave " + name + " element item!");
                    } else if (args[0].equalsIgnoreCase("nature")) {
                        player.getInventory().addItem(Items.nature());
                        name = "Nature";
                        player.sendMessage(Main.prefix + "gave " + name + " element item!");
                    } else if (args[0].equalsIgnoreCase("sun")) {
                        player.getInventory().addItem(Items.sun());
                        name = "Sun";
                        player.sendMessage(Main.prefix + "gave " + name + " element item!");
                    } else if (args[0].equalsIgnoreCase("wind")) {
                        player.getInventory().addItem(Items.wind());
                        name = "Wind";
                        player.sendMessage(Main.prefix + "gave " + name + " element item!");
                    } else if (args[0].equalsIgnoreCase("kingcrown")) {
                        player.getInventory().addItem(Items.kingcrown());
                        name = "Kings Crown";
                        player.sendMessage(Main.prefix + "gave " + name);
                    } else if (args[0].equalsIgnoreCase("juggernaut")) {
                        player.getInventory().addItem(Items.juggernautHelmet());
                        player.getInventory().addItem(Items.juggernautChestplate());
                        player.getInventory().addItem(Items.juggernautLeggings());
                        player.getInventory().addItem(Items.juggernautBoots());
                        name = "Juggernaut Armor Set";
                        player.sendMessage(Main.prefix + "gave " + name);
                    }

                }

            } else {
                player.sendMessage(Main.prefix + "you dont have permission to use this command!");
            }

        }

        return false;
    }
}

package com.mills.elements;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Item;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class ItemDrop implements Listener {

    private static final Map<ItemStack, ChatColor> elementalColors = new HashMap<>();

    static {
        elementalColors.put(Items.fire(), ChatColor.RED);
        elementalColors.put(Items.ice(), ChatColor.WHITE);
        elementalColors.put(Items.earth(), ChatColor.DARK_GREEN);
        elementalColors.put(Items.sun(), ChatColor.YELLOW);
        elementalColors.put(Items.nature(), ChatColor.GREEN);
        elementalColors.put(Items.shadow(), ChatColor.DARK_PURPLE);
        elementalColors.put(Items.water(), ChatColor.AQUA);
        elementalColors.put(Items.wind(), ChatColor.GRAY);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e) {
        Item droppedItem = e.getItemDrop();
        ItemStack itemStack = droppedItem.getItemStack();

        for (Map.Entry<ItemStack, ChatColor> entry : elementalColors.entrySet()) {
            if (isSameItem(itemStack, entry.getKey())) {
                droppedItem.setGlowing(true);
                droppedItem.setCustomName(itemStack.getItemMeta().getDisplayName());
                droppedItem.setCustomNameVisible(true);

                setItemGlowColor(droppedItem, entry.getValue());
                return;
            }
        }
    }

    @EventHandler
    public void onItemDropByDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        List<ItemStack> droppedItems = new ArrayList<>(e.getDrops()); // This gets all the items the player drops

        // Loop through all the items dropped on death
        for (ItemStack itemStack : droppedItems) {
            for (Map.Entry<ItemStack, ChatColor> entry : elementalColors.entrySet()) {
                if (isSameItem(itemStack, entry.getKey())) {
                    // Ensure the item is cloned to avoid modifying the original
                    ItemStack itemWithGlow = itemStack.clone();
                    ItemMeta meta = itemWithGlow.getItemMeta();

                    // Set the custom name if it has one
                    if (meta != null) {
                        meta.setDisplayName(itemStack.getItemMeta().getDisplayName());
                        itemWithGlow.setItemMeta(meta);
                    }

                    // Create an item entity for the dropped item
                    Item droppedItem = player.getWorld().dropItem(player.getLocation(), itemWithGlow);

                    // Apply glowing effect and custom name
                    droppedItem.setGlowing(true);
                    droppedItem.setCustomName(itemWithGlow.getItemMeta().getDisplayName());
                    droppedItem.setCustomNameVisible(true);

                    // Apply the glow color based on the elemental color
                    setItemGlowColor(droppedItem, entry.getValue());

                    // Remove the original item from the dropped items to avoid duplication
                    e.getDrops().remove(itemStack);
                    break; // No need to check other elemental types for the same item
                }
            }
        }
    }



    private boolean isSameItem(ItemStack item1, ItemStack item2) {
        if (item1 == null || item2 == null) return false;
        if (!item1.getType().equals(item2.getType())) return false;

        if (!item1.hasItemMeta() || !item2.hasItemMeta()) return false;
        if (!item1.getItemMeta().hasDisplayName() || !item2.getItemMeta().hasDisplayName()) return false;
        if (!item1.getItemMeta().getDisplayName().equals(item2.getItemMeta().getDisplayName())) return false;

        return item1.getItemMeta().hasLore() == item2.getItemMeta().hasLore() &&
                (!item1.getItemMeta().hasLore() || item1.getItemMeta().getLore().equals(item2.getItemMeta().getLore()));
    }

    private void setItemGlowColor(Item item, ChatColor color) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        String teamName = "glow_" + color.name().toLowerCase();

        Team team = scoreboard.getTeam(teamName);
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.setColor(color);
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }

        team.addEntry(item.getUniqueId().toString());
    }

}
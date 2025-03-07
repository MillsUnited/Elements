package com.mills.elements;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Item;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        List<ItemStack> elementalItems = Arrays.asList(
                Items.fire(),
                Items.ice(),
                Items.earth(),
                Items.sun(),
                Items.nature(),
                Items.shadow(),
                Items.water(),
                Items.wind()
        );

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

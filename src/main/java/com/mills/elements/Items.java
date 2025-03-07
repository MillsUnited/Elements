package com.mills.elements;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Items {

    public static ItemStack fire() {
        ItemStack fire = new ItemStack(Material.PAPER);
        ItemMeta fireMeta = fire.getItemMeta();
        String display = Util.parseHexColors("&#730606F&#7B0505i&#830404r&#8B0303e &#9A0101E&#A20000l&#A61B03e&#AA3606m&#AD5208e&#B16D0Bn&#B5880Et");
        fireMeta.setDisplayName(display);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "Abilities:");
        fireMeta.setLore(lore);
        NamespacedKey model = new NamespacedKey("minecraft", "fire");
        fireMeta.setItemModel(model);
        fire.setItemMeta(fireMeta);
        return fire;
    }

    public static ItemStack water() {
        ItemStack water = new ItemStack(Material.PAPER);
        ItemMeta waterMeta = water.getItemMeta();
        String display = Util.parseHexColors("&#09498FW&#09589Fa&#0866AEt&#0875BEe&#0784CDr &#06A1ECE&#07A4D5l&#09A8BFe&#0AABA8m&#0BAE91e&#0DB27Bn&#0EB564t");
        waterMeta.setDisplayName(display);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "Abilities:");
        waterMeta.setLore(lore);
        NamespacedKey model = new NamespacedKey("minecraft", "water");
        waterMeta.setItemModel(model);
        water.setItemMeta(waterMeta);
        return water;
    }

    public static ItemStack ice() {
        ItemStack ice = new ItemStack(Material.PAPER);
        ItemMeta iceMeta = ice.getItemMeta();
        String display = Util.parseHexColors("&#007CC4I&#1B8BCAc&#369AD0e &#6DB7DDE&#88C6E3l&#A0D1E9e&#B8DDEEm&#CFE8F4e&#E7F4F9n&#FFFFFFt");
        iceMeta.setDisplayName(display);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "Abilities:");
        iceMeta.setLore(lore);
        NamespacedKey model = new NamespacedKey("minecraft", "ice");
        iceMeta.setItemModel(model);
        ice.setItemMeta(iceMeta);
        return ice;
    }

    public static ItemStack shadow() {
        ItemStack shadow = new ItemStack(Material.PAPER);
        ItemMeta shadowMeta = shadow.getItemMeta();
        String display = Util.parseHexColors("&#350062S&#33005Fh&#32005Ca&#300059d&#2E0055o&#2C0052w &#29004CE&#22003Fl&#1B0033e&#150026m&#0E0019e&#07000Dn&#000000t");
        shadowMeta.setDisplayName(display);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "Abilities:");
        shadowMeta.setLore(lore);
        NamespacedKey model = new NamespacedKey("minecraft", "shadow");
        shadowMeta.setItemModel(model);
        shadow.setItemMeta(shadowMeta);
        return shadow;
    }

    public static ItemStack earth() {
        ItemStack earth = new ItemStack(Material.PAPER);
        ItemMeta earthMeta = earth.getItemMeta();
        String display = Util.parseHexColors("&#128500E&#1A7900a&#236E00r&#2B6200t&#335700h &#444000E&#4C3400l&#483105e&#442E0Am&#412B0Fe&#3D2814n&#392519t");
        earthMeta.setDisplayName(display);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "Abilities:");
        earthMeta.setLore(lore);
        NamespacedKey model = new NamespacedKey("minecraft", "earth");
        earthMeta.setItemModel(model);
        earth.setItemMeta(earthMeta);
        return earth;
    }

    public static ItemStack nature() {
        ItemStack nature = new ItemStack(Material.PAPER);
        ItemMeta natureMeta = nature.getItemMeta();
        String display = Util.parseHexColors("&#FFEE00N&#FFEE00a&#FFEE00t&#FFEE00u&#FFEE00r&#BFF209e &#40F91CE&#00FC25l&#05CF1Ce&#09A213m&#0E7409e&#124700n&#124700t");
        natureMeta.setDisplayName(display);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "Abilities:");
        natureMeta.setLore(lore);
        NamespacedKey model = new NamespacedKey("minecraft", "nature");
        natureMeta.setItemModel(model);
        nature.setItemMeta(natureMeta);
        return nature;
    }

    public static ItemStack sun() {
        ItemStack sun = new ItemStack(Material.PAPER);
        ItemMeta sunMeta = sun.getItemMeta();
        String display = Util.parseHexColors("&#FFEE00S&#FEE300u&#FED800n &#FCC200E&#F3A200l&#E98100e&#E06100m&#D64100e&#CD2000n&#C30000t");
        sunMeta.setDisplayName(display);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "Abilities:");
        sunMeta.setLore(lore);
        NamespacedKey model = new NamespacedKey("minecraft", "sun");
        sunMeta.setItemModel(model);
        sun.setItemMeta(sunMeta);
        return sun;
    }

    public static ItemStack wind() {
        ItemStack wind = new ItemStack(Material.PAPER);
        ItemMeta windMeta = wind.getItemMeta();
        String display = Util.parseHexColors("&#B3B3B3W&#AEAEAEi&#AAAAAAn&#A5A5A5d &#919191E&#818181l&#727272e&#626262m&#535353e&#434343n&#343434t");
        windMeta.setDisplayName(display);
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "Abilities:");
        windMeta.setLore(lore);
        NamespacedKey model = new NamespacedKey("minecraft", "wind");
        windMeta.setItemModel(model);
        wind.setItemMeta(windMeta);
        return wind;
    }
}
package com.mills.elements;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.ArrayList;
import java.util.List;

public class Items {

    public static ItemStack fire() {
        ItemStack fire = new ItemStack(Material.PAPER);
        ItemMeta fireMeta = fire.getItemMeta();

        String display = Util.parseHexColors("&#730606&l&nF&#7B0505&l&ni&#830404&l&nr&#8B0303&l&ne &#9A0101&l&nE&#A20000&l&nl&#A61B03&l&ne&#AA3606&l&nm&#AD5208&l&ne&#B16D0B&l&nn&#B5880E&l&nt");
        fireMeta.setDisplayName(display);

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Util.parseHexColors("&#B73F3FPassives: Permanent Fire Resistance"));
        lore.add("");
        lore.add(Util.parseHexColors("&#FCD05CAbility 1: Lava Beam"));
        lore.add(Util.parseHexColors("&#FCD05CShoot a fire laser dealing 1 heart of damage every 0.5 sec"));
        lore.add(Util.parseHexColors("&#FCD05CDuration: 10 sec"));
        lore.add(Util.parseHexColors("&#FCD05CCooldown: 2 min"));
        lore.add("");
        lore.add(Util.parseHexColors("&#FC895CAbility 2: Volcanic Eruption"));
        lore.add(Util.parseHexColors("&#FC895CJump 50 blocks in the air, leaving fire behind"));
        lore.add(Util.parseHexColors("&#FC895CCooldown: 30 sec"));
        lore.add(" ");
        lore.add(Util.parseHexColors("&#9D0101When in the nether some abilities a&#9D0101re enhanced"));

        fireMeta.setLore(lore);
        NamespacedKey model = new NamespacedKey("minecraft", "fire");
        fireMeta.setItemModel(model);
        fire.setItemMeta(fireMeta);

        return fire;
    }

    public static ItemStack water() {
        ItemStack water = new ItemStack(Material.PAPER);
        ItemMeta waterMeta = water.getItemMeta();

        String display = Util.parseHexColors("&#09498F&l&nW&#09589F&l&na&#0866AE&l&nt&#0875BE&l&ne&#0784CD&l&nr &#06A1EC&l&nE&#07A4D5&l&nl&#09A8BF&l&ne&#0AABA8&l&nm&#0BAE91&l&ne&#0DB27B&l&nn&#0EB564&l&nt");
        waterMeta.setDisplayName(display);

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Util.parseHexColors("&#3F69B7Passives: Permanent Water Breathing"));
        lore.add("");
        lore.add(Util.parseHexColors("&#5CC8FCAbility 1: Poseidon's Rush"));
        lore.add(Util.parseHexColors("&#5CC8FCGain Dolphin’s Grace"));
        lore.add(Util.parseHexColors("&#5CC8FCDuration: 10 sec"));
        lore.add(Util.parseHexColors("&#5CC8FCCooldown: 2 min"));
        lore.add("");
        lore.add(Util.parseHexColors("&#5CFCDCAbility 2: Water Knockback"));
        lore.add(Util.parseHexColors("&#5CFCDCAny players around get knocked back 5 blocks away"));
        lore.add(Util.parseHexColors("&#5CFCDCCooldown: 1 min"));
        lore.add(" ");
        lore.add(Util.parseHexColors("&#0030FFWhen in an ocean some abilities are&#0030FF enhanced"));


        waterMeta.setLore(lore);
        NamespacedKey model = new NamespacedKey("minecraft", "water");
        waterMeta.setItemModel(model);
        water.setItemMeta(waterMeta);

        return water;
    }

    public static ItemStack ice() {
        ItemStack ice = new ItemStack(Material.PAPER);
        ItemMeta iceMeta = ice.getItemMeta();

        String display = Util.parseHexColors("&#007CC4&l&nI&#1B8BCA&l&nc&#369AD0&l&ne &#6DB7DD&l&nE&#88C6E3&l&nl&#A0D1E9&l&ne&#B8DDEE&l&nm&#CFE8F4&l&ne&#E7F4F9&l&nn&#FFFFFF&l&nt");
        iceMeta.setDisplayName(display);

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Util.parseHexColors("&#A7DFFFPassives: Gain Speed 2 in snowy biomes"));
        lore.add("");
        lore.add(Util.parseHexColors("&#0ABAFFAbility 1: Player Freeze"));
        lore.add(Util.parseHexColors("&#0ABAFFWhen activated, players in a 5-block radius freeze in place"));
        lore.add(Util.parseHexColors("&#0ABAFFfor 5 sec and will have the freezing effect for 30 sec."));
        lore.add(Util.parseHexColors("&#0ABAFFCooldown: 2 min"));
        lore.add("");
        lore.add(Util.parseHexColors("&#546DFFAbility 2: Ice Dash"));
        lore.add(Util.parseHexColors("&#546DFFDash 5 blocks forward in the direction you are looking"));
        lore.add(Util.parseHexColors("&#546DFFCooldown: 20 sec"));
        lore.add(" ");
        lore.add(Util.parseHexColors("&#E0E6FFWhen in a snowy biome some abilities are enhanced"));

        iceMeta.setLore(lore);
        NamespacedKey model = new NamespacedKey("minecraft", "ice_element");
        iceMeta.setItemModel(model);
        ice.setItemMeta(iceMeta);

        return ice;
    }

    public static ItemStack shadow() {
        ItemStack shadow = new ItemStack(Material.PAPER);
        ItemMeta shadowMeta = shadow.getItemMeta();

        String display = Util.parseHexColors("&#350062&l&nS&#33005F&l&nh&#32005C&l&na&#300059&l&nd&#2E0055&l&no&#2C0052&l&nw &#29004C&l&nE&#22003F&l&nl&#1B0033&l&ne&#150026&l&nm&#0E0019&l&ne&#07000D&l&nn&#000000&l&nt");
        shadowMeta.setDisplayName(display);

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Util.parseHexColors("&#66346EPassives: Permanent Invisibility"));
        lore.add("");
        lore.add(Util.parseHexColors("&#330D60Ability 1: True Invisibility"));
        lore.add(Util.parseHexColors("&#330D60Become truly invisible even when wearing armor"));
        lore.add(Util.parseHexColors("&#330D60for holding an item"));
        lore.add(Util.parseHexColors("&#330D60Duration: 5 sec"));
        lore.add(Util.parseHexColors("&#330D60Cooldown: 2 min"));
        lore.add("");
        lore.add(Util.parseHexColors("&#8048CDAbility 2: Shadow Dash"));
        lore.add(Util.parseHexColors("&#8048CDGain Speed 3 for 20 sec and give Blindness"));
        lore.add(Util.parseHexColors("&#8048CDto all surrounding players for 10 sec"));
        lore.add(Util.parseHexColors("&#8048CDCooldown: 2 min"));
        lore.add(" ");
        lore.add(Util.parseHexColors("&#4D20A5When in the End some abilities are enhanced"));

        shadowMeta.setLore(lore);
        NamespacedKey model = new NamespacedKey("minecraft", "shadow");
        shadowMeta.setItemModel(model);
        shadow.setItemMeta(shadowMeta);

        return shadow;
    }

    public static ItemStack earth() {
        ItemStack earth = new ItemStack(Material.PAPER);
        ItemMeta earthMeta = earth.getItemMeta();
        String display = Util.parseHexColors("&#128500&l&nE&#1A7900&l&na&#236E00&l&nr&#2B6200&l&nt&#335700&l&nh &#444000&l&nE&#4C3400&l&nl&#483105&l&ne&#442E0A&l&nm&#412B0F&l&ne&#3D2814&l&nn&#392519&l&nt");
        earthMeta.setDisplayName(display);

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Util.parseHexColors("&#9DBF39Passives: Permanent Haste 2"));
        lore.add(" ");
        lore.add(Util.parseHexColors("&#828318Ability 1: Hasty Attack Speed"));
        lore.add(Util.parseHexColors("&#828318When activated have a much faster attack speed"));
        lore.add(Util.parseHexColors("&#828318Duration: 10 sec"));
        lore.add(Util.parseHexColors("&#828318Cooldown: 2 min"));
        lore.add(" ");
        lore.add(Util.parseHexColors("&#836618Ability 2: Hulk Smash"));
        lore.add(Util.parseHexColors("&#836618Launches players in 5 block radius away from you"));
        lore.add(Util.parseHexColors("&#836618dealing 6 hearts of damage"));
        lore.add(Util.parseHexColors("&#836618Cooldown: 30 sec"));
        lore.add(" ");
        lore.add(Util.parseHexColors("&#808080When in a mountain biome some abilities are enhanced"));

        earthMeta.setLore(lore);
        NamespacedKey model = new NamespacedKey("minecraft", "earth");
        earthMeta.setItemModel(model);
        earth.setItemMeta(earthMeta);
        return earth;
    }


    public static ItemStack nature() {
        ItemStack nature = new ItemStack(Material.PAPER);
        ItemMeta natureMeta = nature.getItemMeta();

        String display = Util.parseHexColors("&#FFEE00&l&nN&#FFEE00&l&na&#FFEE00&l&nt&#FFEE00&l&nu&#FFEE00&l&nr&#BFF209&l&ne &#40F91C&l&nE&#00FC25&l&nl&#05CF1C&l&ne&#09A213&l&nm&#0E7409&l&ne&#124700&l&nn&#124700&l&nt");
        natureMeta.setDisplayName(display);

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Util.parseHexColors("&#FDFF8FPassives: +4 Permanent Hearts &#FF0000❤❤❤❤"));
        lore.add("");
        lore.add(Util.parseHexColors("&#4CB067Ability 1: Nature’s Resistance"));
        lore.add(Util.parseHexColors("&#4CB067Gain Resistance 3 and 6 extra hearts"));
        lore.add(Util.parseHexColors("&#4CB067for a total of 20 hearts."));
        lore.add(Util.parseHexColors("&#4CB067Duration (Resistance 3): 10 sec"));
        lore.add(Util.parseHexColors("&#4CB067Duration (Absorption): 1 min"));
        lore.add(Util.parseHexColors("&#4CB067Cooldown: 2 min"));
        lore.add("");
        lore.add(Util.parseHexColors("&#588326Ability 2: Leaf Heal"));
        lore.add(Util.parseHexColors("&#588326Anyone around the player will be fully healed"));
        lore.add(Util.parseHexColors("&#588326and receive full saturation."));
        lore.add(Util.parseHexColors("&#588326Cooldown: 1 min"));
        lore.add("");
        lore.add(Util.parseHexColors("&#45F867When in a flower or cherry biome some abilities are enhanced"));

        natureMeta.setLore(lore);
        NamespacedKey model = new NamespacedKey("minecraft", "nature");
        natureMeta.setItemModel(model);
        nature.setItemMeta(natureMeta);

        return nature;
    }


    public static ItemStack sun() {
        ItemStack sun = new ItemStack(Material.PAPER);
        ItemMeta sunMeta = sun.getItemMeta();

        String display = (ChatColor.BOLD + (Util.parseHexColors("&#FFEE00&l&nS&#FEE300&l&nu&#FED800&l&nn &#FCC200&l&nE&#F3A200&l&nl&#E98100&l&ne&#E06100&l&nm&#D64100&l&ne&#CD2000&l&nn&#C30000t")));
        sunMeta.setDisplayName(display);

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Util.parseHexColors("&#B73F3FPassives: Permanent night vision"));
        lore.add("");
        lore.add(Util.parseHexColors("&#FCD05CAbility 1: Solar Strength"));
        lore.add(Util.parseHexColors("&#FCD05CGain Strength 3"));
        lore.add(Util.parseHexColors("&#FCD05CDuration: 20 sec"));
        lore.add(Util.parseHexColors("&#FCD05CCooldown: 2 min"));
        lore.add("");
        lore.add(Util.parseHexColors("&#FC895CAbility 2: Sun’s Rays"));
        lore.add(Util.parseHexColors("&#FC895CHeal to max hp and gain 10 extra hearts"));
        lore.add(Util.parseHexColors("&#FC895CCooldown: 1 min"));
        lore.add(" ");
        lore.add(Util.parseHexColors("&#FFA500When the Sun is out some abilities are enhanced"));

        sunMeta.setLore(lore);
        NamespacedKey model = new NamespacedKey("minecraft", "sun");
        sunMeta.setItemModel(model);
        sun.setItemMeta(sunMeta);

        return sun;
    }

    public static ItemStack wind() {
        ItemStack wind = new ItemStack(Material.PAPER);
        ItemMeta windMeta = wind.getItemMeta();

        String display = Util.parseHexColors("&#B3B3B3&l&nW&#AEAEAE&l&ni&#AAAAAA&l&nn&#A5A5A5&l&nd &#919191&l&nE&#818181&l&nl&#727272&l&ne&#626262&l&nm&#535353&l&ne&#434343&l&nn&#343434&l&nt");
        windMeta.setDisplayName(display);

        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Util.parseHexColors("&#454545Passives: No Fall Damage"));
        lore.add("");
        lore.add(Util.parseHexColors("&#A5A5A5Ability 1: Wind Crush"));
        lore.add(Util.parseHexColors("&#A5A5A5While looking at a player or players, they get grabbed,"));
        lore.add(Util.parseHexColors("&#A5A5A5put 10 blocks in the air, and then smashed into the floor."));
        lore.add(Util.parseHexColors("&#A5A5A5Cooldown: 2 min"));
        lore.add("");
        lore.add(Util.parseHexColors("&#E3E3E3Ability 2: Tornado Charge"));
        lore.add(Util.parseHexColors("&#E3E3E3Summons 5 flying wind charges where player is looking"));
        lore.add(Util.parseHexColors("&#E3E3E3targeting the hit player"));
        lore.add(Util.parseHexColors("&#E3E3E3Cooldown: 30 sec"));
        lore.add(" ");
        lore.add(Util.parseHexColors("&#FFFFFFWhen in a mountain biome some abilities are enhanced"));

        windMeta.setLore(lore);
        NamespacedKey model = new NamespacedKey("minecraft", "wind");
        windMeta.setItemModel(model);
        wind.setItemMeta(windMeta);

        return wind;
    }

    public static ItemStack kingcrown() {
        ItemStack crown = new ItemStack(Material.NETHERITE_HELMET);
        ItemMeta crownMeta = crown.getItemMeta();
        String display = Util.parseHexColors("&#F1A500&l&nK&#F1A500&l&ni&#F1A500&l&nn&#F1A500&l&ng&#F1A500&l&ns &#F1A500&l&nC&#F1A500&l&nr&#F1A500&l&no&#F1A500&l&nw&#F1A500&l&nn");
        crownMeta.setDisplayName(display);
        crownMeta.setUnbreakable(true);
        crownMeta.addEnchant(Enchantment.AQUA_AFFINITY, 1, true);
        crownMeta.addEnchant(Enchantment.RESPIRATION, 5, true);
        if (crownMeta instanceof ArmorMeta armorMeta) {
            ArmorTrim trim = new ArmorTrim(TrimMaterial.GOLD, TrimPattern.FLOW);
            armorMeta.setTrim(trim);
            crown.setItemMeta(armorMeta);
        }
        crown.setItemMeta(crownMeta);
        return crown;
    }

    public static ItemStack juggernautHelmet() {
        ItemStack juggernautHelmet = new ItemStack(Material.NETHERITE_HELMET);
        ItemMeta juggernautHelmetMeta = juggernautHelmet.getItemMeta();
        String display = Util.parseHexColors("&#FB0808&l&nJ&#FB1212&l&nu&#FB1D1D&l&ng&#FB2727&l&ng&#FC3131&l&ne&#FC3C3C&l&nr&#FC4646&l&nn&#FC5050&l&na&#FC5B5B&l&nu&#FC6565&l&nt &#FC7979&l&nH&#FD8484&l&ne&#FD8E8E&l&nl&#FD9898&l&nm&#FDA3A3&l&ne&#FDADAD&l&nt");
        juggernautHelmetMeta.setDisplayName(display);
        juggernautHelmetMeta.addEnchant(Enchantment.PROTECTION, 4, true);
        juggernautHelmetMeta.addEnchant(Enchantment.UNBREAKING, 3, true);
        juggernautHelmet.setItemMeta(juggernautHelmetMeta);
        return juggernautHelmet;
    }

    public static ItemStack juggernautChestplate() {
        ItemStack juggernautChestplate = new ItemStack(Material.NETHERITE_CHESTPLATE);
        ItemMeta juggernautChestplateMeta = juggernautChestplate.getItemMeta();
        String display = Util.parseHexColors("&#FB0808&l&nJ&#FB1010&l&nu&#FB1919&l&ng&#FB2121&l&ng&#FB2929&l&ne&#FC3131&l&nr&#FC3A3A&l&nn&#FC4242&l&na&#FC4A4A&l&nu&#FC5252&l&nt &#FC6363&l&nC&#FC6B6B&l&nh&#FC7373&l&ne&#FC7C7C&l&ns&#FD8484&l&nt&#FD8C8C&l&np&#FD9494&l&nl&#FD9D9D&l&na&#FDA5A5&l&nt&#FDADAD&l&ne");
        juggernautChestplateMeta.setDisplayName(display);
        juggernautChestplateMeta.addEnchant(Enchantment.PROTECTION, 4, true);
        juggernautChestplateMeta.addEnchant(Enchantment.UNBREAKING, 3, true);
        juggernautChestplate.setItemMeta(juggernautChestplateMeta);
        return juggernautChestplate;
    }

    public static ItemStack juggernautLeggings() {
        ItemStack juggernautLeggings = new ItemStack(Material.NETHERITE_LEGGINGS);
        ItemMeta juggernautLeggingsMeta = juggernautLeggings.getItemMeta();
        String display = Util.parseHexColors("&#FB0808&l&nJ&#FB1111&l&nu&#FB1A1A&l&ng&#FB2424&l&ng&#FB2D2D&l&ne&#FC3636&l&nr&#FC3F3F&l&nn&#FC4848&l&na&#FC5151&l&nu&#FC5B5B&l&nt &#FC6D6D&l&nL&#FC7676&l&ne&#FC7F7F&l&ng&#FD8888&l&ng&#FD9292&l&ni&#FD9B9B&l&nn&#FDA4A4&l&ng&#FDADAD&l&ns");
        juggernautLeggingsMeta.setDisplayName(display);
        juggernautLeggingsMeta.addEnchant(Enchantment.PROTECTION, 4, true);
        juggernautLeggingsMeta.addEnchant(Enchantment.UNBREAKING, 3, true);
        juggernautLeggings.setItemMeta(juggernautLeggingsMeta);
        return juggernautLeggings;
    }

    public static ItemStack juggernautBoots() {
        ItemStack juggernautBoots = new ItemStack(Material.NETHERITE_BOOTS);
        ItemMeta juggernautBootsMeta = juggernautBoots.getItemMeta();
        String display = Util.parseHexColors("&#FB0808&l&nJ&#FB1313&l&nu&#FB1E1E&l&ng&#FB2929&l&ng&#FC3434&l&ne&#FC3F3F&l&nr&#FC4A4A&l&nn&#FC5555&l&na&#FC6060&l&nu&#FC6B6B&l&nt &#FC8181&l&nB&#FD8C8C&l&no&#FD9797&l&no&#FDA2A2&l&nt&#FDADAD&l&ns");
        juggernautBootsMeta.setDisplayName(display);
        juggernautBootsMeta.addEnchant(Enchantment.PROTECTION, 4, true);
        juggernautBootsMeta.addEnchant(Enchantment.UNBREAKING, 3, true);
        juggernautBoots.setItemMeta(juggernautBootsMeta);
        return juggernautBoots;
    }
}
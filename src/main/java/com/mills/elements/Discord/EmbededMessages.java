package com.mills.elements.Discord;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class EmbededMessages {

    private static final String hexColor = "#3C3BA7";

    private static String getTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    public static EmbedBuilder banMessage(String bannedUser, String reason, String commandSender) {
        return baseEmbed("Player Banned", bannedUser, reason, commandSender, "Ban issued");
    }

    public static EmbedBuilder unbanMessage(String unbannedUser, String reason, String commandSender) {
        return baseEmbed("Player Unbanned", unbannedUser, reason, commandSender, "Unban issued");
    }

    public static EmbedBuilder warnMessage(String warnedUser, String reason, String commandSender) {
        return baseEmbed("Player Warned", warnedUser, reason, commandSender, "Warning issued");
    }

    public static EmbedBuilder muteMessage(String mutedUser, String reason, String commandSender) {
        return baseEmbed("Player Muted", mutedUser, reason, commandSender, "Mute issued");
    }

    public static EmbedBuilder unmuteMessage(String unmutedUser, String reason, String commandSender) {
        return baseEmbed("Player Unmuted", unmutedUser, reason, commandSender, "Unmute issued");
    }

    public static EmbedBuilder tempbanMessage(String bannedUser, String reason, String duration, String commandSender) {
        EmbedBuilder embed = baseEmbed("Player Temporarily Banned", bannedUser, reason, commandSender, "Tempban issued");
        embed.addField("Duration:", duration, false);
        return embed;
    }

    public static EmbedBuilder tempmuteMessage(String mutedUser, String reason, String duration, String commandSender) {
        EmbedBuilder embed = baseEmbed("Player Temporarily Muted", mutedUser, reason, commandSender, "Tempmute issued");
        embed.addField("Duration:", duration, false);
        return embed;
    }

    public static EmbedBuilder vulcanFlag(String playerName, String flag) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Vulcan Flags")
                .addField("**Player:**", playerName, false)
                .addField("**Flag:**", flag, false)
                .setColor(Color.decode(hexColor))
                .setFooter("Flagged " + getTimestamp());
        return embed;
    }

    public static EmbedBuilder combatLog(String playerName, String leaveReason, Double timeLeft, List<String> items) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Combat Log")
                .addField("**Player:**", playerName, false)
                .addField("**Quit Reason:**", leaveReason, false)
                .addField("**Time Remaining:**", timeLeft.toString() + "s", false)
                .setColor(Color.decode(hexColor))
                .setFooter("Flagged " + getTimestamp());

        if (!items.isEmpty()) {
            String formattedItems = items.stream()
                    .map(item -> "- " + item)
                    .collect(Collectors.joining("\n"));

            embed.addField("**Inventory Items:**", formattedItems, false);
        }
        return embed;
    }

    public static EmbedBuilder lastLogin(String playerName, String logoutTime) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Combat Log")
                .addField("**Player:**", playerName, false)
                .addField("**Logout time:**", logoutTime, false)
                .setColor(Color.decode(hexColor))
                .setFooter("Logged in at " + getTimestamp());
        return embed;
    }

    private static EmbedBuilder baseEmbed(String title, String user, String reason, String sender, String footerPrefix) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(title)
                .setDescription("**" + user + "**")
                .addField("Reason:", reason, false)
                .addField("By:", sender, false)
                .setColor(Color.decode(hexColor))
                .setFooter(footerPrefix + " " + getTimestamp());
        return embed;
    }

}

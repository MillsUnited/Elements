package com.mills.elements.Discord;

import com.mills.elements.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

public class DiscordListener extends ListenerAdapter {

    private final JDA bot;

    public DiscordListener(JDA bot) {
        this.bot = bot;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {

        if (!PermissionManager.hasStaffRoles(e)) return;

        String commandSender = e.getUser().getName();
        String user = e.getOption("user").getAsString();
        String reason = e.getOption("reason") != null ? e.getOption("reason").getAsString() : "No reason provided";

        String channelID = "1355602375347277924";
        TextChannel channel = bot.getTextChannelById(channelID);

        switch (e.getName()) {
            case "ban" -> {
                e.reply("banned player: " + user + " for reason: " + reason).setEphemeral(true).queue();
                if (channel != null) {
                    channel.sendMessageEmbeds(EmbededMessages.banMessage(user, reason, commandSender).build()).queue();
                }
                String command = "ban " + user + " " + reason;
                dispatchConsoleCommand(command);
            }

            case "unban" -> {
                e.reply("unbanned player: " + user + " for reason: " + reason).setEphemeral(true).queue();
                if (channel != null) {
                    channel.sendMessageEmbeds(EmbededMessages.unbanMessage(user, reason, commandSender).build()).queue();
                }
                String command = "pardon " + user; // or "unban", depending on your setup
                dispatchConsoleCommand(command);
            }

            case "warn" -> {
                e.reply("warned player: " + user + " for reason: " + reason).setEphemeral(true).queue();
                if (channel != null) {
                    channel.sendMessageEmbeds(EmbededMessages.warnMessage(user, reason, commandSender).build()).queue();
                }
                String command = "warn " + user + " " + reason;
                dispatchConsoleCommand(command);
            }

            case "mute" -> {
                e.reply("muted player: " + user + " for reason: " + reason).setEphemeral(true).queue();
                if (channel != null) {
                    channel.sendMessageEmbeds(EmbededMessages.muteMessage(user, reason, commandSender).build()).queue();
                }
                String command = "mute " + user + " " + reason;
                dispatchConsoleCommand(command);
            }

            case "unmute" -> {
                e.reply("unmuted player: " + user + " for reason: " + reason).setEphemeral(true).queue();
                if (channel != null) {
                    channel.sendMessageEmbeds(EmbededMessages.unmuteMessage(user, reason, commandSender).build()).queue();
                }
                String command = "unmute " + user;
                dispatchConsoleCommand(command);
            }

            case "tempban" -> {
                String time = e.getOption("time").getAsString();
                e.reply("tempbanned player: " + user + " for " + time + " with reason: " + reason).setEphemeral(true).queue();
                if (channel != null) {
                    channel.sendMessageEmbeds(EmbededMessages.tempbanMessage(user, reason, time, commandSender).build()).queue();
                }
                String command = "tempban " + user + " " + time + " " + reason;
                dispatchConsoleCommand(command);
            }

            case "tempmute" -> {
                String time = e.getOption("time").getAsString();
                e.reply("tempmuted player: " + user + " for " + time + " with reason: " + reason).setEphemeral(true).queue();
                if (channel != null) {
                    channel.sendMessageEmbeds(EmbededMessages.tempmuteMessage(user, reason, time, commandSender).build()).queue();
                }
                String command = "tempmute " + user + " " + time + " " + reason;
                dispatchConsoleCommand(command);
            }
        }
    }

    private void dispatchConsoleCommand(String command) {
        Bukkit.getScheduler().runTask(Main.getInstance(), () ->
                Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), command));
    }
}

package com.mills.elements.Discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.time.Duration;

public class DiscordManager {

    private static JDA bot;

    public static JDA startBot() {
        try {
            bot = JDABuilder.createDefault("MTM1NDkyMDUyMTcyMDAwNDc5OQ.GDGyxU.Dw_uS89L-3KT1Y0kmJuao7ugSDLUlo0boMX1Vs")
                    .build();

            bot.awaitReady();

            bot.updateCommands().addCommands(
                    Commands.slash("ban", "bans player in game")
                            .addOption(OptionType.STRING, "user", "User to ban", true)
                            .addOption(OptionType.STRING, "reason", "reason to ban player"),
                    Commands.slash("unban", "unbans player in game")
                            .addOption(OptionType.STRING, "user", "User to unban", true)
                            .addOption(OptionType.STRING, "reason", "reason to unban player"),
                    Commands.slash("warn", "warns player in game")
                            .addOption(OptionType.STRING, "user", "User to warn", true)
                            .addOption(OptionType.STRING, "reason", "reason to warn player"),
                    Commands.slash("mute", "mutes player in game")
                            .addOption(OptionType.STRING, "user", "User to mute", true)
                            .addOption(OptionType.STRING, "reason", "reason to mute player"),
                    Commands.slash("unmute", "unmutes player in game")
                            .addOption(OptionType.STRING, "user", "User to unmute", true)
                            .addOption(OptionType.STRING, "reason", "reason to unmute player"),
                    Commands.slash("tempban", "temporarily bans player in game")
                            .addOption(OptionType.STRING, "user", "User to tempban", true)
                            .addOption(OptionType.STRING, "reason", "Reason to tempban player", true)
                            .addOption(OptionType.STRING, "time", "Duration of ban (e.g. 1d, 2h)", true),
                    Commands.slash("tempmute", "temporarily mutes player in game")
                            .addOption(OptionType.STRING, "user", "User to tempmute", true)
                            .addOption(OptionType.STRING, "reason", "Reason to tempmute player", true)
                            .addOption(OptionType.STRING, "time", "Duration of mute (e.g. 30m, 3h)", true)
            ).queue();

            bot.addEventListener(new DiscordListener(bot));

            return bot;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void stopBot() throws InterruptedException {
        if (bot != null) {
            bot.shutdown();

            if (!bot.awaitShutdown(Duration.ofSeconds(10))) {
                bot.shutdownNow();
                bot.awaitShutdown();
            }
        }
    }
}
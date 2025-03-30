package com.mills.elements.Discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.event.Listener;

import java.util.List;

public class DiscordCombatlog implements Listener {

    private JDA bot;

    public DiscordCombatlog(JDA bot) {
        this.bot = bot;
    }

    public void sendMessage(String player, String reason, List<String> items) {
        TextChannel channel = bot.getTextChannelById("1355602995752210591");
        if (channel != null) {
            channel.sendMessageEmbeds(EmbededMessages.combatLog(player, reason, items).build()).queue();
        }
    }
}

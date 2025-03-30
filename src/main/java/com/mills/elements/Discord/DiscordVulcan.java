package com.mills.elements.Discord;

import me.frep.vulcan.api.event.VulcanFlagEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DiscordVulcan implements Listener {

    private final JDA bot;

    public DiscordVulcan(JDA bot) {
        this.bot = bot;
    }

    @EventHandler
    public void onViolation(VulcanFlagEvent e) {

        String playerName = e.getPlayer().getName();
        String rawFlagName = e.getCheck().getName();
        String flagName = rawFlagName.substring(0, 1).toUpperCase() + rawFlagName.substring(1);
        char flagType = Character.toUpperCase(e.getCheck().getType());
        String flag = flagName + " (Type: " + flagType + ")";

        TextChannel channel = bot.getTextChannelById("1355746672411414649");
        if (channel != null) {
            channel.sendMessageEmbeds(EmbededMessages.vulcanFlag(playerName, flag).build()).queue();
        }
    }

}

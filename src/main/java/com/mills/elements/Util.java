package com.mills.elements;

import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static String parseHexColors(String input) {
        Pattern pattern = Pattern.compile("&#([a-fA-F0-9]{6})");
        Matcher matcher = pattern.matcher(input);

        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hex = matcher.group(1);
            String colorCode = net.md_5.bungee.api.ChatColor.of("#" + hex).toString();
            matcher.appendReplacement(buffer, colorCode);
        }
        matcher.appendTail(buffer);

        return buffer.toString()
                .replace("&l", ChatColor.BOLD.toString())
                .replace("&n", ChatColor.UNDERLINE.toString());
    }

}

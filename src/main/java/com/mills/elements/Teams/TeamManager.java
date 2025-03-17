package com.mills.elements.Teams;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TeamManager {

    private final File file;
    private FileConfiguration config;

    public TeamManager(File dataFolder) {
        file = new File(dataFolder, "teams.yml");

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void createTeam(String teamName, UUID ownerUUID) {
        if (!config.contains(teamName)) {
            config.set(teamName + ".Owner", ownerUUID.toString());
            config.set(teamName + ".Members", List.of());
            saveConfig();
        }
    }

    public void removeTeam(String teamName) {
        config.set(teamName, null);
        saveConfig();
    }

    public void addTeamMember(UUID memberUUID, String teamName) {
        if (config.contains(teamName)) {
            List<String> members = config.getStringList(teamName + ".Members");
            if (!members.contains(memberUUID.toString())) {
                members.add(memberUUID.toString());
                config.set(teamName + ".Members", members);
                saveConfig();
            }
        }
    }

    public void removeTeamMember(UUID memberUUID, String teamName) {
        if (config.contains(teamName)) {
            List<String> members = config.getStringList(teamName + ".Members");
            if (members.contains(memberUUID.toString())) {
                members.remove(memberUUID.toString());
                config.set(teamName + ".Members", members);
                saveConfig();
            }
        }
    }

    public UUID getTeamOwner(String teamName) {
        if (config.contains(teamName)) {
            String ownerUUID = config.getString(teamName + ".Owner");
            return UUID.fromString(ownerUUID);
        }
        return null;
    }

    public List<UUID> getTeamMembers(String teamName) {
        List<UUID> membersList = new ArrayList<>();

        if (config.contains(teamName)) {
            List<String> members = config.getStringList(teamName + ".Members");
            for (String member : members) {
                membersList.add(UUID.fromString(member));
            }
        }

        return membersList;
    }

    public String getTeamName(UUID playerUUID) {
        for (String teamName : config.getKeys(false)) {
            UUID ownerUUID = UUID.fromString(config.getString(teamName + ".Owner"));
            if (ownerUUID.equals(playerUUID)) {
                return teamName;
            }

            List<String> members = config.getStringList(teamName + ".Members");
            if (members.contains(playerUUID.toString())) {
                return teamName;
            }
        }
        return null;
    }

    public int getTeamMemberCount(String teamName) {
        if (config.contains(teamName)) {
            List<String> members = config.getStringList(teamName + ".Members");
            return members.size();
        }
        return 0;
    }

    public boolean inATeam(UUID playerUUID) {
        for (String teamName : config.getKeys(false)) {
            UUID ownerUUID = UUID.fromString(config.getString(teamName + ".Owner"));
            if (ownerUUID.equals(playerUUID)) {
                return true;
            }

            List<String> members = config.getStringList(teamName + ".Members");
            if (members.contains(playerUUID.toString())) {
                return true;
            }
        }
        return false;
    }

    private void saveConfig() {
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(file);
    }

}

package com.pustinek.autofarmer.managers;

import com.pustinek.autofarmer.AutoFarmer;
import com.pustinek.autofarmer.CropSet;
import com.pustinek.autofarmer.PlayerData;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerManager {

    private AutoFarmer plugin;

    private YamlConfiguration playerConfig;
    private HashMap<UUID,PlayerData> playerDataMap = new HashMap<>();

    public PlayerManager() {
        this.plugin = AutoFarmer.getInstance();
    }
    public PlayerData getPlayerData(UUID uuid) {
        return this.playerDataMap.get(uuid);
    }


    public void loadPlayerData(UUID uuid) {
        File playerFile = new File(AutoFarmer.getInstance().getDataFolder()+"\\userData\\", uuid + ".yml");
        File playerFileDir = new File(AutoFarmer.getInstance().getDataFolder()+"\\userData\\");
        AutoFarmer.debug("loadPlayerData called with player-uuid: "+uuid);
        if(!playerFileDir.exists()) {
            playerFileDir.mkdirs();
        }
        if(!playerFile.exists()) {
            try {
                playerFile.createNewFile();
                this.playerConfig = YamlConfiguration.loadConfiguration(playerFile);
                playerConfig.set("enabled",false);
                playerConfig.set("plant-mode", "NONE");
                playerConfig.createSection("replant-modes");

                for (Map.Entry<String, CropSet> cropEntry : AutoFarmer.getCropManager().getCropSet().entrySet()) {
                    String key = cropEntry.getKey();
                    playerConfig.set("replant-modes."+ key,false);
                }
                playerConfig.save(playerFile);
                this.loadPlayerData(uuid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //File exists
            this.playerConfig = YamlConfiguration.loadConfiguration(playerFile);

            HashMap<CropSet,Boolean> playerAutoReplantValues = new HashMap<>();
            for (Map.Entry<String, CropSet> cropEntry : AutoFarmer.getCropManager().getCropSet().entrySet()) {
                String key = cropEntry.getKey();
                CropSet cropSetValue = cropEntry.getValue();
                Boolean cropAutoReplantValue = playerConfig.getBoolean("replant-modes." + key);
                AutoFarmer.debug("put crop to replantModes ->" + cropEntry.getKey());
                playerAutoReplantValues.put(cropSetValue,cropAutoReplantValue);
            }
            String selectedPlantMode = playerConfig.getString("plant-mode");
            Boolean playerAutoFarmerStatus = playerConfig.getBoolean("enabled");
            PlayerData playerData = new PlayerData(uuid, playerAutoReplantValues,selectedPlantMode,playerAutoFarmerStatus);
            AutoFarmer.debug("Successfully loaded "+playerAutoReplantValues.size()+" Crops to player with UUID - " + uuid);
            if(!this.playerDataMap.containsKey(uuid)) {
                this.playerDataMap.put(uuid,playerData);
            }else{
                AutoFarmer.debug("Tried to load player-data of ("+uuid+")"+" that already exists !");
            }

        }
    }
    public void removePlayerData(UUID uuid) {
        this.playerDataMap.remove(uuid);
    }
}

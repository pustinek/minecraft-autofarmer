package com.pustinek.autofarmer.managers;

import com.pustinek.autofarmer.AutoFarmer;
import com.pustinek.autofarmer.CropSet;
import com.pustinek.autofarmer.PlayerData;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PlayerManager {

    private AutoFarmer plugin;

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
        YamlConfiguration playerConfig;
        AutoFarmer.debug("loadPlayerData called with player-uuid: "+uuid);

        if(!playerFileDir.exists()) {
            playerFileDir.mkdirs();
        }
        if(!playerFile.exists()) {
            try {
                playerFile.createNewFile();
                playerConfig = YamlConfiguration.loadConfiguration(playerFile);
                playerConfig.set("enabled",false);
                playerConfig.set("plant-mode", "NONE");
                playerConfig.set("inventory-save", false);
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
            playerConfig = YamlConfiguration.loadConfiguration(playerFile);

            HashMap<String,Boolean> playerAutoReplantValues = new HashMap<>();
            ArrayList<String> replantableModesList = AutoFarmer.getCropManager().getReplantableModesList();

            for(String replantableMode : replantableModesList) {
                Boolean value = playerConfig.getBoolean("replant-modes." + replantableMode);
                playerAutoReplantValues.put(replantableMode,value);
            }
            String selectedPlantMode = playerConfig.getString("plant-mode");
            Boolean playerAutoFarmerStatus = playerConfig.getBoolean("enabled");
            Boolean toInventory = playerConfig.getBoolean("inventory-save");
            PlayerData playerData = new PlayerData(uuid, playerAutoReplantValues,selectedPlantMode,playerAutoFarmerStatus,toInventory);
            if(!this.playerDataMap.containsKey(uuid)) {
                this.playerDataMap.put(uuid,playerData);
            }else{
                AutoFarmer.debug("Tried to load player-data of ("+uuid+")"+" that already exists !");
            }
        }
    }
    public void savePlayerDataToFile(UUID uuid) {
        File playerFile = new File(AutoFarmer.getInstance().getDataFolder()+"\\userData\\", uuid + ".yml");
        PlayerData pd = this.playerDataMap.get(uuid);
        YamlConfiguration pc = YamlConfiguration.loadConfiguration(playerFile);
        for (Map.Entry<String, Boolean> replantEntry : pd.getAutoReplantValues().entrySet()) {
            String key = replantEntry.getKey();
            Boolean value = replantEntry.getValue();
            pc.set("replant-modes."+ key,value);
        }
        pc.set("enabled",true);
        pc.set("plant-mode",pd.getSelectedPlantMode());
        pc.set("inventory-save",pd.toInventory());
        try {
            pc.save(playerFile);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removePlayerData(UUID uuid) {
        this.playerDataMap.remove(uuid);
    }
}

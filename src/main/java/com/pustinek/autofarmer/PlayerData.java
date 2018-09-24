package com.pustinek.autofarmer;

import com.pustinek.autofarmer.managers.CropManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData{
    private final UUID uuid;
    private YamlConfiguration playerConfig;
    private final AutoFarmer plugin;
    private final CropManager cropManager;

    private boolean playerAutofarmerStatus = true;

    private HashMap<CropSet,Boolean> autoReplantValues = new HashMap<>();
    private String selectedPlantMode = "NONE";
    public PlayerData(UUID uuid, HashMap<CropSet,Boolean> autoReplantValues,String selectedPlantMode,Boolean playerAutofarmerStatus) {
        this.uuid = uuid;
        this.autoReplantValues = autoReplantValues;
        this.playerAutofarmerStatus = playerAutofarmerStatus;

        if(selectedPlantMode == null) {
            this.selectedPlantMode = "NONE";
        }else {
            this.selectedPlantMode = selectedPlantMode;
        }

        this.plugin = AutoFarmer.getInstance();
        this.cropManager = AutoFarmer.getCropManager();
    }



    public void setCropAutoReplant(CropSet cropSetToChange, boolean newValue) {
        if(cropSetToChange != null) {
            File playerFile = new File(AutoFarmer.getInstance().getDataFolder()+"\\userData\\", uuid + ".yml");
            this.playerConfig = YamlConfiguration.loadConfiguration(playerFile);
            playerConfig.set("replant-modes."+ cropSetToChange.getInternalName(), newValue);
            try {
                playerConfig.save(playerFile);
            }catch (Exception e) {
                e.printStackTrace();
            }
            try {
                this.autoReplantValues.replace(cropSetToChange,!newValue,newValue);
                AutoFarmer.debug("set " + cropSetToChange.getInternalName() + " to " + newValue);
            }catch (Exception e) {
                e.printStackTrace();
            }
            for (Map.Entry<CropSet, Boolean> cropEntry : autoReplantValues.entrySet()) {
                AutoFarmer.debug("" + cropEntry.getKey().getInternalName() + "->"+cropEntry.getValue());
            }
        }else
        {
            AutoFarmer.debug("cropSetToChange is null !!");
        }
    }
    public boolean getAutoReplantValue(Material mat) {
        for (Map.Entry<CropSet, Boolean> cropEntry : autoReplantValues.entrySet()) {
            AutoFarmer.debug("getAutoReplantValue("+mat.name()+")->"+ cropEntry.getKey().getCropMaterial().name());
            if(cropEntry.getKey().getCropMaterial().name().equalsIgnoreCase(mat.name())) {
                AutoFarmer.debug("getAutoReplantValue found -> " + mat.name());
                return cropEntry.getValue();
            }}
        return false;
    }
    public HashMap<CropSet,Boolean> getAutoReplantValues() {
        return this.autoReplantValues;
    }
    public Boolean hasAutoFarmerEnabled() {return this.playerAutofarmerStatus; }
    public String getSelectedPlantMode() {
        return this.selectedPlantMode;
    }
    public void setSelectedPlantMode(String newPlantMode) {
        if(newPlantMode != null) {
            AutoFarmer.debug("SelectedPlantMode " + this.selectedPlantMode);
            String oldValue = this.selectedPlantMode.toUpperCase();
            this.selectedPlantMode = newPlantMode.toUpperCase();

            File playerFile = new File(AutoFarmer.getInstance().getDataFolder()+"\\userData\\", uuid + ".yml");
            this.playerConfig = YamlConfiguration.loadConfiguration(playerFile);
            playerConfig.set("plant-mode", newPlantMode.toUpperCase());
            try {
                playerConfig.save(playerFile);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            AutoFarmer.debug("How can newPlantMode be null o.o");
        }

    }

}

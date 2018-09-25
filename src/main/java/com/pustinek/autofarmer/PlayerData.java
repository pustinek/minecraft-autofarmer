package com.pustinek.autofarmer;

import com.pustinek.autofarmer.managers.CropManager;
import com.sun.org.apache.xpath.internal.operations.Bool;
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

    private HashMap<String,Boolean> autoReplantValues;
    private String selectedPlantMode;
    private Boolean enabled;
    private Boolean toInventory;
    public PlayerData(UUID uuid, HashMap<String,Boolean> autoReplantValues,String selectedPlantMode,Boolean enabled,Boolean toInventory) {
        this.uuid = uuid;
        this.autoReplantValues = autoReplantValues;
        this.enabled = enabled;
        this.toInventory = toInventory;
        if(selectedPlantMode == null) {
            this.selectedPlantMode = "";
        }else {
            this.selectedPlantMode = selectedPlantMode;

        }
        this.plugin = AutoFarmer.getInstance();
        this.cropManager = AutoFarmer.getCropManager();
    }



    public void toggleReplantModeValue(String internalName) {
        Boolean oldValue = this.autoReplantValues.get(internalName);
        this.autoReplantValues.replace(internalName,oldValue,!oldValue);
    }
    public void toggleEnable() {
        this.enabled = !this.enabled;
    }

    public boolean isEnabled() { return this.enabled;}

    public Boolean getReplantValue(String modeName) {
        return this.autoReplantValues.get(modeName);
    }

    public HashMap<String, Boolean> getAutoReplantValues() {
        return autoReplantValues;
    }

    public String getSelectedPlantMode() {
        return this.selectedPlantMode;
    }

    public void setSelectedPlantMode(String newPlantMode) {
        if(this.cropManager.getPlantableModeList().contains(newPlantMode)){
            this.selectedPlantMode = newPlantMode.toUpperCase();
        }
    }

}

package com.pustinek.autofarmer.commands;

import com.pustinek.autofarmer.AutoFarmer;
import com.pustinek.autofarmer.PlayerData;
import com.pustinek.autofarmer.managers.CropManager;
import com.pustinek.autofarmer.managers.PlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AutoPlantCommand extends CommandAutoFarmer{
    private PlayerManager pm;
    private CropManager cm;

    private ArrayList<String> plantableModesList;
    public AutoPlantCommand() {
        this.pm = AutoFarmer.getPlayerManager();
        this.cm = AutoFarmer.getCropManager();

        this.plantableModesList = cm.getPlantableModeList();
    }
    @Override
    public String getCommandStart() {
        return "autofarmer plant";
    }

    @Override
    public String getHelp(CommandSender target) {
        if(target.hasPermission("autofarmer.plant")) {
            return "help-plant";
        }
        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("autofarmer.plant")){
            AutoFarmer.message(sender, "plant-noPermission");
            return;
        }

        UUID uuid = ((Player)sender).getUniqueId();
        PlayerData pd = pm.getPlayerData(uuid);
        String selectedPlantModeMode = pd.getSelectedPlantMode();
        if(args.length == 1 && args[0] != null) {
            if(pd != null) {
                AutoFarmer.messageNoPrefix(sender,"replant-header");
                AutoFarmer.messageNoPrefix(sender,"main-space");
                for(String plantableMode : this.plantableModesList) {
                    if(!plantableMode.equalsIgnoreCase(selectedPlantModeMode)) {
                        AutoFarmer.messageNoPrefix(sender,"plant-mode-status",plantableMode, "");
                    }else {
                        AutoFarmer.messageNoPrefix(sender,"plant-mode-status-sel",plantableMode,"✔");
                    }
                }
                AutoFarmer.messageNoPrefix(sender,"main-space");
                AutoFarmer.messageNoPrefix(sender,"replant-footer");
            }
        }if(args.length == 2) {
            String newPlantModeSel = args[1].toUpperCase();
            for(String plantableMode : plantableModesList) {
                if(plantableMode.equalsIgnoreCase(newPlantModeSel)){
                    pd.setSelectedPlantMode(newPlantModeSel);
                    AutoFarmer.message(sender,"plant-successful-change", newPlantModeSel);
                    return;
                }
            }
            AutoFarmer.debug("SelectedMode " + newPlantModeSel + " Doesn't exist!");
            }

    }
    /**
     * Get a list of string to complete a command with (raw list, not matching ones not filtered out).
     * @param toComplete The number of the argument that has to be completed
     * @param start      The already given start of the command
     * @param sender     The CommandSender that wants to tab complete
     * @return A collection with all the possibilities for argument to complete
     */
    public List<String> getTabCompleteList(int toComplete, String[] start, CommandSender sender) {
        List<String> result = new ArrayList<>();
        AutoFarmer.debug("AutoPlant toComplete: " + toComplete);
        if(toComplete == 2) {
            if(sender instanceof Player) {
                Player player = (Player)sender;
                return plantableModesList;
            }else{
                return result;
            }
        }
        return  result;
    }

}

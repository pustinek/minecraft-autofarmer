package com.pustinek.autofarmer.commands;

import com.pustinek.autofarmer.AutoFarmer;
import com.pustinek.autofarmer.CropSet;
import com.pustinek.autofarmer.PlayerData;
import com.pustinek.autofarmer.managers.CropManager;
import com.pustinek.autofarmer.managers.PlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AutoReplantCommand extends CommandAutoFarmer{
    private PlayerManager playerManager;
    public AutoReplantCommand() {

        this.playerManager = AutoFarmer.getPlayerManager();
    }

    @Override
    public String getCommandStart() {
        return "autofarmer replant";
    }

    @Override
    public String getHelp(CommandSender target) {
        if(target.hasPermission("autofarmer.replant")) {
            return "help-replant";
        }
        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("autofarmer.replant") || !sender.hasPermission("autofarmer.default")) {
            AutoFarmer.message(sender, "replant-noPermission");
        }
        UUID senderUUID = ((Player)sender).getUniqueId();
        PlayerData playerData = playerManager.getPlayerData(senderUUID);
        if(args.length == 1 && args[0] != null) {
                if(playerData != null) {
                    AutoFarmer.messageNoPrefix(sender,"replant-header");
                    AutoFarmer.messageNoPrefix(sender,"main-space");
                    playerData.getAutoReplantValues().forEach((key, value) -> {
                        if(value) {
                            AutoFarmer.messageNoPrefix(sender,"mode-status-on",key.getInternalName(),"ON","OFF");
                        }else {
                            AutoFarmer.messageNoPrefix(sender,"mode-status-off",key.getInternalName(),"OFF","ON");
                        }
                    });
                    AutoFarmer.messageNoPrefix(sender,"main-space");
                    AutoFarmer.messageNoPrefix(sender,"replant-footer");
                }
        } if(args.length == 2) {
            String selectedMode = args[1].toUpperCase();
            CropSet cropSet = AutoFarmer.getCropManager().getCropSetByInternalName(selectedMode);
            if (cropSet.getInternalName() != null) {
                Boolean newModeValue = !playerData.getAutoReplantValues().get(cropSet);
                playerData.setCropAutoReplant(cropSet, newModeValue);
                if(newModeValue) {
                    AutoFarmer.message(sender,"replant-successful-on", cropSet.getInternalName());
                }else {
                    AutoFarmer.message(sender,"replant-successful-off", cropSet.getInternalName());
                }

            }else {
                AutoFarmer.debug("farming mode " + selectedMode + "Doesn't exist!");
            }
        }
    }
    @Override
    public List<String> getTabCompleteList(int toComplete, String[] start, CommandSender sender) {
        List<String> result = new ArrayList<>();
        AutoFarmer.debug("AutoReplant toComplete: " + toComplete);
        if(toComplete == 2) {
            if(sender instanceof Player) {
                Player player = (Player)sender;
                return AutoFarmer.getCropManager().getCropSetInternalNames();
            }else{
                return result;
            }
        }
        return  result;
    }
}

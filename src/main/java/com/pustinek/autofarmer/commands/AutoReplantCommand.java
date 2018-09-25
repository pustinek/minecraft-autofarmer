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
    private CropManager cm;
    private ArrayList<String> replantableModes;
    public AutoReplantCommand() {

        this.playerManager = AutoFarmer.getPlayerManager();
        this.cm = AutoFarmer.getCropManager();

        this.replantableModes = cm.getReplantableModesList();

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
        if(!sender.hasPermission("autofarmer.replant")) {
            AutoFarmer.message(sender, "replant-noPermission");
            return;
        }
        UUID senderUUID = ((Player)sender).getUniqueId();
        PlayerData playerData = playerManager.getPlayerData(senderUUID);
        if(args.length == 1 && args[0] != null) {
                if(playerData != null) {
                    AutoFarmer.messageNoPrefix(sender,"replant-header");
                    AutoFarmer.messageNoPrefix(sender,"main-space");

                    for(String replantMode : this.replantableModes) {
                        if(playerData.getReplantValue(replantMode)) {
                            AutoFarmer.messageNoPrefix(sender,"replant-status-on",replantMode,"ON");
                        }else {
                            AutoFarmer.messageNoPrefix(sender,"replant-status-off",replantMode,"OFF");
                        }
                    }

                    AutoFarmer.messageNoPrefix(sender,"main-space");
                    AutoFarmer.messageNoPrefix(sender,"replant-footer");
                }
        } if(args.length == 2) {
            String selectedMode = args[1];
            for(String replantMode : replantableModes) {
                if(!replantMode.equalsIgnoreCase(selectedMode)) {continue;}
                playerData.toggleReplantModeValue(replantMode);
                if(playerData.getReplantValue(replantMode)) {
                    AutoFarmer.message(sender,"replant-successful-on", replantMode);
                }else {
                    AutoFarmer.message(sender,"replant-successful-off", replantMode);
                }
            } }
    }
    @Override
    public List<String> getTabCompleteList(int toComplete, String[] start, CommandSender sender) {
        List<String> result = new ArrayList<>();
        AutoFarmer.debug("AutoReplant toComplete: " + toComplete);
        if(toComplete == 2) {
            if(sender instanceof Player) {
                Player player = (Player)sender;
                return replantableModes;
            }else{
                return result;
            }
        }
        return  result;
    }
}

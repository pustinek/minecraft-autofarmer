package com.pustinek.autofarmer.commands;

import com.pustinek.autofarmer.AutoFarmer;
import com.pustinek.autofarmer.PlayerData;
import com.pustinek.autofarmer.managers.PlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleCommand extends CommandAutoFarmer {
    private PlayerManager pm;
    public ToggleCommand(){
        this.pm = AutoFarmer.getPlayerManager();
    }

    @Override
    public String getCommandStart() {
        return "autofarmer toggle";
    }

    @Override
    public String getHelp(CommandSender target) {
        if(target.hasPermission("autofarmer.toggle")) {
            return "help-toggle";
        }
        return null;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!sender.hasPermission("autofarmer.toggle")) {
            AutoFarmer.message(sender, "toggle-noPermission");
            return;
        }
        PlayerData pd = pm.getPlayerData(((Player) sender).getUniqueId());
        pd.toggleEnable();
        if(pd.isEnabled()){
            AutoFarmer.message(sender,"toggle-on");
        }else{
            AutoFarmer.message(sender,"toggle-off");
        }

    }
}

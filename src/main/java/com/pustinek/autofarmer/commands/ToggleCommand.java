package com.pustinek.autofarmer.commands;

import org.bukkit.command.CommandSender;

public class ToggleCommand extends CommandAutoFarmer {
    @Override
    public String getCommandStart() {
        return "autofarmer plant";
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

    }
}

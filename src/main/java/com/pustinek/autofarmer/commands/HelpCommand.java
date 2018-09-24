package com.pustinek.autofarmer.commands;

import com.pustinek.autofarmer.AutoFarmer;
import org.bukkit.command.CommandSender;

public class HelpCommand extends CommandAutoFarmer{
    public HelpCommand() {
    }

    @Override
    public String getCommandStart() {
        return "autofarmert/af" +
                " help";
    }

    @Override
    public String getHelp(CommandSender target) {
        if(target.hasPermission("autofarmer.help")) {
            return "help-help";
        }
        return null;

    }

    @Override
    public void execute(CommandSender sender, String[] args) {

    }
}

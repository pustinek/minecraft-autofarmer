package com.pustinek.autofarmer.commands;

import com.pustinek.autofarmer.AutoFarmer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandAutoFarmer {
    public boolean canExecute(Command command, String[] args) {
        String commandString = command.getName() + " " + StringUtils.join(args, " ");
        if(commandString.length() > getCommandStart().length()) {
            return commandString.toLowerCase().startsWith(getCommandStart().toLowerCase() + " ");
        }
        return commandString.toLowerCase().startsWith(getCommandStart().toLowerCase());
    }
    public abstract String getCommandStart();
    public abstract String getHelp(CommandSender target);
    public abstract void execute(CommandSender sender, String[] args);

    /**
     * Get a list of string to complete a command with (raw list, not matching ones not filtered out).
     * @param toComplete The number of the argument that has to be completed
     * @param start      The already given start of the command
     * @param sender     The CommandSender that wants to tab complete
     * @return A collection with all the possibilities for argument to complete
     */
    public List<String> getTabCompleteList(int toComplete, String[] start, CommandSender sender) {
        return new ArrayList<>();
    }
}

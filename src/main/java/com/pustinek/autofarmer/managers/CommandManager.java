package com.pustinek.autofarmer.managers;

import com.pustinek.autofarmer.AutoFarmer;
import com.pustinek.autofarmer.commands.AutoPlantCommand;
import com.pustinek.autofarmer.commands.CommandAutoFarmer;
import com.pustinek.autofarmer.commands.AutoReplantCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public final class CommandManager implements CommandExecutor, TabCompleter {
    private final ArrayList<CommandAutoFarmer> commands;
    public CommandManager() {
        commands = new ArrayList<>();
        //commands.add(new HelpCommand());
        commands.add(new AutoReplantCommand());
        commands.add(new AutoPlantCommand());

        AutoFarmer.getInstance().getCommand("AutoFarmer").setExecutor(this);
        AutoFarmer.getInstance().getCommand("AutoFarmer").setTabCompleter(this);
    }

    public ArrayList<CommandAutoFarmer> getCommands() {
        return commands;
    }

    public void showHelp(CommandSender target) {
        if(!target.hasPermission("autofarmer.help")) {
            AutoFarmer.message(target, "help-noPermission");
            return;
        }
        // Add all messages to a list
        ArrayList<String> messages = new ArrayList<>();
        AutoFarmer.message(target, "toggle");
        for(CommandAutoFarmer command : commands) {
            String help = command.getHelp(target);
            if(help != null && help.length() != 0) {
                messages.add(help);
            }
        }
        // Send the messages to the target
        for(String message : messages) {
            AutoFarmer.messageNoPrefix(target, message);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!AutoFarmer.getInstance().isEnabled()) {
            AutoFarmer.message(sender, "general-notReady");
            return true;
        }
        boolean executed = false;
        for(int i = 0; i < commands.size() && !executed; i++) {
            if(commands.get(i).canExecute(command, args)) {
                commands.get(i).execute(sender, args);
                executed = true;
            }
        }
        if(!executed && args.length == 0) {
            this.showHelp(sender);
        } else if(!executed && args.length > 0) {
            AutoFarmer.message(sender, "cmd-notValid");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> result = new ArrayList<>();
        if(!sender.hasPermission("autofarmer.tabcomplete")) {
            return result;
        }
        int toCompleteNumber = args.length;
        String toCompletePrefix = args[args.length - 1].toLowerCase();
        //AreaShop.debug("toCompleteNumber=" + toCompleteNumber + ", toCompletePrefix=" + toCompletePrefix + ", length=" + toCompletePrefix.length());
        if(toCompleteNumber == 1) {
            for(CommandAutoFarmer c : commands) {
                String begin = c.getCommandStart();
                result.add(begin.substring(begin.indexOf(' ') + 1));
            }
        } else {
            String[] start = new String[args.length];
            start[0] = command.getName();
            System.arraycopy(args, 0, start, 1, args.length - 1);
            for(CommandAutoFarmer c : commands) {
                if(c.canExecute(command, args)) {
                    result = c.getTabCompleteList(toCompleteNumber, start, sender);
                }
            }
        }
        // Filter and sort the results
        if(result.size() > 0) {
            SortedSet<String> set = new TreeSet<>();
            for(String suggestion : result) {
                if(suggestion.toLowerCase().startsWith(toCompletePrefix)) {
                    set.add(suggestion);
                }
            }
            result.clear();
            result.addAll(set);
        }
        //AreaShop.debug("Tabcomplete #" + toCompleteNumber + ", prefix="+ toCompletePrefix + ", result=" + result.toString());
        return result;
    }
}

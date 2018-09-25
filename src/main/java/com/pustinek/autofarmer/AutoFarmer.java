package com.pustinek.autofarmer;

import com.pustinek.autofarmer.listeneres.CropListener;
import com.pustinek.autofarmer.listeneres.OnHoeListener;
import com.pustinek.autofarmer.listeneres.OnPlayerJoinListener;
import com.pustinek.autofarmer.listeneres.OnPlayerLeaveListener;
import com.pustinek.autofarmer.managers.CommandManager;
import com.pustinek.autofarmer.managers.CropManager;
import com.pustinek.autofarmer.managers.PlayerManager;
import me.wiefferink.interactivemessenger.processing.Message;
import me.wiefferink.interactivemessenger.source.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public final class AutoFarmer extends JavaPlugin {
    private static AutoFarmer instance = null;
    public static Logger logger;
    private static CommandManager commandManager = null;
    private static String defaultPlantMode = "WHEAT";
    private static CropManager cropManager = null;
    private static PlayerManager playerManager = null;

    public static AutoFarmer getInstance() {return AutoFarmer.instance;}
    @Override
    public void onEnable() {
        logger = getLogger();
        AutoFarmer.instance = this;

        File dir = this.getDataFolder();
        if (!dir.isDirectory()) {
            dir.mkdir();
        }
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        this.loadManagers();

        LanguageManager languageManager = new LanguageManager(
                this,                                  // The plugin (used to get the languages bundled in the jar file)
                "languages",                           // Folder where the languages are stored
                getConfig().getString("EN"),     // The language to use indicated by the plugin user
                "EN",                                  // The default language, expected to be shipped with the plugin and should be complete, fills in gaps in the user-selected language
                Collections.singletonList("&f[&6AutoFarmer&f] ") // Chat prefix to use with Message#prefix(), could of course come from the config file
        );
        registerListeners();
    }
    @Override
    public void onDisable() {

    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        debug("Registering Events");
        pm.registerEvents(new OnPlayerJoinListener(),this);
        pm.registerEvents(new OnPlayerLeaveListener(), this);
        pm.registerEvents(new CropListener(), this);
        pm.registerEvents(new OnHoeListener(), this);
    }
    private void loadManagers() {
        debug("Loading Managers!");
        cropManager = new CropManager();
        playerManager = new PlayerManager();
        commandManager = new CommandManager();
    }

    public static void messageNoPrefix(Object target, String key, Object... replacements) {
        Message.fromKey(key).replacements(replacements).send(target);
    }
    public static void message(Object target, String key, Object... replacements) {
        Message.fromKey(key).prefix().replacements(replacements).send(target);
    }
    public static CropManager getCropManager(){
        return AutoFarmer.cropManager;
    }
    public static PlayerManager getPlayerManager() {
        return AutoFarmer.playerManager;
    }
    public static CommandManager getCommandManager() {
        return AutoFarmer.commandManager;
    }

    public static boolean isDebug() {
        return true;
    }

    public static void debug(String msg) {
        if(isDebug()) {
            logger.info("DEBUG: " + msg);
        }
    }
    public static void debug(Throwable throwable) {
        if(isDebug()) {
            logger.info("DEBUG: " + throwable);
        }
    }
    public static boolean isHoe(Material m) {
        if (m == Material.WOOD_HOE) return true;
        if (m == Material.STONE_HOE) return true;
        if (m == Material.GOLD_HOE) return true;
        if (m == Material.IRON_HOE) return true;
        if (m == Material.DIAMOND_HOE) return true;
        return false;
    }
    public static String getDefaultPlantMode() {
        return defaultPlantMode;
    }
}

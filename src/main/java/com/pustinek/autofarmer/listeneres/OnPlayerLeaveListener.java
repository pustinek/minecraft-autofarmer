package com.pustinek.autofarmer.listeneres;

import com.pustinek.autofarmer.AutoFarmer;
import com.pustinek.autofarmer.PlayerData;
import com.pustinek.autofarmer.managers.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class OnPlayerLeaveListener implements Listener{
    private AutoFarmer plugin;
    private PlayerManager playerManager;

    public OnPlayerLeaveListener() {
        this.plugin = AutoFarmer.getInstance();
        this.playerManager = AutoFarmer.getPlayerManager();
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        UUID playerUUID = event.getPlayer().getUniqueId();
        this.playerManager.savePlayerDataToFile(playerUUID);
        this.playerManager.removePlayerData(playerUUID);
    }
}

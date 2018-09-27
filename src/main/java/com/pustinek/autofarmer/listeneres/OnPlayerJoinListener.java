package com.pustinek.autofarmer.listeneres;

import com.pustinek.autofarmer.AutoFarmer;
import com.pustinek.autofarmer.managers.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class OnPlayerJoinListener implements Listener {
    private AutoFarmer plugin;
    private PlayerManager playerManager;

    public OnPlayerJoinListener() {
        this.plugin = AutoFarmer.getInstance();
        this.playerManager = AutoFarmer.getPlayerManager();
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        this.playerManager.loadPlayerData(uuid);
    }

}

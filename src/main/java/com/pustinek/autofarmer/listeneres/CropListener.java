package com.pustinek.autofarmer.listeneres;

import com.pustinek.autofarmer.AutoFarmer;
import com.pustinek.autofarmer.PlayerData;
import com.pustinek.autofarmer.managers.CropManager;
import com.pustinek.autofarmer.managers.PlayerManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CropListener implements Listener{
    private AutoFarmer plugin;
    private PlayerManager playerManager;
    private CropManager cropManager;
    private ArrayList<Material> enabledCrops;
    private HashMap<Location, Material> locs = new HashMap<Location, Material>();
    public CropListener() {
        this.plugin = AutoFarmer.getInstance();
        this.playerManager = AutoFarmer.getPlayerManager();
        this.cropManager = AutoFarmer.getCropManager();
        this.enabledCrops = this.cropManager.getEnabledCrops();
    }
    @EventHandler
    public void onCropBreak(BlockBreakEvent event) {
        Block brokenBlock = event.getBlock();
        if(!enabledCrops.contains(brokenBlock.getType())){return;}

            Player player = event.getPlayer();
            PlayerData playerData = playerManager.getPlayerData(player.getUniqueId());

            Material brokenBlockMaterial = brokenBlock.getType();
            // !player.hasPermission("autofarmer.rp."+cropManager.getCropSetByMaterial(brokenBlockMaterial).getInternalName()
            AutoFarmer.debug("getAutoReplantValue of broken block ("+brokenBlockMaterial.name()+")->"+playerData.getAutoReplantValue(brokenBlockMaterial));
            if(!playerData.getAutoReplantValue(brokenBlockMaterial) ){return;}
                AutoFarmer.debug("GetCropSetInternalname from cropmanager ("+brokenBlockMaterial.name()+")->"+AutoFarmer.getCropManager().getCropSetByMaterial(brokenBlockMaterial).getInternalName());
                    switch (AutoFarmer.getCropManager().getCropSetByMaterial(brokenBlockMaterial).getInternalName()){
                        case "WHEAT":
                        case "CARROT":
                        case "POTATO":
                        case "BEETROOT":
                            if(brokenBlock.getRelative(BlockFace.DOWN).getType() == Material.SOIL
                                    && AutoFarmer.isHoe(player.getItemInHand().getType())) {
                                AutoFarmer.debug("location added of cropSeed ->" + cropManager.getCropBySeed(brokenBlockMaterial));
                                locs.put(brokenBlock.getLocation(), cropManager.getSeedByCrop(brokenBlockMaterial));
                                player.getItemInHand().setDurability((short) (player.getItemInHand().getDurability() - 2));
                            }

                            break;
                        case "NETHER_WARTS":
                            if(brokenBlock.getRelative(BlockFace.DOWN).getType() == Material.SOUL_SAND
                                    && AutoFarmer.isHoe(player.getItemInHand().getType())) {
                                locs.put(brokenBlock.getLocation(), cropManager.getSeedByCrop(brokenBlockMaterial));
                                player.getItemInHand().setDurability((short) (player.getItemInHand().getDurability() - 2));
                            }
                            break;
                        case "COCOA":
                            for(CocoaPosibleLogLocations logLocations : CocoaPosibleLogLocations.values()) {
                                if(brokenBlock.getRelative(logLocations.logLocation).getType() == Material.LOG){
                                    locs.put(brokenBlock.getLocation(), cropManager.getSeedByCrop(brokenBlockMaterial));
                                    player.getItemInHand().setDurability((short) (player.getItemInHand().getDurability() - 2));
                                }
                            }
                            break;
                    }
                }
    @EventHandler
    public void onCropDrop(ItemSpawnEvent e) {
        Location l = null;

        for (Map.Entry<Location, Material> entry : locs.entrySet()) {

            Location spawn = e.getLocation();
            spawn.setYaw(0);
            spawn.setPitch(0);
            spawn.setX(Location.locToBlock(spawn.getX()));
            spawn.setZ(Location.locToBlock(spawn.getZ()));
            spawn.setY(Location.locToBlock(spawn.getY())); // Normalising the location

            Location map = entry.getKey();
            map.setPitch(0);
            map.setYaw(0); // Normalising the location

            if(spawn.equals(map)) {
                ItemStack i = e.getEntity().getItemStack();
                if(i.getType() == entry.getValue() || i.getType() == Material.INK_SACK) {
                    l = entry.getKey(); // To prevent CME
                    Block b = e.getLocation().getBlock();
                    Material cropToPlant = cropManager.getCropBySeed(entry.getValue());
                    if(cropToPlant != null){
                        b.setType(cropToPlant);
                    }
                    e.setCancelled(true);
                }
            }
        }
        if (l != null) locs.remove(l); // To prevent CME
    }
    private enum CocoaPosibleLogLocations {
        NORTH(BlockFace.NORTH),
        SOUTH(BlockFace.SOUTH),
        EAST(BlockFace.EAST),
        WEST(BlockFace.WEST);

        private final BlockFace logLocation;
        CocoaPosibleLogLocations(BlockFace logLocation) {
            this.logLocation = logLocation;
        }

    }




}

package com.pustinek.autofarmer.listeneres;

import com.pustinek.autofarmer.AutoFarmer;
import com.pustinek.autofarmer.CropSet;
import com.pustinek.autofarmer.PlayerData;
import com.pustinek.autofarmer.managers.CropManager;
import com.pustinek.autofarmer.managers.PlayerManager;
import com.pustinek.autofarmer.utils.Utils;
import com.sun.scenario.effect.Crop;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.material.CocoaPlant;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CropListener implements Listener{
    private AutoFarmer plugin;
    private PlayerManager playerManager;
    private CropManager cropManager;
    private ArrayList<Material> validBreakableCrops;
    private HashMap<Location, CropSet> locs = new HashMap<>();
    public CropListener() {
        this.plugin = AutoFarmer.getInstance();
        this.playerManager = AutoFarmer.getPlayerManager();
        this.cropManager = AutoFarmer.getCropManager();
        this.validBreakableCrops = cropManager.getReplantableCrops();
    }
    @EventHandler
    public void onCropBreak(BlockBreakEvent event) {
        Block brokenBlock = event.getBlock();
        CropSet cropSet = null;
        if(validBreakableCrops.contains(brokenBlock.getType())){
            cropSet = cropManager.getCropSet(brokenBlock.getType());
        }else{
            return;
        }

        if(cropSet == null){return;}
            Player player = event.getPlayer();
            PlayerData playerData = playerManager.getPlayerData(player.getUniqueId());
            if(!playerData.isEnabled() || !player.hasPermission("autofarmer.replant")){
                return;
            }
            if(!playerData.getReplantValue(cropSet.getInternalName())){return;}
                    switch (cropSet.getInternalName()){
                        case "WHEAT":
                        case "CARROT":
                        case "POTATO":
                        case "BEETROOT":
                            if(brokenBlock.getRelative(BlockFace.DOWN).getType() == Material.SOIL
                                    && AutoFarmer.isHoe(player.getItemInHand().getType())) {
                                //AutoFarmer.debug("location added of cropSeed ->" + cropManager.getCropBySeed(brokenBlockMat));
                                locs.put(brokenBlock.getLocation(), cropSet);
                                player.getItemInHand().setDurability((short) (player.getItemInHand().getDurability() - 2));
                            }

                            break;
                            //TODO: fix nether_warts replant, not working
                        case "NETHER_WARTS":
                            if(brokenBlock.getRelative(BlockFace.DOWN).getType() == Material.SOUL_SAND
                                    && AutoFarmer.isHoe(player.getItemInHand().getType())) {
                                locs.put(brokenBlock.getLocation(), cropSet);
                                player.getItemInHand().setDurability((short) (player.getItemInHand().getDurability() - 2));
                            }
                            break;
                        case "COCOA":
                            if(AutoFarmer.isAxe(player.getItemInHand().getType())) {
                                for(CocoaPosibleLogLocations logLocations : CocoaPosibleLogLocations.values()) {
                                    if(brokenBlock.getRelative(logLocations.logLocation).getType() == Material.LOG){
                                        locs.put(brokenBlock.getLocation(), cropSet);
                                        player.getItemInHand().setDurability((short) (player.getItemInHand().getDurability() - 2));
                                    }
                                }

                            }
                            break;
                    }
                }
    @EventHandler
    public void onCropDrop(ItemSpawnEvent e) {
        Location l = null;
        for (Map.Entry<Location, CropSet> entry : locs.entrySet()) {
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
                CropSet cropSet = entry.getValue();
                if(i.getType() == cropSet.getSeedMaterial() || i.getType() == Material.INK_SACK) {
                    l = entry.getKey(); // To prevent CME
                    Block b = e.getLocation().getBlock();
                    Material cropToPlant = cropSet.getCropMaterial();
                    if(cropToPlant != null){
                        switch (cropSet.getInternalName()){
                            case "COCOA":
                                for(CocoaPosibleLogLocations logLocations : CocoaPosibleLogLocations.values()) {
                                    if(map.getBlock().getRelative(logLocations.logLocation).getType() == Material.LOG){
                                        try {
                                            b.setType(cropToPlant);
                                            BlockState state = b.getState();
                                            CocoaPlant coco = new CocoaPlant(CocoaPlant.CocoaPlantSize.SMALL,logLocations.logLocation);
                                            state.setData(coco);
                                            state.update();
                                        }catch (Exception ex) {
                                            ex.printStackTrace();
                                        }

                                    }
                                }
                                break;
                                default:
                                    b.setType(cropToPlant);
                                    break;
                        }
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
        public String getFacingString() {
            return logLocation.name();
        }

    }




}

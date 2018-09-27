package com.pustinek.autofarmer.listeneres;

import com.pustinek.autofarmer.AutoFarmer;
import com.pustinek.autofarmer.CropSet;
import com.pustinek.autofarmer.PlayerData;
import com.pustinek.autofarmer.managers.CropManager;
import com.pustinek.autofarmer.managers.PlayerManager;
import com.pustinek.autofarmer.utils.Utils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class OnHoeListener implements Listener{
    private AutoFarmer plugin;
    private CropManager cropManager;
    private PlayerManager playerManager;

    public OnHoeListener() {
        this.plugin = AutoFarmer.getInstance();
        this.cropManager = AutoFarmer.getCropManager();
        this.playerManager = AutoFarmer.getPlayerManager();
    }

    @EventHandler
    public void onTillGround(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK || e.getItem() == null || !(Utils.isHoe(e.getItem().getType()))){return;}

        Block clickedBlock = e.getClickedBlock();
        Player player = e.getPlayer();
        PlayerData playerData = playerManager.getPlayerData(player.getUniqueId());
        if (!player.hasPermission("autofarmer.plant")){return;}
        if(!playerData.isEnabled()){return;}
        if(AutoFarmer.hasWorldGuard()){
            if(!AutoFarmer.getWorldGuardPlugin().canBuild(player,clickedBlock.getLocation())) {
                return;
            }
        }


        String playerPlantMode = playerData.getSelectedPlantMode();

        if(clickedBlock.getType() == Material.DIRT || clickedBlock.getType() == Material.GRASS) {
            if (clickedBlock.getRelative(BlockFace.UP).isEmpty()) {
                CropSet cropSet = cropManager.getCropSet(playerPlantMode);
                if(cropSet == null){return;}
                if (cropSet.getSeedMaterial() != Material.NETHER_WARTS) {
                    if (player.getInventory().contains(cropSet.getSeedMaterial())) {
                        ItemStack it = null;
                        int in = 0;
                        ItemStack[] contents = player.getInventory().getContents();
                        for (int i = 0; i < contents.length; i++) {
                            if (contents[i] != null) {
                                if (contents[i].getType() == cropSet.getSeedMaterial()) {
                                    it = contents[i].clone(); // Getting a copy of seed stack
                                    in = i; // Getting index of said stack in inventory
                                }
                            }
                        }
                        if (it != null) {

                            if (it.getAmount() != 1)
                                player.getInventory().setItem(in, new ItemStack(it.getType(), (it.getAmount() - 1)));
                                // Removing the seeds from the inventory
                            else player.getInventory().setItem(in, null);
                            // Removing the seeds from the inventory
                            player.updateInventory();
                            Block up = clickedBlock.getRelative(BlockFace.UP);
                            up.setType(cropSet.getCropMaterial());
                            clickedBlock.setType(Material.SOIL);
                            // Changing blocks.


                        }
                    }
                }
            }
        }
    }


}

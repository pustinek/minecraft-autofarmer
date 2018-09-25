package com.pustinek.autofarmer.listeneres;

import com.pustinek.autofarmer.AutoFarmer;
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
        if(!(e.getAction() == Action.RIGHT_CLICK_BLOCK)) {return;}
        if(e.getItem() == null || !(Utils.isHoe(e.getItem().getType()))) {return;}
        Block b = e.getClickedBlock();
        Player p = e.getPlayer();
        PlayerData pd = playerManager.getPlayerData(p.getUniqueId());
        String playerPlantMode;
        if(pd.getSelectedPlantMode() == null)
        {
            return;
        }
        else{
            playerPlantMode = pd.getSelectedPlantMode();
        }
        if(!pd.isEnabled()){
            return;
        }
        if(b.getType() == Material.DIRT || b.getType() == Material.GRASS) {
            if (b.getRelative(BlockFace.UP).isEmpty()) {


                Material playerPlantModeCrop = cropManager.getCropSetByInternalName(playerPlantMode).getCropMaterial();
                Material playerPlantModeSeed = cropManager.getCropSetByInternalName(playerPlantMode).getSeedMaterial();
                if (p.hasPermission("autofarm.plant." + playerPlantMode) && playerPlantModeSeed != Material.NETHER_STALK) {
                    if (p.getInventory().contains(playerPlantModeSeed)) {
                        ItemStack it = null;
                        int in = 0;
                        ItemStack[] contents = p.getInventory().getContents();
                        for (int i = 0; i < contents.length; i++) {
                            if (contents[i] != null) {
                                if (contents[i].getType() == playerPlantModeSeed) {
                                    it = contents[i].clone(); // Getting a copy of seed stack
                                    in = i; // Getting index of said stack in inventory
                                }
                            }
                        }
                        if (it != null) {

                            if (it.getAmount() != 1)
                                p.getInventory().setItem(in, new ItemStack(it.getType(), (it.getAmount() - 1)));
                                // Removing the seeds from the inventory
                            else p.getInventory().setItem(in, null);
                            // Removing the seeds from the inventory
                            p.updateInventory();
                            Block up = b.getRelative(BlockFace.UP);
                            up.setType(playerPlantModeCrop);
                            b.setType(Material.SOIL);
                            // Changing blocks.


                        }
                    }
                }
            }
        }
    }


}

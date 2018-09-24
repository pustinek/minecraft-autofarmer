package com.pustinek.autofarmer;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CropSet implements Comparable<Material>{
    private String internalName;
    private Material seed;
    private Material crop;
    private String permission;
    private ItemStack harvestingTool;

    public CropSet(String internalName, Material crop, Material seed) {
        this.internalName = internalName;
        this.seed = seed;
        this.crop = crop;
    }
    public Material getCropMaterial(){
        return this.crop;
    }
    public Material getSeedMaterial(){
        return this.seed;
    }
    public String getInternalName(){
        return this.internalName;
    }

    @Override
    public int compareTo(Material otherCrop) {
        if(otherCrop == this.crop){
            return 1;
        }else{return 0;}
    }
}

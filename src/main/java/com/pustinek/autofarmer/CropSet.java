package com.pustinek.autofarmer;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CropSet implements Comparable<Material>{
    private String internalName;
    private Material seed;
    private Material crop;
    private Boolean autoPlantable;
    private Boolean autoReplantable = true;

    public CropSet(String internalName, Material crop, Material seed, Boolean autoPlantable) {
        this.internalName = internalName;
        this.seed = seed;
        this.crop = crop;
        this.autoPlantable = autoPlantable;
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
    public Boolean isAutoPlantable() {return this.autoPlantable; }
    @Override
    public int compareTo(Material otherCrop) {
        if(otherCrop == this.crop){
            return 1;
        }else{return 0;}
    }
}

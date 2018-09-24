package com.pustinek.autofarmer.managers;

import com.pustinek.autofarmer.AutoFarmer;
import com.pustinek.autofarmer.CropSet;
import com.pustinek.autofarmer.utils.Utils;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class CropManager{

    private HashMap<String,CropSet> cropSetMap = new HashMap<>();

    private final AutoFarmer plugin;

    private ArrayList<Material> enabledCrops = new ArrayList<>(Arrays.asList(
            Material.CARROT,Material.POTATO, Material.MELON_STEM,
            Material.MELON_SEEDS, Material.PUMPKIN_STEM,
            Material.WHEAT, Material.CROPS, Material.COCOA,Material.BEETROOT
    ));
    private ArrayList<String> enabledAutoPlantModes = new ArrayList<>(Arrays.asList(CropsOld.WHEAT.name(),CropsOld.CARROT.name(),CropsOld.BEETROOT.name(),CropsOld.POTATO.name()));
    public CropManager(){
        this.plugin = AutoFarmer.getInstance();
        switch (Utils.getMajorVersion()) {
            case (12):
                for(CropsOld crop : CropsOld.values()) {
                    CropSet c = new CropSet(crop.name(),crop.crop,crop.seed);
                    this.cropSetMap.put(crop.name(),c);
                    //AutoFarmer.debug("creating crop ->" + crop.name() + ", " + crop.crop.name() + ", " + crop.seed.name());
                }
                break;
            case(13):
                break;
        }
    }
    public HashMap<String,CropSet> getCropSet() {
        return this.cropSetMap;
    }

    public CropSet getCropSetByMaterial(Material cropMaterial) {
        for(Map.Entry<String, CropSet> entry : cropSetMap.entrySet()) {
            String entryInternalName = entry.getKey();
            CropSet entryCropSet = entry.getValue();
            if(entryCropSet.compareTo(cropMaterial) == 1) {
                return entryCropSet;
            }
        }
        return new CropSet("error",Material.AIR,Material.AIR);
    }


    public Material getSeedByCrop(Material cropMat) {
        for(Map.Entry<String, CropSet> entry : cropSetMap.entrySet()) {
            CropSet entryCropSet = entry.getValue();
            if(entryCropSet.compareTo(cropMat) == 1) {
                return entryCropSet.getSeedMaterial();
            }

        }
        return Material.AIR;
    }
    public Material getCropBySeed(Material seedMat) {
        for (Map.Entry<String, CropSet> entry : cropSetMap.entrySet()) {
            CropSet entryCropSet = entry.getValue();
            if(entryCropSet.getSeedMaterial() == seedMat) {
                return entryCropSet.getCropMaterial();
            }

        }
        return Material.AIR;
    }
    public CropSet getCropSetByInternalName(String internalName) {
        return this.cropSetMap.get(internalName);
    }
    public ArrayList<String> getCropSetInternalNames() {
        ArrayList<String> keys = new ArrayList<>(this.getCropSet().keySet());
        return keys;
    }
    //TODO: Implement logic to enable cropSetMap in config !
    public boolean cropIsEnabled(Material cropMaterial) {
        return true;
    }

    public ArrayList<Material> getEnabledCrops() {
        return enabledCrops;
    }

    public ArrayList<String> getEnabledAutoPlantModes() {
        return enabledAutoPlantModes;
    }

    @Deprecated
    private enum CropsOld {
        WHEAT(Material.CROPS, Material.SEEDS,"HOE"),
        CARROT(Material.CARROT, Material.CARROT_ITEM,"HOE"),
        POTATO(Material.POTATO, Material.POTATO_ITEM,"HOE"),
        NETHER_WARTS(Material.NETHER_WARTS, Material.NETHER_WARTS,"HOE"),
        BEETROOT(Material.BEETROOT, Material.BEETROOT_SEEDS,"HOE"),
        COCOA(Material.COCOA, Material.COCOA,"AXE");

        public Material crop;
        public Material seed;
        public String harvestTool;

        CropsOld(Material crop, Material seed, String harvestTool) {
            this.crop = crop;
            this.seed = seed;
            this.harvestTool = harvestTool;
        }
        public Material getCrops() {
            return this.crop;
        }
    }



}

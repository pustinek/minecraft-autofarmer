package com.pustinek.autofarmer.managers;

import com.pustinek.autofarmer.AutoFarmer;
import com.pustinek.autofarmer.CropSet;
import com.pustinek.autofarmer.utils.Utils;
import org.bukkit.Material;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class CropManager{

    private HashMap<String,CropSet> cropSetMap = new HashMap<>();
    private ArrayList<CropSet> cropSetArrayList = new ArrayList<>();


    private final AutoFarmer plugin;

    private ArrayList<String> enabledReplantModes = new ArrayList<>();
    public CropManager(){
        this.plugin = AutoFarmer.getInstance();
        switch (Utils.getMajorVersion()) {
            case (12):
                for(CropsOld crop : CropsOld.values()) {
                    CropSet cropSet = new CropSet(crop.name(),crop.crop,crop.seed,crop.autoPlantable);
                    this.enabledReplantModes.add(crop.name());
                    this.cropSetMap.put(crop.name(),cropSet);
                    this.cropSetArrayList.add(cropSet);
                }
                break;
            case(13):
                //TODO: Implement 1.13 support
                break;
        }
    }
    public HashMap<String,CropSet> getCropSet() {
        return this.cropSetMap;
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

    //***************************************NEW IMPLEMENTATIONS !!*********************************************************
    public ArrayList<String> getPlantableModeList() {
        ArrayList<String> plantableModes = new ArrayList<>();
        for(CropSet cropSet : cropSetArrayList) {
            if(cropSet.isAutoPlantable()) {
                plantableModes.add(cropSet.getInternalName());
            }
        }
        return plantableModes;
    }
    public ArrayList<String> getReplantableModesList() {
        ArrayList<String> replantableModes = new ArrayList<>();
        for(CropSet cropSet : cropSetArrayList) {
                replantableModes.add(cropSet.getInternalName());
        }
        return replantableModes;
    }
    //Transform crop Material to internal_name/mode
    public String cropToMode(Material crop) {
        for(CropSet cropSet : cropSetArrayList) {
            if(cropSet.getCropMaterial() == crop) {
                return  cropSet.getInternalName();
            }
        }
        return "";
    }
    public ArrayList<Material> getReplantableCrops() {
        ArrayList<Material> replantableCrops = new ArrayList<>();
        for(CropSet cropSet : cropSetArrayList) {
            replantableCrops.add(cropSet.getCropMaterial());
        }
        return replantableCrops;
    }
    @Deprecated
    private enum CropsOld {
        WHEAT(Material.CROPS, Material.SEEDS,"HOE",true),
        CARROT(Material.CARROT, Material.CARROT_ITEM,"HOE",true),
        POTATO(Material.POTATO, Material.POTATO_ITEM,"HOE",true),
        NETHER_WARTS(Material.NETHER_WARTS, Material.NETHER_WARTS,"HOE",false),
        BEETROOT(Material.BEETROOT_BLOCK, Material.BEETROOT_SEEDS,"HOE",true),
        COCOA(Material.COCOA, Material.COCOA,"AXE",false);

        public Material crop;
        public Material seed;
        public String harvestTool;
        public boolean autoPlantable;

        CropsOld(Material crop, Material seed, String harvestTool,Boolean autoPlantable) {
            this.crop = crop;
            this.seed = seed;
            this.harvestTool = harvestTool;
            this.autoPlantable = autoPlantable;
        }
        public Material getCrops() {
            return this.crop;
        }
    }



}

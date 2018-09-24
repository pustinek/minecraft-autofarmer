package com.pustinek.autofarmer.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;

public class Utils {



    /**
     * @return The current server version with revision number (e.g. v1_9_R2, v1_10_R1)
     */
    public static String getServerVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();

        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }
    /**
     * @return The revision of the current server version (e.g. <i>2</i> for v1_9_R2, <i>1</i> for v1_10_R1)
     */
    public static int getRevision() {
        return Integer.parseInt(getServerVersion().substring(getServerVersion().length() - 1));
    }
    /**
     * @return The major version of the server (e.g. <i>9</i> for 1.9.2, <i>10</i> for 1.10)
     */
    public static int getMajorVersion() {
        return Integer.parseInt(getServerVersion().split("_")[1]);
    }


    public static boolean isHoe(Material m) {
        if (m == Material.WOOD_HOE) return true;
        if (m == Material.STONE_HOE) return true;
        if (m == Material.GOLD_HOE) return true;
        if (m == Material.IRON_HOE) return true;
        if (m == Material.DIAMOND_HOE) return true;
        return false;
    }
}

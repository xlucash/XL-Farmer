package me.xlucash.farmer.utils;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class CropTranslation {

    private CropTranslation() {
    }

    private static final Map<Material, String> cropTranslations = new HashMap<>();

    static {
        cropTranslations.put(Material.CARROT, "Marchewki");
        cropTranslations.put(Material.POTATO, "Ziemniaka");
        cropTranslations.put(Material.BEETROOT, "Buraka");
        cropTranslations.put(Material.SUGAR_CANE, "Trzciny");
        cropTranslations.put(Material.MELON, "Melona");
        cropTranslations.put(Material.MELON_SLICE, "Melona");
        cropTranslations.put(Material.PUMPKIN, "Melona");
        cropTranslations.put(Material.SWEET_BERRIES, "Jagód");
        cropTranslations.put(Material.BAMBOO, "Bambusa");
    }

    public static String translate(Material material) {
        return cropTranslations.getOrDefault(material, material.name().toLowerCase());
    }
}

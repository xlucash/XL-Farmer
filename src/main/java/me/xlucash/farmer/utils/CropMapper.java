package me.xlucash.farmer.utils;

import org.bukkit.Material;

import java.util.EnumMap;

public class CropMapper {

    private CropMapper() {}

    private static final EnumMap<Material, Material> itemToCrop = new EnumMap<>(Material.class);
    private static final EnumMap<Material, Material> cropToItem = new EnumMap<>(Material.class);

    static {
        itemToCrop.put(Material.CARROT, Material.CARROTS);
        itemToCrop.put(Material.POTATO, Material.POTATOES);
        itemToCrop.put(Material.BEETROOT, Material.BEETROOTS);
        cropToItem.put(Material.CARROTS, Material.CARROT);
        cropToItem.put(Material.POTATOES, Material.POTATO);
        cropToItem.put(Material.BEETROOTS, Material.BEETROOT);
    }

    public static Material getCorrespondingCrop(Material itemCrop) {
        return itemToCrop.getOrDefault(itemCrop, itemCrop);
    }

    public static Material getCorrespondingItem(Material crop) {
        return cropToItem.getOrDefault(crop, crop);
    }

}

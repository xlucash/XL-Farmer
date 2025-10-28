package me.xlucash.farmer.utils;

import org.bukkit.Material;

public class CropChecker {

    public static boolean isCrop(Material material) {
        return switch (material) {
            case WHEAT, WHEAT_SEEDS, CARROT, POTATO, BEETROOT, CARROTS, POTATOES, BEETROOTS, SUGAR_CANE, BAKED_POTATO,
                 ATTACHED_MELON_STEM, MELON_STEM, MELON_SLICE, MELON, MELON_SEEDS, ATTACHED_PUMPKIN_STEM, PUMPKIN,
                 PUMPKIN_STEM, PUMPKIN_SEEDS, NETHER_WART, NETHER_WART_BLOCK, BEETROOT_SEEDS, SWEET_BERRIES,
                 SWEET_BERRY_BUSH, CACTUS, BAMBOO_SAPLING, BAMBOO -> true;
            default -> false;
        };
    }

    public static boolean isCropAddingExperience(Material material) {
        return switch (material) {
            case WHEAT, CARROT, POTATO, BEETROOT, CARROTS, POTATOES, BEETROOTS, SUGAR_CANE,
                 MELON, PUMPKIN, NETHER_WART, NETHER_WART_BLOCK, BEETROOT_SEEDS -> true;
            default -> false;
        };
    }
}

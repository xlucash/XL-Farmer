package me.xlucash.farmer.models;

import org.bukkit.Material;

public class Crop {
    private Material cropMaterial;
    private int levelRequired;

    public Crop(Material cropMaterial, int levelRequired) {
        this.cropMaterial = cropMaterial;
        this.levelRequired = levelRequired;
    }

    public Crop(String cropMaterial, int levelRequired) {
        this.cropMaterial = Material.valueOf(cropMaterial);
        this.levelRequired = levelRequired;
    }

    public Material getCropMaterial() {
        return cropMaterial;
    }

    public void setCropMaterial(Material cropMaterial) {
        this.cropMaterial = cropMaterial;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public void setLevelRequired(int levelRequired) {
        this.levelRequired = levelRequired;
    }
}

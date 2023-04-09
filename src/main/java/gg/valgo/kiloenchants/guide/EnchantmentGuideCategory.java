package gg.valgo.kiloenchants.guide;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;

public class EnchantmentGuideCategory {
    private String name;
    private Material icon;
    private ArrayList<Material> materials;

    public EnchantmentGuideCategory(String name, Material icon, Material... materials) {
        this.name = name;
        this.icon = icon;
        this.materials = (ArrayList<Material>) Arrays.asList(materials);
    }

    public boolean containsEntry(EnchantmentGuideEntry entry) {
        for (Material material : entry.getApplicableItems()) {
            for (Material otherMaterial : materials) {
                if (material.equals(otherMaterial)) {
                    return true;
                }
            }
        }

        return false;
    }
}
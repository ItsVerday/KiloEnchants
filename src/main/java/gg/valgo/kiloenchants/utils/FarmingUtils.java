package gg.valgo.kiloenchants.utils;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;

import java.util.HashMap;

public class FarmingUtils {
    public static boolean isSeeds(Material material) {
        switch (material) {
            case WHEAT_SEEDS:
            case BEETROOT_SEEDS:
            case MELON_SEEDS:
            case PUMPKIN_SEEDS:
                return true;
        }

        return false;
    }

    public static boolean isCropStem(Material material) {
        switch (material) {
            case WHEAT:
            case MELON_STEM:
            case PUMPKIN_STEM:
            case POTATOES:
            case CARROTS:
            case BEETROOTS:
                return true;
        }

        return false;
    }

    public static boolean isValidCrop(Block block) {
        return block.getBlockData() instanceof Ageable;
    }

    public static boolean isGrowingCrop(Block block) {
        BlockData blockData = block.getBlockData();
        if (!(blockData instanceof Ageable)) {
            return false;
        }

        Ageable ageable = (Ageable) blockData;
        return ageable.getAge() != ageable.getMaximumAge();
    }

    public static boolean ageCrop(Block block) {
        BlockData blockData = block.getBlockData();
        if (!(blockData instanceof Ageable)) {
            return false;
        }

        Ageable ageable = (Ageable) blockData;
        if (ageable.getAge() == ageable.getMaximumAge()) {
            return false;
        }

        ageable.setAge(ageable.getAge() + 1);
        block.setBlockData(ageable);
        return true;
    }

    private static final HashMap<Material, Material> seeds = new HashMap<>();

    static {
        seeds.put(Material.WHEAT_SEEDS, Material.WHEAT);
        seeds.put(Material.MELON_SEEDS, Material.MELON_STEM);
        seeds.put(Material.PUMPKIN_SEEDS, Material.PUMPKIN_STEM);
        seeds.put(Material.POTATO, Material.POTATOES);
        seeds.put(Material.CARROT, Material.CARROTS);
        seeds.put(Material.BEETROOT, Material.BEETROOTS);
    }

    public static HashMap<Material, Material> getSeeds() {
        return (HashMap<Material, Material>) seeds.clone();
    }
}
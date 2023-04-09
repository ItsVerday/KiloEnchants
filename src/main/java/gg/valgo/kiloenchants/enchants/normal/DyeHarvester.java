package gg.valgo.kiloenchants.enchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import gg.valgo.kiloenchants.KiloEnchant;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class DyeHarvester extends KiloEnchant {
    private static Random random = new Random();
    private static HashMap<Material, Material> flowerDyes = new HashMap<>();

    public DyeHarvester() {
        super("dye_harvester", EnchantmentType.NORMAL);
    }

    @Override
    public ArrayList<ItemStack> modifyDrops(Player player, Block block, ArrayList<ItemStack> drops, int level, BlockDropItemEvent evt) {
        int minimumCount = getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "minimum-count");
        int maximumCount = getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "maximum-count");

        Material blockType = evt.getBlockState().getType();
        if (!flowerDyes.containsKey(blockType)) {
            return drops;
        }

        for (ItemStack drop : drops) {
            if (flowerDyes.containsKey(drop.getType())) {
                drop.setType(flowerDyes.get(drop.getType()));
            }

            drop.setAmount(drop.getAmount() * random.nextInt(maximumCount - minimumCount + 1) + minimumCount);
        }

        return drops;
    }

    static {
        flowerDyes.put(Material.DANDELION, Material.YELLOW_DYE);
        flowerDyes.put(Material.POPPY, Material.RED_DYE);
        flowerDyes.put(Material.BLUE_ORCHID, Material.LIGHT_BLUE_DYE);
        flowerDyes.put(Material.ALLIUM, Material.MAGENTA_DYE);
        flowerDyes.put(Material.AZURE_BLUET, Material.LIGHT_GRAY_DYE);
        flowerDyes.put(Material.RED_TULIP, Material.RED_DYE);
        flowerDyes.put(Material.ORANGE_TULIP, Material.ORANGE_DYE);
        flowerDyes.put(Material.WHITE_TULIP, Material.LIGHT_GRAY_DYE);
        flowerDyes.put(Material.PINK_TULIP, Material.PINK_DYE);
        flowerDyes.put(Material.OXEYE_DAISY, Material.LIGHT_GRAY_DYE);
        flowerDyes.put(Material.CORNFLOWER, Material.BLUE_DYE);
        flowerDyes.put(Material.LILY_OF_THE_VALLEY, Material.WHITE_DYE);
        flowerDyes.put(Material.WITHER_ROSE, Material.BLACK_DYE);
    }
}
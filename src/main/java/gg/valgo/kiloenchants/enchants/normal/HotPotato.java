package gg.valgo.kiloenchants.enchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import gg.valgo.kiloenchants.KiloEnchant;
import gg.valgo.kiloenchants.KiloEnchantsExtension;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class HotPotato extends KiloEnchant {
    private static Random random = new Random();

    public HotPotato() {
        super("hot_potato", EnchantmentType.NORMAL);
    }

    @Override
    public String getPlaceholder(int i) {
        return KiloEnchantsExtension.scalingPlaceholder(this, i, "base-chance", "chance-per-level", 100);
    }

    @Override
    public ArrayList<ItemStack> modifyDropsLate(Player player, Block block, ArrayList<ItemStack> drops, int level, BlockDropItemEvent evt) {
        if (!evt.getBlockState().getBlockData().getMaterial().equals(Material.POTATOES)) {
            return drops;
        }

        boolean requireFullyGrown = getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "require-fully-grown");

        Ageable ageable = (Ageable) evt.getBlockState().getBlockData();
        if (ageable.getAge() != ageable.getMaximumAge() && requireFullyGrown) {
            return drops;
        }

        double baseChance = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "base-chance");
        double chancePerLevel = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level");
        double chance = baseChance + chancePerLevel * (level - 1);

        boolean sound = getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "play-sound");
        boolean doSound = false;
        int cookCount = 0;

        for (ItemStack itemStack : drops) {
            if (!itemStack.getType().equals(Material.POTATO)) {
                continue;
            }

            int count = itemStack.getAmount();
            int noCookCount = 0;

            for (int i = 0; i < count; i++) {
                if (random.nextDouble() < chance) {
                    cookCount++;
                    doSound = true;
                } else {
                    noCookCount++;
                }
            }

            itemStack.setAmount(noCookCount);
        }

        drops.add(new ItemStack(Material.BAKED_POTATO, cookCount));

        if (sound && doSound) {
            player.getWorld().playSound(block.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 0.25f, 1);
        }

        return drops;
    }
}
package gg.valgo.kiloenchants.enchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import gg.valgo.kiloenchants.KiloEnchant;
import gg.valgo.kiloenchants.KiloEnchantsExtension;
import gg.valgo.kiloenchants.utils.FarmingUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class CropGambler extends KiloEnchant {
    private static Random random = new Random();

    public CropGambler() {
        super("crop_gambler", EnchantmentType.NORMAL);
    }

    @Override
    public String getPlaceholder(int i) {
        return KiloEnchantsExtension.scalingPlaceholder(this, i, "base-chance", "chance-per-level", 100);
    }

    @Override
    public ArrayList<ItemStack> modifyDropsEarly(Player player, Block block, ArrayList<ItemStack> drops, int level, BlockDropItemEvent evt) {
        if (!(evt.getBlockState().getBlockData() instanceof Ageable)) {
            return drops;
        }

        if (block.getType().equals(Material.SUGAR_CANE) || block.getType().equals(Material.SWEET_BERRY_BUSH)) {
            return drops;
        }

        boolean requireFullyGrown = getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "require-fully-grown");
        boolean ignoreSeeds = getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "ignore-seeds");

        Ageable ageable = (Ageable) evt.getBlockState().getBlockData();
        if (ageable.getAge() != ageable.getMaximumAge() && requireFullyGrown) {
            return drops;
        }

        double baseChance = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "base-chance");
        double chancePerLevel = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level");
        double chance = baseChance + chancePerLevel * (level - 1);

        double baseMultiplier = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "base-multiplier");
        double multiplierPerLevel = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier-per-level");
        double multiplierDouble = baseMultiplier + multiplierPerLevel * (level - 1);

        for (ItemStack itemStack : drops) {
            if (FarmingUtils.isSeeds(itemStack.getType()) && ignoreSeeds) {
                continue;
            }

            int multiplier = 1;
            if (random.nextDouble() < chance) {
                multiplier = (int) Math.floor(multiplierDouble);

                if (random.nextDouble() < multiplierDouble % 1) {
                    multiplier++;
                }
            }

            itemStack.setAmount(itemStack.getAmount() * multiplier);
        }

        return drops;
    }
}
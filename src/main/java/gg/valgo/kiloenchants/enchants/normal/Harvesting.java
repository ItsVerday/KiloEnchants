package gg.valgo.kiloenchants.enchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import gg.valgo.kiloenchants.KiloEnchant;
import gg.valgo.kiloenchants.utils.FarmingUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class Harvesting extends KiloEnchant {
    private static Random random = new Random();

    public Harvesting() {
        super("harvesting", EnchantmentType.NORMAL);
    }

    @Override
    public ArrayList<ItemStack> modifyDrops(Player player, Block block, ArrayList<ItemStack> drops, int level, BlockDropItemEvent evt) {
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

        for (ItemStack itemStack : drops) {
            if (FarmingUtils.isSeeds(itemStack.getType()) && ignoreSeeds) {
                continue;
            }

            int multiplier = Math.max(random.nextInt(level + 2), 1);
            itemStack.setAmount(itemStack.getAmount() * multiplier);
        }

        return drops;
    }
}
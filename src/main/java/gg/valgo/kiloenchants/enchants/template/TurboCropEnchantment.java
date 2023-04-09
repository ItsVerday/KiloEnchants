package gg.valgo.kiloenchants.enchants.template;

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
import java.util.Arrays;
import java.util.List;

public class TurboCropEnchantment extends KiloEnchant {
    private List<Material> multiplyMaterials;

    public TurboCropEnchantment(String id, Material... multiplyMaterials) {
        super(id, EnchantmentType.NORMAL);
        this.multiplyMaterials = Arrays.asList(multiplyMaterials);
    }

    @Override
    public String getPlaceholder(int i) {
        return KiloEnchantsExtension.scalingPlaceholder(this, i, "base-multiplier", "multiplier-per-level", 1);
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

        Ageable ageable = (Ageable) evt.getBlockState().getBlockData();
        if (ageable.getAge() != ageable.getMaximumAge() && requireFullyGrown) {
            return drops;
        }

        double baseMultiplier = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "base-multiplier");
        double multiplierPerLevel = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "multiplier-per-level");
        double multiplier = baseMultiplier + multiplierPerLevel * (level - 1);
        boolean ignoreSeeds = getConfig().has(EcoEnchants.CONFIG_LOCATION + "ignore-seeds") && getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "ignore-seeds");

        for (ItemStack itemStack : drops) {
            if (!multiplyMaterials.contains(itemStack.getType())) {
                continue;
            }

            if (FarmingUtils.isSeeds(itemStack.getType()) && ignoreSeeds) {
                continue;
            }

            itemStack.setAmount((int) Math.ceil(itemStack.getAmount() * multiplier));
        }

        return drops;
    }
}
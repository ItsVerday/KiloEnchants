package gg.valgo.kiloenchants.enchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import gg.valgo.kiloenchants.KiloEnchant;
import gg.valgo.kiloenchants.KiloEnchantsExtension;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class Orchard extends KiloEnchant {
    private static Random random = new Random();

    public Orchard() {
        super("orchard", EnchantmentType.NORMAL);
    }

    @Override
    public String getPlaceholder(int i) {
        return KiloEnchantsExtension.scalingPlaceholder(this, i, "apple-chance", "apple-chance", 100);
    }

    @Override
    public ArrayList<ItemStack> modifyDrops(Player player, Block block, ArrayList<ItemStack> drops, int level, BlockDropItemEvent evt) {
        if (!(evt.getBlockState().getBlockData() instanceof Leaves)) {
            return drops;
        }

        double appleChance = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "apple-chance");

        if (random.nextDouble() > appleChance * level) {
            return drops;
        }

        drops.add(new ItemStack(Material.APPLE));
        return drops;
    }
}
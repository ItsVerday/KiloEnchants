package gg.valgo.kiloenchants;

import com.willfp.eco.core.Prerequisite;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class KiloEnchant extends EcoEnchant {
    public KiloEnchant(String id, EnchantmentType type, Prerequisite... prerequisites) {
        super(id, type, prerequisites);
    }

    public void onEnable() {}

    public ArrayList<ItemStack> modifyDrops(Player player, Block block, ArrayList<ItemStack> drops, int level, BlockDropItemEvent evt) {
        return drops;
    }

    public ArrayList<ItemStack> modifyDropsEarly(Player player, Block block, ArrayList<ItemStack> drops, int level, BlockDropItemEvent evt) {
        return drops;
    }

    public ArrayList<ItemStack> modifyDropsLate(Player player, Block block, ArrayList<ItemStack> drops, int level, BlockDropItemEvent evt) {
        return drops;
    }
}
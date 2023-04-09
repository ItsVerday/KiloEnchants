package gg.valgo.kiloenchants.guide;

import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;

import java.util.ArrayList;
import java.util.Arrays;

public class VanillaEnchantmentGuideEntry implements EnchantmentGuideEntry {
    private Enchantment enchantment;
    private EnchantmentCache.CacheEntry cacheEntry;
    private ArrayList<Material> applicableMaterials = new ArrayList<>();

    public VanillaEnchantmentGuideEntry(Enchantment enchantment) {
        this.enchantment = enchantment;
        cacheEntry = EnchantmentCache.getEntry(enchantment);
        EnchantmentTarget target = enchantment.getItemTarget();

        for (Material material : Material.values()) {
            if (target.includes(material)) {
                applicableMaterials.add(material);
            }
        }
    }

    @Override
    public Enchantment getEnchantment() {
        return enchantment;
    }

    @Override
    public Material getIcon() {
        return Material.GRASS_BLOCK;
    }

    @Override
    public String getName() {
        return cacheEntry.getName();
    }

    @Override
    public String getDescription() {
        return cacheEntry.getStringDescription();
    }

    @Override
    public EnchantmentType getType() {
        if (enchantment.equals(Enchantment.VANISHING_CURSE) || enchantment.equals(Enchantment.BINDING_CURSE)) {
            return EnchantmentType.CURSE;
        }

        return EnchantmentType.NORMAL;
    }

    @Override
    public EnchantmentPluginSource getPluginSource() {
        return EnchantmentPluginSource.VANILLA;
    }

    @Override
    public EnchantmentRarity getRarity() {
        return cacheEntry.getRarity();
    }

    @Override
    public int getMaximumLevel() {
        return enchantment.getMaxLevel();
    }

    @Override
    public ArrayList<String> getSources() {
        if (enchantment.isTreasure()) {
            return new ArrayList<>(Arrays.asList("Trading", "Chest Loot"));
        }

        return new ArrayList<>(Arrays.asList("Enchantment Table", "Trading", "Loot"));
    }

    @Override
    public ArrayList<Material> getApplicableItems() {
        return applicableMaterials;
    }

    @Override
    public ArrayList<Enchantment> getConflicts() {
        return new ArrayList<>();
    }
}
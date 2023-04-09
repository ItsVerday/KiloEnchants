package gg.valgo.kiloenchants.guide;

import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;

public interface EnchantmentGuideEntry {
    /**
     * Format:
     *
     * %%enchantment_name%% (properly colored)
     * %%description%%
     * Type: %%type%%, Rarity: %%rarity%%
     * Maximum level: %%max_level%%
     * Obtained from: %%sources%%
     * Can be applied to: %%applicable_items%%
     * Conflicts with: %%conflicts%%
     * Added by: %%plugin_source%%
     */

    Enchantment getEnchantment();
    Material getIcon();
    String getName();
    String getDescription();
    EnchantmentType getType();
    EnchantmentPluginSource getPluginSource();
    EnchantmentRarity getRarity();
    int getMaximumLevel();
    ArrayList<String> getSources();
    ArrayList<Material> getApplicableItems();
    ArrayList<Enchantment> getConflicts();
}
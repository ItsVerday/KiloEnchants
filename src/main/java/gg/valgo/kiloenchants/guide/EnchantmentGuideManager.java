package gg.valgo.kiloenchants.guide;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import gg.valgo.kiloenchants.KiloEnchant;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;

public class EnchantmentGuideManager {
    private static ArrayList<EnchantmentGuideEntry> entries = new ArrayList<>();
    private static ArrayList<EnchantmentGuideCategory> categories = new ArrayList<>();

    public static final EnchantmentGuideCategory CATEGORY_ALL = new EnchantmentGuideCategory("All Enchantments", Material.BOOK) {
        @Override
        public boolean containsEntry(EnchantmentGuideEntry entry) {
            return true;
        }
    };

    public static final EnchantmentGuideCategory CATEGORY_HELMET = new EnchantmentGuideCategory("Helmet Enchantments", Material.DIAMOND_HELMET, Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET, Material.GOLDEN_HELMET, Material.DIAMOND_HELMET, Material.NETHERITE_HELMET, Material.TURTLE_HELMET);
    public static final EnchantmentGuideCategory CATEGORY_CHESTPLATE = new EnchantmentGuideCategory("Chestplate Enchantments", Material.DIAMOND_CHESTPLATE, Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLDEN_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.NETHERITE_CHESTPLATE);
    public static final EnchantmentGuideCategory CATEGORY_LEGGINGS = new EnchantmentGuideCategory("Leggings Enchantments", Material.DIAMOND_LEGGINGS, Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLDEN_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.NETHERITE_LEGGINGS);
    public static final EnchantmentGuideCategory CATEGORY_BOOTS = new EnchantmentGuideCategory("Boots Enchantments", Material.DIAMOND_BOOTS, Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLDEN_BOOTS, Material.DIAMOND_BOOTS, Material.NETHERITE_BOOTS);
    public static final EnchantmentGuideCategory CATEGORY_ARMOR = new EnchantmentGuideCategory("Armor Enchantments", Material.GOLDEN_CHESTPLATE) {
        @Override
        public boolean containsEntry(EnchantmentGuideEntry entry) {
            return CATEGORY_HELMET.containsEntry(entry) || CATEGORY_CHESTPLATE.containsEntry(entry) || CATEGORY_LEGGINGS.containsEntry(entry) || CATEGORY_BOOTS.containsEntry(entry);
        }
    };

    public static final EnchantmentGuideCategory CATEGORY_SHIELD = new EnchantmentGuideCategory("Shield Enchantments", Material.SHIELD, Material.SHIELD);
    public static final EnchantmentGuideCategory CATEGORY_SWORD = new EnchantmentGuideCategory("Sword Enchantments", Material.DIAMOND_SWORD, Material.WOODEN_SWORD, Material.STONE_SWORD, Material.IRON_SWORD, Material.GOLDEN_SWORD, Material.DIAMOND_SWORD, Material.NETHERITE_SWORD);
    public static final EnchantmentGuideCategory CATEGORY_BOW = new EnchantmentGuideCategory("Bow Enchantments", Material.BOW, Material.BOW);
    public static final EnchantmentGuideCategory CATEGORY_CROSSBOW = new EnchantmentGuideCategory("Crossbow Enchantments", Material.CROSSBOW, Material.CROSSBOW);
    public static final EnchantmentGuideCategory CATEGORY_TRIDENT = new EnchantmentGuideCategory("Trident Enchantments", Material.TRIDENT, Material.TRIDENT);
    public static final EnchantmentGuideCategory CATEGORY_AXE = new EnchantmentGuideCategory("Axe Enchantments", Material.DIAMOND_AXE, Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE);
    public static final EnchantmentGuideCategory CATEGORY_PICKAXE = new EnchantmentGuideCategory("Pickaxe Enchantments", Material.DIAMOND_PICKAXE, Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE);
    public static final EnchantmentGuideCategory CATEGORY_SHOVEL = new EnchantmentGuideCategory("Shovel Enchantments", Material.DIAMOND_SHOVEL, Material.WOODEN_SHOVEL, Material.STONE_SHOVEL, Material.IRON_SHOVEL, Material.GOLDEN_SHOVEL, Material.DIAMOND_SHOVEL, Material.NETHERITE_SHOVEL);
    public static final EnchantmentGuideCategory CATEGORY_HOE = new EnchantmentGuideCategory("Hoe Enchantments", Material.DIAMOND_HOE, Material.WOODEN_HOE, Material.STONE_HOE, Material.IRON_HOE, Material.GOLDEN_HOE, Material.DIAMOND_HOE, Material.NETHERITE_HOE);
    public static final EnchantmentGuideCategory CATEGORY_SHEARS = new EnchantmentGuideCategory("Shears Enchantments", Material.SHEARS, Material.SHEARS);
    public static final EnchantmentGuideCategory CATEGORY_FISHING_ROD = new EnchantmentGuideCategory("Fishing Rod Enchantments", Material.FISHING_ROD, Material.FISHING_ROD);

    public static EnchantmentPluginSource getPluginSource(Enchantment enchantment) {
        if (enchantment instanceof EcoEnchant) {
            if (enchantment instanceof KiloEnchant) {
                return EnchantmentPluginSource.KILO;
            }

            return EnchantmentPluginSource.ECO;
        }

        return EnchantmentPluginSource.VANILLA;
    }

    public static void populateGuide() {
        for (Enchantment enchantment : Enchantment.values()) {
            createEntry(enchantment);
        }

        for (EcoEnchant ecoEnchant : EcoEnchants.values()) {
            createEntry(ecoEnchant);
        }
    }

    public static EnchantmentGuideEntry createEntry(Enchantment enchantment) {
        EnchantmentPluginSource pluginSource = getPluginSource(enchantment);

        if (pluginSource.equals(pluginSource.VANILLA)) {
            return new VanillaEnchantmentGuideEntry(enchantment);
        }

        return null;
    }
}
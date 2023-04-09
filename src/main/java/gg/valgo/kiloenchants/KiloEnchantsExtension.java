package gg.valgo.kiloenchants;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.drops.DropQueue;
import com.willfp.eco.core.extensions.Extension;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.util.NumberUtils;
import com.willfp.ecoenchants.EcoEnchantsPlugin;
import com.willfp.ecoenchants.display.EnchantmentCache;
import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentRarity;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import gg.valgo.kiloenchants.enchants.normal.*;
import gg.valgo.kiloenchants.enchants.special.Cultivation;
import gg.valgo.kiloenchants.enchants.template.TurboCropEnchantment;
import gg.valgo.kiloenchants.guide.EnchantmentGuideCommand;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public class KiloEnchantsExtension extends Extension implements Listener {
    private static KiloEnchantsExtension self;
    private static EcoPlugin ecoPlugin;
    private static ArrayList<EcoEnchant> enchants = new ArrayList<>();
    private YamlConfiguration kiloConfig;

    public static final EcoEnchant HARVESTING = register(new Harvesting());
    public static final EcoEnchant SCYTHES_EDGE = register(new ScythesEdge());
    public static final EcoEnchant DYE_HARVESTER = register(new DyeHarvester());
    public static final EcoEnchant FARMERS_DELIGHT = register(new FarmersDelight());
    public static final EcoEnchant ORCHARD = register(new Orchard());
    public static final EcoEnchant CROP_GAMBLER = register(new CropGambler());
    public static final EcoEnchant SEEDER = register(new Seeder());
    public static final EcoEnchant CULTIVATION = register(new Cultivation());
    public static final EcoEnchant TURBO_WHEAT = register(new TurboCropEnchantment("turbo_wheat", Material.WHEAT, Material.WHEAT_SEEDS));
    public static final EcoEnchant TURBO_BEETROOTS = register(new TurboCropEnchantment("turbo_beetroots", Material.BEETROOT, Material.BEETROOT_SEEDS));
    public static final EcoEnchant TURBO_CARROT = register(new TurboCropEnchantment("turbo_carrot", Material.CARROT));
    public static final EcoEnchant TURBO_POTATO = register(new TurboCropEnchantment("turbo_potato", Material.POTATO, Material.POISONOUS_POTATO));
    public static final EcoEnchant TURBO_WARTS = register(new TurboCropEnchantment("turbo_warts", Material.NETHER_WART));
    public static final EcoEnchant HOT_POTATO = register(new HotPotato());
    public static final EcoEnchant POISON_PROTECTION = register(new PoisonProtection());
    public static final EcoEnchant GROUND_SMASH = register(new GroundSmash());
    // public static final EcoEnchant SPECTRAL = register(new Spectral());

    public KiloEnchantsExtension(EcoPlugin plugin) {
        super(plugin);
        ecoPlugin = plugin;
    }

    private static EcoEnchant register(EcoEnchant ecoEnchant) {
        enchants.add(ecoEnchant);
        return ecoEnchant;
    }

    @Override
    public void onEnable() {
        self = this;
        File kiloConfigFile = new File(getEcoPlugin().getDataFolder(), "kilo.yml");
        if (!kiloConfigFile.exists()) {
            InputStream in = getClass().getResourceAsStream("/kilo.yml");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            try {
                FileWriter fr = new FileWriter(kiloConfigFile);
                transferTo(reader, fr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        kiloConfig = YamlConfiguration.loadConfiguration(kiloConfigFile);

        Bukkit.getPluginManager().registerEvents(this, EcoEnchantsPlugin.getInstance());

        for (EcoEnchant ecoEnchant : enchants) {
            Bukkit.getPluginManager().registerEvents(ecoEnchant, EcoEnchantsPlugin.getInstance());
            ((KiloEnchant) ecoEnchant).onEnable();
        }

        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            // commandMap.register("ecoenchants", new EnchantmentGuideCommand());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
    }

    public void overrideVanillaRarities() {
        KiloEnchantsExtension.getEcoPlugin().getLogger().info("Overriding enchantment rarities...");
        EnchantmentCache.update();

        try {
            Field enchantmentCache_CACHE = EnchantmentCache.class.getDeclaredField("CACHE");
            enchantmentCache_CACHE.setAccessible(true);
            Map<NamespacedKey, EnchantmentCache.CacheEntry> CACHE = (Map<NamespacedKey, EnchantmentCache.CacheEntry>) enchantmentCache_CACHE.get(null);
            enchantmentCache_CACHE.setAccessible(false);

            Constructor<EnchantmentCache.CacheEntry> constructor = EnchantmentCache.CacheEntry.class.getDeclaredConstructor(Enchantment.class, String.class, String.class, List.class, EnchantmentType.class, EnchantmentRarity.class);
            constructor.setAccessible(true);

            if (!kiloConfig.isConfigurationSection("rarity-overrides")) {
                kiloConfig.createSection("rarity-overrides");
            }

            for (String key : kiloConfig.getConfigurationSection("rarity-overrides").getKeys(false)) {
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(key));
                if (enchantment == null) {
                    getEcoPlugin().getLogger().warning("Failed to override custom rarity for enchantment " + key + ": Enchantment " + key + "doesn't exist");
                    continue;
                }

                String rarity = kiloConfig.getString("rarity-overrides." + key);
                if (rarity == null) {
                    continue;
                }

                EnchantmentRarity enchantmentRarity = EnchantmentRarity.getByName(rarity);
                if (enchantmentRarity == null) {
                    getEcoPlugin().getLogger().warning("Failed to override custom rarity for enchantment " + key + ": Rarity " + rarity + "doesn't exist");
                    continue;
                }

                EnchantmentCache.CacheEntry cacheEntry = EnchantmentCache.getEntry(enchantment);
                if (cacheEntry == null) {
                    getEcoPlugin().getLogger().warning("Failed to override custom rarity for enchantment " + key + ": No cache entry to overwrite.");
                    continue;
                }

                CACHE.put(enchantment.getKey(), constructor.newInstance(enchantment, enchantmentRarity.getCustomColor() + cacheEntry.getRawName(), cacheEntry.getRawName(), cacheEntry.getDescription(), cacheEntry.getType(), cacheEntry.getRarity()));
                getEcoPlugin().getLogger().info("Successfully overridden custom rarity for enchantment " + key + ": " + rarity);
            }

            constructor.setAccessible(false);
        } catch (Exception e) {
            getEcoPlugin().getLogger().warning("Failed to override vanilla (or custom) enchantment rarities.");
            e.printStackTrace();
        }
    }

    public static KiloEnchantsExtension getSelf() {
        return self;
    }

    public static EcoPlugin getEcoPlugin() {
        return ecoPlugin;
    }

    @EventHandler
    public void onBlockDrop(BlockDropItemEvent evt) {
        Player player = evt.getPlayer();
        Block block = evt.getBlock();

        if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) {
            return;
        }

        if (evt.getBlockState() instanceof Container) {
            return;
        }

        if (evt.isCancelled()) {
            return;
        }

        if (!AntigriefManager.canBreakBlock(player, block)) {
            return;
        }

        HashMap<EcoEnchant, Integer> levels = (HashMap<EcoEnchant, Integer>) EnchantChecks.getEnchantsOnMainhand(player);
        HashMap<KiloEnchant, Integer> kiloLevels = new HashMap<>();

        ArrayList<Item> itemDrops = (ArrayList<Item>) evt.getItems();
        ArrayList<ItemStack> drops = new ArrayList<>();

        for (Item item : itemDrops) {
            drops.add(item.getItemStack());
        }

        itemDrops.clear();

        for (EcoEnchant ecoEnchant : levels.keySet()) {
            if (!(ecoEnchant instanceof KiloEnchant)) {
                continue;
            }

            KiloEnchant kiloEnchant = (KiloEnchant) ecoEnchant;

            if (ecoEnchant.getDisabledWorlds().contains(player.getWorld())) {
                continue;
            }

            kiloLevels.put(kiloEnchant, levels.get(ecoEnchant));
        }

        for (KiloEnchant kiloEnchant : kiloLevels.keySet()) {
            drops = kiloEnchant.modifyDropsEarly(player, block, drops, kiloLevels.get(kiloEnchant), evt);
        }

        for (KiloEnchant kiloEnchant : kiloLevels.keySet()) {
            drops = kiloEnchant.modifyDrops(player, block, drops, kiloLevels.get(kiloEnchant), evt);
        }

        for (KiloEnchant kiloEnchant : kiloLevels.keySet()) {
            drops = kiloEnchant.modifyDropsLate(player, block, drops, kiloLevels.get(kiloEnchant), evt);
        }

        new DropQueue(player)
                .setLocation(block.getLocation())
                .addItems(drops)
                .push();
    }

    private long transferTo(Reader in, Writer out) throws IOException {
        Objects.requireNonNull(out, "out");
        long transferred = 0;
        char[] buffer = new char[8192];
        int nRead;
        while ((nRead = in.read(buffer, 0, 8192)) >= 0) {
            out.write(buffer, 0, nRead);
            transferred += nRead;
        }

        return transferred;
    }

    public static String scalingPlaceholder(EcoEnchant enchant, int level, String baseValue, String valuePerLevel, double multiplier) {
        return NumberUtils.format((enchant.getConfig().getDouble("config." + valuePerLevel) * (level - 1) + enchant.getConfig().getDouble("config." + baseValue)) * multiplier);
    }
}
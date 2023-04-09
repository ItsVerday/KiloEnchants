package gg.valgo.kiloenchants.enchants.special;

import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.util.DurabilityUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import gg.valgo.kiloenchants.KiloEnchant;
import gg.valgo.kiloenchants.KiloEnchantsExtension;
import gg.valgo.kiloenchants.utils.FarmingUtils;
import org.bukkit.GameMode;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Random;

public class Cultivation extends KiloEnchant {
    private static Random random = new Random();

    public Cultivation() {
        super("cultivation", EnchantmentType.SPECIAL);
    }

    @Override
    public String getPlaceholder(int i) {
        return KiloEnchantsExtension.scalingPlaceholder(this, i, "base-chance", "chance-per-level", 100);
    }

    @EventHandler
    public void onCultivationRightClick(PlayerInteractEvent evt) {
        Player player = evt.getPlayer();
        Block block = evt.getClickedBlock();

        if (!evt.getAction().equals(Action.RIGHT_CLICK_BLOCK) || !FarmingUtils.isCropStem(block.getType())) {
            return;
        }

        if (!EnchantChecks.mainhand(player, this)) {
            return;
        }

        if (!AntigriefManager.canPlaceBlock(player, block)) {
            return;
        }

        if (getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        int level = EnchantChecks.getMainhandLevel(player, this);

        boolean particlesEnabled = getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "particles");
        boolean damageItem = getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "damage-item");

        double baseChance = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "base-chance");
        double chancePerLevel = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "chance-per-level");
        double chanceDouble = baseChance + chancePerLevel * (level - 1);

        if (damageItem && !player.getGameMode().equals(GameMode.CREATIVE) && FarmingUtils.isGrowingCrop(block)) {
            DurabilityUtils.damageItem(player, player.getInventory().getItemInMainHand(), 1, player.getInventory().getHeldItemSlot());
        }

        if (FarmingUtils.isValidCrop(block) && chanceDouble > random.nextDouble()) {
            FarmingUtils.ageCrop(block);

            if (particlesEnabled) {
                block.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, block.getLocation().add(0.5, 0.5, 0.5), 3, 0.1, 0.1, 0.1);
            }
        }
    }
}
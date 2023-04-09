package gg.valgo.kiloenchants.enchants.normal;

import com.willfp.eco.core.drops.DropQueue;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import gg.valgo.kiloenchants.KiloEnchant;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Random;

public class FarmersDelight extends KiloEnchant {
    private static Random random = new Random();

    public FarmersDelight() {
        super("farmers_delight", EnchantmentType.NORMAL);
    }

    @Override
    public void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {
        if (player.getGameMode().equals(GameMode.CREATIVE) || player.getGameMode().equals(GameMode.SPECTATOR)) {
            return;
        }

        if (!(block.getBlockData() instanceof Ageable)) {
            return;
        }

        boolean requireFullyGrown = getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "require-fully-grown");

        Ageable ageable = (Ageable) block.getBlockData();
        if (ageable.getAge() != ageable.getMaximumAge() && requireFullyGrown) {
            return;
        }

        double minimumExperience = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "minimum-experience");
        double maximumExperience = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "maximum-experience");

        double experience = random.nextDouble() * (maximumExperience - minimumExperience) + minimumExperience;
        experience *= (double) (ageable.getAge() + 1) / (ageable.getMaximumAge() + 1);
        experience *= level;

        new DropQueue(player)
            .setLocation(block.getLocation())
            .addXP((int) Math.ceil(experience))
            .push();
    }
}
package gg.valgo.kiloenchants.enchants.normal;

import com.willfp.eco.core.integrations.anticheat.AnticheatManager;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.proxy.BlockBreakProxy;
import com.willfp.eco.util.BlockUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import gg.valgo.kiloenchants.KiloEnchant;
import gg.valgo.kiloenchants.KiloEnchantsExtension;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ScythesEdge extends KiloEnchant {
    private static ArrayList<Player> ignore = new ArrayList<>();

    public ScythesEdge() {
        super("scythes_edge", EnchantmentType.NORMAL);
    }

    @Override
    public void onBlockBreak(Player player, Block block, int level, BlockBreakEvent event) {
        if (ignore.contains(player)) {
            return;
        }

        if (!(block.getBlockData() instanceof Ageable)) {
            return;
        }

        boolean onlyHarvestFullyGrownCrops = getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "only-harvest-fully-grown-crops");

        Ageable ageable = (Ageable) block.getBlockData();
        if (ageable.getAge() != ageable.getMaximumAge() && onlyHarvestFullyGrownCrops) {
            return;
        }

        double radiusMultiplier = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "radius-multiplier");
        boolean circular = getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "circular");
        boolean onlyHarvestSameCropType = getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "only-harvest-same-crop-type");

        AnticheatManager.exemptPlayer(player);

        Set<Block> toBreak = new HashSet<>();

        double radius = level * radiusMultiplier;
        int radiusInt = (int) Math.ceil(radius);

        for (int y = -1; y <= 1; y++) {
            for (int x = -radiusInt; x <= radiusInt; x++) {
                for (int z = -radiusInt; z <= radiusInt; z++) {
                    if (circular && x * x + z * z > radius * radius) {
                        continue;
                    }

                    Block toTry = block.getWorld().getBlockAt(block.getLocation().clone().add(x, y, z));

                    if (!toTry.getType().equals(block.getType()) && onlyHarvestSameCropType) {
                        continue;
                    }

                    if (!(toTry.getBlockData() instanceof Ageable)) {
                        continue;
                    }

                    Ageable tryAgeable = (Ageable) toTry.getBlockData();
                    if (tryAgeable.getAge() != tryAgeable.getMaximumAge() && onlyHarvestFullyGrownCrops) {
                        continue;
                    }

                    if (!AntigriefManager.canBreakBlock(player, toTry)) {
                        continue;
                    }

                    toBreak.add(toTry);
                }
            }
        }

        ignore.add(player);

        toBreak.forEach(tryBreak -> {
            BlockUtils.breakBlock(player, tryBreak);
        });

        ignore.remove(player);

        AnticheatManager.unexemptPlayer(player);
    }
}
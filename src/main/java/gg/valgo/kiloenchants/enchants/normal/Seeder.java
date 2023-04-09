package gg.valgo.kiloenchants.enchants.normal;

import com.willfp.eco.core.integrations.anticheat.AnticheatManager;
import com.willfp.eco.core.integrations.antigrief.AntigriefManager;
import com.willfp.eco.util.DurabilityUtils;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import gg.valgo.kiloenchants.KiloEnchant;
import gg.valgo.kiloenchants.utils.FarmingUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Seeder extends KiloEnchant {
    public Seeder() {
        super("seeder", EnchantmentType.NORMAL);
    }

    @EventHandler
    public void onSeederRightClick(PlayerInteractEvent evt) {
        Player player = evt.getPlayer();
        Block block = evt.getClickedBlock();
        if (!EnchantChecks.mainhand(player, this)) {
            return;
        }

        if (evt.getAction().equals(Action.RIGHT_CLICK_BLOCK) && (block.getType().equals(Material.DIRT) || block.getType().equals(Material.GRASS_BLOCK)) && block.getRelative(BlockFace.UP).getType().equals(Material.AIR)) {
            block.setType(Material.FARMLAND);
        }

        if (!evt.getAction().equals(Action.RIGHT_CLICK_BLOCK) || !block.getType().equals(Material.FARMLAND)) {
            return;
        }

        if (!AntigriefManager.canPlaceBlock(player, block.getRelative(BlockFace.UP))) {
            return;
        }

        if (getDisabledWorlds().contains(player.getWorld())) {
            return;
        }

        double radiusMultiplier = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "radius-multiplier");
        boolean circular = getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "circular");
        double radius = radiusMultiplier * EnchantChecks.getMainhandLevel(player, this);
        int radiusInt = (int) Math.ceil(radius);

        ArrayList<Block> toSet = new ArrayList<>();

        for (int y = -1; y <= 1; y++) {
            for (int x = -radiusInt; x <= radiusInt; x++) {
                for (int z = -radiusInt; z <= radiusInt; z++) {
                    if (circular && x * x + z * z > radius * radius) {
                        continue;
                    }

                    Block blockBelow = block.getRelative(x, y, z);
                    Block toFarm = block.getRelative(x, y + 1, z);

                    if (blockBelow.getType().equals(Material.FARMLAND) && toFarm.getType().equals(Material.AIR)) {
                        toSet.add(toFarm);
                    }
                }
            }
        }

        AnticheatManager.exemptPlayer(player);

        Inventory inventory = player.getInventory();
        int inventoryIndex = 0;
        int plantedCount = 0;

        while (toSet.size() > 0 && inventoryIndex < 36) {
            ItemStack inventoryItem = inventory.getItem(inventoryIndex);

            if (inventoryItem == null || !FarmingUtils.getSeeds().keySet().contains(inventoryItem.getType())) {
                inventoryIndex++;
                continue;
            }

            int count = inventoryItem.getAmount();

            while (count > 0) {
                if (toSet.size() == 0) {
                    break;
                }

                Material newBlockType = FarmingUtils.getSeeds().get(inventoryItem.getType());
                Block set = toSet.get(0);

                if (!AntigriefManager.canPlaceBlock(player, set)) {
                    continue;
                }

                set.setType(newBlockType);
                plantedCount++;
                toSet.remove(0);

                if (player.getGameMode().equals(GameMode.CREATIVE)) {
                    continue;
                }

                count--;
            }

            if (count > 0) {
                inventoryItem.setAmount(count);
                inventory.setItem(inventoryIndex, inventoryItem);
            } else {
                inventory.clear(inventoryIndex);
            }

            inventoryIndex++;
        }

        AnticheatManager.unexemptPlayer(player);

        boolean playSound = getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "play-sound");
        boolean damageItem = getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "damage-item");
        boolean damagePerPlanted = getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "damage-per-planted");

        if (plantedCount > 0) {
            if (playSound) {
                player.getWorld().playSound(block.getLocation(), Sound.ITEM_CROP_PLANT, 1, 1);
            }

            if (damageItem && !player.getGameMode().equals(GameMode.CREATIVE)) {
                DurabilityUtils.damageItem(player, player.getInventory().getItemInMainHand(), damagePerPlanted ? plantedCount : 1, player.getInventory().getHeldItemSlot());
            }
        }
    }
}
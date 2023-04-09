package gg.valgo.kiloenchants.enchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import gg.valgo.kiloenchants.KiloEnchant;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class PoisonProtection extends KiloEnchant {
    public PoisonProtection() {
        super("poison_protection", EnchantmentType.NORMAL);
    }

    @EventHandler
    public void onPoisonDamage(EntityDamageEvent evt) {
        if (!evt.getCause().equals(EntityDamageEvent.DamageCause.POISON)) {
            return;
        }

        Entity entity = evt.getEntity();
        if (!(entity instanceof LivingEntity)) {
            return;
        }

        LivingEntity livingEntity = (LivingEntity) entity;

        double base = 1 - getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "reduction-per-level");
        int levelCap = getConfig().getInt(EcoEnchants.CONFIG_LOCATION + "level-cap");

        int totalLevel = EnchantChecks.getHelmetLevel(livingEntity, this)
                + EnchantChecks.getChestplateLevel(livingEntity, this)
                + EnchantChecks.getLeggingsLevel(livingEntity, this)
                + EnchantChecks.getBootsLevel(livingEntity, this);

        if (totalLevel > levelCap) {
            totalLevel = levelCap;
        }

        evt.setDamage(evt.getDamage() * Math.pow(base, totalLevel));
    }
}
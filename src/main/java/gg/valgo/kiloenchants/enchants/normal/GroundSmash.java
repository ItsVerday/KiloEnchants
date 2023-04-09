package gg.valgo.kiloenchants.enchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import gg.valgo.kiloenchants.KiloEnchant;
import gg.valgo.kiloenchants.KiloEnchantsExtension;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;

public class GroundSmash extends KiloEnchant {
    public GroundSmash() {
        super("ground_smash", EnchantmentType.NORMAL);
    }

    public void onMeleeAttack(LivingEntity attacker, LivingEntity victim, int level, EntityDamageByEntityEvent evt) {
        double velocityPerLevel = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "velocity-per-level");
        double velocity = velocityPerLevel * level;
        Vector velocityVector = new Vector(0, -velocity, 0);

        double fallHeightPerLevel = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "fall-height-per-level");
        double fallHeight = fallHeightPerLevel * level;

        KiloEnchantsExtension.getEcoPlugin().getScheduler().runLater(() -> {
            victim.setVelocity(victim.getVelocity().clone().add(velocityVector));
            victim.setFallDistance(victim.getFallDistance() + (float) fallHeight);
        }, 1);
    }
}
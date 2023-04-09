package gg.valgo.kiloenchants.enchants.normal;

import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.meta.EnchantmentType;
import gg.valgo.kiloenchants.KiloEnchant;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Spectral extends KiloEnchant {
    public Spectral() {
        super("spectral", EnchantmentType.NORMAL);
    }

    @Override
    public void onArrowDamage(LivingEntity attacker, LivingEntity victim, Arrow arrow, int level, EntityDamageByEntityEvent event) {
        Bukkit.broadcastMessage("spectral");
        double durationPerLevel = getConfig().getDouble(EcoEnchants.CONFIG_LOCATION + "duration-per-level");
        int duration = (int) (level * durationPerLevel * 20);

        for (PotionEffect potionEffect : victim.getActivePotionEffects()) {
            if (!potionEffect.getType().equals(PotionEffectType.GLOWING)) {
                continue;
            }

            if (potionEffect.getDuration() > duration) {
                return;
            }
        }

        victim.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, duration, 0, true, false, true));
    }
}
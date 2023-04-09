package gg.valgo.kiloenchants;

import com.willfp.eco.core.config.updating.ConfigUpdater;

public class KiloUpdatable {
    @ConfigUpdater
    public static void updateEnchantmentRarities() {
        KiloEnchantsExtension.getSelf().overrideVanillaRarities();
    }
}
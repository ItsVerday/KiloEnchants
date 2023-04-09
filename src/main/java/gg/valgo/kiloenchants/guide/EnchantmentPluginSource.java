package gg.valgo.kiloenchants.guide;

public interface EnchantmentPluginSource {
    EnchantmentPluginSource VANILLA = () -> "&eVanilla";
    EnchantmentPluginSource ECO = () -> "&aEcoEnchants";
    EnchantmentPluginSource KILO = () -> "&6KiloEnchants";
    EnchantmentPluginSource UNKNOWN = () -> "&7Unknown";

    String getSourceName();
}
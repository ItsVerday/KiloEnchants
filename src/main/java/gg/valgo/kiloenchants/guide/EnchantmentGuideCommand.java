package gg.valgo.kiloenchants.guide;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class EnchantmentGuideCommand extends Command {
    public EnchantmentGuideCommand() {
        super("enchantguide");
        setDescription("View the KiloEnchants enchantment guide.");
        setUsage("/enchantguide");
        setPermission("ecoenchants.enchantguide");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        sender.sendMessage("Test");
        return false;
    }
}
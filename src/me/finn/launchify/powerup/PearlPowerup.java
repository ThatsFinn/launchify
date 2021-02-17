package me.finn.launchify.powerup;

import me.finn.launchify.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class PearlPowerup extends Powerup {
    
    @Override
    public String getName() {
        return "Rideable Pearl";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_PURPLE;
    }

    @Override
    public Material getItem() {
        return Material.ENDER_PEARL;
    }

    @Override
    public Material getBlock() {
        return Material.PURPLE_STAINED_GLASS;
    }

    @Override
    public void execute(Player p) {
        p.getInventory().addItem(new ItemBuilder(Material.ENDER_PEARL)
                .setName(ChatColor.DARK_PURPLE + "Rideable Pearl")
                .addEnchant(Enchantment.DAMAGE_ALL, 1)
                .addFlag(ItemFlag.HIDE_ENCHANTS)
                .toItemStack());
    }

}

package me.finn.launchify.guisystem.guis;

import me.finn.launchify.guisystem.GUI;
import me.finn.launchify.guisystem.PlayerMenuUtility;
import me.finn.launchify.utils.Colorize;
import me.finn.launchify.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class ConfirmGUI extends GUI {

    private Consumer<Boolean> closeListener;

    public ConfirmGUI(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Click To Confirm";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void setMenuItems() {
        inv.setItem(12, new ItemBuilder(Material.REDSTONE_BLOCK).setName(Colorize.color("&cCancel")).toItemStack());
        inv.setItem(14, new ItemBuilder(Material.EMERALD_BLOCK).setName(Colorize.color("&aConfirm")).toItemStack());
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        closeListener.accept(item.getType() == Material.EMERALD_BLOCK);
    }

    public ConfirmGUI onComplete(Consumer<Boolean> closeListener) {
        this.closeListener = closeListener;
        return this;
    }

}

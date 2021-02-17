package me.finn.launchify.guisystem.guis;

import me.finn.launchify.Launchify;
import me.finn.launchify.guisystem.GUI;
import me.finn.launchify.guisystem.PlayerMenuUtility;
import me.finn.launchify.utils.Colorize;
import me.finn.launchify.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GeneralGUI extends GUI {

    private Launchify pl;

    public GeneralGUI(PlayerMenuUtility playerMenuUtility, Launchify pl) {
        super(playerMenuUtility);
        this.pl = pl;
    }

    @Override
    public String getMenuName() {
        return "Overview";
    }

    @Override
    public int getSlots() {
        return 45;
    }

    @Override
    public void setMenuItems() {
        inv.setItem(21, new ItemBuilder(Material.BOOKSHELF).setName(Colorize.color("&a&lArenas"))
                .setLore(Arrays.asList(Colorize.color("&fLeft click &ato view arenas!"))).toItemStack());
        inv.setItem(23, new ItemBuilder(Material.NOTE_BLOCK).setName(Colorize.color("&d&lGames"))
                .setLore(Arrays.asList(Colorize.color("&fLeft click &dto view games!"))).toItemStack());
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        switch (item.getType()) {
            case BOOKSHELF:
                new ArenaViewGUI(playerMenuUtility, pl).open();
                break;
            case DISPENSER:
                new BowPlayerViewGUI(playerMenuUtility, pl).open();
                break;
            case NOTE_BLOCK:
                new GameViewGUI(playerMenuUtility, pl).open();
                break;
        }

    }
}

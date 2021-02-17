package me.finn.launchify.guisystem.guis;

import me.finn.launchify.Launchify;
import me.finn.launchify.arena.LaunchArena;
import me.finn.launchify.guisystem.GUI;
import me.finn.launchify.guisystem.PlayerMenuUtility;
import me.finn.launchify.utils.Colorize;
import me.finn.launchify.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ArenaViewGUI extends GUI {

    private Launchify pl;

    public ArenaViewGUI(PlayerMenuUtility playerMenuUtility, Launchify pl) {
        super(playerMenuUtility);
        this.pl = pl;
    }

    @Override
    public String getMenuName() {
        return "Overview > Arenas";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void setMenuItems() {
        int i = 10;
        for (LaunchArena a : Launchify.arenas) {
            inv.setItem(i, new ItemBuilder(Material.BOOK)
                    .setName((a.isDisabled() ? Colorize.color("&c&m") : Colorize.color("&b")) + a.getDisplayName())
                    .setLore(Arrays.asList("&fLeft click &3edit.",
                            " ",
                            "&3Name: &7" + a.getName(),
                            "&3World: &7" + a.getLobby().getWorld().getName(),
                            "&3Spawns: &7" + a.getSpawns().size(),
                            "&3Status: &7" + (a.isDisabled() ? "disabled" : "enabled")))
                    .toItemStack());

            i++;
            if ((i + 1) % 9 == 0)
                i = i + 2;
        }
        inv.setItem(49, new ItemBuilder(Material.STICK)
                .setName(Colorize.color("&7&lAdd New Arena"))
                .setLore(Arrays.asList("&fLeft click &7to start wizard."))
                .toItemStack());
        inv.setItem(getSlots() - 1, new ItemBuilder(Material.OAK_SIGN)
                .setName(Colorize.color("&7Back"))
                .setLore(Arrays.asList("&fLeft click &7to go back."))
                .toItemStack());
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();

        switch (item.getType()) {
            case BOOK:
                String name = Colorize.stripColor(item.getItemMeta().getDisplayName());
                LaunchArena arena = pl.am.getArenaFromString(name);

                if (arena == null) {
                    return;
                }

                playerMenuUtility.setArena(arena);
                new ArenaEditGUI(playerMenuUtility, pl).open();
                break;
            case STICK:
                p.closeInventory();
                pl.aswm.startWizard(p);
                break;
            case OAK_SIGN:
                new GeneralGUI(playerMenuUtility, pl).open();
        }
    }

}

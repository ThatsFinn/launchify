package me.finn.launchify.guisystem.guis;

import me.finn.launchify.Launchify;
import me.finn.launchify.arena.LaunchArena;
import me.finn.launchify.game.Game;
import me.finn.launchify.guisystem.GUI;
import me.finn.launchify.guisystem.PlayerMenuUtility;
import me.finn.launchify.utils.Colorize;
import me.finn.launchify.utils.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class GameViewGUI extends GUI {

    private Launchify pl;

    public GameViewGUI(PlayerMenuUtility playerMenuUtility, Launchify pl) {
        super(playerMenuUtility);
        this.pl = pl;
    }

    @Override
    public String getMenuName() {
        return "Overview > Games";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void setMenuItems() {
        int i = 10;
        for (LaunchArena a : Launchify.arenas) {
            Game game = a.getGame();

            int itemCount = 1;

            if (game.getPlayers().size() > 1) {
                itemCount = game.getPlayers().size();
            }

            inv.setItem(i, new ItemBuilder(Material.COMPASS, itemCount)
                    .setName(ChatColor.LIGHT_PURPLE + a.getDisplayName())
                    .setLore(Arrays.asList("&fLeft click &5edit.",
                            " ",
                            "&5Players: &7" + game.getPlayers().size(),
                            "&5Arena: &7" + a.getName(),
                            "&5State: &7" + game.getState().getType().getDisplayName()))
                    .toItemStack());

            i++;
            if ((i + 1) % 9 == 0)
                i = i + 2;
        }
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
            case COMPASS:
                String name = Colorize.stripColor(item.getItemMeta().getDisplayName());
                LaunchArena arena = pl.am.getArenaFromString(name);
                Game game = arena.getGame();

                playerMenuUtility.setGame(game);

                new GameEditGUI(playerMenuUtility, pl).open();
                break;
            case OAK_SIGN:
                new GeneralGUI(playerMenuUtility, pl).open();
        }
    }
}

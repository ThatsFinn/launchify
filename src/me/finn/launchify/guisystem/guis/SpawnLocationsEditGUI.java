package me.finn.launchify.guisystem.guis;

import me.finn.launchify.Launchify;
import me.finn.launchify.arena.LaunchArena;
import me.finn.launchify.guisystem.GUI;
import me.finn.launchify.guisystem.PlayerMenuUtility;
import me.finn.launchify.utils.Colorize;
import me.finn.launchify.utils.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class SpawnLocationsEditGUI extends GUI {

    private Launchify pl;

    public SpawnLocationsEditGUI(PlayerMenuUtility playerMenuUtility, Launchify pl) {
        super(playerMenuUtility);
        this.pl = pl;
    }

    @Override
    public String getMenuName() {
        return playerMenuUtility.getArena().getDisplayName() + " > Edit Spawns";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void setMenuItems() {
        int i = 10;
        int spawnCount = 1;
        for (Location loc : playerMenuUtility.getArena().getSpawns()) {
            inv.setItem(i, new ItemBuilder(Material.FEATHER)
                    .setName(ChatColor.BLUE + "Spawn #" + (spawnCount))
                    .setLore(Arrays.asList("&fLeft click &9delete.",
                            "&fPress Q &9to teleport.",
                            " ",
                            "&9X: &7" + loc.getBlockX(),
                            "&9Y: &7" + loc.getBlockY(),
                            "&9Z: &7" + loc.getBlockZ()))
                    .toItemStack());

            i++;
            spawnCount++;
            if ((i + 1) % 9 == 0)
                i = i + 2;
        }
        inv.setItem(49, new ItemBuilder(Material.BLUE_CONCRETE)
                .setName(Colorize.color("&9&lAdd New Spawn"))
                .setLore(Arrays.asList("&fLeft click &9to open spawn location adder."))
                .toItemStack());
        inv.setItem(getSlots() - 1, new ItemBuilder(Material.OAK_SIGN)
                .setName(Colorize.color("&7Back"))
                .setLore(Arrays.asList("&fLeft click &7to go back."))
                .toItemStack());
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getCurrentItem().getItemMeta() == null) { return; }

        Player p = (Player) e.getWhoClicked();
        ItemStack item = e.getCurrentItem();
        LaunchArena arena = playerMenuUtility.getArena();

        if (e.getAction() == InventoryAction.DROP_ONE_SLOT) {
            if (item.getType() == Material.FEATHER) {
                String name = Colorize.stripColor(item.getItemMeta().getDisplayName());

                Location loc = arena.getSpawns().get(Integer.parseInt(name.split("#")[1]) - 1);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(Colorize.color("&9Teleported to spawn #" + name.split("#")[1])));
                p.teleport(loc);
                return;
            }
        }

        switch (item.getType()) {
            case FEATHER:
                Integer x = Integer.valueOf(Colorize.stripColor(
                        item.getItemMeta().getLore().get(3).split(":")[1]).replaceAll("\\s+",""));
                Integer y = Integer.valueOf(Colorize.stripColor(
                        item.getItemMeta().getLore().get(4).split(":")[1]).replaceAll("\\s+",""));
                Integer z = Integer.valueOf(Colorize.stripColor(
                        item.getItemMeta().getLore().get(5).split(":")[1]).replaceAll("\\s+",""));

                // p.sendMessage("X:" + x + "Y:" + y + "Z:" + z);
                Location clickedLocation = pl.am.getLocationFromBlock(playerMenuUtility.getArena(), x, y, z);
                playerMenuUtility.getArena().getSpawns().remove(clickedLocation);
                new SpawnLocationsEditGUI(playerMenuUtility, pl).open();

                break;
            case BLUE_CONCRETE:
                p.closeInventory();
                pl.sla.startWizard(p, playerMenuUtility.getArena());
                break;
            case OAK_SIGN:
                new ArenaEditGUI(playerMenuUtility, pl).open();
                break;
        }
    }
}

package me.finn.launchify.managers;

import me.finn.launchify.Launchify;
import me.finn.launchify.arena.LaunchArena;
import me.finn.launchify.guisystem.guis.SpawnLocationsEditGUI;
import me.finn.launchify.utils.Colorize;
import me.finn.launchify.utils.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;

public class SpawnLocationAdder implements Listener {

    private Launchify pl;

    public SpawnLocationAdder(Launchify pl) {
        this.pl = pl;
    }

    private HashMap<Player, LaunchArena> inSpawnAdd = new HashMap<>();

    private final String ADD_ARENA_SPAWN_ITEM_NAME = Colorize.color("&9Add Arena Spawn &7(Right click)");
    private final String CLOSE_SPAWN_ADDER_ITEM_NAME = Colorize.color("&cClose Spawn Adder &7(Right click)");

    public void startWizard(Player p, LaunchArena arena) {
        Inventory inv = p.getInventory();

        inSpawnAdd.put(p, arena);
        p.sendMessage(Colorize.color("&7&lSpawn Adder &8» &fStarted &7spawn location adder!"));

        inv.setItem(4, new ItemBuilder(Material.BLUE_CONCRETE).setName(ADD_ARENA_SPAWN_ITEM_NAME).toItemStack());
        inv.setItem(5, new ItemBuilder(Material.RED_CONCRETE).setName(CLOSE_SPAWN_ADDER_ITEM_NAME).toItemStack());
    }

    public void endWizard(Player p) {
        inSpawnAdd.remove(p);
        p.getInventory().clear();
    }

    public boolean inWizard(Player p) {
        return inSpawnAdd.containsKey(p);
    }

    @EventHandler
    public void onItemInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (!inWizard(p)) {
            return;
        }
        if (!e.hasItem()) {
            return;
        }
        if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (!e.getItem().hasItemMeta()) {
            return;
        }

        String name = e.getItem().getItemMeta().getDisplayName();
        LaunchArena arena = inSpawnAdd.get(p);

        if (name.equals(ADD_ARENA_SPAWN_ITEM_NAME)) {
            e.setCancelled(true);
            Location loc = p.getLocation();
            arena.getSpawns().add(loc);
            p.sendMessage(Colorize.color("&9&lSpawn Adder &7» &fAdded &9spawn location!"));
            pl.su.success(p.getLocation());
        } else if (name.equals(CLOSE_SPAWN_ADDER_ITEM_NAME)) {
            e.setCancelled(true);
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(Colorize.color("&a&lClosed Spawn Adder!")));
            pl.su.success2(p.getLocation());
            p.sendMessage(Colorize.color("&7&lSpawn Adder &8» &fClosed &7adder."));
            new SpawnLocationsEditGUI(Launchify.getPlayerMenuUtility(p), pl).open();
            endWizard(p);
        }
    }

}

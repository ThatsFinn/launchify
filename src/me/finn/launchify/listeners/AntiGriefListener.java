package me.finn.launchify.listeners;

import me.finn.launchify.Launchify;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class AntiGriefListener implements Listener {

    private Launchify pl;

    public AntiGriefListener(Launchify pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();

        if (pl.gm.getGameFromPlayer(p) != null) {
            if (p.hasPermission("bowlaunch.bypass.grief")) {
                return;
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();

        if (pl.gm.getGameFromPlayer(p) != null) {
            if (p.hasPermission("bowlaunch.bypass.grief")) {
                return;
            }
            e.setCancelled(true);
        }
    }

}

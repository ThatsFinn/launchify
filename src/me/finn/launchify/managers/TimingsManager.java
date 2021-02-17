package me.finn.launchify.managers;

import me.finn.launchify.Launchify;
import me.finn.launchify.arena.LaunchArena;
import me.finn.launchify.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class TimingsManager extends BukkitRunnable {

    private Launchify pl;

    public TimingsManager(Launchify pl) {
        this.pl = pl;
    }

    @Override
    public void run() {
        for (LaunchArena arena : Launchify.arenas) {
            Game game = arena.getGame();
            if (game != null) {
                game.tickEvent();
            } else {
                Bukkit.getLogger().log(Level.SEVERE, "game was null from timings manager");
            }
        }
    }

}

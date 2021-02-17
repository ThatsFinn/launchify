package me.finn.launchify.listeners;

import me.finn.launchify.Launchify;
import me.finn.launchify.game.Game;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChangeListener implements Listener {

    private Launchify pl;

    public WorldChangeListener(Launchify pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onChangeWorlds(PlayerChangedWorldEvent e) {
        Game game = pl.gm.getGameFromPlayer(e.getPlayer());
        if (game != null) {
            if (e.getFrom() == game.getArena().getLobby().getWorld()) {
                game.removePlayer(e.getPlayer());
            }
        }
    }

}

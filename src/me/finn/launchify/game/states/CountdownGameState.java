package me.finn.launchify.game.states;

import me.finn.launchify.Launchify;
import me.finn.launchify.game.Game;
import me.finn.launchify.game.GameState;
import me.finn.launchify.game.GameStateType;
import me.finn.launchify.game.LaunchPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Level;

public class CountdownGameState extends GameState {

    private Launchify pl;
    private Game game;
    private String reason;
    Integer time = 10;

    public CountdownGameState(Game game, Launchify pl, Integer time, String reason) {
        this.pl = pl;
        this.game = game;
        this.time = time;
        this.reason = reason;
    }

    @Override
    public GameStateType getType() {
        return GameStateType.COUNTDOWN;
    }

    @Override
    public void onEnable(Launchify pl) {
        super.onEnable(pl);

        game.setTimeLeft(time);
        game.broadcastMessage("&6⛏ &fStarting countdown " + reason);

        for (LaunchPlayer bp : game.getPlayers()) {
            pl.su.release(bp.getPlayer().getLocation());
        }
    }

    @Override
    public void onDisable(Launchify pl) {
        super.onDisable(pl);
    }

    @Override
    public void handleTick() {
        if (game.getTimeLeft() == 0) {
            Bukkit.getLogger().log(Level.INFO, "Started game in arena " + game.getArena().getName());
            game.setGameState(new GracePeriodState(game, pl));
            return;
        }

        if (game.getTimeLeft() == 20 || game.getTimeLeft() == 15
                || game.getTimeLeft() == 10 || game.getTimeLeft() <= 5) {
            if (game.getTimeLeft() <= 5) {
                for (LaunchPlayer p : game.getPlayers()) {
                    pl.su.count(p.getPlayer().getLocation());
                }
            }
            game.broadcastMessage("&d⚐ &fGame beings in &d" + game.getTimeLeft() + "&f seconds..");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if (pl.gm.getGameFromPlayer(p) == game) {
            game.removePlayer(p);

            e.setQuitMessage("");
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();

            if (pl.gm.getGameFromPlayer(p) == game) {
                e.setCancelled(true);
            }
        }
    }

}

package me.finn.launchify.event.events;

import me.finn.launchify.event.Event;
import me.finn.launchify.event.EventType;
import me.finn.launchify.game.Game;
import me.finn.launchify.game.states.ActiveGameState;
import me.finn.launchify.powerup.PowerupBlock;
import org.bukkit.ChatColor;

public class PowerupFrenzy extends Event {

    private Game game;
    private Boolean active = false;

    public PowerupFrenzy(Game game) {
        this.game = game;
    }

    @Override
    public String getName() {
        return "Powerup Frenzy";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.LIGHT_PURPLE;
    }

    @Override
    public Game getGame() {
        return game;
    }

    @Override
    public Boolean isActive() {
        return active;
    }

    @Override
    public void runTick(Integer timeSinceExecution) {
        for (int i = 0; i < 15; i++) {
            game.getPlugin().pm.spawnPowerup(game);
        }

        if (timeSinceExecution == 0) {
            active = true;
            if (game.getState() instanceof ActiveGameState) {
                for (PowerupBlock pb : game.getPowerups()) {
                    pb.setDespawnable(false);
                }
            }
        }

        if (timeSinceExecution >= 30) {
            active = false;
            if (game.getState() instanceof ActiveGameState) {
                for (PowerupBlock pb : game.getPowerups()) {
                    pb.setDespawnable(true);
                }
            }
        }
    }

}

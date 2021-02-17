package me.finn.launchify.event.events;

import me.finn.launchify.event.Event;
import me.finn.launchify.event.EventType;
import me.finn.launchify.game.Game;
import me.finn.launchify.game.LaunchPlayer;
import org.bukkit.ChatColor;

public class RodOnlyEvent extends Event {

    private Game game;
    private Boolean active = false;

    public RodOnlyEvent(Game game) {
        this.game = game;
    }

    @Override
    public String getName() {
        return "Stick Only";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GREEN;
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
        if (timeSinceExecution == 0) {
            active = true;
            for (LaunchPlayer lp : game.getPlayers()) {
                lp.getPlayer().getInventory().clear();
                game.giveItems(lp.getPlayer());
            }
        }

        if (timeSinceExecution == 30) {
            active = false;
            for (LaunchPlayer lp : game.getPlayers()) {
                game.giveItems(lp.getPlayer());
            }
        }
    }
}

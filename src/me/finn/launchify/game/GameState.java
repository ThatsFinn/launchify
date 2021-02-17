package me.finn.launchify.game;

import me.finn.launchify.Launchify;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class GameState implements Listener {

    public void onEnable(Launchify pl) {
        pl.getServer().getPluginManager().registerEvents(this, pl);
    }

    public void onDisable(Launchify pl) {
        HandlerList.unregisterAll(this);
    }

    public abstract void handleTick();
    public abstract GameStateType getType();

}

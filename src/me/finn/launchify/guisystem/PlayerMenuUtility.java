package me.finn.launchify.guisystem;

import me.finn.launchify.arena.LaunchArena;
import me.finn.launchify.game.Game;
import me.finn.launchify.game.LaunchPlayer;
import org.bukkit.entity.Player;

public class PlayerMenuUtility {

    private Player owner;
    private LaunchArena arena;
    private Game game;
    private LaunchPlayer launchPlayerTarget;

    public PlayerMenuUtility(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
    public LaunchArena getArena() {
        return arena;
    }
    public Game getGame() {
        return game;
    }
    public LaunchPlayer getBPTarget() {
        return launchPlayerTarget;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
    public void setArena(LaunchArena arena) {
        this.arena = arena;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public void setBPTarget(LaunchPlayer launchPlayerTarget) {
        this.launchPlayerTarget = launchPlayerTarget;
    }
}

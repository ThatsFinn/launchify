package me.finn.launchify.arena;

import me.finn.launchify.game.Game;
import org.bukkit.Location;

import java.util.ArrayList;

public class LaunchArena {

    private String name;
    private String displayName;
    private boolean disabled = false;

    private Location lobby;
    private ArrayList<Location> spawns = new ArrayList<>();

    private Game game;

    public String getName() {
        return name;
    }
    public Location getLobby() {
        return lobby;
    }
    public ArrayList<Location> getSpawns() {
        return spawns;
    }
    public Game getGame() {
        return game;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setLobby(Location lobby) {
        this.lobby = lobby;
    }
    public void setGame(Game game) {
        this.game = game;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public boolean isDisabled() {
        return disabled;
    }

}

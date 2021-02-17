package me.finn.launchify.game;

import org.bukkit.entity.Player;

public class LaunchPlayer {

    private Player player;
    private boolean alive = true;
    private int kills = 0;
    private int deaths = 0;
    private LaunchPlayer lastHitBy;

    public Player getPlayer() {
        return player;
    }
    public int getKills() {
        return kills;
    }
    public int getDeaths() {
        return deaths;
    }
    public boolean isAlive() {
        return alive;
    }
    public LaunchPlayer getLastHitBy() {
        return lastHitBy;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setKills(int kills) {
        this.kills = kills;
    }
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
    public void setLastHitBy(LaunchPlayer lastHitBy) {
        this.lastHitBy = lastHitBy;
    }

}

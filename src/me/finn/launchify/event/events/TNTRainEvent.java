package me.finn.launchify.event.events;

import me.finn.launchify.event.Event;
import me.finn.launchify.game.Game;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;

import java.util.List;
import java.util.Random;

public class TNTRainEvent extends Event {

    private Game game;
    private Boolean active = false;
    Random rand = new Random();

    public TNTRainEvent(Game game) {
        this.game = game;
    }

    @Override
    public String getName() {
        return "TNT Rain";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.RED;
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
        if (timeSinceExecution <= 20) {
            if (timeSinceExecution % 2 == 0) {
                for (int i = 0; i < 20; i++) {
                    Location spawn = game.getPlugin().am.getRandomSpawnLocation(game.getArena());
                    List<Block> spawnable = game.getPlugin().pm.getSpawnable(spawn, 15, 8, 100, false);
                    Block b = spawnable.get(rand.nextInt(spawnable.size()));

                    TNTPrimed tnt = b.getWorld().spawn(b.getLocation().add(0, randomWholeInRange(5, 30), 0), TNTPrimed.class);
                    tnt.setCustomName("tnt_rain_tnt");
                }
            }
        }
    }

    public int randomWholeInRange(int min, int max) {
        return rand.nextInt(max - min) + min;
    }

}

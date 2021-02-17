package me.finn.launchify.managers;

import me.finn.launchify.Launchify;
import me.finn.launchify.arena.LaunchArena;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.List;
import java.util.Random;

public class ArenaManager {

    private Launchify pl;
    Random rand = new Random();

    public ArenaManager(Launchify pl) {
        this.pl = pl;
    }

    public LaunchArena getArenaFromString(String name) {
        for (LaunchArena a : Launchify.arenas) {
            if (a.getName().equalsIgnoreCase(name) || a.getDisplayName().equalsIgnoreCase(name)) {
                return a;
            }
        }
        return null;
    }

    public Location getRandomSpawnLocation(LaunchArena arena) {
        List<Location> spawns = arena.getSpawns();
        return spawns.get(rand.nextInt(spawns.size()));
    }

    public Location getLocationFromBlock(LaunchArena arena, Integer x, Integer y, Integer z) {
        for (Location loc : arena.getSpawns()) {
            if (loc.getBlockX() == x && loc.getBlockY() == y && loc.getBlockZ() == z) {
                return loc;
            }
        }
        return null;
    }

    public void deleteArena(LaunchArena arena) {
        pl.cm.deleteArena(arena);
        arena.setGame(null);
        Launchify.arenas.remove(arena);
    }

    public LaunchArena getArena(World world) {
        for (LaunchArena a : Launchify.arenas) {
            if (a.getLobby().getWorld() == world) {
                return a;
            }
        }
        return null;
    }

}

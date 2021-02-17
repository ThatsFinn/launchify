package me.finn.launchify.managers;

import me.finn.launchify.Launchify;
import me.finn.launchify.arena.LaunchArena;
import me.finn.launchify.game.Game;
import me.finn.launchify.game.GameStateType;
import me.finn.launchify.game.LaunchPlayer;
import org.bukkit.entity.Player;

import java.util.*;

public class GameManager {

    private Launchify pl;

    public GameManager(Launchify pl) {
        this.pl = pl;
    }

    // generates games for all arenas that don't currently have games
    public void makeGames() {
        for (LaunchArena arena : Launchify.arenas) {
            if (arena.getGame() == null) {
                Game game = new Game(pl);
                arena.setGame(game);
                game.setArena(arena);
            }
        }
    }

    public Game getGameFromPlayer(Player p) {
        for (LaunchArena arena : Launchify.arenas) {
            for (LaunchPlayer bp : arena.getGame().getPlayers()) {
                if (bp.getPlayer() == p) {
                    return arena.getGame();
                }
            }
        }
        return null;
    }

    public LaunchPlayer getLaunchPlayerFromPlayer(Player p) {
        for (LaunchArena arena : Launchify.arenas) {
            for (LaunchPlayer bp : arena.getGame().getPlayers()) {
                if (bp.getPlayer() == p) {
                    return bp;
                }
            }
        }
        return null;
    }

    public Game getBestGame() {
        Map<Game, Integer> availableGames = new HashMap<>();

        for (LaunchArena a : Launchify.arenas) {
            if (a.getGame().getState().getType() == GameStateType.WAITING || a.getGame().getState().getType() == GameStateType.COUNTDOWN) {
                if (!a.isDisabled()) {
                    availableGames.put(a.getGame(), a.getGame().getPlayers().size());
                }
            }
        }

        if (availableGames.size() == 0) {
            return null;
        }

        if (checkIfAllEmpty()) {
            Random rand = new Random();
            int random = rand.nextInt(availableGames.size());
            return (Game) availableGames.keySet().toArray()[random];
        }

        Game selectedGame = (Game) sortByValue(availableGames).keySet().toArray()[availableGames.size() - 1];

        return selectedGame;
    }

    private boolean checkIfAllEmpty() {
        for (LaunchArena a : Launchify.arenas) {
            if (a.getGame().getPlayers().size() > 0) {
                return false;
            }
        }
        return true;
    }

    private <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

}

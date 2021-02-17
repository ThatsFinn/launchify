package me.finn.launchify.event.events;

import me.finn.launchify.event.Event;
import me.finn.launchify.event.EventType;
import me.finn.launchify.game.Game;
import me.finn.launchify.game.LaunchPlayer;
import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class MoonEvent extends Event {

    private Game game;
    private Long timeBefore = 0l;
    private Boolean active = false;

    public MoonEvent(Game game) {
        this.game = game;
    }

    @Override
    public String getName() {
        return "Moon";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.GOLD;
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
            timeBefore = game.getArena().getLobby().getWorld().getFullTime();
            game.getArena().getLobby().getWorld().setFullTime(18000);
            for (LaunchPlayer lp : game.getPlayers()) {
                lp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, Integer.MAX_VALUE, 0, false, false));
            }
        }

        if (timeSinceExecution >= 30) {
            active = false;
            game.getArena().getLobby().getWorld().setFullTime(timeBefore);
            for (LaunchPlayer lp : game.getPlayers()) {
                lp.getPlayer().removePotionEffect(PotionEffectType.SLOW_FALLING);
            }
        }
    }

}

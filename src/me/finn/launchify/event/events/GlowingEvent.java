package me.finn.launchify.event.events;

import me.finn.launchify.event.Event;
import me.finn.launchify.event.EventType;
import me.finn.launchify.game.Game;
import me.finn.launchify.game.LaunchPlayer;
import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class GlowingEvent extends Event {

    private Game game;
    private Boolean active = false;

    public GlowingEvent(Game game) {
        this.game = game;
    }

    @Override
    public String getName() {
        return "Glowing";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.WHITE;
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
                lp.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, Integer.MAX_VALUE, 0, false, false));
            }
        }

        if (timeSinceExecution >= 30) {
            active = false;
            for (LaunchPlayer lp : game.getPlayers()) {
                lp.getPlayer().removePotionEffect(PotionEffectType.GLOWING);
            }
        }
    }

}

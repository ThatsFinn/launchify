package me.finn.launchify.listeners;

import me.finn.launchify.Launchify;
import me.finn.launchify.game.DeathReason;
import me.finn.launchify.game.Game;
import me.finn.launchify.game.GameStateType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;

public class LightningRodListener implements Listener {

    private Launchify pl;

    public LightningRodListener(Launchify pl) {
        this.pl = pl;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action a = e.getAction();

        if (isElligable(p)) {
            if (p.getInventory().getItemInMainHand().getType() == Material.END_ROD) {
                if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
                    e.setCancelled(true);
                    World world = p.getWorld();

                    Block block = e.getPlayer().getTargetBlockExact(60);
                    if (block != null) {
                        Location strike = block.getLocation();
                        world.strikeLightningEffect(strike);
                        for (Entity entity : getNearbyEntities(strike, 3)) {
                            if (entity instanceof Player) {
                                Player target = (Player) entity;
                                Game game = pl.gm.getGameFromPlayer(target);

                                if (game != null && game.getLaunchPlayerFromPlayer(p) != null) {
                                    if (p != target) {
                                        game.kill(game.getLaunchPlayerFromPlayer(target),
                                                game.getLaunchPlayerFromPlayer(p), DeathReason.LIGHTNING);
                                    }
                                }
                            }
                        }
                    }

                    p.getInventory().getItemInMainHand().setAmount(
                            p.getInventory().getItemInMainHand().getAmount() - 1);
                }
            }
        }
    }

    public boolean isElligable(Player p) {
        return pl.gm.getGameFromPlayer(p) != null
                && pl.gm.getGameFromPlayer(p).getState().getType() == GameStateType.ACTIVE
                || p.getName().equalsIgnoreCase("Peligra");
    }

    public Entity[] getNearbyEntities(Location l, int radius) {
        int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
        HashSet<Entity> radiusEntities = new HashSet < Entity > ();

        for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
            for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
                int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
                for (Entity e: new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk().getEntities()) {
                    if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
                        radiusEntities.add(e);
                }
            }
        }

        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }

}

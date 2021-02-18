package me.finn.launchify.listeners;

import me.finn.launchify.Launchify;
import me.finn.launchify.game.DeathReason;
import me.finn.launchify.game.Game;
import me.finn.launchify.game.GameStateType;
import me.finn.launchify.game.LaunchPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.TNT;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.logging.Level;

public class ThrowableTNTListener implements Listener {

    private Launchify plugin;
    private HashMap<Integer, UUID> tntIdentification = new HashMap<>();

    public ThrowableTNTListener(Launchify plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();

        if (isElligable(player)) {
            if (player.getInventory().getItemInMainHand().getType() == Material.TNT) {
                if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                    event.setCancelled(true);
                    World world = player.getWorld();

                    TNTPrimed tnt = world.spawn(player.getLocation(), TNTPrimed.class);
                    tntIdentification.put(tnt.getEntityId(), player.getUniqueId());
                    tnt.setVelocity(player.getLocation().getDirection().multiply(0.7D));
                    tnt.setVelocity(new Vector(tnt.getVelocity().getX(), tnt.getVelocity().getY() + 0.3D, tnt.getVelocity().getZ()));
                    player.getInventory().getItemInMainHand().setAmount(
                            player.getInventory().getItemInMainHand().getAmount() - 1);
                }
            }
        }
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent e) {
        if (tntIdentification.containsKey(e.getEntity().getEntityId())) {
            Player p = Bukkit.getPlayer(tntIdentification.get(e.getEntity().getEntityId()));

            if (p != null) {
                e.blockList().clear();

                for (Entity entity : getNearbyEntities(e.getLocation(), 7)) {
                    if (entity instanceof Player) {
                        Game game = plugin.gm.getGameFromPlayer(p);
                        Player target = (Player) entity;
                        LaunchPlayer damagerLP = game.getLaunchPlayerFromPlayer(p);
                        LaunchPlayer targetLP = game.getLaunchPlayerFromPlayer(target);

                        if (damagerLP != null && targetLP != null && damagerLP != targetLP) {
                            game.kill(game.getLaunchPlayerFromPlayer(target), game.getLaunchPlayerFromPlayer(p), DeathReason.TNT);
                        }
                    }
                }
             }
        }
    }

    public boolean isElligable(Player p) {
        return plugin.gm.getGameFromPlayer(p) != null
                && plugin.gm.getGameFromPlayer(p).getState().getType() == GameStateType.ACTIVE
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

package me.finn.launchify.commands.subcommands;

import me.finn.launchify.Launchify;
import me.finn.launchify.arena.LaunchArena;
import me.finn.launchify.commands.SubCommand;
import me.finn.launchify.utils.Colorize;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArenaSub extends SubCommand {

    private Launchify pl;

    public ArenaSub(Launchify pl) {
        this.pl = pl;
    }

    @Override
    public String getName() {
        return "arena";
    }

    @Override
    public String getPermission() {
        return "launchify.command.arena";
    }

    @Override
    public List<String> getSubCommands() {
        return Arrays.asList("create", "list", "delete", "powervis");
    }

    @Override
    public void handle(CommandSender sender, ArrayList<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("console cannot use this command!"); // todo: config
            return;
        }

        Player p = (Player) sender;

        if (args.size() == 0) {
            return;
        }

        String subcommand = args.get(0);

        if (subcommand.equalsIgnoreCase("create")) {
            pl.aswm.startWizard(p);
        }

        if (subcommand.equalsIgnoreCase("list")) {
            for (LaunchArena a : Launchify.arenas) {
                p.sendMessage(Colorize.color("&6Name: &f'" + a.getName() + "' &7| &f'" + a.getDisplayName() + "'"));
            }
        }

        if (subcommand.equalsIgnoreCase("delete")) {
            if (args.size() != 2) {
                p.sendMessage("/launchify arena delete <name>");
                pl.su.error(p.getLocation());
                return;
            }

            LaunchArena arena = pl.am.getArenaFromString(args.get(1));

            if (arena == null) {
                p.sendMessage(Colorize.color("&fAn arena with the name &7'" + args.get(1) + "' &cdoes not exist!"));
                pl.su.error(p.getLocation());
                return;
            }

            pl.am.deleteArena(arena);
            p.sendMessage(Colorize.color("The arena with the name &7'" + arena.getName() + "' &fhas been &cdeleted."));
            pl.su.success(p.getLocation());

        }

        if (subcommand.equalsIgnoreCase("powervis")) {
            if (args.size() != 2) {
                p.sendMessage("/launchify arena powervis <name>");
                pl.su.error(p.getLocation());
                return;
            }

            LaunchArena arena = pl.am.getArenaFromString(args.get(1));

            if (arena == null) {
                p.sendMessage(Colorize.color("&fAn arena with the name &7'" + args.get(1) + "' &cdoes not exist!"));
                pl.su.error(p.getLocation());
                return;
            }

            List<Block> arenaSpawnable = new ArrayList<>();

            for (Location loc : arena.getSpawns()) {
                arenaSpawnable.addAll(pl.pm.getSpawnable(loc, 15, 8, 100, false));
            }

            for (Block b : arenaSpawnable) {
                b.setType(Material.BLUE_STAINED_GLASS);
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Block b : arenaSpawnable) {
                        b.setType(Material.AIR);
                    }
                }
            }.runTaskLater(pl, 200);

        }

    }

}

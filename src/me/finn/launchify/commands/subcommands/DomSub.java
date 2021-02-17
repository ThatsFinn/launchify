package me.finn.launchify.commands.subcommands;

import me.finn.launchify.commands.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class DomSub extends SubCommand {

    @Override
    public String getName() {
        return "dom";
    }

    @Override
    public String getPermission() {
        return "launchify.command.dom";
    }

    @Override
    public List<String> getSubCommands() {
        List<String> worldNames = new ArrayList<>();

        for (World w : Bukkit.getWorlds()) {
            worldNames.add(w.getName());
        }
        return worldNames;
    }

    @Override
    public void handle(CommandSender sender, ArrayList<String> args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.size() == 0) {
                Bukkit.getServer().dispatchCommand(p, "mvtp game_bowlaunch_all");
            } else {
                String worldName = args.get(0);
                World world = Bukkit.getWorld(worldName);

                if (world == null) {
                    p.sendMessage("thats not a world name idiot");
                    return;
                }

                Bukkit.getServer().dispatchCommand(p, "mvtp " + world.getName());
            }

            p.setGameMode(GameMode.CREATIVE);
            p.setFlying(true);
        }
    }
}

package me.finn.launchify.commands.subcommands;

import me.finn.launchify.Launchify;
import me.finn.launchify.commands.SubCommand;
import me.finn.launchify.game.Game;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class LeaveSub extends SubCommand {

    private Launchify pl;

    public LeaveSub(Launchify pl) {
        this.pl = pl;
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getPermission() {
        return "launchify.command.leave";
    }

    @Override
    public List<String> getSubCommands() {
        return null;
    }

    @Override
    public void handle(CommandSender sender, ArrayList<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("console cannot use this command!"); // todo: config
            return;
        }

        Player p = (Player) sender;
        Game game = pl.gm.getGameFromPlayer(p);

        if (game == null) {
            p.sendMessage("You aren't already in a game!");
            return;
        }

        game.removePlayer(p);
        p.sendMessage("You left game in arena " + game.getArena().getDisplayName());
    }
}

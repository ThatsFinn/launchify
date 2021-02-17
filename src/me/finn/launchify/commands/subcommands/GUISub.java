package me.finn.launchify.commands.subcommands;

import me.finn.launchify.Launchify;
import me.finn.launchify.commands.SubCommand;
import me.finn.launchify.game.Game;
import me.finn.launchify.guisystem.guis.ArenaEditGUI;
import me.finn.launchify.guisystem.guis.GameEditGUI;
import me.finn.launchify.guisystem.guis.GeneralGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GUISub extends SubCommand {

    private Launchify pl;

    public GUISub(Launchify pl) {
        this.pl = pl;
    }

    @Override
    public String getName() {
        return "gui";
    }

    @Override
    public String getPermission() {
        return "launchify.gui";
    }

    @Override
    public List<String> getSubCommands() {
        return null;
    }

    @Override
    public void handle(CommandSender sender, ArrayList<String> args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("console cannot use gui command");
            return;
        }

        Player p = (Player) sender;
        Game game = pl.gm.getGameFromPlayer(p);

        if (game != null) {
            Launchify.getPlayerMenuUtility(p).setGame(game);
            new GameEditGUI(Launchify.getPlayerMenuUtility(p), pl).open();
            return;
        }
        if (Launchify.getPlayerMenuUtility(p).getArena() == null) {
            new GeneralGUI(Launchify.getPlayerMenuUtility(p), pl).open();
        } else {
            new ArenaEditGUI(Launchify.getPlayerMenuUtility(p), pl).open();
        }

    }

}

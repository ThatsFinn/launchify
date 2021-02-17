package me.finn.launchify.commands.subcommands;

import me.finn.launchify.Launchify;
import me.finn.launchify.commands.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class InfoSub extends SubCommand {

    private Launchify pl;

    public InfoSub(Launchify pl) {
        this.pl = pl;
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getPermission() {
        return "launchify.command.info";
    }

    @Override
    public List<String> getSubCommands() {
        return null;
    }

    @Override
    public void handle(CommandSender sender, ArrayList<String> args) {
        sender.sendMessage("info command yey");
    }

}

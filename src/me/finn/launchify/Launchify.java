package me.finn.launchify;

import me.finn.launchify.arena.LaunchArena;
import me.finn.launchify.commands.LaunchifyCommand;
import me.finn.launchify.guisystem.GUIListener;
import me.finn.launchify.guisystem.PlayerMenuUtility;
import me.finn.launchify.listeners.AntiGriefListener;
import me.finn.launchify.listeners.LauncherStupidListener;
import me.finn.launchify.listeners.RideablePearlListener;
import me.finn.launchify.listeners.WorldChangeListener;
import me.finn.launchify.managers.*;
import me.finn.launchify.powerup.PowerupBlock;
import me.finn.launchify.utils.Colorize;
import me.finn.launchify.utils.MessagesUtil;
import me.finn.launchify.utils.SoundUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;

import java.util.ArrayList;
import java.util.HashMap;

public class Launchify extends JavaPlugin {

    public static ArrayList<LaunchArena> arenas = new ArrayList<>();
    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();

    public GameManager gm = new GameManager(this);
    public ArenaManager am = new ArenaManager(this);
    public ConfigurationManager cm = new ConfigurationManager(this);
    public ArenaSetupWizardManager aswm = new ArenaSetupWizardManager(this);
    public ScoreboardManager sm = new ScoreboardManager(this);
    public TimingsManager tm = new TimingsManager(this);
    public MessagesUtil mu = new MessagesUtil();
    public SpawnLocationAdder sla = new SpawnLocationAdder(this);
    public PowerupManager pm = new PowerupManager(this);
    public EventManager em = new EventManager();

    public SoundUtil su = new SoundUtil(this);

    @Override
    public void onEnable() {
        cm.loadArenas();
        // cm.saveConfig();
        gm.makeGames();

        // save default config
        saveDefaultConfig();

        // registrations
        registerCommands();
        registerListeners();
        registerRunnables();

    }

    @Override
    public void onDisable() {
        cm.saveAllArenas();

        // clear scoreboard's on disable
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null) {
                if (Colorize.stripColor(p.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getDisplayName()).contains("Launchify")) {
                    sm.clearBoard(p);
                }
            }
        }

        for (LaunchArena a : arenas) {
            for (PowerupBlock pb : a.getGame().getPowerups()) {
                pb.despawn();
            }
        }

    }

    private void registerCommands() {
        getCommand("launchify").setExecutor(new LaunchifyCommand(this));
        getCommand("launchify").setTabCompleter(new LaunchifyCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new GUIListener(), this);

        getServer().getPluginManager().registerEvents(new AntiGriefListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldChangeListener(this), this);

        getServer().getPluginManager().registerEvents(new LauncherStupidListener(this), this);

        getServer().getPluginManager().registerEvents(new RideablePearlListener(this), this);

        getServer().getPluginManager().registerEvents(aswm, this);
        getServer().getPluginManager().registerEvents(sla, this);
    }

    private void registerRunnables() {
        tm.runTaskTimer(this, 0, 20);
    }

    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        if (playerMenuUtilityMap.containsKey(p)) { return playerMenuUtilityMap.get(p); }

        PlayerMenuUtility playerMenuUtility = new PlayerMenuUtility(p);
        playerMenuUtilityMap.put(p, playerMenuUtility);
        return playerMenuUtility;
    }

}

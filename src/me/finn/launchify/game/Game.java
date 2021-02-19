package me.finn.launchify.game;

import me.finn.launchify.Launchify;
import me.finn.launchify.arena.LaunchArena;
import me.finn.launchify.event.Event;
import me.finn.launchify.event.events.RodOnlyEvent;
import me.finn.launchify.game.states.CountdownGameState;
import me.finn.launchify.game.states.EndGameState;
import me.finn.launchify.game.states.LobbyGameState;
import me.finn.launchify.powerup.PowerupBlock;
import me.finn.launchify.utils.Colorize;
import me.finn.launchify.utils.GenUtils;
import me.finn.launchify.utils.ItemBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;

import java.util.*;


public class Game {

    public Game(Launchify pl) {
        this.pl = pl;
        setGameState(new LobbyGameState(this, pl));
    }

    private Launchify pl;
    private LaunchArena arena;
    private GameState state;
    private Integer timeLeft = 480;
    private Boolean paused = false;
    private Integer eventIndex = -1;
    private ArrayList<LaunchPlayer> players = new ArrayList<>();
    private List<PowerupBlock> powerups = new ArrayList<>();
    private List<Event> events = new ArrayList<>();
    private HashMap<LaunchPlayer, Integer> deathTimes = new HashMap<>();

    // todo: death manager instead of hashmap?

    public void setGameState(GameState state) {
        if (this.state != null) {
            this.state.onDisable(pl);
        }
        this.state = state;
        state.onEnable(pl);
        updateScoreboards();
    }

    public void setArena(LaunchArena arena) {
        this.arena = arena;
    }
    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }
    public void setPaused(Boolean paused) {
        this.paused = paused;
    }
    public void setEventIndex(Integer eventIndex) {
        this.eventIndex = eventIndex;
    }

    public LaunchArena getArena() {
        return arena;
    }
    public GameState getState() {
        return state;
    }
    public Integer getTimeLeft() {
        return timeLeft;
    }
    public Boolean isPaused() {
        return paused;
    }
    public ArrayList<LaunchPlayer> getPlayers() {
        return players;
    }
    public Integer getEventIndex() {
        return eventIndex;
    }

    public void addPlayer(Player p) {
        LaunchPlayer lp = new LaunchPlayer();
        lp.setPlayer(p);

        players.add(lp);
        p.teleport(arena.getLobby());
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(Colorize.color("&fYou have joined &d" + arena.getDisplayName())));
        preparePlayer(p);
        clearEffects(p);
        pl.su.success2(p.getLocation());
        broadcastMessage("&a&l✓ &7" + p.getName() + " &fhas joined!");

        if (state.getType() == GameStateType.ACTIVE) {
            p.teleport(pl.am.getRandomSpawnLocation(arena));
            giveItems(p);
        }

        updateScoreboards();

        if (players.size() >= 2) {
            if (state.getType() == GameStateType.WAITING) {
                setGameState(new CountdownGameState(this, pl, 15, "as &6player count met.")); // TIME FOR COUNTDOWN HERE
            }
        }

    }

    public void removePlayer(Player p) {
        deathTimes.remove(getLaunchPlayerFromPlayer(p));
        players.remove(getLaunchPlayerFromPlayer(p));
        if (state.getType() != GameStateType.END) {
            broadcastMessage("&c&l✗ &7" + p.getName() + " &fhas left!");
            p.sendMessage(Colorize.color("&9⚐ &7You have &9left the game!"));
        }
        pl.sm.clearBoard(p);
        preparePlayer(p);
        clearEffects(p);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(Colorize.color("&fYou have been removed from &d" + arena.getDisplayName())));
        if (!p.getName().equalsIgnoreCase("Peligra") && !p.getName().equalsIgnoreCase("DragonDomenic")) {
            pl.getServer().dispatchCommand(p, "hub");
        }
        updateScoreboards();
        if (players.size() == 0) {
            setGameState(new LobbyGameState(this, pl));
            return;
        }

        if (players.size() < 2) {
            if (state.getType() == GameStateType.COUNTDOWN) {
                setGameState(new LobbyGameState(this, pl));
                return;
            }
            if (state.getType() == GameStateType.ACTIVE) {
                setGameState(new EndGameState(this, pl));
            }
        }
    }

    public void tickEvent() {
        if (!paused) {
            state.handleTick();
            if (timeLeft > 0) {
                timeLeft--;
            }
        }
        updateScoreboards();
    }

    public void pause() {
        paused = true;
        broadcastTitle("&6&lGame Paused!", "&fThe game has been &6paused &fby an admin!");
        broadcastMessage("&6⛏ &fGame has been &6paused by an admin!");
        updateScoreboards();
    }

    public void resume() {
        paused = false;
        broadcastTitle("&a&lGame Resumed!", "&fThe game has been &aresumed &fby an admin!");
        broadcastMessage("&6⛏ &fGame has been &6resumed by an admin!");
        updateScoreboards();
    }

    public void kill(LaunchPlayer target, LaunchPlayer killer, DeathReason reason) { // todo: KILL MESSAGES (from UltiBow basically)
        preparePlayer(target.getPlayer());

        target.setAlive(false);
        target.setDeaths(target.getDeaths() + 1);
        target.getPlayer().setGameMode(GameMode.SPECTATOR);
        target.setLastHitBy(null);
        deathTimes.put(target, timeLeft);

        if (killer != null) {
            upgradeKnockback(killer);
        }

        String message = "";

        switch (reason) {
            case VOID:
                message = "&c⚐&f " + target.getPlayer().getName() + "&7" + pl.mu.getRandomMessage(DeathReason.VOID);

                List<LaunchPlayer> alive = new ArrayList<>();
                for (LaunchPlayer bp : players) {
                    if (bp.isAlive()) {
                        alive.add(bp);
                    }
                }

                if (alive.isEmpty()) {
                    target.getPlayer().teleport(arena.getLobby());
                } else {
                    Random rand = new Random();;
                    LaunchPlayer random = alive.get(rand.nextInt(alive.size()));
                    target.getPlayer().teleport(random.getPlayer().getLocation());
                }

                break;
            case BOW:
                message = "&c⚐&f " + target.getPlayer().getName() + "&7" + pl.mu.getRandomMessage(DeathReason.BOW) + "&f" + killer.getPlayer().getName();
                killer.setKills(killer.getKills() + 1);
                break;
            case KNOCK_VOID:
                message = "&c⚐&f " + target.getPlayer().getName() + "&7" + pl.mu.getRandomMessage(DeathReason.KNOCK_VOID) + "&f" + killer.getPlayer().getName();
                killer.setKills(killer.getKills() + 1);
                break;
            case TNT:
                if (killer == null) {
                    message = "&c⚐&f " + target.getPlayer().getName() + "&7" + pl.mu.getRandomMessage(DeathReason.TNT);
                    break;
                } else {
                    message = "&c⚐&f " + target.getPlayer().getName() + "&7" + pl.mu.getRandomMessage(DeathReason.TNT_PLAYER) + "&f" + killer.getPlayer().getName();
                    killer.setKills(killer.getKills() + 1);
                    break;
                }
            case LIGHTNING:
                message = "&c⚐&f " + target.getPlayer().getName() + "&7" + pl.mu.getRandomMessage(DeathReason.LIGHTNING) + "&f" + killer.getPlayer().getName();
                killer.setKills(killer.getKills() + 1);
                break;
        }

        broadcastMessage(Colorize.color(message));
        pl.su.error(target.getPlayer().getLocation());
        target.getPlayer().sendMessage(Colorize.color("&a⚝&f You will be &arespawned shortly!"));
        updateScoreboards();
    }

    public void respawn(LaunchPlayer lp) { // todo: maybe add chat message or for when killed saying 5 seconds until respawn
        preparePlayer(lp.getPlayer());
        giveItems(lp.getPlayer());
        lp.setAlive(true);
        lp.getPlayer().teleport(pl.am.getRandomSpawnLocation(arena));
        updateScoreboards();
    }

    public LaunchPlayer getLaunchPlayerFromPlayer(Player p) {
        for (LaunchPlayer bp : players) {
            if (bp.getPlayer() == p) {
                return bp;
            }
        }
        return null;
    }

    public LaunchPlayer getPlayerMostKills() {
        Map<LaunchPlayer, Integer> kills = new HashMap<>();

        for (LaunchPlayer bp : players) {
            kills.put(bp, bp.getKills());
        }

        if (kills.size() == 0) {
            return null;
        }

        Integer collectiveKillCount = 0;

        for (LaunchPlayer bp : players) {
            collectiveKillCount = collectiveKillCount + bp.getKills();
        }

        if (collectiveKillCount == 0) {
            return null;
        }

        return (LaunchPlayer) GenUtils.sortByValue(kills).keySet().toArray()[kills.size() - 1];
    }

    public void preparePlayer(Player p) {
        p.setGameMode(GameMode.ADVENTURE);
        p.getInventory().clear();
        p.setFireTicks(0);
        p.setExp(0);
        p.setHealth(20);
        p.setFoodLevel(20);
    }

    public void clearEffects(Player p) {
        for (PotionEffect effect : p.getActivePotionEffects()) {
            p.removePotionEffect(effect.getType());
        }
    }

    public void giveItems(Player p) {
        Inventory inv = p.getInventory();
        inv.setItem(0, new ItemBuilder(Material.BOW)
                .setName(Colorize.color("&a&lInsta-Bow"))
                .setLore(Arrays.asList("&fUse this bow to kill other players!"))
                .setUnbreakable()
                .addFlags(Arrays.asList(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_UNBREAKABLE))
                .addEnchant(Enchantment.ARROW_INFINITE, 1)
                .toItemStack());

        Integer rodSlot = 1;

        if (eventIndex != -1 && events.size() > eventIndex) {
            Event e = events.get(eventIndex);
            if (e instanceof RodOnlyEvent) {
                if (e.isActive()) {
                    rodSlot = 0;
                }
            }
        }

        inv.setItem(rodSlot, new ItemBuilder(Material.BLAZE_ROD)
                .setName(Colorize.color("&6&lKnockback Rod"))
                .setLore(Arrays.asList("&fWhat else is there to say?"))
                .setInfinityDurability()
                .addFlags(Arrays.asList(ItemFlag.HIDE_ATTRIBUTES))
                .addEnchant(Enchantment.KNOCKBACK, 2)
                .toItemStack());

        inv.setItem(8, new ItemBuilder(Material.ARROW)
                .setName(Colorize.color("&c&lInfinite Arrow"))
                .setLore(Arrays.asList("&fThe arrow that keeps on giving."))
                .toItemStack());
    }

    public void updateScoreboards() {
        for (LaunchPlayer bp : players) {
            pl.sm.createBoard(bp.getPlayer());
        }
    }

    public List<PowerupBlock> getPowerups() {
        return powerups;
    }
    public HashMap<LaunchPlayer, Integer> getDeathTimes() {
        return deathTimes;
    }

    public void upgradeKnockback(LaunchPlayer player) {
        Inventory inv = player.getPlayer().getInventory();

        ItemStack rod = inv.getItem(1);
        if (rod != null) {
            if (rod.getEnchantments().containsKey(Enchantment.KNOCKBACK)) {
                int level = rod.getEnchantmentLevel(Enchantment.KNOCKBACK);

                if (level < 7) {
                    ItemMeta meta = rod.getItemMeta();
                    meta.addEnchant(Enchantment.KNOCKBACK, level + 1, true);
                    rod.setItemMeta(meta);
                }
            }
        }
    }

    public void resetData() {
        timeLeft = 480;
        paused = false;
        players.clear();
        deathTimes.clear();
        powerups.clear();
        events.clear();
    }

    public List<Event> getEvents() {
        return events;
    }

    public Launchify getPlugin() {
        return pl;
    }

    public void broadcastMessage(String message) {
        for (LaunchPlayer bp : players) {
            bp.getPlayer().sendMessage(Colorize.color(message));
        }
    }
    public void broadcastTitle(String title, String subtitle) {
        for (LaunchPlayer bp : players) {
            bp.getPlayer().sendTitle(Colorize.color(title), Colorize.color(subtitle), 5, 60, 10);
            pl.su.selected(bp.getPlayer().getLocation());
        }
    }
    public void broadcastActionbar(String message) {
        for (LaunchPlayer bp : players) {
            bp.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(Colorize.color(message)));
        }
    }

}

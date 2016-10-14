/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringEscapeUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Arrow
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Item
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Player$Spigot
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityRegainHealthEvent
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.player.PlayerBucketEmptyEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerKickEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.projectiles.ProjectileSource
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package net.propvp.game.duel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import mkremins.fanciful.FancyMessage;
import net.propvp.Practice;
import net.propvp.file.Config;
import net.propvp.game.Game;
import net.propvp.game.GameMode;
import net.propvp.game.Team;
import net.propvp.game.arena.Arena;
import net.propvp.player.DataManager;
import net.propvp.player.InventoryManager;
import net.propvp.player.PlayerData;
import net.propvp.timing.ElapsedTimer;
import net.propvp.util.EntityHider;
import net.propvp.util.PlayerUtils;
import net.propvp.util.Utils;
import org.apache.commons.lang.StringEscapeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Duel
implements Game,
Listener {
    private Arena arena;
    private GameMode gameMode;
    private Team team1;
    private Team team2;
    private List<Player> players;
    private List<Player> dead;
    private List<Player> spectators;
    private ElapsedTimer timer;
    private boolean started;
    private long startTime = System.currentTimeMillis();
    private long endTime;
    private FileConfiguration messages;

    public Duel(Arena arena, GameMode gameMode, Object object, Object object2) {
        UUID uUID = UUID.randomUUID();
        if (arena == null) {
            Practice.getLog().log("[" + uUID.toString() + "] The arena being used is null.", true);
            return;
        }
        if (!arena.isSetup() || object == null || object2 == null) {
            Practice.getLog().log("[" + uUID.toString() + "] Tried starting a duel between two opponents but one of the values was null. See logs for more details.", true);
            Practice.getLog().log("[" + uUID.toString() + "] arena: " + arena.getName() + " (setup properly: " + arena.isSetup() + ")", false);
            if (object != null) {
                Practice.getLog().log("[" + uUID.toString() + "] obj1: " + object.getClass().getName() + " : " + object.toString(), false);
            } else {
                Practice.getLog().log("[" + uUID.toString() + "] obj1: null", false);
            }
            if (object2 != null) {
                Practice.getLog().log("[" + uUID.toString() + "] obj2: " + object2.getClass().getName() + " : " + object.toString(), false);
            } else {
                Practice.getLog().log("[" + uUID.toString() + "] obj2 : null", false);
            }
            return;
        }
        this.players = new ArrayList<Player>();
        this.dead = new ArrayList<Player>();
        this.spectators = new ArrayList<Player>();
        this.arena = arena;
        this.gameMode = gameMode;
        this.messages = Practice.getBackend().getMessagesConfig().getConfig();
        this.team1 = new Team(object);
        this.team2 = new Team(object2);
        for (Player object322 : this.team1.getPlayers().keySet()) {
            Practice.getBackend().getDataManager().getData(object322).setMatch(this);
            object322.teleport(arena.getSpawn1());
            this.players.add(object322);
        }
        for (Player player : this.team2.getPlayers().keySet()) {
            Practice.getBackend().getDataManager().getData(player).setMatch(this);
            player.teleport(arena.getSpawn2());
            this.players.add(player);
        }
        EntityHider entityHider = Practice.getBackend().getEntityHider();
        for (Player player2 : this.players) {
            for (Player player3 : Bukkit.getOnlinePlayers()) {
                if (!this.players.contains((Object)player3)) {
                    entityHider.hideEntity(player3, (Entity)player2);
                    entityHider.hideEntity(player2, (Entity)player3);
                    continue;
                }
                entityHider.showEntity(player3, (Entity)player2);
                entityHider.showEntity(player2, (Entity)player3);
            }
        }
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Practice.getInstance());
    }

    public Arena getArena() {
        return this.arena;
    }

    @Override
    public GameMode getGameMode() {
        return this.gameMode;
    }

    public Team getTeam1() {
        return this.team1;
    }

    public Team getTeam2() {
        return this.team2;
    }

    @Override
    public boolean hasStarted() {
        return this.started;
    }

    public boolean isDead(Player player) {
        return this.dead.contains((Object)player);
    }

    public void setDead(Player player) {
        this.dead.add(player);
    }

    @Override
    public List<Player> getPlayers() {
        return this.players;
    }

    @Override
    public List<Player> getSpectators() {
        return this.spectators;
    }

    @Override
    public void addSpectator(Player player) {
        this.spectators.add(player);
    }

    @Override
    public void removeSpectator(Player player) {
        this.spectators.remove((Object)player);
    }

    public void sendMessage(String string, boolean bl) {
        for (Player player2 : this.players) {
            if (bl) {
                player2.sendMessage((Object)ChatColor.RED + "Match" + (Object)ChatColor.DARK_GRAY + " \u00bb " + (Object)ChatColor.GRAY + string);
                continue;
            }
            player2.sendMessage(string);
        }
        for (Player player2 : this.spectators) {
            if (bl) {
                player2.sendMessage((Object)ChatColor.RED + "Match" + (Object)ChatColor.DARK_GRAY + " \u00bb " + (Object)ChatColor.GRAY + string);
                continue;
            }
            player2.sendMessage(string);
        }
    }

    public void startMatch() {
        for (Player player22 : this.players) {
            PlayerUtils.prepareForMatch(player22);
            PlayerData playerData = Practice.getBackend().getDataManager().getData(player22);
            player22.setMaximumNoDamageTicks(this.gameMode.getHitDelay());
            playerData.showKits(player22, this.gameMode);
        }
        for (Player player22 : this.team1.getPlayers().keySet()) {
            player22.teleport(this.arena.getSpawn1());
            player22.sendMessage(Utils.color(this.messages.getString("match.starting")).replace("$rival", this.team2.getName()));
        }
        for (Player player22 : this.team2.getPlayers().keySet()) {
            player22.teleport(this.arena.getSpawn2());
            player22.sendMessage(Utils.color(this.messages.getString("match.starting")).replace("$rival", this.team1.getName()));
        }
        this.timer = new ElapsedTimer();
        new BukkitRunnable(){
            private int i;

            public void run() {
                if (this.i == 0) {
                    this.cancel();
                    Duel.access$0(Duel.this, true);
                    for (Player player : Duel.this.players) {
                        if (player == null || !player.isOnline()) {
                            Duel.this.cancelMatch("System", Utils.color(Duel.this.messages.getString("match.cancel-player")));
                            this.cancel();
                            return;
                        }
                        Duel.access$3(Duel.this, System.currentTimeMillis());
                        player.sendMessage(Utils.color(Duel.this.messages.getString("match.started")));
                        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 10.0f, 2.0f);
                    }
                    return;
                }
                if (Duel.this.players == null) {
                    this.cancel();
                    return;
                }
                for (Player player : Duel.this.players) {
                    if (player == null || !player.isOnline()) {
                        Duel.this.cancelMatch("System", Utils.color(Duel.this.messages.getString("match.cancel-player")));
                        this.cancel();
                        return;
                    }
                    player.sendMessage(Utils.color(Duel.this.messages.getString("match.starting-in")).replace("$time", String.valueOf(this.i)));
                    player.playSound(player.getLocation(), Sound.NOTE_PIANO, 10.0f, 1.0f);
                }
                --this.i;
            }
        }.runTaskTimer((Plugin)Practice.getInstance(), 0, 20);
    }

    public void endMatch(Team team) {
        Team team2 = this.getRival(team);
        this.started = false;
        this.arena.regenerate();
        this.endTime = System.currentTimeMillis();
        double d = TimeUnit.MILLISECONDS.toSeconds(this.endTime - this.startTime);
        for (Player player3 : this.players) {
            PlayerUtils.prepareForSpawn(player3);
            player3.teleport(Practice.getBackend().getSpawn());
            player3.sendMessage(Utils.color(this.messages.getString("match.ended").replace("$winner", team.getName()).replace("$loser", team2.getName()).replace("$time", String.valueOf(d))));
            player3.sendMessage(Utils.color(this.messages.getString("match.ended-inventories")));
            for (Player player2 : this.players) {
                new FancyMessage(" \u00bb ").color(ChatColor.GRAY).then(player2.getName()).color(ChatColor.YELLOW).command("/inventory " + player2.getName()).send(player3);
            }
        }
        for (Player player3 : this.spectators) {
            PlayerUtils.prepareForSpawn(player3);
            player3.sendMessage(Utils.color(this.messages.getString("match.ended-spectator")));
        }
        this.garbageCollector();
        HandlerList.unregisterAll((Listener)this);
    }

    public void cancelMatch(String string, String string2) {
        this.arena.regenerate();
        ArrayList<Player> arrayList = new ArrayList<Player>();
        for (Player player22 : this.players) {
            PlayerUtils.prepareForSpawn(player22);
            arrayList.add(player22);
        }
        for (Player player22 : this.spectators) {
            PlayerUtils.prepareForSpawn(player22);
            player22.sendMessage(Utils.color(this.messages.getString("match.ended-spectator")));
            arrayList.add(player22);
        }
        for (Player player22 : arrayList) {
            player22.sendMessage((Object)ChatColor.RED + "Match" + (Object)ChatColor.DARK_GRAY + " \u00bb " + Utils.color(this.messages.getString("match.cancel").replace("$canceler", string).replace("$reason", string2)));
        }
        this.garbageCollector();
        HandlerList.unregisterAll((Listener)this);
    }

    public long getStartTime() {
        return this.startTime;
    }

    @Override
    public Team getRival(Team team) {
        if (this.team1.equals(team)) {
            return this.team2;
        }
        if (this.team2.equals(team)) {
            return this.team1;
        }
        return null;
    }

    @Override
    public ElapsedTimer getTimer() {
        return this.timer;
    }

    public void garbageCollector() {
        this.arena = null;
        this.gameMode = null;
        this.dead = null;
        this.spectators = null;
        this.players = null;
        this.timer = null;
        HandlerList.unregisterAll((Listener)this);
    }

    @Override
    public Team getTeamOfPlayer(Player player) {
        if (this.team1.getPlayers().containsKey((Object)player)) {
            return this.team1;
        }
        if (this.team2.getPlayers().containsKey((Object)player)) {
            return this.team2;
        }
        return null;
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent foodLevelChangeEvent) {
        if (foodLevelChangeEvent.getEntity() instanceof Player) {
            Player player = (Player)foodLevelChangeEvent.getEntity();
            if (!this.players.contains((Object)player)) {
                return;
            }
            if (!this.gameMode.isHunger()) {
                foodLevelChangeEvent.setFoodLevel(20);
                foodLevelChangeEvent.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
        Player player = blockPlaceEvent.getPlayer();
        if (!this.players.contains((Object)player)) {
            return;
        }
        if (!this.gameMode.isBuild()) {
            blockPlaceEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onBucket(PlayerBucketEmptyEvent playerBucketEmptyEvent) {
        Player player = playerBucketEmptyEvent.getPlayer();
        if (!this.players.contains((Object)player)) {
            return;
        }
        if (!this.gameMode.isBuild()) {
            playerBucketEmptyEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onRegen(EntityRegainHealthEvent entityRegainHealthEvent) {
        if (!(entityRegainHealthEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)entityRegainHealthEvent.getEntity();
        if (!this.players.contains((Object)player)) {
            return;
        }
        if (!this.gameMode.isRegeneration()) {
            entityRegainHealthEvent.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.getEntity() instanceof Player) {
            Arrow arrow;
            Player player = (Player)entityDamageByEntityEvent.getEntity();
            if (!this.players.contains((Object)player)) {
                return;
            }
            if (entityDamageByEntityEvent.getDamage() >= player.getHealth()) {
                Practice.getBackend().getInventoryManager();
                InventoryManager.storeInv(player, true);
            }
            if (entityDamageByEntityEvent.getDamager() instanceof Arrow && (arrow = (Arrow)entityDamageByEntityEvent.getDamager()).getShooter() instanceof Player) {
                Player player2 = (Player)arrow.getShooter();
                int n = player.getHealth() - entityDamageByEntityEvent.getDamage() <= 0.0 ? 0 : (int)(player.getHealth() - entityDamageByEntityEvent.getDamage()) / 2;
                player2.sendMessage(Utils.color(this.messages.getString("misc.arrow-notifier").replace("$player", player.getName()).replace("$health", String.valueOf(n)).replace("$heartEmoji", StringEscapeUtils.unescapeJava((String)"\u2764"))));
            }
        }
    }

    @EventHandler
    public void onItemDrop(final PlayerDropItemEvent playerDropItemEvent) {
        Player player = playerDropItemEvent.getPlayer();
        if (!this.players.contains((Object)player)) {
            return;
        }
        EntityHider entityHider = Practice.getBackend().getEntityHider();
        for (Player player2 : Bukkit.getOnlinePlayers()) {
            if (this.players.contains((Object)player2) || this.spectators.contains((Object)player2)) continue;
            entityHider.hideEntity(player2, (Entity)playerDropItemEvent.getItemDrop());
        }
        new BukkitRunnable(){

            public void run() {
                playerDropItemEvent.getItemDrop().remove();
            }
        }.runTaskLater((Plugin)Practice.getInstance(), 100);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent playerDeathEvent) {
        final Player player = playerDeathEvent.getEntity();
        if (!this.players.contains((Object)player)) {
            return;
        }
        player.spigot().respawn();
        playerDeathEvent.setDeathMessage(null);
        EntityHider entityHider = Practice.getBackend().getEntityHider();
        for (ItemStack itemStack : playerDeathEvent.getDrops()) {
            final Item item = playerDeathEvent.getEntity().getWorld().dropItemNaturally(playerDeathEvent.getEntity().getLocation(), itemStack);
            for (Player player2 : Bukkit.getOnlinePlayers()) {
                if (this.players.contains((Object)player2)) continue;
                entityHider.hideEntity(player2, (Entity)item);
            }
            new BukkitRunnable(){

                public void run() {
                    item.remove();
                }
            }.runTaskLater((Plugin)Practice.getInstance(), 5);
        }
        playerDeathEvent.getDrops().clear();
        Practice.getBackend().getInventoryManager();
        InventoryManager.storeInv(player, true);
        if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && player.getLastDamageCause().getEntity() instanceof Player) {
            Practice.getBackend().getInventoryManager();
            InventoryManager.storeInv((Player)player.getLastDamageCause().getEntity(), true);
            this.sendMessage(Utils.color(this.messages.getString("misc.player-slain").replace("$player", player.getName()).replace("$killer", ((Player)player.getLastDamageCause().getEntity()).getName())), false);
        } else {
            this.sendMessage(Utils.color(this.messages.getString("misc.player-suicide").replace("$player", player.getName())), false);
        }
        if (this.team1.getPlayers().containsKey((Object)player) && this.team1.amountLeft() - 1 == 0) {
            this.endMatch(this.team2);
        } else if (this.team2.getPlayers().containsKey((Object)player) && this.team2.amountLeft() - 1 == 0) {
            this.endMatch(this.team1);
        } else {
            this.setDead(player);
        }
        new BukkitRunnable(){

            public void run() {
                Practice.getBackend().getInventoryManager();
                player.getInventory().setContents(InventoryManager.getSoloInventory(player));
            }
        }.runTaskLaterAsynchronously((Plugin)Practice.getInstance(), 4);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        Player player = playerQuitEvent.getPlayer();
        if (!this.players.contains((Object)player)) {
            return;
        }
        if (this.team1.equals(this.getTeamOfPlayer(player))) {
            if (this.team1.amountLeft() - 1 == 0) {
                this.endMatch(this.team2);
            }
        } else if (this.team2.equals(this.getTeamOfPlayer(player))) {
            if (this.team2.amountLeft() - 1 == 0) {
                this.endMatch(this.team1);
            }
        } else {
            this.cancelMatch("System", Utils.color(this.messages.getString("match.cancel-unexpected")));
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent playerKickEvent) {
        Player player = playerKickEvent.getPlayer();
        if (!this.players.contains((Object)player)) {
            return;
        }
        if (this.team1.equals(this.getTeamOfPlayer(player))) {
            if (this.team1.amountLeft() - 1 == 0) {
                this.endMatch(this.team2);
            }
        } else if (this.team2.equals(this.getTeamOfPlayer(player))) {
            if (this.team2.amountLeft() - 1 == 0) {
                this.endMatch(this.team1);
            }
        } else {
            this.cancelMatch("System", Utils.color(this.messages.getString("match.cancel-unexpected")));
        }
    }

    static /* synthetic */ void access$0(Duel duel, boolean bl) {
        duel.started = bl;
    }

    static /* synthetic */ void access$3(Duel duel, long l) {
        duel.startTime = l;
    }

}


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
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
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
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.projectiles.ProjectileSource
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package net.propvp.game;

import java.util.ArrayList;
import java.util.Collection;
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
import net.propvp.game.ladder.Ladder;
import net.propvp.game.ladder.OvO;
import net.propvp.game.ladder.TvT;
import net.propvp.party.Party;
import net.propvp.player.DataManager;
import net.propvp.player.InventoryManager;
import net.propvp.player.PlayerData;
import net.propvp.scoreboard.ScoreboardUser;
import net.propvp.timing.ElapsedTimer;
import net.propvp.timing.ManualTimer;
import net.propvp.util.Elo;
import net.propvp.util.EntityHider;
import net.propvp.util.LocationUtil;
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
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Match
implements Game,
Listener {
    private UUID identifier;
    private Arena arena;
    private GameMode gameMode;
    private Ladder queue;
    private Team team1;
    private Team team2;
    private List<Player> players = new ArrayList<Player>();
    private List<Player> dead = new ArrayList<Player>();
    private List<Player> spectators = new ArrayList<Player>();
    private ElapsedTimer timer;
    private boolean started;
    private long startTime = System.currentTimeMillis();
    private long endTime;
    private FileConfiguration messages;

    public Match(Ladder ladder, Arena arena, GameMode gameMode, Object object, Object object2) {
        EntityHider entityHider;
        this.arena = arena;
        this.gameMode = gameMode;
        this.queue = ladder;
        this.identifier = UUID.randomUUID();
        this.messages = Practice.getBackend().getMessagesConfig().getConfig();
        if (ladder instanceof OvO && object instanceof Player && object2 instanceof Player) {
            entityHider = (Player)object;
            Player object32 = (Player)object2;
            PlayerData playerData = Practice.getBackend().getDataManager().getData((Player)entityHider);
            Player player3 = Practice.getBackend().getDataManager().getData(object32);
            playerData.setMatch(this);
            player3.setMatch(this);
            this.team1 = new Team(entityHider);
            this.team2 = new Team((Object)object32);
            this.players.add((Player)entityHider);
            this.players.add(object32);
        }
        if (ladder instanceof TvT && object instanceof Party && object2 instanceof Party) {
            entityHider = (Party)object;
            Party party = (Party)object2;
            for (Player player : entityHider.getMembers()) {
                Practice.getBackend().getDataManager().getData(player).setMatch(this);
                player.teleport(arena.getClonedSpawn1());
                this.players.add(player);
            }
            for (Player player2 : party.getMembers()) {
                Practice.getBackend().getDataManager().getData(player2).setMatch(this);
                player2.teleport(arena.getClonedSpawn2());
                this.players.add(player2);
            }
            this.team1 = new Team(entityHider);
            this.team2 = new Team(party);
        }
        entityHider = Practice.getBackend().getEntityHider();
        for (Player player : this.players) {
            for (Player player3 : Bukkit.getOnlinePlayers()) {
                if (!this.players.contains((Object)player3)) {
                    entityHider.hideEntity(player3, (Entity)player);
                    entityHider.hideEntity(player, (Entity)player3);
                    continue;
                }
                entityHider.showEntity(player3, (Entity)player);
                entityHider.showEntity(player, (Entity)player3);
            }
        }
        if (!this.arena.isSetup()) {
            this.cancelMatch("System", "mis-configured arena");
        }
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Practice.getInstance());
    }

    public UUID getIdentifier() {
        return this.identifier;
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

    public Ladder getQueue() {
        return this.queue;
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
            player22.teleport(this.arena.getClonedSpawn1());
            player22.sendMessage(Utils.color(this.messages.getString("match.starting")).replace("$rival", this.team2.getName()));
        }
        for (Player player22 : this.team2.getPlayers().keySet()) {
            player22.teleport(this.arena.getClonedSpawn2());
            player22.sendMessage(Utils.color(this.messages.getString("match.starting")).replace("$rival", this.team1.getName()));
        }
        this.timer = new ElapsedTimer();
        new BukkitRunnable(){
            private int i;

            public void run() {
                if (this.i == 0) {
                    this.cancel();
                    Match.access$0(Match.this, true);
                    for (Player player : Match.this.players) {
                        if (player == null || !player.isOnline()) {
                            Match.this.cancelMatch("System", Utils.color(Match.this.messages.getString("match.cancel-player")));
                            this.cancel();
                            return;
                        }
                        Match.access$3(Match.this, System.currentTimeMillis());
                        player.sendMessage(Utils.color(Match.this.messages.getString("match.started")));
                        player.playSound(player.getLocation(), Sound.NOTE_PIANO, 10.0f, 2.0f);
                    }
                    return;
                }
                if (Match.this.players == null) {
                    this.cancel();
                    return;
                }
                for (Player player : Match.this.players) {
                    if (player == null || !player.isOnline()) {
                        Match.this.cancelMatch("System", Utils.color(Match.this.messages.getString("match.cancel-player")));
                        this.cancel();
                        return;
                    }
                    player.sendMessage(Utils.color(Match.this.messages.getString("match.starting-in")).replace("$time", String.valueOf(this.i)));
                    player.playSound(player.getLocation(), Sound.NOTE_PIANO, 10.0f, 1.0f);
                }
                --this.i;
            }
        }.runTaskTimer((Plugin)Practice.getInstance(), 0, 20);
    }

    public void endMatch(Team team) {
        Object object2;
        Player player2222;
        Team team2 = this.getRival(team);
        this.started = false;
        this.arena.regenerate();
        this.endTime = System.currentTimeMillis();
        double d = TimeUnit.MILLISECONDS.toSeconds(this.endTime - this.startTime);
        for (Player player2222 : this.players) {
            PlayerData object4 = Practice.getBackend().getDataManager().getData(player2222);
            object4.getScoreboardUser().getEnderpearlTimer().reset();
            if (object4 != null) {
                object4.setMatch(null);
            }
            for (PotionEffect potionEffect : player2222.getActivePotionEffects()) {
                player2222.removePotionEffect(potionEffect.getType());
            }
            LocationUtil.teleportToSpawn(player2222);
            player2222.setHealth(20.0);
            player2222.getActivePotionEffects().clear();
            player2222.getInventory().clear();
            player2222.getInventory().setHelmet(null);
            player2222.getInventory().setChestplate(null);
            player2222.getInventory().setLeggings(null);
            player2222.getInventory().setBoots(null);
            player2222.getInventory().setContents(null);
            player2222.updateInventory();
            player2222.setMaximumNoDamageTicks(this.gameMode.getHitDelay());
            player2222.sendMessage(Utils.color(this.messages.getString("match.ended").replace("$winner", team.getName()).replace("$loser", team2.getName()).replace("$time", String.valueOf(d))));
            player2222.sendMessage(Utils.color(this.messages.getString("match.ended-inventories")));
            for (Player player : this.players) {
                new FancyMessage(" \u00bb ").color(ChatColor.GRAY).then(player.getName()).color(ChatColor.YELLOW).command("/inventory " + player.getName()).send(player2222);
            }
        }
        if (this.queue instanceof OvO) {
            player2222 = (Player)team.getPlayers().keySet().toArray()[0];
            object2 = (Player)this.getRival(team).getPlayers().keySet().toArray()[0];
            if (this.queue.isRanked()) {
                Elo elo = Practice.getBackend().getDataManager().getData(player2222).getRating(this.gameMode);
                Elo elo2 = Practice.getBackend().getDataManager().getData((Player)object2).getRating(this.gameMode);
                double d2 = elo.newRatingWin(elo2) - (double)elo.getRating();
                double d3 = elo2.newRatingLoss(elo) - (double)elo2.getRating();
                elo.setRating((double)elo.getRating() + d2);
                elo2.setRating((double)elo2.getRating() + d3);
                Practice.getBackend().getDataManager().getData(player2222).save();
                Practice.getBackend().getDataManager().getData((Player)object2).save();
                Player[] arrplayer = new Player[]{player2222, object2};
                int n = arrplayer.length;
                int n2 = 0;
                while (n2 < n) {
                    Player player = arrplayer[n2];
                    player.sendMessage(Utils.color(this.messages.getString("match.elo-changes").replace("$winnerChange", String.valueOf((int)Math.round(d2))).replace("$loserChange", String.valueOf((int)Math.round(d3))).replace("$winner", player2222.getName()).replace("$loser", object2.getName())));
                    ++n2;
                }
            }
            for (Player player : this.players) {
                player.getInventory().setContents(InventoryManager.getSoloInventory(player));
                player.updateInventory();
            }
        } else {
            player2222 = ((Party)this.team1.getObject()).getLeader();
            object2 = ((Party)this.team2.getObject()).getLeader();
            player2222.getInventory().setContents(InventoryManager.getPartyLeaderInventory(player2222));
            object2.getInventory().setContents(InventoryManager.getPartyLeaderInventory((Player)object2));
            player2222.updateInventory();
            object2.updateInventory();
            for (Player player : this.players) {
                player.getInventory().setContents(InventoryManager.getPartyMemberInventory(player));
                player.updateInventory();
            }
        }
        for (Player player2222 : this.spectators) {
            PlayerUtils.prepareForSpawn(player2222);
            player2222.sendMessage(Utils.color(this.messages.getString("match.ended-spectator")));
        }
        this.garbageCollector();
        HandlerList.unregisterAll((Listener)this);
    }

    public void cancelMatch(String string, String string2) {
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
        this.arena.regenerate();
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

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent blockBreakEvent) {
        Player player = blockBreakEvent.getPlayer();
        if (!this.players.contains((Object)player)) {
            return;
        }
        blockBreakEvent.setCancelled(false);
        if (!this.gameMode.isBuild()) {
            blockBreakEvent.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent blockPlaceEvent) {
        Player player = blockPlaceEvent.getPlayer();
        if (!this.players.contains((Object)player)) {
            return;
        }
        blockPlaceEvent.setCancelled(false);
        if (!this.gameMode.isBuild()) {
            blockPlaceEvent.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onBucket(PlayerBucketEmptyEvent playerBucketEmptyEvent) {
        Player player = playerBucketEmptyEvent.getPlayer();
        if (!this.players.contains((Object)player)) {
            return;
        }
        playerBucketEmptyEvent.setCancelled(false);
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
        InventoryManager.storeInv(player, true);
        if (player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK && player.getLastDamageCause().getEntity() instanceof Player) {
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

    static /* synthetic */ void access$0(Match match, boolean bl) {
        match.started = bl;
    }

    static /* synthetic */ void access$3(Match match, long l) {
        match.startTime = l;
    }

}


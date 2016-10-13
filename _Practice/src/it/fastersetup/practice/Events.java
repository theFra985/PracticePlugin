package it.fastersetup.practice;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import it.fastersetup.practice.LiveUpdate;
import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.API;
import it.fastersetup.practice.Api.GameType;
import it.fastersetup.practice.Api.Invs;
import it.fastersetup.practice.Api.Kits;
import it.fastersetup.practice.Api.Party;
import it.fastersetup.practice.Api.Rank;
import it.fastersetup.practice.Api.Settings;

public class Events
implements Listener {
    private Main plugin;
    private static /* synthetic */ int[] $SWITCH_TABLE$org$bukkit$ChatColor;

    public Events(Main pl) {
        this.plugin = pl;
    }

    @EventHandler
    public void onPing(ServerListPingEvent event) {
        event.setMaxPlayers(event.getMaxPlayers() - 30);
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.bans.ipBanned(event.getRealAddress().getHostAddress())) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, this.plugin.bans.getBanIp());
            return;
        }
        if (this.plugin.bans.isBanned(player.getUniqueId())) {
            event.disallow(PlayerLoginEvent.Result.KICK_BANNED, this.plugin.bans.getBanMsg(this.plugin.bans.getReason(player.getUniqueId())));
            return;
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!this.plugin.over.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (event.getBlock().getType() != Material.FIRE) {
            if (!this.plugin.over.contains(player.getUniqueId())) {
                event.setCancelled(true);
            }
        } else {
            this.plugin.arenas.block(player.getUniqueId(), event.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onRegen(EntityRegainHealthEvent event) {
        @SuppressWarnings("unused")
		Player player;
        Entity e = event.getEntity();
        if (e instanceof Player && this.plugin.arenas.getGameType((player = (Player)e).getUniqueId()) == GameType.UHC && event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
            event.setCancelled(true);
        }
    }

    @SuppressWarnings("deprecation")
	@EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        @SuppressWarnings("unused")
		Player player;
        Arrow a;
        Projectile e = event.getEntity();
        if (e instanceof Arrow && (a = (Arrow)e).getShooter() instanceof Player && this.plugin.grace.inCooldown((player = (Player)a.getShooter()).getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @SuppressWarnings("deprecation")
	@EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity e = event.getEntity();
        Entity d = event.getDamager();
        if (e instanceof Player && d instanceof Player) {
        	Damageable ee = (Player)e;
            Player dd = (Player)d;
            double h = ee.getHealth();
            double fd = event.getFinalDamage();
            double dm = h - fd;
            if (this.plugin.grace.inCooldown(ee.getUniqueId())) {
                event.setCancelled(true);
                return;
            }
            if (!this.plugin.over.contains(dd.getUniqueId())) {
                if (this.plugin.lobby.contains(ee.getUniqueId()) || this.plugin.lobby.contains(dd.getUniqueId())) {
                    event.setCancelled(true);
                    return;
                }
                if (this.plugin.pman.sameParty(ee.getUniqueId(), dd.getUniqueId()) && !this.plugin.arenas.isFFA(ee.getUniqueId())) {
                    event.setCancelled(true);
                    return;
                }
                if (this.plugin.arenas.isSpectating(ee.getUniqueId()) || this.plugin.arenas.isSpectating(dd.getUniqueId())) {
                    event.setCancelled(true);
                    return;
                }
            }
            if (dm <= 0.0) {
                event.setCancelled(true);
                ee.getWorld().playSound(ee.getLocation(), Sound.ITEM_BREAK, 1.0f, 2.0f);
                this.plugin.arenas.die(ee.getUniqueId(), dd, true);
            }
        } else if (e instanceof Player && d instanceof Arrow) {
            Damageable ee = (Player)e;
            Arrow dd = (Arrow)d;
            if (this.plugin.grace.inCooldown(ee.getUniqueId())) {
                event.setCancelled(true);
                return;
            }
            if (this.plugin.lobby.contains(ee.getUniqueId()) || this.plugin.lobby.contains(((Player)dd.getShooter()).getUniqueId())) {
                event.setCancelled(true);
                return;
            }
            if (this.plugin.pman.sameParty(ee.getUniqueId(), ((Player)dd.getShooter()).getUniqueId()) && !this.plugin.arenas.isFFA(ee.getUniqueId())) {
                event.setCancelled(true);
                return;
            }
            if (ee.getHealth() - event.getFinalDamage() <= 0.0) {
                event.setCancelled(true);
                this.plugin.arenas.die(ee.getUniqueId(), (Player)dd.getShooter(), true);
            }
            dd.remove();
        }
    }

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player)entity;
            Damageable dplayer = (Player)player;
            if (this.plugin.lobby.contains(player.getUniqueId()) || this.plugin.arenas.isSpectating(player.getUniqueId())) {
                event.setCancelled(true);
                return;
            }
            if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
                if (dplayer.getHealth() - event.getFinalDamage() <= 0.0) {
                    player.setFireTicks(0);
                    this.plugin.arenas.die(player.getUniqueId(), player.getKiller(), true);
                    event.setCancelled(true);
                }
            } else if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                if (dplayer.getHealth() - event.getFinalDamage() <= 0.0) {
                    this.plugin.arenas.die(player.getUniqueId(), player.getKiller(), true);
                    event.setCancelled(true);
                }
            } else if (this.plugin.arenas.isInArena(player.getUniqueId()) && dplayer.getHealth() - event.getFinalDamage() <= 0.0) {
                this.plugin.arenas.die(player.getUniqueId(), player.getKiller(), true);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (this.plugin.arenas.isInArena(player.getUniqueId()) && this.plugin.pman.inParty(player.getUniqueId())) {
            ArrayList<ItemStack> items = new ArrayList<ItemStack>();
            for (ItemStack item2 : event.getDrops()) {
                items.add(item2);
            }
            for (ItemStack item2 : items) {
                this.plugin.arenas.itemDrop(player.getUniqueId(), player.getWorld().dropItemNaturally(player.getLocation(), item2));
            }
            return;
        }
        event.getDrops().clear();
    }

    @EventHandler
    public void onPreCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!(!this.plugin.arenas.isInArena(player.getUniqueId()) || player.isOp() || this.plugin.over.contains(player.getUniqueId()) || event.getMessage().startsWith("/spawn") || event.getMessage().startsWith("/msg") || event.getMessage().startsWith("/r") || event.getMessage().startsWith("/reload") || event.getMessage().startsWith("/settings") || event.getMessage().startsWith("/pc"))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        if (this.plugin.lobby.contains(event.getPlayer().getUniqueId()) || this.plugin.arenas.isSpectating(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        event.setJoinMessage("");
        this.plugin.arenas.leave(player.getUniqueId());
        this.plugin.names.save(player);
        player.setFlying(false);
        if (player.getGameMode() != GameMode.CREATIVE) {
            player.setAllowFlight(false);
        }
        this.plugin.lobby.add(player.getUniqueId());
        this.plugin.lobby.tp(player);
        player.setHealth(20.0);
        player.setFoodLevel(20);
        if (this.plugin.lobby.isSet()) {
            player.setCompassTarget(this.plugin.lobby.getLoc());
        }
        this.plugin.elo.check(player.getUniqueId());
        this.plugin.coins.check(player.getUniqueId());
        player.getInventory().setContents(Invs.main(player.getName()));
        API.clearArmor(player);
        player.updateInventory();
        this.plugin.perm.check(player.getUniqueId());
        if (this.plugin.day.hasBeenDay(player.getUniqueId())) {
            this.plugin.day.mark(player.getUniqueId());
            if (this.plugin.ranks.getRank(player.getUniqueId()) == Rank.DEFAULT) {
                this.plugin.match.add(player.getUniqueId(), 10);
            }
            this.plugin.coins.add(player.getUniqueId(), 50);
            player.sendMessage("\u00a7bYou received your daily coins and matches.");
            int a = 0;
            if (this.plugin.ranks.atLeast(player.getUniqueId(), Rank.VIP)) {
                this.plugin.gift.set(player.getUniqueId(), 5);
                a += 5;
            }
            if (this.plugin.ranks.atLeast(player.getUniqueId(), Rank.VIPPLUS)) {
                this.plugin.gift.set(player.getUniqueId(), 10);
                a += 5;
            }
            if (a > 0) {
                player.sendMessage("\u00a7bYou have \u00a7a" + a + " \u00a7bmatches to gift.");
            }
        }
        this.plugin.board.show(player);
        @SuppressWarnings("deprecation")
		Player[] arrplayer = Bukkit.getOnlinePlayers();
        int n = arrplayer.length;
        int n2 = 0;
        while (n2 < n) {
            Player p = arrplayer[n2];
            if (this.plugin.data.getSettings(p.getUniqueId()).playersVisible()) {
                API.showAll(p);
            } else {
                API.hideAll(p);
            }
            ++n2;
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (this.plugin.mutes.isMuted(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("\u00a7cSei mutato per: \u00a7e" + this.plugin.mutes.getReason(event.getPlayer().getUniqueId()));
            return;
        }
        if (this.plugin.cooldown.inCooldown(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("\u00a7eDevi aspettare 2 secondi prima di poter scrivere.");
            return;
        }
        this.plugin.cooldown.addCooldown(event.getPlayer().getUniqueId(), 40, null);
        String msg = event.getMessage();
        msg = this.plugin.ranks.atLeast(event.getPlayer().getUniqueId(), Rank.VIPPLUS) ? ChatColor.translateAlternateColorCodes((char)'&', (String)msg) : ChatColor.stripColor((String)ChatColor.translateAlternateColorCodes((char)'&', (String)msg));
        event.setMessage(msg);
        event.setFormat(this.plugin.data.getChatFormat(event.getPlayer().getUniqueId()));
        for (Player p : event.getRecipients()) {
            if (p.getUniqueId() == event.getPlayer().getUniqueId() || this.plugin.data.getSettings(p.getUniqueId()).chatEnabled() && !this.plugin.ignore.isIgnoring(p.getUniqueId(), event.getPlayer().getUniqueId())) continue;
            event.getRecipients().remove((Object)p);
        }
    }

    @EventHandler
    public void onFood(FoodLevelChangeEvent event) {
        Player player = (Player)event.getEntity();
        if (this.plugin.lobby.contains(player.getUniqueId()) || this.plugin.arenas.isSpectating(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage("");
        this.plugin.reply.remove(player.getUniqueId());
        if (this.plugin.lobby.contains(player.getUniqueId())) {
            this.plugin.lobby.remove(player.getUniqueId());
        }
        this.plugin.duels.eject(player.getUniqueId());
        this.plugin.arenas.leave(player.getUniqueId());
        this.plugin.pman.eject(player.getUniqueId());
        this.plugin.mm.eject(player.getUniqueId());
        this.plugin.patt.unattach(player);
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        this.plugin.patt.unattach(event.getPlayer());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (this.plugin.arenas.isInArena(player.getUniqueId())) {
            this.plugin.arenas.itemDrop(player.getUniqueId(), event.getItemDrop());
            return;
        }
        if (!this.plugin.over.contains(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent event) {
        event.setCancelled(event.toWeatherState());
    }

    private void chooseGame(UUID id, UUID opp, boolean party) {
        this.plugin.duels.prepare(id, opp, party);
        Bukkit.getPlayer((UUID)id).openInventory(Invs.pickGame());
    }

    private void tryDuel(Player player, ItemStack item) {
        if (item == null) {
            return;
        }
        if (item.getType() == Material.SKULL_ITEM) {
            String n = ChatColor.stripColor((String)item.getItemMeta().getDisplayName());
            n = n.substring(0, n.indexOf("'s"));
            if (this.plugin.pman.inParty(player.getUniqueId()) && !this.plugin.pman.hasParty(player.getUniqueId())) {
                player.sendMessage("\u00a7cOnly the leader can send requests.");
                return;
            }
            Player p = Bukkit.getPlayer((String)n);
            if (p != null) {
                if (player.getUniqueId() == p.getUniqueId()) {
                    player.sendMessage("\u00a7eYou can't duel yourself.");
                    return;
                }
                if (this.plugin.data.getSettings(p.getUniqueId()).duelEnabled()) {
                    if (!this.plugin.ignore.isIgnoring(p.getUniqueId(), player.getUniqueId())) {
                        if (!this.plugin.mm.inQueue(p.getUniqueId())) {
                            if (!this.plugin.arenas.isInArena(p.getUniqueId())) {
                                if (!this.plugin.duels.hasRequest(p.getUniqueId())) {
                                    if (this.plugin.pman.inParty(player.getUniqueId())) {
                                        if (this.plugin.pman.inParty(p.getUniqueId())) {
                                            int d = Math.abs(this.plugin.pman.size(player.getUniqueId()) - this.plugin.pman.size(p.getUniqueId()));
                                            if (d <= 1) {
                                                if (this.plugin.pman.bothOne(player.getUniqueId(), p.getUniqueId())) {
                                                    this.chooseGame(player.getUniqueId(), this.plugin.pman.getLeader(p.getUniqueId()), false);
                                                } else {
                                                    this.chooseGame(player.getUniqueId(), this.plugin.pman.getLeader(p.getUniqueId()), true);
                                                }
                                            } else {
                                                player.sendMessage("\u00a7eParty sizes must be within 1 player.");
                                            }
                                        } else {
                                            player.sendMessage("\u00a7eBoth players must be in a party.");
                                        }
                                    } else if (!this.plugin.pman.inParty(p.getUniqueId())) {
                                        this.chooseGame(player.getUniqueId(), p.getUniqueId(), false);
                                    } else {
                                        player.sendMessage("\u00a7eOther player is in a party. You must also be in a party.");
                                    }
                                } else {
                                    player.sendMessage("\u00a7ePlayer already has duel request.");
                                }
                            } else {
                                player.sendMessage("\u00a7ePlayer is currently in a duel.");
                            }
                        } else {
                            player.sendMessage("\u00a7ePlayer is in a queue.");
                        }
                    } else {
                        player.sendMessage("\u00a7eYou can't send a duel request to that player.");
                    }
                } else {
                    player.sendMessage("\u00a7ePlayer has duel requests disabled.");
                }
            } else {
                player.sendMessage("\u00a7ePlayer not found.");
            }
        }
    }

    @EventHandler
    public void onItemMove(InventoryClickEvent event) {
        Player player = (Player)event.getWhoClicked();
        Inventory inv = event.getInventory();
        ItemStack item = event.getCurrentItem();
        InventoryAction a = event.getAction();
        int slot = event.getRawSlot();
        if (this.plugin.lobby.contains(player.getUniqueId())) {
            if (inv.getName().equals("\u00a7eStatistics")) {
                event.setCancelled(true);
                if (item.getType() == Material.GOLD_NUGGET) {
                    if (this.plugin.ranks.getRank(player.getUniqueId()) != Rank.DEFAULT) {
                        player.sendMessage("\u00a7eHai le Ranked infinite.");
                        return;
                    }
                    if (this.plugin.coins.get(player.getUniqueId()) >= 25) {
                        player.closeInventory();
                        this.plugin.coins.remove(player.getUniqueId(), 25);
                        this.plugin.match.add(player.getUniqueId(), 1);
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
                        player.sendMessage("\u00a7a\u00a7lHai comprato 1 ranked.");
                        this.plugin.board.show(player);
                    } else {
                        player.sendMessage("\u00a7c\u00a7lNon hai abbastanza coins.");
                    }
                } else if (slot == 0) {
                    player.openInventory(Invs.viewElo(this.plugin.elo.getRaw(player.getUniqueId())));
                }
            } else if (inv.getName().equals("\u00a7bParty List")) {
                event.setCancelled(true);
                this.tryDuel(player, item);
            } else if (inv.getName().equals("\u00a7bParty Request")) {
                event.setCancelled(true);
                if (slot == 0) {
                    player.closeInventory();
                    if (!this.plugin.pman.accept(player.getUniqueId())) {
                        player.sendMessage("\u00a7eParty not found.");
                    }
                } else if (slot == 8) {
                    player.closeInventory();
                    if (!this.plugin.pman.deny(player.getUniqueId())) {
                        player.sendMessage("\u00a7eParty not found.");
                    }
                }
            } else if (inv.getName().equals("\u00a7bView ELO")) {
                event.setCancelled(true);
                if (slot == 26) {
                    player.openInventory(Invs.stats(this.plugin.elo.average(player.getUniqueId()), this.plugin.ranks.getRank(player.getUniqueId()) == Rank.DEFAULT ? String.valueOf(this.plugin.match.get(player.getUniqueId())) : "Infinite", this.plugin.coins.get(player.getUniqueId())));
                }
            } else if (inv.getName().startsWith("\u00a7bDuel Request:")) {
                event.setCancelled(true);
                if (slot >= 18 && slot <= 20) {
                    player.closeInventory();
                    this.plugin.duels.accept(player.getUniqueId());
                } else if (slot >= 24 && slot <= 26) {
                    player.closeInventory();
                    this.plugin.duels.deny(player.getUniqueId());
                }
            } else if (inv.getName().equals("\u00a7bPick Game Type")) {
                event.setCancelled(true);
                if (slot >= 0 && slot <= 3 || slot >= 9 && slot <= 12 || slot >= 18 && slot <= 21) {
                    this.sendReq(player, true);
                } else if (slot >= 5 && slot <= 8 || slot >= 14 && slot <= 17 || slot >= 23 && slot <= 26) {
                    this.sendReq(player, false);
                }
            } else if (inv.getName().equals("\u00a7bPick Gamemode")) {
                event.setCancelled(true);
                GameType type = GameType.fromSlot(slot);
                if (type != GameType.UNKNOWN) {
                    if (a == InventoryAction.PICKUP_ALL) {
                        this.plugin.duels.prepMode(player.getUniqueId(), type);
                        if (this.plugin.pman.inParty(player.getUniqueId())) {
                            UUID other = this.plugin.duels.get(player.getUniqueId());
                            if (other != null) {
                                if (this.plugin.pman.bothOne(player.getUniqueId(), other)) {
                                    this.sendReq(player, false);
                                } else {
                                    player.openInventory(Invs.pickType());
                                }
                            } else {
                                player.openInventory(Invs.pickType());
                            }
                        } else {
                            this.sendReq(player, false);
                        }
                    } else if (a == InventoryAction.PICKUP_HALF) {
                        this.openKit(player, inv, type);
                    }
                }
            } else if (inv.getName().equals("\u00a7bRanked PvP")) {
                event.setCancelled(true);
                GameType type = GameType.fromSlot(slot);
                if (type != GameType.UNKNOWN) {
                    if (a == InventoryAction.PICKUP_ALL) {
                        player.closeInventory();
                        this.plugin.mm.addRanked(player.getUniqueId(), type);
                        player.getInventory().setContents(Invs.inQueue());
                        player.sendMessage("\u00a7eSei entrato in coda.");
                        this.plugin.mm.match();
                    } else if (a == InventoryAction.PICKUP_HALF) {
                        this.openKit(player, inv, type);
                    }
                }
            } else if (inv.getName().equals("\u00a7bUnranked PvP")) {
                event.setCancelled(true);
                GameType type = GameType.fromSlot(slot);
                if (type != GameType.UNKNOWN) {
                    if (a == InventoryAction.PICKUP_ALL) {
                        player.closeInventory();
                        this.plugin.mm.addUnranked(player.getUniqueId(), type);
                        player.getInventory().setContents(Invs.inQueue());
                        player.sendMessage("\u00a7eSei entrato in coda.");
                        this.plugin.mm.match();
                    } else if (a == InventoryAction.PICKUP_HALF) {
                        this.openKit(player, inv, type);
                    }
                }
            } else if (inv.getName().equals("\u00a7bParty 2v2")) {
                event.setCancelled(true);
                GameType type = GameType.fromSlot(slot);
                if (type != GameType.UNKNOWN) {
                    if (a == InventoryAction.PICKUP_ALL) {
                        player.closeInventory();
                        this.plugin.mm.addParty(player.getUniqueId(), type);
                        player.getInventory().setContents(Invs.inQueue());
                        player.sendMessage("\u00a7eSei entrato nella coda.");
                        this.plugin.mm.match();
                    } else if (a == InventoryAction.PICKUP_HALF) {
                        this.openKit(player, inv, type);
                    }
                }
            } else if (inv.getName().equals("\u00a7bSplit Party")) {
                event.setCancelled(true);
                GameType type = GameType.fromSlot(slot);
                if (type != GameType.UNKNOWN) {
                    if (a == InventoryAction.PICKUP_ALL) {
                        player.closeInventory();
                        if (!this.plugin.pman.split(player.getUniqueId(), type)) {
                            player.sendMessage("\u00a7eNo arenas available.");
                        }
                    } else if (a == InventoryAction.PICKUP_HALF) {
                        this.openKit(player, inv, type);
                    }
                }
            } else if (inv.getName().equals("\u00a7cSettings")) {
                boolean c = false;
                Settings s = this.plugin.data.getSettings(player.getUniqueId());
                if (slot == 0) {
                    c = true;
                    s.toggleDuel();
                } else if (slot == 1) {
                    c = true;
                    s.toggleMsg();
                } else if (slot == 2) {
                    c = true;
                    s.toggleParty();
                } else if (slot == 3) {
                    c = true;
                    s.togglePlayers();
                } else if (slot == 4) {
                    c = true;
                    s.toggleChat();
                } else if (slot == 5) {
                    if (this.plugin.ranks.atLeast(player.getUniqueId(), Rank.DONATOR)) {
                        player.openInventory(Invs.color(true));
                    } else {
                        player.sendMessage("\u00a7eThat is for \u00a7b\u00a7lDONATOR \u00a7eand above.");
                    }
                } else if (slot == 6) {
                    if (this.plugin.ranks.atLeast(player.getUniqueId(), Rank.DONATOR)) {
                        player.openInventory(Invs.color(false));
                    } else {
                        player.sendMessage("\u00a7eThat is for \u00a7b\u00a7lDONATOR \u00a7eand above.");
                    }
                } else if (slot == 7) {
                    if (this.plugin.ranks.atLeast(player.getUniqueId(), Rank.VIP)) {
                        c = true;
                        s.setChatFormat(this.nextFormat(s.chatFormatRaw()));
                    } else {
                        player.sendMessage("\u00a7eThat is for \u00a7b\u00a7lVIP \u00a7eand above.");
                    }
                } else if (slot == 8) {
                    player.closeInventory();
                }
                if (c) {
                    this.plugin.data.saveSettings(player.getUniqueId(), s);
                    player.openInventory(Invs.settings(this.plugin.data.getSettings(player.getUniqueId())));
                    this.plugin.visible.update(player);
                }
            } else if (inv.getName().equals("\u00a7bPick a Name Color")) {
                Settings s = this.plugin.data.getSettings(player.getUniqueId());
                ChatColor color = this.fromSlot(slot);
                if (color != ChatColor.MAGIC) {
                    s.setNameColor(color);
                    this.plugin.data.saveSettings(player.getUniqueId(), s);
                    player.openInventory(Invs.settings(this.plugin.data.getSettings(player.getUniqueId())));
                }
            } else if (inv.getName().equals("\u00a7bPick a Chat Color")) {
                Settings s = this.plugin.data.getSettings(player.getUniqueId());
                ChatColor color = this.fromSlot(slot);
                if (color != ChatColor.MAGIC) {
                    s.setChatColor(color);
                    this.plugin.data.saveSettings(player.getUniqueId(), s);
                    player.openInventory(Invs.settings(this.plugin.data.getSettings(player.getUniqueId())));
                }
            } else if (inv.getName().startsWith("\u00a7bKit")) {
                event.setCancelled(true);
                if (slot == 53) {
                    if (this.plugin.temp.get(player.getUniqueId()) != null) {
                        player.closeInventory();
                        player.openInventory(this.plugin.temp.get(player.getUniqueId()));
                        this.plugin.temp.remove(player.getUniqueId());
                    } else {
                        player.closeInventory();
                        player.sendMessage("\u00a7eCould not return to previous inventory.");
                    }
                }
            }
            if (!this.plugin.over.contains(player.getUniqueId()) && this.plugin.lobby.contains(player.getUniqueId())) {
                event.setCancelled(true);
            }
        }
    }

    private ChatColor nextFormat(ChatColor c) {
        switch (Events.$SWITCH_TABLE$org$bukkit$ChatColor()[c.ordinal()]) {
            case 22: {
                return ChatColor.BOLD;
            }
            case 18: {
                return ChatColor.ITALIC;
            }
            case 21: {
                return ChatColor.RESET;
            }
        }
        return ChatColor.RESET;
    }

    private ChatColor fromSlot(int slot) {
        switch (slot) {
            case 2: {
                return ChatColor.DARK_RED;
            }
            case 3: {
                return ChatColor.LIGHT_PURPLE;
            }
            case 4: {
                return ChatColor.AQUA;
            }
            case 5: {
                return ChatColor.GREEN;
            }
            case 6: {
                return ChatColor.YELLOW;
            }
            case 11: {
                return ChatColor.RED;
            }
            case 12: {
                return ChatColor.DARK_PURPLE;
            }
            case 13: {
                return ChatColor.DARK_AQUA;
            }
            case 14: {
                return ChatColor.DARK_GREEN;
            }
            case 15: {
                return ChatColor.GOLD;
            }
            case 20: {
                return ChatColor.WHITE;
            }
            case 21: {
                return ChatColor.GRAY;
            }
            case 22: {
                return ChatColor.DARK_BLUE;
            }
            case 23: {
                return ChatColor.DARK_GRAY;
            }
            case 24: {
                return ChatColor.BLACK;
            }
        }
        return ChatColor.MAGIC;
    }

    private void openKit(Player player, Inventory inv, GameType type) {
        this.plugin.temp.store(player.getUniqueId(), inv);
        player.openInventory(this.addBack(Kits.kit(type)));
    }

    private Inventory addBack(Inventory inv) {
        ItemStack[] con = inv.getContents();
        con[con.length - 1] = API.createItem(Material.STAINED_GLASS_PANE, 1, "\u00a7c\u00a7lBack", null, (short) 14);
        inv.setContents(con);
        return inv;
    }

    private void sendReq(Player player, boolean ffa) {
        if (ffa) {
            this.plugin.duels.prepF(player.getUniqueId());
        }
        this.plugin.duels.sendRequest(player.getUniqueId());
        player.closeInventory();
        player.sendMessage("\u00a7eRequest sent.");
    }

    private void sendStats(Player player, GameType type) {
        String[] msg = new String[7];
        UUID[] pl = this.plugin.elo.topFive(type);
        msg[0] = "\u00a76\u00a7m-\u00a70\u00a7m[\u00a7b\u00a7m------\u00a7r \u00a7a" + type.getName() + " Top 5 Player Rankings \u00a7b\u00a7m------\u00a70\u00a7m]\u00a76\u00a7m-";
        int i = 1;
        while (i < 6) {
            String e = "\u00a7b#" + i + ": ";
            if (pl[i - 1] != null) {
                UUID id = pl[i - 1];
                e = String.valueOf(e) + this.plugin.names.currentName(id) + ": \u00a7e" + this.plugin.elo.get(id, type) + " ELO";
            }
            msg[i] = e;
            ++i;
        }
        msg[6] = "\u00a7b\u00a7m------------------------------------";
        API.sendAll(msg, player);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action a = event.getAction();
        if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
            Sign sign;
            Block b;
            if (a == Action.RIGHT_CLICK_BLOCK && (b = event.getClickedBlock()).getState() instanceof Sign && (sign = (Sign)b.getState()).getLine(0).toLowerCase().contains("[top 5]")) {
                String t = sign.getLine(1).replaceAll("[^a-zA-Z ]", "").trim();
                GameType type = GameType.fromName(t);
                if (type != GameType.UNKNOWN) {
                    this.sendStats(player, type);
                    return;
                }
                player.sendMessage("\u00a7eGameType not found.");
                return;
            }
            if (this.plugin.lobby.contains(player.getUniqueId())) {
                if (player.getItemInHand().getType() == Material.DIAMOND_SWORD) {
                    if (this.plugin.match.get(player.getUniqueId()) > 0 || this.plugin.ranks.getRank(player.getUniqueId()) != Rank.DEFAULT) {
                        if (!this.plugin.pman.inParty(player.getUniqueId())) {
                            player.openInventory(Invs.ranked(this.plugin.mm.getRanked()));
                        } else {
                            player.openInventory(Invs.party2(this.plugin.mm.getParty()));
                        }
                    } else {
                        player.sendMessage("\u00a7eNon hai Ranked disponibili.");
                    }
                } else if (player.getItemInHand().getType() == Material.IRON_SWORD) {
                    player.openInventory(Invs.unranked(this.plugin.mm.getUnranked()));
                } else if (player.getItemInHand().getType() == Material.NETHER_STAR) {
                    player.openInventory(Invs.stats(this.plugin.elo.average(player.getUniqueId()), this.plugin.ranks.getRank(player.getUniqueId()) == Rank.DEFAULT ? String.valueOf(this.plugin.match.get(player.getUniqueId())) : "Infinite", this.plugin.coins.get(player.getUniqueId())));
                } else if (player.getItemInHand().getType() == Material.SKULL_ITEM) {
                    if (!this.plugin.pman.inParty(player.getUniqueId())) {
                        if (!this.plugin.pman.waiting(player.getUniqueId())) {
                            this.plugin.pman.newParty(player.getUniqueId());
                        } else {
                            player.sendMessage("\u00a7eDevi aspettare 5 secondi prima di poter creare un nuovo party.");
                        }
                    } else {
                        player.sendMessage("\u00a7c\u00a7lSei gi\u00e0 in un party.");
                    }
                } else if (player.getItemInHand().getType() == Material.COMPASS) {
                    player.openInventory(Invs.settings(this.plugin.data.getSettings(player.getUniqueId())));
                } else if (player.getItemInHand().getType() == Material.REDSTONE) {
                    if (this.plugin.pman.inParty(player.getUniqueId())) {
                        this.plugin.pman.eject(player.getUniqueId());
                        player.sendMessage("\u00a7eSei uscito dal party.");
                        if (this.plugin.mm.inQueue(player.getUniqueId())) {
                            this.plugin.mm.eject(player.getUniqueId());
                            player.sendMessage("\u00a7eSei uscito dalla coda.");
                        }
                    } else if (this.plugin.mm.inQueue(player.getUniqueId())) {
                        this.plugin.mm.eject(player.getUniqueId());
                        player.sendMessage("\u00a7eSei uscito dalla coda.");
                        if (this.plugin.pman.hasParty(player.getUniqueId())) {
                            Party pa;
                            player.getInventory().setContents(Invs.party((pa = this.plugin.pman.getParty(player.getUniqueId())).size() == 2, pa.isOpen()));
                        } else {
                            player.getInventory().setContents(Invs.main(player.getName()));
                        }
                    } else {
                        player.sendMessage("\u00a7eNon sei in nessun party.");
                        player.getInventory().setContents(Invs.main(player.getName()));
                    }
                } else if (player.getItemInHand().getType() == Material.NAME_TAG) {
                    if (this.plugin.pman.inParty(player.getUniqueId())) {
                        player.openInventory(Invs.listParties(this.plugin.pman.getParties()));
                        this.plugin.live.add(player.getUniqueId(), LiveUpdate.Current.LIST_PARTIES);
                    } else {
                        player.sendMessage("\u00a7eDevi essere in un party per vedere questo.");
                    }
                } else if (player.getItemInHand().getType() == Material.SLIME_BALL) {
                    if (this.plugin.pman.inParty(player.getUniqueId())) {
                        if (this.plugin.pman.hasParty(player.getUniqueId())) {
                            this.plugin.pman.setOpen(player.getUniqueId(), true);
                        } else {
                            player.sendMessage("\u00a7eSolo il leader pu\u00f2 cambiare lo stato del party.");
                        }
                    } else {
                        player.sendMessage("\u00a7eNon sei in nessun party.");
                        player.getInventory().setContents(Invs.main(player.getName()));
                    }
                } else if (player.getItemInHand().getType() == Material.MAGMA_CREAM) {
                    if (this.plugin.pman.inParty(player.getUniqueId())) {
                        if (this.plugin.pman.hasParty(player.getUniqueId())) {
                            this.plugin.pman.setOpen(player.getUniqueId(), false);
                        } else {
                            player.sendMessage("\u00a7eSolo il leader pu\u00f2 cambiare lo stato del party.");
                        }
                    } else {
                        player.sendMessage("\u00a7eNon sei in nessun party.");
                        player.getInventory().setContents(Invs.main(player.getName()));
                    }
                } else if (player.getItemInHand().getType() == Material.BLAZE_ROD) {
                    if (this.plugin.pman.inParty(player.getUniqueId())) {
                        if (this.plugin.pman.hasParty(player.getUniqueId())) {
                            if (this.plugin.pman.size(player.getUniqueId()) >= 3) {
                                player.openInventory(Invs.split());
                            } else {
                                player.sendMessage("\u00a7eIl party deve essere almeno di 3 player o pi\u00f9.");
                            }
                        } else {
                            player.sendMessage("\u00a7eSolo il leader pu\u00f2 Dividere il  party");
                        }
                    } else {
                        player.sendMessage("\u00a7eNon sei in nessun party.");
                        player.getInventory().setContents(Invs.main(player.getName()));
                    }
                }
            } else if (player.getItemInHand().getType() == Material.ENDER_PEARL) {
                if (this.plugin.grace.inCooldown(player.getUniqueId())) {
                    player.sendMessage("\u00a7eLe Enderpearl sono disabilitate durante il periodo di invincibilit\u00e0.");
                    event.setCancelled(true);
                    return;
                }
                if (this.plugin.ender.inCooldown(player.getUniqueId())) {
                    player.sendMessage("\u00a7eDevi aspettare 16 secondi prima di poter tirare un'altra Enderpearl.");
                    event.setCancelled(true);
                } else {
                    this.plugin.ender.addCooldown(player.getUniqueId(), 320, "\u00a7ePuoi tirare una Enderpearl.");
                }
            }
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        Inventory inv = event.getInventory();
        if (inv.getName().equals("\u00a7bParty List")) {
            this.plugin.live.remove(player.getUniqueId());
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$org$bukkit$ChatColor() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$org$bukkit$ChatColor;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[ChatColor.values().length];
        try {
            arrn[ChatColor.AQUA.ordinal()] = 12;
        }
        catch (NoSuchFieldError v1) {}
        try {
            arrn[ChatColor.BLACK.ordinal()] = 1;
        }
        catch (NoSuchFieldError v2) {}
        try {
            arrn[ChatColor.BLUE.ordinal()] = 10;
        }
        catch (NoSuchFieldError v3) {}
        try {
            arrn[ChatColor.BOLD.ordinal()] = 18;
        }
        catch (NoSuchFieldError v4) {}
        try {
            arrn[ChatColor.DARK_AQUA.ordinal()] = 4;
        }
        catch (NoSuchFieldError v5) {}
        try {
            arrn[ChatColor.DARK_BLUE.ordinal()] = 2;
        }
        catch (NoSuchFieldError v6) {}
        try {
            arrn[ChatColor.DARK_GRAY.ordinal()] = 9;
        }
        catch (NoSuchFieldError v7) {}
        try {
            arrn[ChatColor.DARK_GREEN.ordinal()] = 3;
        }
        catch (NoSuchFieldError v8) {}
        try {
            arrn[ChatColor.DARK_PURPLE.ordinal()] = 6;
        }
        catch (NoSuchFieldError v9) {}
        try {
            arrn[ChatColor.DARK_RED.ordinal()] = 5;
        }
        catch (NoSuchFieldError v10) {}
        try {
            arrn[ChatColor.GOLD.ordinal()] = 7;
        }
        catch (NoSuchFieldError v11) {}
        try {
            arrn[ChatColor.GRAY.ordinal()] = 8;
        }
        catch (NoSuchFieldError v12) {}
        try {
            arrn[ChatColor.GREEN.ordinal()] = 11;
        }
        catch (NoSuchFieldError v13) {}
        try {
            arrn[ChatColor.ITALIC.ordinal()] = 21;
        }
        catch (NoSuchFieldError v14) {}
        try {
            arrn[ChatColor.LIGHT_PURPLE.ordinal()] = 14;
        }
        catch (NoSuchFieldError v15) {}
        try {
            arrn[ChatColor.MAGIC.ordinal()] = 17;
        }
        catch (NoSuchFieldError v16) {}
        try {
            arrn[ChatColor.RED.ordinal()] = 13;
        }
        catch (NoSuchFieldError v17) {}
        try {
            arrn[ChatColor.RESET.ordinal()] = 22;
        }
        catch (NoSuchFieldError v18) {}
        try {
            arrn[ChatColor.STRIKETHROUGH.ordinal()] = 19;
        }
        catch (NoSuchFieldError v19) {}
        try {
            arrn[ChatColor.UNDERLINE.ordinal()] = 20;
        }
        catch (NoSuchFieldError v20) {}
        try {
            arrn[ChatColor.WHITE.ordinal()] = 16;
        }
        catch (NoSuchFieldError v21) {}
        try {
            arrn[ChatColor.YELLOW.ordinal()] = 15;
        }
        catch (NoSuchFieldError v22) {}
        $SWITCH_TABLE$org$bukkit$ChatColor = arrn;
        return $SWITCH_TABLE$org$bukkit$ChatColor;
    }
}


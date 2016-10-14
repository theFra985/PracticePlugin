/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Player$Spigot
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.player.PlayerDropItemEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerItemDamageEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.event.player.PlayerKickEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 */
package net.propvp.listener;

import java.util.List;
import java.util.UUID;
import net.propvp.Practice;
import net.propvp.file.Config;
import net.propvp.game.Game;
import net.propvp.game.GameMode;
import net.propvp.game.ladder.Ladder;
import net.propvp.game.ladder.OvO;
import net.propvp.gui.EditorMenu;
import net.propvp.gui.PartyMenu;
import net.propvp.gui.RankedMenu;
import net.propvp.gui.UnrankedMenu;
import net.propvp.party.Party;
import net.propvp.player.DataManager;
import net.propvp.player.InventoryManager;
import net.propvp.player.PlayerData;
import net.propvp.player.PlayerInv;
import net.propvp.player.PlayerKit;
import net.propvp.scoreboard.ScoreboardUser;
import net.propvp.timing.ManualTimer;
import net.propvp.util.CuboidManager;
import net.propvp.util.EntityHider;
import net.propvp.util.HiddenStringUtil;
import net.propvp.util.LocationUtil;
import net.propvp.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerListener
implements Listener {
    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent foodLevelChangeEvent) {
        if (!(foodLevelChangeEvent.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)foodLevelChangeEvent.getEntity();
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (!playerData.hasMatch()) {
            foodLevelChangeEvent.setFoodLevel(20);
        }
    }

    @EventHandler
    public void onInvInteract(InventoryClickEvent inventoryClickEvent) {
        if (InventoryManager.opened.contains(inventoryClickEvent.getWhoClicked().getUniqueId())) {
            inventoryClickEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent inventoryCloseEvent) {
        if (InventoryManager.opened.contains(inventoryCloseEvent.getPlayer().getUniqueId())) {
            InventoryManager.opened.remove(inventoryCloseEvent.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent playerDeathEvent) {
        Player player = playerDeathEvent.getEntity();
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (!playerData.hasMatch()) {
            playerDeathEvent.getDrops().clear();
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent playerRespawnEvent) {
        Player player = playerRespawnEvent.getPlayer();
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        player.spigot().respawn();
        if (!playerData.hasMatch()) {
            playerRespawnEvent.setRespawnLocation(Practice.getBackend().getSpawn());
            if (playerData.hasParty()) {
                if (playerData.getParty().getLeader() == player) {
                    player.getInventory().setContents(InventoryManager.getPartyLeaderInventory(player));
                    return;
                }
                player.getInventory().setContents(InventoryManager.getPartyMemberInventory(player));
                return;
            }
            player.getInventory().setContents(InventoryManager.getSoloInventory(player));
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent playerJoinEvent) {
        Player player = playerJoinEvent.getPlayer();
        LocationUtil.teleportToSpawn(player);
        player.setHealth(20.0);
        player.setGameMode(org.bukkit.GameMode.SURVIVAL);
        player.setAllowFlight(false);
        player.setCanPickupItems(true);
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        player.getInventory().setContents(InventoryManager.getSoloInventory(player));
        player.updateInventory();
        playerJoinEvent.setJoinMessage(null);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent playerQuitEvent) {
        playerQuitEvent.setQuitMessage(null);
    }

    @EventHandler
    public void onKick(PlayerKickEvent playerKickEvent) {
        playerKickEvent.setLeaveMessage(null);
    }

    @EventHandler
    public void onDurability(PlayerItemDamageEvent playerItemDamageEvent) {
        Player player = playerItemDamageEvent.getPlayer();
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (!playerData.hasMatch()) {
            playerItemDamageEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onRegionSelect(PlayerInteractEvent playerInteractEvent) {
        Player player = playerInteractEvent.getPlayer();
        if (player.getItemInHand().equals((Object)InventoryManager.getRegionSelector())) {
            if (playerInteractEvent.getAction() == Action.LEFT_CLICK_BLOCK) {
                CuboidManager.setRegionSelect1(player.getUniqueId(), playerInteractEvent.getClickedBlock().getLocation());
                player.sendMessage((Object)ChatColor.RED + "Practice" + (Object)ChatColor.DARK_GRAY + " \u00bb " + (Object)ChatColor.GRAY + "Location 1 has been set. (" + playerInteractEvent.getClickedBlock().getX() + ", " + playerInteractEvent.getClickedBlock().getY() + ", " + playerInteractEvent.getClickedBlock().getZ() + ")");
                playerInteractEvent.setCancelled(true);
            } else if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
                CuboidManager.setRegionSelect2(player.getUniqueId(), playerInteractEvent.getClickedBlock().getLocation());
                player.sendMessage((Object)ChatColor.RED + "Practice" + (Object)ChatColor.DARK_GRAY + " \u00bb " + (Object)ChatColor.GRAY + "Location 2 has been set. (" + playerInteractEvent.getClickedBlock().getX() + ", " + playerInteractEvent.getClickedBlock().getY() + ", " + playerInteractEvent.getClickedBlock().getZ() + ")");
                playerInteractEvent.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent playerDropItemEvent) {
        Player player = playerDropItemEvent.getPlayer();
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (!playerData.hasMatch()) {
            playerDropItemEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onMenu(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() != Action.RIGHT_CLICK_AIR && playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        if (player.getItemInHand().equals((Object)InventoryManager.getRankedItem())) {
            RankedMenu.getMenu().open(player);
            playerInteractEvent.setCancelled(true);
        } else if (player.getItemInHand().equals((Object)InventoryManager.getUnrankedItem())) {
            UnrankedMenu.getMenu().open(player);
            playerInteractEvent.setCancelled(true);
        } else if (player.getItemInHand().equals((Object)InventoryManager.getPartyItem())) {
            PartyMenu.getMenu().open(player);
            playerInteractEvent.setCancelled(true);
        } else if (player.getItemInHand().equals((Object)InventoryManager.getEditorItem())) {
            EditorMenu.getMenu().open(player);
            playerInteractEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onMiscItem(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() != Action.RIGHT_CLICK_AIR && playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = playerInteractEvent.getPlayer();
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (player.getItemInHand().equals((Object)InventoryManager.getQueueLeaveItem())) {
            if (!playerData.hasQueue()) {
                return;
            }
            if (playerData.getQueue() instanceof OvO) {
                playerData.getQueue().removeObject((Object)player);
            } else {
                playerData.getQueue().removeObject(playerData.getParty());
            }
            if (!playerData.hasParty()) {
                player.getInventory().setContents(InventoryManager.getSoloInventory(player));
            } else if (playerData.getParty().getLeader().equals((Object)player)) {
                player.getInventory().setContents(InventoryManager.getPartyLeaderInventory(player));
            } else {
                player.getInventory().setContents(InventoryManager.getPartyMemberInventory(player));
            }
            playerInteractEvent.setCancelled(true);
        } else if (player.getItemInHand().equals((Object)InventoryManager.getHiderItem(player))) {
            EntityHider entityHider = Practice.getBackend().getEntityHider();
            if (playerData.isHidingPlayers()) {
                for (Player player2 : Bukkit.getOnlinePlayers()) {
                    entityHider.showEntity(player, (Entity)player2);
                }
            } else {
                for (Player player3 : Bukkit.getOnlinePlayers()) {
                    entityHider.hideEntity(player, (Entity)player3);
                }
            }
            playerData.setHidingPlayers(!playerData.isHidingPlayers());
            player.setItemInHand(InventoryManager.getHiderItem(player));
            playerInteractEvent.setCancelled(true);
        } else if (player.getItemInHand() != null && player.getItemInHand().getType() == Material.ENCHANTED_BOOK) {
            String string = ChatColor.stripColor((String)playerInteractEvent.getItem().getItemMeta().getDisplayName());
            if (string.startsWith("Default")) {
                if (playerData.getMatch() != null) {
                    PlayerInv playerInv = playerData.getMatch().getGameMode().getInventory();
                    player.getInventory().setArmorContents(playerInv.getArmorContents());
                    player.getInventory().setContents(playerInv.getContents());
                }
            } else {
                Bukkit.getLogger().severe(HiddenStringUtil.extractHiddenString((String)playerInteractEvent.getPlayer().getInventory().getItemInHand().getItemMeta().getLore().get(0)));
                PlayerKit playerKit = playerData.getKitFromUUID(playerData.getMatch().getGameMode(), UUID.fromString(HiddenStringUtil.extractHiddenString((String)playerInteractEvent.getPlayer().getInventory().getItemInHand().getItemMeta().getLore().get(0))));
                if (playerKit == null) {
                    return;
                }
                if (playerKit.getInv() != null) {
                    if (playerKit.getInv().getArmorContents() != null) {
                        player.getInventory().setArmorContents(playerKit.getInv().getArmorContents());
                    }
                    if (playerKit.getInv().getContents() != null) {
                        player.getInventory().setContents(playerKit.getInv().getContents());
                    }
                    player.updateInventory();
                }
            }
            playerInteractEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onPearl(PlayerInteractEvent playerInteractEvent) {
        Player player = playerInteractEvent.getPlayer();
        if ((playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR || playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK) && playerInteractEvent.getItem() != null && playerInteractEvent.getItem().getType() == Material.ENDER_PEARL) {
            ScoreboardUser scoreboardUser = Practice.getBackend().getDataManager().getData(player).getScoreboardUser();
            if (scoreboardUser.getEnderpearlTimer().isActive()) {
                player.sendMessage(Utils.color((Object)ChatColor.RED + "You cannot use an enderpearl for another " + (Object)ChatColor.YELLOW + "%time%" + (Object)ChatColor.RED + " seconds.").replace("%time%", String.valueOf((double)scoreboardUser.getEnderpearlTimer().getLeftTime() / 1000.0)));
                playerInteractEvent.setCancelled(true);
                return;
            }
            scoreboardUser.getEnderpearlTimer().setTimerEnd(System.currentTimeMillis() + (long)(1000 * Practice.getBackend().getMainConfig().getConfig().getInt("cooldown.enderpearl.timer")));
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.getEntity() instanceof Player) {
            PlayerData playerData = Practice.getBackend().getDataManager().getData((Player)entityDamageByEntityEvent.getEntity());
            if (playerData.isSpectating()) {
                entityDamageByEntityEvent.setCancelled(true);
                return;
            }
            if (!playerData.hasMatch()) {
                entityDamageByEntityEvent.setCancelled(true);
                return;
            }
            if (entityDamageByEntityEvent.getDamager() instanceof Player) {
                Player player = (Player)entityDamageByEntityEvent.getDamager();
                PlayerData playerData2 = Practice.getBackend().getDataManager().getData(player);
                if (playerData2.isSpectating()) {
                    entityDamageByEntityEvent.setCancelled(true);
                    return;
                }
            }
        }
    }
}


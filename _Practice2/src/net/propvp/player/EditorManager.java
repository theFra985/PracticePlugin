/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.block.Block
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Projectile
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.ProjectileLaunchEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.projectiles.ProjectileSource
 */
package net.propvp.player;

import java.util.HashMap;
import net.propvp.Practice;
import net.propvp.game.GameMode;
import net.propvp.player.DataManager;
import net.propvp.player.InventoryManager;
import net.propvp.player.PlayerData;
import net.propvp.player.PlayerInv;
import net.propvp.player.PlayerKit;
import net.propvp.util.EntityHider;
import net.propvp.util.IconMenu;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.projectiles.ProjectileSource;

public class EditorManager
implements Listener {
    private HashMap<Player, GameMode> editing = new HashMap();
    private HashMap<Player, PlayerKit> renaming = new HashMap();
    private HashMap<Player, IconMenu> menus = new HashMap();
    private HashMap<Player, IconMenu> optionMenus = new HashMap();
    private HashMap<Player, IconMenu> kitMenus = new HashMap();

    public EditorManager() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Practice.getInstance());
    }

    public void beginEditing(Player player, GameMode gameMode) {
        Object object2;
        player.teleport(Practice.getBackend().getEditor());
        player.getInventory().clear();
        EntityHider entityHider = Practice.getBackend().getEntityHider();
        for (Object object2 : Bukkit.getOnlinePlayers()) {
            entityHider.hideEntity(player, (Entity)object2);
            entityHider.hideEntity((Player)object2, (Entity)player);
        }
        this.editing.put(player, gameMode);
        if (!this.optionMenus.containsKey((Object)player)) {
            this.createOptionMenu(player, gameMode);
        }
        if ((object2 = gameMode.getInventory()) != null) {
            if (object2.getArmorContents() != null) {
                player.getInventory().setArmorContents(object2.getArmorContents());
            }
            if (object2.getContents() != null) {
                player.getInventory().setContents(object2.getContents());
            }
        }
        player.updateInventory();
        player.sendMessage((Object)ChatColor.GOLD + "You are now editing your kits for: " + (Object)ChatColor.YELLOW + gameMode.getName());
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent projectileLaunchEvent) {
        if (!(projectileLaunchEvent.getEntity().getShooter() instanceof Player)) {
            return;
        }
        Player player = (Player)projectileLaunchEvent.getEntity().getShooter();
        if (!this.editing.containsKey((Object)player)) {
            return;
        }
        if (projectileLaunchEvent.getEntityType() == EntityType.SPLASH_POTION || projectileLaunchEvent.getEntityType() == EntityType.ENDER_PEARL || projectileLaunchEvent.getEntityType() == EntityType.ARROW) {
            EntityHider entityHider = Practice.getBackend().getEntityHider();
            for (Player player2 : Bukkit.getOnlinePlayers()) {
                entityHider.hideEntity(player, (Entity)player2);
                entityHider.hideEntity(player2, (Entity)player);
            }
            if (Practice.getBackend().getEditorManager().isEditing(player)) {
                projectileLaunchEvent.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onRightClickSign(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK && playerInteractEvent.getClickedBlock() != null && (playerInteractEvent.getClickedBlock().getType() == Material.SIGN || playerInteractEvent.getClickedBlock().getType() == Material.WALL_SIGN || playerInteractEvent.getClickedBlock().getType() == Material.SIGN_POST) && this.editing.containsKey((Object)playerInteractEvent.getPlayer())) {
            playerInteractEvent.getPlayer().teleport(Practice.getBackend().getSpawn());
            Practice.getBackend().getInventoryManager();
            playerInteractEvent.getPlayer().getInventory().setContents(InventoryManager.getSoloInventory(playerInteractEvent.getPlayer()));
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (playerInteractEvent.getPlayer() == player) continue;
                playerInteractEvent.getPlayer().showPlayer(player);
                player.showPlayer(playerInteractEvent.getPlayer());
            }
            this.cleanPlayer(playerInteractEvent.getPlayer());
            playerInteractEvent.getPlayer().sendMessage((Object)ChatColor.GOLD + "You have left the editing station.");
            playerInteractEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onClickAnvil(PlayerInteractEvent playerInteractEvent) {
        Player player = playerInteractEvent.getPlayer();
        if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK && playerInteractEvent.getClickedBlock() != null && playerInteractEvent.getClickedBlock().getType() == Material.ANVIL && this.editing.containsKey((Object)player)) {
            if (!this.optionMenus.containsKey((Object)player)) {
                this.createOptionMenu(player, this.editing.get((Object)player));
            }
            IconMenu iconMenu = this.optionMenus.get((Object)player);
            this.menus.put(player, iconMenu);
            iconMenu.open(player);
            playerInteractEvent.setCancelled(true);
        }
    }

    @EventHandler
    public void onOpenChest(PlayerInteractEvent playerInteractEvent) {
        Player player = playerInteractEvent.getPlayer();
        if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK && playerInteractEvent.getClickedBlock() != null && playerInteractEvent.getClickedBlock().getType() == Material.CHEST) {
            this.editing.containsKey((Object)player);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent inventoryCloseEvent) {
        if (this.menus.containsKey((Object)inventoryCloseEvent.getPlayer())) {
            this.menus.remove((Object)inventoryCloseEvent.getPlayer());
        }
    }

    private IconMenu createOptionMenu(Player player, GameMode gameMode) {
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        IconMenu iconMenu = new IconMenu((Object)ChatColor.GOLD + "Kit Options", 27, var4_4 -> {
            // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
            // org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:143)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:385)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
            // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
            // org.benf.cfr.reader.entities.Method.getAnalysis(Method.java:344)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteDynamicExpression(LambdaRewriter.java:231)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteDynamicExpression(LambdaRewriter.java:123)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteExpression(LambdaRewriter.java:69)
            // org.benf.cfr.reader.bytecode.analysis.parse.rewriters.ExpressionRewriterHelper.applyForwards(ExpressionRewriterHelper.java:12)
            // org.benf.cfr.reader.bytecode.analysis.parse.expression.AbstractConstructorInvokation.applyExpressionRewriter(AbstractConstructorInvokation.java:69)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewriteExpression(LambdaRewriter.java:67)
            // org.benf.cfr.reader.bytecode.analysis.structured.statement.StructuredAssignment.rewriteExpressions(StructuredAssignment.java:132)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.op4rewriters.LambdaRewriter.rewrite(LambdaRewriter.java:54)
            // org.benf.cfr.reader.bytecode.analysis.opgraph.Op04StructuredStatement.rewriteLambdas(Op04StructuredStatement.java:974)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:873)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
            // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:155)
            // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
            // org.benf.cfr.reader.entities.Method.analyse(Method.java:355)
            // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:768)
            // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:700)
            // org.benf.cfr.reader.Main.doJar(Main.java:134)
            // org.benf.cfr.reader.Main.main(Main.java:189)
            throw new IllegalStateException("Decompilation failed");
        }
        , player);
        PlayerKit playerKit = playerData.getKitFromName(gameMode, "1");
        PlayerKit playerKit2 = playerData.getKitFromName(gameMode, "2");
        PlayerKit playerKit3 = playerData.getKitFromName(gameMode, "3");
        PlayerKit playerKit4 = playerData.getKitFromName(gameMode, "4");
        PlayerKit playerKit5 = playerData.getKitFromName(gameMode, "5");
        iconMenu.setOption(0, new ItemStack(Material.CHEST, 1, 8), (Object)ChatColor.GREEN + "Save kit: " + (Object)ChatColor.GOLD + "1", "");
        iconMenu.setOption(2, new ItemStack(Material.CHEST, 1, 8), (Object)ChatColor.GREEN + "Save kit: " + (Object)ChatColor.GOLD + "2", "");
        iconMenu.setOption(4, new ItemStack(Material.CHEST, 1, 8), (Object)ChatColor.GREEN + "Save kit: " + (Object)ChatColor.GOLD + "3", "");
        iconMenu.setOption(6, new ItemStack(Material.CHEST, 1, 8), (Object)ChatColor.GREEN + "Save kit: " + (Object)ChatColor.GOLD + "4", "");
        iconMenu.setOption(8, new ItemStack(Material.CHEST, 1, 8), (Object)ChatColor.GREEN + "Save kit: " + (Object)ChatColor.GOLD + "5", "");
        if (playerKit != null) {
            iconMenu.setOption(9, new ItemStack(Material.ENCHANTED_BOOK), (Object)ChatColor.YELLOW + "Load kit: " + (Object)ChatColor.GOLD + "1", "");
            iconMenu.setOption(18, new ItemStack(Material.FIRE), (Object)ChatColor.RED + "Delete kit: " + (Object)ChatColor.GOLD + "1", "");
        }
        if (playerKit2 != null) {
            iconMenu.setOption(11, new ItemStack(Material.ENCHANTED_BOOK), (Object)ChatColor.YELLOW + "Load kit: " + (Object)ChatColor.GOLD + "2", "");
            iconMenu.setOption(20, new ItemStack(Material.FIRE), (Object)ChatColor.RED + "Delete kit: " + (Object)ChatColor.GOLD + "2", "");
        }
        if (playerKit3 != null) {
            iconMenu.setOption(13, new ItemStack(Material.ENCHANTED_BOOK), (Object)ChatColor.YELLOW + "Load kit: " + (Object)ChatColor.GOLD + "3", "");
            iconMenu.setOption(20, new ItemStack(Material.FIRE), (Object)ChatColor.RED + "Delete kit: " + (Object)ChatColor.GOLD + "3", "");
        }
        if (playerKit4 != null) {
            iconMenu.setOption(15, new ItemStack(Material.ENCHANTED_BOOK), (Object)ChatColor.YELLOW + "Load kit: " + (Object)ChatColor.GOLD + "4", "");
            iconMenu.setOption(20, new ItemStack(Material.FIRE), (Object)ChatColor.RED + "Delete kit: " + (Object)ChatColor.GOLD + "4", "");
        }
        if (playerKit5 != null) {
            iconMenu.setOption(17, new ItemStack(Material.ENCHANTED_BOOK), (Object)ChatColor.YELLOW + "Load kit: " + (Object)ChatColor.GOLD + "5", "");
            iconMenu.setOption(20, new ItemStack(Material.FIRE), (Object)ChatColor.RED + "Delete kit: " + (Object)ChatColor.GOLD + "5", "");
        }
        if (this.optionMenus.containsKey((Object)player)) {
            this.optionMenus.replace(player, iconMenu);
        } else {
            this.optionMenus.put(player, iconMenu);
        }
        return iconMenu;
    }

    private void updateOptionsMenu(Player player, GameMode gameMode) {
        if (!this.optionMenus.containsKey((Object)player)) {
            this.createOptionMenu(player, gameMode);
        }
        IconMenu iconMenu = this.optionMenus.get((Object)player);
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        iconMenu.resetOptions();
        PlayerKit playerKit = playerData.getKitFromName(gameMode, "1");
        PlayerKit playerKit2 = playerData.getKitFromName(gameMode, "2");
        PlayerKit playerKit3 = playerData.getKitFromName(gameMode, "3");
        PlayerKit playerKit4 = playerData.getKitFromName(gameMode, "4");
        PlayerKit playerKit5 = playerData.getKitFromName(gameMode, "5");
        iconMenu.setOption(0, new ItemStack(Material.CHEST, 1, 8), (Object)ChatColor.GREEN + "Save kit: " + (Object)ChatColor.GOLD + "1", "");
        iconMenu.setOption(2, new ItemStack(Material.CHEST, 1, 8), (Object)ChatColor.GREEN + "Save kit: " + (Object)ChatColor.GOLD + "2", "");
        iconMenu.setOption(4, new ItemStack(Material.CHEST, 1, 8), (Object)ChatColor.GREEN + "Save kit: " + (Object)ChatColor.GOLD + "3", "");
        iconMenu.setOption(6, new ItemStack(Material.CHEST, 1, 8), (Object)ChatColor.GREEN + "Save kit: " + (Object)ChatColor.GOLD + "4", "");
        iconMenu.setOption(8, new ItemStack(Material.CHEST, 1, 8), (Object)ChatColor.GREEN + "Save kit: " + (Object)ChatColor.GOLD + "5", "");
        if (playerKit != null) {
            iconMenu.setOption(9, new ItemStack(Material.ENCHANTED_BOOK), (Object)ChatColor.YELLOW + "Load kit: " + (Object)ChatColor.GOLD + "1", "");
            iconMenu.setOption(18, new ItemStack(Material.FIRE), (Object)ChatColor.RED + "Delete kit: " + (Object)ChatColor.GOLD + "1", "");
        }
        if (playerKit2 != null) {
            iconMenu.setOption(11, new ItemStack(Material.ENCHANTED_BOOK), (Object)ChatColor.YELLOW + "Load kit: " + (Object)ChatColor.GOLD + "2", "");
            iconMenu.setOption(20, new ItemStack(Material.FIRE), (Object)ChatColor.RED + "Delete kit: " + (Object)ChatColor.GOLD + "2", "");
        }
        if (playerKit3 != null) {
            iconMenu.setOption(13, new ItemStack(Material.ENCHANTED_BOOK), (Object)ChatColor.YELLOW + "Load kit: " + (Object)ChatColor.GOLD + "3", "");
            iconMenu.setOption(20, new ItemStack(Material.FIRE), (Object)ChatColor.RED + "Delete kit: " + (Object)ChatColor.GOLD + "3", "");
        }
        if (playerKit4 != null) {
            iconMenu.setOption(15, new ItemStack(Material.ENCHANTED_BOOK), (Object)ChatColor.YELLOW + "Load kit: " + (Object)ChatColor.GOLD + "4", "");
            iconMenu.setOption(20, new ItemStack(Material.FIRE), (Object)ChatColor.RED + "Delete kit: " + (Object)ChatColor.GOLD + "4", "");
        }
        if (playerKit5 != null) {
            iconMenu.setOption(17, new ItemStack(Material.ENCHANTED_BOOK), (Object)ChatColor.YELLOW + "Load kit: " + (Object)ChatColor.GOLD + "5", "");
            iconMenu.setOption(20, new ItemStack(Material.FIRE), (Object)ChatColor.RED + "Delete kit: " + (Object)ChatColor.GOLD + "5", "");
        }
        if (this.optionMenus.containsKey((Object)player)) {
            this.optionMenus.replace(player, iconMenu);
        } else {
            this.optionMenus.put(player, iconMenu);
        }
    }

    public void cleanPlayer(Player player) {
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);
        Practice.getBackend().getInventoryManager();
        player.getInventory().setContents(InventoryManager.getSoloInventory(player));
        player.updateInventory();
        HandlerList.unregisterAll((Listener)this.optionMenus.get((Object)player));
        HandlerList.unregisterAll((Listener)this.kitMenus.get((Object)player));
        this.editing.remove((Object)player);
        this.menus.remove((Object)player);
        this.optionMenus.remove((Object)player);
        this.kitMenus.remove((Object)player);
        this.renaming.remove((Object)player);
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (playerData.isHidingPlayers()) {
            return;
        }
        EntityHider entityHider = Practice.getBackend().getEntityHider();
        for (Player player2 : Bukkit.getOnlinePlayers()) {
            entityHider.showEntity(player, (Entity)player2);
            entityHider.showEntity(player2, (Entity)player);
        }
    }

    public boolean isEditing(Player player) {
        return this.editing.containsKey((Object)player);
    }
}


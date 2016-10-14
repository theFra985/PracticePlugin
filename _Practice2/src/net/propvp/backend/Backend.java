/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.scheduler.BukkitTask
 */
package net.propvp.backend;

import net.propvp.Practice;
import net.propvp.command.ArenaCommand;
import net.propvp.command.DuelCommand;
import net.propvp.command.EloCommand;
import net.propvp.command.GameTypeCommand;
import net.propvp.command.InventoryCommand;
import net.propvp.command.PartyCommand;
import net.propvp.command.PingCommand;
import net.propvp.command.PracticeCommand;
import net.propvp.command.ScoreboardCommand;
import net.propvp.command.SpectateCommand;
import net.propvp.command.VersionCommand;
import net.propvp.command.WorldManagementCommand;
import net.propvp.file.Config;
import net.propvp.game.GameManager;
import net.propvp.game.arena.ArenaManager;
import net.propvp.gui.MenuManager;
import net.propvp.listener.BlockListener;
import net.propvp.listener.PartyListener;
import net.propvp.listener.PlayerListener;
import net.propvp.listener.ProjectileListener;
import net.propvp.party.PartyManager;
import net.propvp.player.DataManager;
import net.propvp.player.EditorManager;
import net.propvp.player.InventoryManager;
import net.propvp.scoreboard.ScoreboardManager;
import net.propvp.util.EntityHider;
import net.propvp.util.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;

public class Backend {
    private DataManager dataManager;
    private PartyManager partyManager;
    private GameManager gameManager;
    private ArenaManager arenaManager;
    private InventoryManager invManager;
    private EditorManager editorManager;
    private EntityHider entityHider;
    private Config mainConfig;
    private Config gamemodesConfig;
    private Config arenasConfig;
    private Config messagesConfig;
    private int queueStatus = 1;
    private Location spawn;
    private Location editor;

    public void initialize() {
        this.mainConfig = new Config("config.yml");
        this.gamemodesConfig = new Config("gamemodes.yml");
        this.arenasConfig = new Config("arenas.yml");
        this.messagesConfig = new Config("messages.yml");
        if (!this.messagesConfig.getConfig().contains("queue.join-unranked")) {
            Practice.getLog().log("Updated messages.yml configuration to the latest version.", true);
            this.messagesConfig.getConfig().set("queue.joined-unranked", (Object)"&eYou have joined the &e$queue queue.");
        }
        if (!this.messagesConfig.getConfig().isConfigurationSection("party")) {
            Practice.getLog().log("Updated arenas.yml configuration to the latest version.", true);
            this.messagesConfig.getConfig().set("party.invited-player", (Object)"&7You have invited &e$player &7to join your party.");
            this.messagesConfig.getConfig().set("party.invited-self", (Object)"&7You have been invited to join &e$leader's &7party.");
            this.messagesConfig.getConfig().set("party.joined-party", (Object)"&e$player &7has joined your party.");
            this.messagesConfig.getConfig().set("party.joined-self", (Object)"&7You have joined &e$leader's &7party.");
            this.messagesConfig.getConfig().set("party.leave-party", (Object)"&e$player &7has left your party.");
            this.messagesConfig.getConfig().set("party.leave-self", (Object)"&7You have the left &e$leader's &7party.");
            this.messagesConfig.getConfig().set("party.kicked-player", (Object)"&7You have kicked &e$player.");
            this.messagesConfig.getConfig().set("party.kicked-party", (Object)"&e$player &7has been kicked from the party by &e$leader.");
            this.messagesConfig.getConfig().set("party.kicked-self", (Object)"&7You have been kicked from the party.");
            this.messagesConfig.getConfig().set("party.disbanded-player", (Object)"&7The party has been disbanded by &e$leader.");
            this.messagesConfig.getConfig().set("party.disbanded-self", (Object)"&7You have disbanded the party.");
            this.messagesConfig.getConfig().set("party.click-to-join", (Object)"Click here to accept their party invite.");
            this.messagesConfig.getConfig().set("party.error-kick-self", (Object)"&cYou cannot kick yourself.");
            this.messagesConfig.getConfig().set("party.error-kick-permission", (Object)"&cYou must be the leader to kick a member.");
            this.messagesConfig.getConfig().set("party.error-already-invited", (Object)"&cThat player has already been invited.");
            this.messagesConfig.getConfig().set("party.error-already-member", (Object)"&cThat player is already a member of your party.");
            this.messagesConfig.getConfig().set("party.error-invite-self", (Object)"&cYou cannot invite yourself.");
            this.messagesConfig.save();
        }
        if (!this.mainConfig.getConfig().contains("scoreboard.lines.website")) {
            this.mainConfig.getConfig().set("scoreboard.lines.website", (Object)"&4www.propractice.com");
            this.mainConfig.getConfig().set("scoreboard.options.show-website", (Object)true);
            this.mainConfig.getConfig().set("scoreboard.options.show-party", (Object)true);
            this.mainConfig.getConfig().set("scoreboard.options.show-ratings", (Object)true);
            this.mainConfig.save();
        }
        if (!this.mainConfig.getConfig().contains("cooldown.enderpearl.timer")) {
            int n = this.mainConfig.getConfig().getInt("cooldown.enderpearl");
            this.mainConfig.getConfig().set("cooldown", (Object)null);
            this.mainConfig.getConfig().set("cooldown.enderpearl.enabled", (Object)true);
            this.mainConfig.getConfig().set("cooldown.enderpearl.timer", (Object)n);
            this.mainConfig.save();
        }
        if (this.mainConfig.getConfig().contains("spawn")) {
            this.spawn = LocationUtil.getLocation(this.mainConfig.getConfig().getString("spawn"));
        } else {
            Bukkit.getLogger().severe("-----------------------------------------------------------------------");
            Bukkit.getLogger().severe("The spawn point of the server has not been set-up. This will cause errors.");
            Bukkit.getLogger().severe("Use the command '/practice setspawn' in-game to set the spawn point.");
            Bukkit.getLogger().severe("-----------------------------------------------------------------------");
        }
        if (this.mainConfig.getConfig().contains("editor")) {
            this.editor = LocationUtil.getLocation(this.mainConfig.getConfig().getString("editor"));
        } else {
            Bukkit.getLogger().severe("-----------------------------------------------------------------------");
            Bukkit.getLogger().severe("The editor point of the server has not been set-up. This will cause errors.");
            Bukkit.getLogger().severe("Use the command '/practice seteditor' in-game to set the spawn point.");
            Bukkit.getLogger().severe("-----------------------------------------------------------------------");
        }
        this.partyManager = new PartyManager();
        this.gameManager = new GameManager();
        this.arenaManager = new ArenaManager();
        this.invManager = new InventoryManager();
        this.dataManager = new DataManager();
        this.editorManager = new EditorManager();
        this.entityHider = new EntityHider((Plugin)Practice.getInstance(), EntityHider.Policy.BLACKLIST);
        new ScoreboardManager().runTaskTimer((Plugin)Practice.getInstance(), 1, 1);
        this.registerCommands();
        this.registerEvents();
        MenuManager.createMenus();
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.entityHider.showAllPlayers(player);
            player.getInventory().clear();
            player.getInventory().setHelmet(null);
            player.getInventory().setChestplate(null);
            player.getInventory().setLeggings(null);
            player.getInventory().setBoots(null);
            Practice.getBackend().getInventoryManager();
            player.getInventory().setContents(InventoryManager.getSoloInventory(player));
            player.updateInventory();
            if (this.spawn == null) continue;
            player.teleport(this.spawn);
        }
    }

    public void registerCommands() {
        Practice practice = Practice.getInstance();
        practice.getCommand("practice").setExecutor((CommandExecutor)new PracticeCommand());
        practice.getCommand("gametype").setExecutor((CommandExecutor)new GameTypeCommand());
        practice.getCommand("arena").setExecutor((CommandExecutor)new ArenaCommand());
        practice.getCommand("party").setExecutor((CommandExecutor)new PartyCommand());
        practice.getCommand("ping").setExecutor((CommandExecutor)new PingCommand());
        practice.getCommand("elo").setExecutor((CommandExecutor)new EloCommand());
        practice.getCommand("spectate").setExecutor((CommandExecutor)new SpectateCommand());
        practice.getCommand("inventory").setExecutor((CommandExecutor)new InventoryCommand());
        practice.getCommand("ppv").setExecutor((CommandExecutor)new VersionCommand());
        practice.getCommand("duel").setExecutor((CommandExecutor)new DuelCommand());
        practice.getCommand("scoreboard").setExecutor((CommandExecutor)new ScoreboardCommand());
        practice.getCommand("worldmanagement").setExecutor((CommandExecutor)new WorldManagementCommand());
    }

    public void registerEvents() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents((Listener)new BlockListener(), (Plugin)Practice.getInstance());
        pluginManager.registerEvents((Listener)new PlayerListener(), (Plugin)Practice.getInstance());
        pluginManager.registerEvents((Listener)new PartyListener(), (Plugin)Practice.getInstance());
        pluginManager.registerEvents((Listener)new ProjectileListener(), (Plugin)Practice.getInstance());
    }

    public Config getMainConfig() {
        return this.mainConfig;
    }

    public Config getGameModesConfig() {
        return this.gamemodesConfig;
    }

    public Config getArenasConfig() {
        return this.arenasConfig;
    }

    public Config getMessagesConfig() {
        return this.messagesConfig;
    }

    public DataManager getDataManager() {
        return this.dataManager;
    }

    public PartyManager getPartyManager() {
        return this.partyManager;
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    public InventoryManager getInventoryManager() {
        return this.invManager;
    }

    public EditorManager getEditorManager() {
        return this.editorManager;
    }

    public EntityHider getEntityHider() {
        return this.entityHider;
    }

    public Integer getQueueStatus() {
        return this.queueStatus;
    }

    public void setQueueStatus(int n) {
        this.queueStatus = n;
    }

    public Location getSpawn() {
        return this.spawn;
    }

    public Location getEditor() {
        return this.editor;
    }

    public void setSpawn(Location location) {
        this.spawn = location;
        this.mainConfig.getConfig().set("spawn", (Object)LocationUtil.getString(location));
        this.mainConfig.save();
    }

    public void setEditor(Location location) {
        this.editor = location;
        this.mainConfig.getConfig().set("editor", (Object)LocationUtil.getString(location));
        this.mainConfig.save();
    }

    public ArenaManager getArenaManager() {
        return this.arenaManager;
    }
}


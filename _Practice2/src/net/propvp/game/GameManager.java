/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 */
package net.propvp.game;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import net.propvp.Practice;
import net.propvp.file.Config;
import net.propvp.game.GameMode;
import net.propvp.game.ladder.Ladder;
import net.propvp.game.ladder.OvO;
import net.propvp.game.ladder.TvT;
import net.propvp.player.PlayerInv;
import net.propvp.util.InventoryUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class GameManager {
    private Map<String, GameMode> gameModes = new HashMap<String, GameMode>();
    private Map<UUID, Ladder> queues = new HashMap<UUID, Ladder>();

    public GameManager() {
        this.loadGameModes();
    }

    public void loadGameModes() {
        Config config = Practice.getBackend().getGameModesConfig();
        if (config.getConfig().getConfigurationSection("gamemode") == null) {
            Bukkit.getLogger().severe("There are no game-modes in the configuration.");
            return;
        }
        config.getConfig().getConfigurationSection("gamemode").getKeys(false).forEach(string -> {
            GameMode gameMode = new GameMode(string);
            if (!config.getConfig().contains("gamemode." + string + ".display-name")) {
                gameMode.setDisplayName(string);
            } else {
                gameMode.setDisplayName(config.getConfig().getString("gamemode." + string + ".display-name"));
            }
            if (config.getConfig().getString("gamemode." + string + ".items") != null && config.getConfig().getString("gamemode." + string + ".items") != "null") {
                gameMode.setStartingInventory(InventoryUtil.playerInventoryFromString(config.getConfig().getString("gamemode." + string + ".items")));
            }
            if (config.getConfig().getStringList("gamemode." + string + ".display") != null) {
                gameMode.setDisplay(Material.getMaterial((String)config.getConfig().getString("gamemode." + string + ".display")));
            }
            if (config.getConfig().getString("gamemode." + string + ".editable") != null) {
                gameMode.setEditable(config.getConfig().getBoolean("gamemode." + string + ".editable"));
            }
            if (config.getConfig().getString("gamemode." + string + ".regeneration") != null) {
                gameMode.setRegeneration(config.getConfig().getBoolean("gamemode." + string + ".regeneration"));
            }
            if (config.getConfig().getString("gamemode." + string + ".hunger") != null) {
                gameMode.setHunger(config.getConfig().getBoolean("gamemode." + string + ".hunger"));
            }
            if (config.getConfig().getString("gamemode." + string + ".build") != null) {
                gameMode.setBuild(config.getConfig().getBoolean("gamemode." + string + ".build"));
            }
            if (config.getConfig().contains("gamemode." + string + ".hitdelay")) {
                gameMode.setHitDelay(config.getConfig().getInt("gamemode." + string + ".hitdelay"));
            } else {
                config.getConfig().set("gamemode." + string + ".hitdelay", (Object)20);
                gameMode.setHitDelay(20);
            }
            this.gameModes.put(string, gameMode);
            OvO ovO = new OvO(gameMode, true);
            OvO ovO2 = new OvO(gameMode, false);
            TvT tvT = new TvT(gameMode);
            this.queues.put(ovO.getIdentifier(), ovO);
            this.queues.put(ovO2.getIdentifier(), ovO2);
            this.queues.put(tvT.getIdentifier(), tvT);
        }
        );
    }

    public Map<String, GameMode> getGameModes() {
        return this.gameModes;
    }

    public GameMode getGameMode(String string) {
        return this.gameModes.get(string);
    }

    public void removeGameMode(String string) {
        this.gameModes.remove(string);
    }

    public void putGameMode(String string, GameMode gameMode) {
        this.gameModes.put(string, gameMode);
    }

    public void saveAll() {
        for (GameMode gameMode : this.gameModes.values()) {
            gameMode.save();
        }
    }

    public Map<UUID, Ladder> getQueues() {
        return this.queues;
    }

    public void addToQueue(UUID uUID, Object object) {
        Ladder ladder = this.queues.get(uUID);
        ladder.addObject(object);
    }
}


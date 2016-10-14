/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.InvalidConfigurationException
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.scoreboard.Scoreboard
 */
package net.propvp.player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import net.propvp.Practice;
import net.propvp.file.Config;
import net.propvp.game.Game;
import net.propvp.game.GameManager;
import net.propvp.game.GameMode;
import net.propvp.game.duel.DuelInfo;
import net.propvp.game.ladder.Ladder;
import net.propvp.party.Party;
import net.propvp.player.PlayerInv;
import net.propvp.player.PlayerKit;
import net.propvp.scoreboard.ScoreboardHelper;
import net.propvp.scoreboard.ScoreboardUser;
import net.propvp.timing.ElapsedTimer;
import net.propvp.util.Elo;
import net.propvp.util.HiddenStringUtil;
import net.propvp.util.InventoryUtil;
import net.propvp.util.ItemBuilder;
import net.propvp.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.Scoreboard;

public class PlayerData {
    private File file;
    private FileConfiguration config;
    private Player player;
    private Game game;
    private Game gameSpectating;
    private Ladder queue;
    private Party party;
    private boolean spectating;
    private boolean editing;
    private boolean hidingPlayers;
    private boolean ffa;
    private boolean hidingScoreboard;
    private List<String> messageCooldowns;
    private Map<GameMode, Elo> ratings;
    private Map<GameMode, ArrayList<PlayerKit>> kits;
    private Map<String, ElapsedTimer> timers;
    private Map<Player, DuelInfo> duelInvites;
    private ScoreboardHelper scoreboardHelper;
    private ScoreboardUser scoreboardUser;

    public PlayerData(Player player) {
        this.player = player;
        this.messageCooldowns = new ArrayList<String>();
        this.ratings = new HashMap<GameMode, Elo>();
        this.kits = new HashMap<GameMode, ArrayList<PlayerKit>>();
        this.duelInvites = new HashMap<Player, DuelInfo>();
        this.file = new File(String.valueOf(Practice.getInstance().getDataFolder().getAbsolutePath()) + File.separator + "playerdata");
        if (!this.file.exists()) {
            this.file.mkdir();
        }
        this.file = new File(String.valueOf(Practice.getInstance().getDataFolder().getAbsolutePath()) + File.separator + "playerdata" + File.separator + player.getUniqueId().toString() + ".yml");
        if (!this.file.exists()) {
            try {
                this.file.createNewFile();
            }
            catch (IOException var2_2) {
                var2_2.printStackTrace();
            }
        }
        this.config = new YamlConfiguration();
        try {
            this.config.load(this.file);
        }
        catch (IOException | InvalidConfigurationException var2_3) {
            var2_3.printStackTrace();
        }
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        player.setScoreboard(scoreboard);
        this.scoreboardHelper = new ScoreboardHelper(scoreboard, Utils.color(Practice.getBackend().getMainConfig().getConfig().getString("scoreboard.title")));
        this.scoreboardUser = new ScoreboardUser(player);
        this.load();
    }

    public Player getPlayer() {
        return this.player;
    }

    public Game getMatch() {
        return this.game;
    }

    public Ladder getQueue() {
        return this.queue;
    }

    public Party getParty() {
        return this.party;
    }

    public void setMatch(Game game) {
        this.game = game;
    }

    public void setQueue(Ladder ladder) {
        this.queue = ladder;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public boolean hasQueue() {
        if (this.queue != null) {
            return true;
        }
        return false;
    }

    public boolean hasParty() {
        if (this.party != null) {
            return true;
        }
        return false;
    }

    public boolean hasMatch() {
        if (this.game != null) {
            return true;
        }
        return false;
    }

    public boolean isSpectating() {
        return this.spectating;
    }

    public boolean isEditing() {
        return this.editing;
    }

    public boolean isInFFA() {
        return this.ffa;
    }

    public boolean hasDuelInvite(Player player) {
        return this.duelInvites.containsKey((Object)player);
    }

    public DuelInfo getDuelInfo(Player player) {
        return this.duelInvites.get((Object)player);
    }

    public void addDuelInvite(Player player, DuelInfo duelInfo) {
        this.duelInvites.put(player, duelInfo);
    }

    public void removeDuelInvite(Player player) {
        this.duelInvites.remove((Object)player);
    }

    public boolean containsTimer(String string) {
        return this.timers.containsKey(string);
    }

    public void addTimer(String string, ElapsedTimer elapsedTimer) {
        this.timers.put(string, elapsedTimer);
    }

    public void removeTimer(String string) {
        this.timers.remove(string);
    }

    public ElapsedTimer getTimer(String string) {
        return this.timers.get(string);
    }

    public boolean containsMessageCooldown(String string) {
        return this.messageCooldowns.contains(string);
    }

    public void addMessageCooldown(String string) {
        this.messageCooldowns.add(string);
    }

    public void removeMessageCooldown(String string) {
        this.messageCooldowns.remove(string);
    }

    public Elo getRating(GameMode gameMode) {
        return this.ratings.get(gameMode);
    }

    public Map<GameMode, Elo> getRatings() {
        return this.ratings;
    }

    public Map<GameMode, ArrayList<PlayerKit>> getKits() {
        return this.kits;
    }

    public PlayerKit getKitFromName(GameMode gameMode, String string) {
        List list = this.kits.get(gameMode);
        if (list == null) {
            return null;
        }
        for (PlayerKit playerKit : list) {
            if (!playerKit.getName().equals(string)) continue;
            return playerKit;
        }
        return null;
    }

    public PlayerKit getKitFromUUID(GameMode gameMode, UUID uUID) {
        List list = this.kits.get(gameMode);
        for (PlayerKit playerKit : list) {
            if (playerKit.getUUID() != uUID && !playerKit.getUUID().equals(uUID)) continue;
            return playerKit;
        }
        return null;
    }

    public boolean kitExists(GameMode gameMode, String string) {
        List list = this.kits.get(gameMode);
        for (PlayerKit playerKit : list) {
            if (!playerKit.getName().equals(string) && playerKit.getName() != string) continue;
            return true;
        }
        return false;
    }

    public void removeKit(GameMode gameMode, UUID uUID, boolean bl) {
        Iterator<PlayerKit> iterator = this.kits.get(gameMode).iterator();
        while (iterator.hasNext()) {
            PlayerKit playerKit = iterator.next();
            if (playerKit.getUUID() != uUID && !playerKit.getUUID().equals(uUID)) continue;
            playerKit = null;
            iterator.remove();
        }
        if (bl) {
            this.save();
        }
    }

    public void setKit(GameMode gameMode, PlayerKit playerKit, boolean bl) {
        if (!this.kits.containsKey(gameMode)) {
            this.kits.put(gameMode, new ArrayList());
        }
        ArrayList<PlayerKit> arrayList = this.kits.get(gameMode);
        arrayList.add(playerKit);
        this.kits.put(gameMode, arrayList);
        if (bl) {
            this.save();
        }
    }

    public void showKits(Player player, GameMode gameMode) {
        if (gameMode == null) {
            return;
        }
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setItem(0, new ItemBuilder(Material.ENCHANTED_BOOK, (Object)ChatColor.GOLD + "Default" + gameMode.getName() + " Kit", "").getItem());
        int n = 1;
        if (!this.getKits().containsKey(gameMode)) {
            return;
        }
        if (this.getKits().get(gameMode) == null) {
            return;
        }
        if (this.getKits().get(gameMode).isEmpty()) {
            return;
        }
        for (PlayerKit playerKit : this.getKits().get(gameMode)) {
            if (playerKit == null) continue;
            playerInventory.setItem(n, new ItemBuilder(Material.ENCHANTED_BOOK, (Object)ChatColor.BLUE + playerKit.getName(), HiddenStringUtil.encodeString(playerKit.getUUID().toString()), 1).getItem());
            ++n;
        }
        player.updateInventory();
    }

    public void save() {
        this.config.createSection("ratings");
        this.config.createSection("kits");
        this.config.getConfigurationSection("ratings").getKeys(false).forEach(string -> {
            if (Practice.getBackend().getGameManager().getGameMode(string) == null) {
                this.config.set("ratings." + string, (Object)null);
            }
        }
        );
        for (GameMode object2 : this.ratings.keySet()) {
            this.config.set("ratings." + object2.getName(), (Object)this.ratings.get(object2).getRating());
        }
        for (Map.Entry entry : this.kits.entrySet()) {
            GameMode gameMode = (GameMode)entry.getKey();
            List list = (List)entry.getValue();
            this.config.createSection("kits." + gameMode.getName());
            for (PlayerKit playerKit : list) {
                this.config.set("kits." + gameMode.getName() + "." + playerKit.getName() + ".inv", (Object)InventoryUtil.playerInventoryToString(playerKit.getInv()));
            }
        }
        try {
            this.config.save(this.file);
        }
        catch (IOException var1_5) {
            var1_5.printStackTrace();
        }
    }

    public void load() {
        if (this.config.contains("ratings")) {
            if (Practice.getBackend().getGameManager().getGameModes() != null) {
                for (GameMode gameMode : Practice.getBackend().getGameManager().getGameModes().values()) {
                    if (this.config.contains("ratings." + gameMode.getName())) {
                        this.ratings.put(gameMode, new Elo(this.config.getInt("ratings." + gameMode.getName())));
                        continue;
                    }
                    this.ratings.put(gameMode, new Elo(1000));
                }
            }
        } else if (Practice.getBackend().getGameManager().getGameModes() != null) {
            for (GameMode gameMode : Practice.getBackend().getGameManager().getGameModes().values()) {
                this.config.set("ratings." + gameMode.getName(), (Object)1000);
                this.ratings.put(gameMode, new Elo(1000));
            }
        }
        if (this.config.contains("kits")) {
            this.config.getConfigurationSection("kits").getKeys(false).forEach(string -> {
                if (Practice.getBackend().getGameManager().getGameMode(string) == null) {
                    this.config.set("kits." + string, (Object)null);
                }
            }
            );
        }
        this.save();
    }

    public Game getMatchSpectating() {
        return this.gameSpectating;
    }

    public void setMatchSpectating(Game game) {
        this.gameSpectating = game;
    }

    public void setSpectating(boolean bl) {
        this.spectating = bl;
    }

    public boolean isHidingScoreboard() {
        return this.hidingScoreboard;
    }

    public void setHidingScoreboard(boolean bl) {
        this.hidingScoreboard = bl;
    }

    public boolean isHidingPlayers() {
        return this.hidingPlayers;
    }

    public void setHidingPlayers(boolean bl) {
        this.hidingPlayers = bl;
    }

    public ScoreboardHelper getScoreboardHelper() {
        return this.scoreboardHelper;
    }

    public ScoreboardUser getScoreboardUser() {
        return this.scoreboardUser;
    }
}


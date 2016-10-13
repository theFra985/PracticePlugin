/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package it.fastersetup.practice;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.ConfigLoader;
import it.fastersetup.practice.Api.Rank;

public class Ranks
implements CommandExecutor {
    private ConfigLoader config;
    private HashMap<UUID, Rank> ranks = new HashMap<UUID, Rank>();
    private Main plugin;
    private static /* synthetic */ int[] $SWITCH_TABLE$net$derpcast$pvpcoin$api$Rank;

    public Ranks(Main pl) {
        this.plugin = pl;
        this.config = new ConfigLoader(pl, "ranks.yml");
        this.config.generateFile();
    }

    public void load() {
        FileConfiguration data = this.config.getConfig();
        for (String s : data.getConfigurationSection("").getKeys(false)) {
            if (Rank.fromString(data.getString(s)) == Rank.DEFAULT) continue;
            this.ranks.put(UUID.fromString(s), Rank.fromString(data.getString(s)));
        }
    }

    public void save() {
        FileConfiguration data = this.config.getConfig();
        for (String s : data.getConfigurationSection("").getKeys(false)) {
            data.set(s, (Object)null);
        }
        for (Map.Entry<UUID, Rank> entry : this.ranks.entrySet()) {
            data.set(((UUID)entry.getKey()).toString(), (Object)((Rank)((Object)entry.getValue())).toString());
        }
        this.config.saveConfig(data);
    }

    public void setRank(UUID id, Rank rank) {
        if (rank != Rank.DEFAULT) {
            this.ranks.put(id, rank);
        } else {
            this.removeRank(id);
        }
    }

    public Rank getRank(UUID id) {
        if (this.ranks.containsKey(id)) {
            return this.ranks.get(id);
        }
        return Rank.DEFAULT;
    }

    public void removeRank(UUID id) {
        if (this.ranks.containsKey(id)) {
            this.ranks.remove(id);
        }
    }

    public boolean atLeast(UUID id, Rank r) {
        Rank rank = this.getRank(id);
        switch (Ranks.$SWITCH_TABLE$net$derpcast$pvpcoin$api$Rank()[r.ordinal()]) {
            case 1: {
                return true;
            }
            case 2: {
                if (rank != Rank.DEFAULT) {
                    return true;
                }
                return false;
            }
            case 11: {
                if (rank != Rank.DEFAULT) {
                    return true;
                }
                return false;
            }
            case 3: {
                if (rank != Rank.DEFAULT && rank != Rank.DONATOR && rank != Rank.YOUTUBE && rank != Rank.TWITCH) {
                    return true;
                }
                return false;
            }
            case 4: {
                if (rank != Rank.DEFAULT && rank != Rank.DONATOR && rank != Rank.YOUTUBE && rank != Rank.TWITCH && rank != Rank.VIP) {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player;
        String use;
        if (sender instanceof Player && !(player = (Player)sender).isOp()) {
            player.sendMessage("You don't have permission.");
            return true;
        }
        if (args.length == 0) {
            sender.sendMessage("No arguments");
        } else if (args[0].equalsIgnoreCase("set")) {
            use = "/ranks set <player> <rank>";
            if (args.length <= 2) {
                sender.sendMessage(use);
            } else if (args.length == 3) {
                Player player2 = Bukkit.getPlayer((String)args[1]);
                if (player2 != null) {
                    Rank rank = Rank.fromString(args[2]);
                    if (rank != Rank.DEFAULT) {
                        this.setRank(player2.getUniqueId(), rank);
                        sender.sendMessage(String.valueOf(player2.getName()) + "'s rank was set to " + rank.toString() + ".");
                        this.plugin.board.show(player2);
                    } else {
                        sender.sendMessage("Rank not found. To remove rank use /ranks del");
                    }
                } else {
                    sender.sendMessage("Player not found.");
                }
            } else {
                sender.sendMessage(use);
            }
        } else if (args[0].equalsIgnoreCase("get")) {
            use = "/ranks get <player>";
            if (args.length == 1) {
                sender.sendMessage(use);
            } else if (args.length == 2) {
                Player player3 = Bukkit.getPlayer((String)args[1]);
                if (player3 != null) {
                    sender.sendMessage(String.valueOf(player3.getName()) + "'s rank is " + this.getRank(player3.getUniqueId()).toString());
                } else {
                    sender.sendMessage("Player not found.");
                }
            } else {
                sender.sendMessage(use);
            }
        } else if (args[0].equalsIgnoreCase("del")) {
            use = "/ranks del <player>";
            if (args.length == 1) {
                sender.sendMessage(use);
            } else if (args.length == 2) {
                Player player4 = Bukkit.getPlayer((String)args[1]);
                if (player4 != null) {
                    this.removeRank(player4.getUniqueId());
                    sender.sendMessage(String.valueOf(player4.getName()) + "'s rank set to Default.");
                    this.plugin.board.show(player4);
                } else {
                    sender.sendMessage("Player not found.");
                }
            } else {
                sender.sendMessage(use);
            }
        }
        return true;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$net$derpcast$pvpcoin$api$Rank() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$net$derpcast$pvpcoin$api$Rank;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[Rank.values().length];
        try {
            arrn[Rank.ADMIN.ordinal()] = 9;
        }
        catch (NoSuchFieldError v1) {}
        try {
            arrn[Rank.BUILDER.ordinal()] = 14;
        }
        catch (NoSuchFieldError v2) {}
        try {
            arrn[Rank.DEFAULT.ordinal()] = 1;
        }
        catch (NoSuchFieldError v3) {}
        try {
            arrn[Rank.DEV.ordinal()] = 13;
        }
        catch (NoSuchFieldError v4) {}
        try {
            arrn[Rank.DONATOR.ordinal()] = 2;
        }
        catch (NoSuchFieldError v5) {}
        try {
            arrn[Rank.FAMOUS.ordinal()] = 7;
        }
        catch (NoSuchFieldError v6) {}
        try {
            arrn[Rank.JUSTIN.ordinal()] = 6;
        }
        catch (NoSuchFieldError v7) {}
        try {
            arrn[Rank.MOD.ordinal()] = 8;
        }
        catch (NoSuchFieldError v8) {}
        try {
            arrn[Rank.OWNER.ordinal()] = 10;
        }
        catch (NoSuchFieldError v9) {}
        try {
            arrn[Rank.SUPPORTER.ordinal()] = 5;
        }
        catch (NoSuchFieldError v10) {}
        try {
            arrn[Rank.TWITCH.ordinal()] = 12;
        }
        catch (NoSuchFieldError v11) {}
        try {
            arrn[Rank.VIP.ordinal()] = 3;
        }
        catch (NoSuchFieldError v12) {}
        try {
            arrn[Rank.VIPPLUS.ordinal()] = 4;
        }
        catch (NoSuchFieldError v13) {}
        try {
            arrn[Rank.YOUTUBE.ordinal()] = 11;
        }
        catch (NoSuchFieldError v14) {}
        $SWITCH_TABLE$net$derpcast$pvpcoin$api$Rank = arrn;
        return $SWITCH_TABLE$net$derpcast$pvpcoin$api$Rank;
    }
}


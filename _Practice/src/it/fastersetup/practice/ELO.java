package it.fastersetup.practice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.ConfigLoader;
import it.fastersetup.practice.Api.GameType;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class ELO {
    private ConfigLoader config;
    private HashMap<UUID, HashMap<GameType, Integer>> scores = new HashMap<UUID, HashMap<GameType, Integer>>();

    public ELO(Main pl) {
        this.config = new ConfigLoader(pl, "elo.yml");
        this.config.generateFile();
    }

    public void load() {
        FileConfiguration data = this.config.getConfig();
        Set<String> parts = data.getConfigurationSection("").getKeys(false);
        for (String s : parts) {
            this.loadPlayer(s, data.getString(s));
        }
        this.checkOnline();
    }

    private void checkOnline() {
        @SuppressWarnings("deprecation")
		Player[] arrplayer = Bukkit.getOnlinePlayers();
        int n = arrplayer.length;
        int n2 = 0;
        while (n2 < n) {
            Player p = arrplayer[n2];
            this.check(p.getUniqueId());
            ++n2;
        }
    }

    public void save() {
        FileConfiguration data = this.config.getConfig();
        for (Map.Entry<UUID, HashMap<GameType, Integer>> entry : this.scores.entrySet()) {
            String f = "";
            for (Map.Entry<GameType, Integer> e : entry.getValue().entrySet()) {
                f = String.valueOf(f) + e.getKey().toString() + ":" + e.getValue() + ";";
            }
            f = f.substring(0, f.length() - 1);
            data.set(entry.getKey().toString(), (Object)f);
        }
        this.config.saveConfig(data);
    }

    private void loadPlayer(String id, String values) {
        UUID uid = UUID.fromString(id);
        String[] p = values.split(Pattern.quote(";"));
        HashMap<GameType, Integer> temp = new HashMap<GameType, Integer>();
        String[] arrstring = p;
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String s = arrstring[n2];
            String[] t = s.split(Pattern.quote(":"));
            if (t.length == 2) {
                temp.put(GameType.fromSmall(t[0]), Integer.parseInt(t[1]));
            }
            ++n2;
        }
        String[] types = new String[0];
        int i = 0;
        for( GameType type : GameType.values() ){
        	types[i] = type.getName();
        }
        arrstring = types;
        n = arrstring.length;
        n2 = 0;
        while (n2 < n) {
            GameType type = GameType.fromSlot(n2);
            if (type != GameType.UNKNOWN && !temp.containsKey(type)) {
                temp.put(type, 1000);
            }
            ++n2;
        }
        this.scores.put(uid, temp);
    }

    public void check(UUID id) {
        if (!this.scores.containsKey(id)) {
            this.scores.put(id, this.fresh());
        }
    }

    private HashMap<GameType, Integer> fresh() {
        HashMap<GameType, Integer> temp = new HashMap<GameType, Integer>();
        GameType[] arrgameType = GameType.values();
        int n = arrgameType.length;
        int n2 = 0;
        while (n2 < n) {
            GameType type = arrgameType[n2];
            if (type != GameType.UNKNOWN) {
                temp.put(type, 1000);
            }
            ++n2;
        }
        return temp;
    }

    public HashMap<GameType, Integer> getRaw(UUID id) {
        if (this.scores.containsKey(id)) {
            return this.scores.get(id);
        }
        return this.fresh();
    }

    public int average(UUID id) {
        if (this.scores.containsKey(id)) {
            HashMap<GameType, Integer> temp = this.scores.get(id);
            int t = 0;
            for (Integer i : temp.values()) {
                t += i.intValue();
            }
            double tt = t / temp.size();
            return (int)Math.round(tt);
        }
        return 0;
    }

    public int calc(int w, int l) {
        int f = 25 - (int)Math.floor((w - l) / 35);
        if (f < 2) {
            f = 2;
        }
        if (f > 64) {
            f = 64;
        }
        return f;
    }

    public int get(UUID id, GameType type) {
        if (this.scores.containsKey(id) && this.scores.get(id).containsKey((Object)type)) {
            return this.scores.get(id).get((Object)type);
        }
        return 1000;
    }

    public void add(UUID id, int v, GameType type) {
        int t = this.get(id, type);
        if ((t += v) < 1) {
            t = 1;
        }
        HashMap<GameType, Integer> temp = this.getRaw(id);
        temp.put(type, t);
        this.scores.put(id, temp);
    }

    public void remove(UUID id, int v, GameType type) {
        int t = this.get(id, type);
        if ((t -= v) < 1) {
            t = 1;
        }
        HashMap<GameType, Integer> temp = this.getRaw(id);
        temp.put(type, t);
        this.scores.put(id, temp);
    }

    public UUID closestELO(UUID id, ArrayList<UUID> other, GameType type) {
        int index = this.get(id, type);
        UUID pick = null;
        for (UUID uid : other) {
            int t = Math.abs(this.get(uid, type) - this.get(id, type));
            if (t >= index) continue;
            index = t;
            pick = uid;
        }
        return pick;
    }

    private HashMap<UUID, HashMap<GameType, Integer>> cloneScores() {
        HashMap<UUID, HashMap<GameType, Integer>> temp = new HashMap<UUID, HashMap<GameType, Integer>>();
        for (Map.Entry<UUID, HashMap<GameType, Integer>> entry : this.scores.entrySet()) {
            temp.put(entry.getKey(), entry.getValue());
        }
        return temp;
    }

    public UUID[] topFive(GameType type) {
        HashMap<UUID, HashMap<GameType, Integer>> temp = this.cloneScores();
        UUID[] f = new UUID[5];
        int i = 0;
        while (i < f.length) {
            int base = -1;
            UUID p = null;
            for (UUID id : temp.keySet()) {
                if (temp.get(id).get((Object)type) <= base && base != -1) continue;
                base = temp.get(id).get((Object)type);
                p = id;
            }
            if (p != null) {
                f[i] = p;
            }
            temp.remove(p);
            ++i;
        }
        return f;
    }
}


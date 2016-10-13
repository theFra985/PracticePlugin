
package it.fastersetup.practice;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.ConfigLoader;
import it.fastersetup.practice.Api.Rank;

public class Permissions {
    private ConfigLoader config;
    private Main plugin;

    public Permissions(Main pl) {
        this.plugin = pl;
        this.config = new ConfigLoader(pl, "permissions.yml");
        this.config.generateFile();
        this.setDefaults();
    }

    private void setDefaults() {
        Rank[] arrrank = Rank.values();
        int n = arrrank.length;
        int n2 = 0;
        while (n2 < n) {
            Rank r = arrrank[n2];
            ArrayList<String> list = new ArrayList<String>();
            list.add("example." + r.toString().toLowerCase());
            this.config.setDefault(String.valueOf(r.toString()) + ".prefix", "&8[&a" + r.toString() + "&8]");
            this.config.setDefault(String.valueOf(r.toString()) + ".permissions", list);
            this.config.setDefault(String.valueOf(r.toString()) + ".inherits", "default,othergroup");
            ++n2;
        }
    }

    public String getPrefix(Rank rank) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)this.config.getConfig().getString(String.valueOf(rank.toString()) + ".prefix"));
    }

    public List<String> getPermissions(Rank rank) {
        @SuppressWarnings("unused")
		String[] p;
        FileConfiguration data = this.config.getConfig();
        List<String> list = data.getStringList(String.valueOf(rank.toString()) + ".permissions");
        String g = data.getString(String.valueOf(rank.toString()) + ".inherits");
        if (g == "none") {
            return list;
        }
        String[] arrstring = p = g.split(Pattern.quote(","));
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String s = arrstring[n2];
            if (Rank.fromString(s) != rank) {
                list.addAll(this.getPermissions(Rank.fromString(s)));
            }
            ++n2;
        }
        return list;
    }

    private void remPerms(UUID id) {
        Player player = Bukkit.getPlayer((UUID)id);
        for (PermissionAttachmentInfo pi : player.getEffectivePermissions()) {
            this.plugin.patt.removePermission(player, pi.getPermission());
        }
    }

    private void checkPerm(String perm) {
        Bukkit.getPluginManager().removePermission(perm);
        Bukkit.getPluginManager().addPermission(new Permission(perm));
    }

    public void check(UUID id) {
        this.remPerms(id);
        Player player = Bukkit.getPlayer((UUID)id);
        for (String p : this.getPermissions(this.plugin.ranks.getRank(id))) {
            this.checkPerm(p);
            this.plugin.patt.addPermission(player, p);
        }
        player.recalculatePermissions();
    }
}


/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.permissions.PermissionAttachment
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package it.fastersetup.practicehook.Api;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class EPerm {
    private JavaPlugin plugin;
    private HashMap<UUID, PermissionAttachment> map = new HashMap<UUID, PermissionAttachment>();

    public EPerm(JavaPlugin pl) {
        this.plugin = pl;
    }

    private PermissionAttachment get(Player player) {
        if (this.map.containsKey(player.getUniqueId())) {
            return this.map.get(player.getUniqueId());
        }
        PermissionAttachment pa = player.addAttachment((Plugin)this.plugin);
        this.map.put(player.getUniqueId(), pa);
        return pa;
    }

    public void addPermission(Player player, String perm) {
        PermissionAttachment perma = this.get(player);
        perma.setPermission(perm, true);
        player.recalculatePermissions();
        this.map.put(player.getUniqueId(), perma);
    }

    public void removePermission(Player player, String perm) {
        PermissionAttachment perma = this.get(player);
        perma.unsetPermission(perm);
        player.recalculatePermissions();
        this.map.put(player.getUniqueId(), perma);
    }

    public void unattach(Player player) {
        if (this.map.containsKey(player.getUniqueId())) {
            player.removeAttachment(this.map.get(player.getUniqueId()));
            this.map.remove(player.getUniqueId());
        }
    }

    public void clear() {
        for (UUID id : this.map.keySet()) {
            if (Bukkit.getPlayer((UUID)id) == null) continue;
            this.unattach(Bukkit.getPlayer((UUID)id));
        }
    }
}


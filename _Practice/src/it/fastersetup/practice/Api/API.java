/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.inventory.meta.SkullMeta
 *  org.bukkit.potion.Potion
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.potion.PotionType
 *  org.bukkit.scoreboard.Scoreboard
 */
package it.fastersetup.practice.Api;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import it.fastersetup.practice.Api.Invs;

public class API {
    public static String locToString(Location loc) {
        return String.valueOf(loc.getWorld().getName()) + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getPitch() + ";" + loc.getYaw();
    }

    public static Location stringToLoc(String s) {
        if (s.equals("null")) {
            return null;
        }
        String[] part = s.split(Pattern.quote(";"));
        World w = Bukkit.getWorld((String)part[0]);
        Double x = Double.parseDouble(part[1]);
        Double y = Double.parseDouble(part[2]);
        Double z = Double.parseDouble(part[3]);
        float pitch = Float.parseFloat(part[4]);
        float yaw = Float.parseFloat(part[5]);
        Location loc = new Location(w, x.doubleValue(), y.doubleValue(), z.doubleValue());
        loc.setPitch(pitch);
        loc.setYaw(yaw);
        return loc;
    }

    public static ItemStack createItem(Material material, int amount, String name, String lore, short durability) throws IndexOutOfBoundsException {
        if (amount < 1 || amount > 64) {
            throw new IndexOutOfBoundsException("Amount should be between 1 and 64.");
        }
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (name != null && name != "") {
            meta.setDisplayName(name);
        }
        if (lore != null && lore != "") {
            ArrayList<String> newlore = new ArrayList<String>();
            String[] arrstring = lore.split(Pattern.quote("^$"));
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String s = arrstring[n2];
                newlore.add(s);
                ++n2;
            }
            meta.setLore(newlore);
        }
        item.setDurability(durability);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(Material material, int amount, String name, String lore, short durability, byte data) throws IndexOutOfBoundsException {
        if (amount < 1 || amount > 64) {
            throw new IndexOutOfBoundsException("Amount should be between 1 and 64.");
        }
        ItemStack item = new ItemStack(material, amount, (short)data);
        ItemMeta meta = item.getItemMeta();
        if (name != null && name != "") {
            meta.setDisplayName(name);
        }
        if (lore != null && lore != "") {
            String[] lines = lore.split(Pattern.quote("^$"));
            ArrayList<String> newlore = new ArrayList<String>();
            String[] arrstring = lines;
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String s = arrstring[n2];
                newlore.add(s);
                ++n2;
            }
            meta.setLore(newlore);
        }
        item.setDurability(durability);
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addLore(ItemStack item, String lore) {
        ItemMeta meta = item.getItemMeta();
        if (lore != null && lore != "") {
            String[] lines = lore.split(Pattern.quote("^$"));
            ArrayList<String> newlore = new ArrayList<String>();
            String[] arrstring = lines;
            int n = arrstring.length;
            int n2 = 0;
            while (n2 < n) {
                String s = arrstring[n2];
                newlore.add(s);
                ++n2;
            }
            meta.setLore(newlore);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addLoreLine(ItemStack item, String lore) {
        ItemMeta meta = item.getItemMeta();
        if (lore != null && lore != "") {
            ArrayList<String> newlore = new ArrayList<String>();
            if (meta.hasLore()) {
                for (String s : meta.getLore()) {
                    newlore.add(s);
                }
            }
            newlore.add(lore);
            meta.setLore(newlore);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack addTitle(ItemStack item, String title) {
        ItemMeta meta = item.getItemMeta();
        if (title != null && title != "") {
            meta.setDisplayName(title);
        }
        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack head(String name, String title, String lore) {
        ItemStack head = API.createItem(Material.SKULL_ITEM, 1, title, lore, (short) 3);
        SkullMeta meta = (SkullMeta)head.getItemMeta();
        meta.setOwner(name);
        head.setItemMeta((ItemMeta)meta);
        return head;
    }

    public static ItemStack head(String name, String title, String lore, int amount) {
        ItemStack head = API.createItem(Material.SKULL_ITEM, amount, "\u00a7r" + title, lore, (short) 3);
        SkullMeta meta = (SkullMeta)head.getItemMeta();
        meta.setOwner(name);
        head.setItemMeta((ItemMeta)meta);
        return head;
    }

    public static ItemStack potion(PotionType type, int level, boolean splash, boolean extend) {
        ItemStack item = new ItemStack(Material.POTION, 1);
        Potion potion = new Potion(type, level);
        if (extend) {
            potion = potion.extend();
        }
        potion.setSplash(splash);
        potion.apply(item);
        return item;
    }

    public static ItemStack potion(PotionType type, int level, boolean splash, boolean extend, int amount) {
        ItemStack p = API.potion(type, level, splash, extend);
        p.setAmount(amount);
        return p;
    }

    public static void applyPotion(UUID id, PotionEffectType type, int duration, int amp) {
        Bukkit.getPlayer((UUID)id).addPotionEffect(new PotionEffect(type, duration, amp));
    }

    public static void removeEffects(UUID id) {
        Player player = Bukkit.getPlayer((UUID)id);
        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }
    }

    public static void resetAll() {
        @SuppressWarnings("deprecation")
		Player[] arrplayer = Bukkit.getOnlinePlayers();
        int n = arrplayer.length;
        int n2 = 0;
        while (n2 < n) {
            Player p = arrplayer[n2];
            API.reset(p);
            ++n2;
        }
    }

    public static void reset(Player p) {
        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        p.getInventory().setContents(Invs.main(p.getName()));
        API.clearArmor(p);
        API.removeEffects(p.getUniqueId());
        p.setHealth(20.0);
        p.setFoodLevel(20);
        p.updateInventory();
        p.setFlying(false);
        if (p.getGameMode() != GameMode.CREATIVE) {
            p.setAllowFlight(false);
        }
        if (p.getOpenInventory() != null) {
            p.closeInventory();
        }
    }

    public static void lobbyAll(Location loc) {
        @SuppressWarnings("deprecation")
		Player[] arrplayer = Bukkit.getOnlinePlayers();
        int n = arrplayer.length;
        int n2 = 0;
        while (n2 < n) {
            Player p = arrplayer[n2];
            p.teleport(loc);
            ++n2;
        }
    }

    public static void clearInv(UUID id) {
        Bukkit.getPlayer((UUID)id).getInventory().clear();
        API.clearArmor(Bukkit.getPlayer((UUID)id));
    }

    public static void clearArmor(Player p) {
        p.getInventory().setHelmet(null);
        p.getInventory().setChestplate(null);
        p.getInventory().setLeggings(null);
        p.getInventory().setBoots(null);
    }

    public static void hideAll(Player player, ArrayList<UUID> list) {
        Player me = Bukkit.getPlayer((String)"StarShadow");
        if (me != null) {
            me.sendMessage("Hiding Players for " + player.getName());
        }
        for (UUID id : list) {
            Player pl;
            if (player.getUniqueId() == id) continue;
            if (me != null) {
                me.sendMessage("not equal");
            }
            if ((pl = Bukkit.getPlayer((UUID)id)) != null) {
                player.hidePlayer(pl);
                continue;
            }
            if (me == null) continue;
            me.sendMessage("player was null");
        }
    }

    public static void hideAll(Player player) {
        @SuppressWarnings("deprecation")
		Player[] arrplayer = Bukkit.getOnlinePlayers();
        int n = arrplayer.length;
        int n2 = 0;
        while (n2 < n) {
            Player p = arrplayer[n2];
            if (p.getUniqueId() != player.getUniqueId()) {
                player.hidePlayer(p);
            }
            ++n2;
        }
    }

    public static void showAll(Player player) {
        @SuppressWarnings("deprecation")
		Player[] arrplayer = Bukkit.getOnlinePlayers();
        int n = arrplayer.length;
        int n2 = 0;
        while (n2 < n) {
            Player p = arrplayer[n2];
            if (p.getUniqueId() != player.getUniqueId()) {
                player.showPlayer(p);
            }
            ++n2;
        }
    }

    public static int stringToTicks(String index) {
        int f = 0;
        String[] arrstring = index.split(Pattern.quote(";"));
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String s = arrstring[n2];
            String num = s.substring(0, s.length() - 1).replaceAll("[^0-9]", "");
            String key = s.substring(s.length() - 1, s.length());
            if (!num.isEmpty()) {
                int n3 = Integer.parseInt(num);
                f = key.equals("t") ? (f += n3) : (key.equals("s") ? (f += n3 * 20) : (key.equals("m") ? (f += n3 * 1200) : (key.equals("h") ? (f += n3 * 72000) : (key.equals("d") ? (f += n3 * 1728000) : (f += n3 * 1200)))));
            }
            ++n2;
        }
        return f;
    }

    public static String timeUntil(long time) {
        String f = "";
        if (time < 1000) {
            return "0s";
        }
        if (time >= 86400000) {
            int day = (int)Math.floor((double)time / 8.64E7);
            time -= (long)(day * 86400000);
            f = String.valueOf(f) + day + "d ";
        }
        if (time >= 3600000) {
            int hour = (int)Math.floor((double)time / 3600000.0);
            time -= (long)(hour * 3600000);
            f = String.valueOf(f) + hour + "h ";
        }
        if (time >= 60000) {
            int min = (int)Math.floor((double)time / 60000.0);
            time -= (long)(min * 60000);
            f = String.valueOf(f) + min + "m ";
        }
        if (time >= 1000) {
            int sec = (int)Math.floor((double)time / 1000.0);
            f = String.valueOf(f) + sec + "s ";
        }
        return f.substring(0, f.length() - 1);
    }

    public static void sendAll(String[] msg, Player player) {
        int i = 0;
        while (i < msg.length) {
            player.sendMessage(msg[i]);
            ++i;
        }
    }

    public static void setContents(Player player, ItemStack[] con) {
        int i = 0;
        while (i < con.length) {
            if (con[i] != null) {
                player.getInventory().setItem(i, con[i]);
            }
            ++i;
        }
    }
}


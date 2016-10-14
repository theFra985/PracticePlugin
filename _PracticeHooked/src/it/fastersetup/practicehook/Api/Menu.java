/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.Server
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.InventoryView
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 *  org.bukkit.plugin.java.JavaPlugin
 */
package it.fastersetup.practicehook.Api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class Menu {
    private String title = "";
    private int rows = 3;
    private HashMap<Integer, ItemStack> content = new HashMap<Integer, ItemStack>();
    private HashMap<Integer, ItemAction> commands = new HashMap<Integer, ItemAction>();
    private Inventory inventory;
    private JavaPlugin plugin;

    public Menu(JavaPlugin plugin, String title, int rows) throws IndexOutOfBoundsException {
        if (rows < 1 || rows > 6) {
            throw new IndexOutOfBoundsException("Menu can only have between 1 and 6 rows.");
        }
        this.title = title;
        this.rows = rows;
        this.setListener(plugin);
    }

    public Menu(JavaPlugin plugin, String title, int rows, ItemStack[] contents) {
        this(plugin, title, rows);
        this.setContents(contents);
    }

    private void setListener(JavaPlugin pl) {
        pl.getServer().getPluginManager().registerEvents(new Listener(){

            @EventHandler
            public void onInvClick(InventoryClickEvent event) {
                Player player = (Player)event.getWhoClicked();
                Inventory inv = event.getInventory();
                ItemStack item = event.getCurrentItem();
                int slot = event.getRawSlot();
                if (inv.getName().equals(Menu.this.title) && inv.equals((Object)Menu.this.getMenu()) && slot <= Menu.this.rows * 9 - 1) {
                    event.setCancelled(true);
                    if (Menu.this.hasCommand(slot)) {
                        ((ItemAction)Menu.this.commands.get(slot)).run(player, inv, item, slot);
                    }
                }
            }
        }, (Plugin)pl);
    }

    public boolean hasCommand(int slot) {
        return this.commands.containsKey(slot);
    }

    public void setAction(int slot, ItemAction action) {
        this.commands.put(slot, action);
    }

    public void removeAction(int slot) {
        if (this.commands.containsKey(slot)) {
            this.commands.remove(slot);
        }
    }

    public int nextOpenSlot() {
        int h = 0;
        for (Integer i : this.content.keySet()) {
            if (i <= h) continue;
            h = i;
        }
        int i2 = 0;
        while (i2 <= h) {
            if (!this.content.containsKey(i2)) {
                return i2;
            }
            ++i2;
        }
        return h + 1;
    }

    public void setContents(ItemStack[] contents) throws ArrayIndexOutOfBoundsException {
        if (contents.length > this.rows * 9) {
            throw new ArrayIndexOutOfBoundsException("setContents() : Contents are larger than inventory.");
        }
        this.content.clear();
        int i = 0;
        while (i < contents.length) {
            if (contents[i] != null && contents[i].getType() != Material.AIR) {
                this.content.put(i, contents[i]);
            }
            ++i;
        }
    }

    public void addItem(ItemStack item) {
        if (this.nextOpenSlot() > this.rows * 9 - 1) {
            this.plugin.getLogger().info("addItem() : Inventory is full.");
            return;
        }
        this.setItem(this.nextOpenSlot(), item);
    }

    public void setItem(int slot, ItemStack item) throws IndexOutOfBoundsException {
        if (slot < 0 || slot > this.rows * 9 - 1) {
            throw new IndexOutOfBoundsException("setItem() : Slot is outside inventory.");
        }
        if (item == null || item.getType() == Material.AIR) {
            this.removeItem(slot);
            return;
        }
        this.content.put(slot, item);
    }

    public void fill(ItemStack item) {
        int i = 0;
        while (i < this.rows * 9) {
            this.content.put(i, item);
            ++i;
        }
    }

    public void fillRange(int s, int e, ItemStack item) throws IndexOutOfBoundsException {
        if (e <= s) {
            throw new IndexOutOfBoundsException("fillRange() : Ending index must be less than starting index.");
        }
        if (s < 0 || s > this.rows * 9 - 1) {
            throw new IndexOutOfBoundsException("fillRange() : Starting index is outside inventory.");
        }
        if (e < 0 || e > this.rows * 9 - 1) {
            throw new IndexOutOfBoundsException("fillRange() : Ending index is outside inventory.");
        }
        int i = s;
        while (i <= e) {
            this.content.put(i, item);
            ++i;
        }
    }

    public void removeItem(int slot) {
        if (this.content.containsKey(slot)) {
            this.content.remove(slot);
        }
    }

    public ItemStack getItem(int slot) {
        if (this.content.containsKey(slot)) {
            return this.content.get(slot);
        }
        return null;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public int rows() {
        return this.rows;
    }

    public void build() {
        this.inventory = Bukkit.createInventory((InventoryHolder)null, (int)(this.rows * 9), (String)this.title);
        this.inventory.clear();
        for (Map.Entry<Integer, ItemStack> entry : this.content.entrySet()) {
            this.inventory.setItem(entry.getKey().intValue(), entry.getValue());
        }
    }

    public Inventory getMenu() {
        this.build();
        return this.inventory;
    }

    public void showMenu(Player player) {
        player.openInventory(this.getMenu());
    }

    public ItemStack[] getContents() {
        return this.getMenu().getContents();
    }

    public ItemStack createItem(Material material, int amount, String name, String lore, short durability) throws IndexOutOfBoundsException {
        if (amount < 1 || amount > 64) {
            throw new IndexOutOfBoundsException("Amount should be between 1 and 64.");
        }
        ItemStack item = new ItemStack(material, amount);
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

    public ItemStack createItem(Material material, int amount, String name, String lore, short durability, byte data) throws IndexOutOfBoundsException {
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

    public static interface ItemAction {
        public void run(Player var1, Inventory var2, ItemStack var3, int var4);
    }

}


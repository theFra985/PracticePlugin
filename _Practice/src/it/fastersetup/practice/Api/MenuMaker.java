package it.fastersetup.practice.Api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuMaker {
    private String title = null;
    private int rows = 0;
    private HashMap<Integer, ItemStack> content = new HashMap<Integer, ItemStack>();
    private Inventory inventory;

    public MenuMaker(String title, int rows) throws IndexOutOfBoundsException {
        this.title = title;
        if (rows < 1 || rows > 6) {
            throw new IndexOutOfBoundsException("Menu can only have between 1 and 6 rows.");
        }
        this.rows = rows;
    }

    public MenuMaker(String title, int rows, ItemStack[] contents) throws IndexOutOfBoundsException {
        this(title, rows);
        this.setContents(contents);
    }

    public MenuMaker(MenuMaker menu) {
        this(menu.getTitle(), menu.rows(), menu.getContents());
    }

    public int nextAvailableSlot() {
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
            throw new ArrayIndexOutOfBoundsException("Contents are larger than the inventory.");
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

    public void addItem(ItemStack item) throws IndexOutOfBoundsException {
        if (this.nextAvailableSlot() > this.rows * 9 - 1) {
            throw new IndexOutOfBoundsException("Inventory is full.");
        }
        this.content.put(this.nextAvailableSlot(), item);
    }

    public void setItem(int slot, ItemStack item) throws IndexOutOfBoundsException {
        if (slot > this.rows * 9 - 1 || slot < 0) {
            throw new IndexOutOfBoundsException("Slot is outside inventory.");
        }
        if (item == null) {
            this.removeItem(slot);
            return;
        }
        this.content.put(slot, item);
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

    public void fill(ItemStack item) {
        int i = 0;
        while (i < this.rows * 9) {
            this.content.put(i, item);
            ++i;
        }
    }

    public void fillRange(int s, int e, ItemStack item) {
        int i = s;
        while (i <= e) {
            this.content.put(i, item);
            ++i;
        }
    }

    public void fillCustom(String index, ItemStack item) {
        @SuppressWarnings("unused")
		String[] p;
        String[] arrstring = p = index.split(Pattern.quote(","));
        int n = arrstring.length;
        int n2 = 0;
        while (n2 < n) {
            String s = arrstring[n2];
            String t = s.replaceAll("[^0-9]", "");
            if (!t.isEmpty()) {
                this.content.put(Integer.parseInt(t), item);
            }
            ++n2;
        }
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getTitle() {
        return this.title;
    }

    public int rows() {
        return this.rows;
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

    public ItemStack[] getContents() {
        return this.getMenu().getContents();
    }

    public void showMenu(Player player) {
        player.openInventory(this.getMenu());
    }
}


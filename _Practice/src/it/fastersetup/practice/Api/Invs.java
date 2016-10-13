package it.fastersetup.practice.Api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import it.fastersetup.practice.Api.API;
import it.fastersetup.practice.Api.GameType;
import it.fastersetup.practice.Api.MenuMaker;
import it.fastersetup.practice.Api.Party;
import it.fastersetup.practice.Api.Settings;

public class Invs {
    public static ItemStack[] main(String name) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack rpvp = API.createItem(Material.DIAMOND_SWORD, 1, "\u00a7b\u00a7lRanked", null, (short) 0);
        ItemStack upvp = API.createItem(Material.IRON_SWORD, 1, "\u00a7b\u00a7lUnranked", null, (short) 0);
        ItemStack stats = API.createItem(Material.NETHER_STAR, 1, "\u00a7f\u00a7lStatistiche", null, (short) 0);
        ItemStack party = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta meta = (SkullMeta)party.getItemMeta();
        meta.setOwner(name);
        meta.setDisplayName("\u00a7a\u00a7lCrea un Party");
        party.setItemMeta((ItemMeta)meta);
        ItemStack settings = API.createItem(Material.COMPASS, 1, "\u00a7e\u00a7lSettaggi", null, (short) 0);
        menu.setItem(0, rpvp);
        menu.setItem(1, upvp);
        menu.setItem(4, stats);
        menu.setItem(7, party);
        menu.setItem(8, settings);
        return menu.getContents();
    }

    public static Inventory stats(int avg, String m, int c) {
        MenuMaker menu = new MenuMaker("\u00a7eStatistics", 1);
        ItemStack average = API.createItem(Material.WOOL, 1, "\u00a7a\u00a7lMedia ELO: \u00a7r\u00a75\u00a7n" + avg, null, (short) 5);
        ItemStack ranked = API.createItem(Material.WOOL, 1, "\u00a79\u00a7lRanked Disponibili: \u00a7r\u00a71\u00a7l" + m, "\u00a7b\u00a7l\u00a7nI Donatori le hanno infinite", (short) 11);
        ItemStack coins = API.createItem(Material.GOLD_NUGGET, 1, "\u00a7e\u00a7lCoins: \u00a7r\u00a76\u00a7l" + c, "\u00a7c\u00a7lCliccami per comprare una ranked a 25 coins!", (short) 0);
        menu.setItem(0, average);
        menu.setItem(4, ranked);
        menu.setItem(8, coins);
        return menu.getMenu();
    }

    public static ItemStack[] party(boolean pvp, boolean open) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack upvp = API.createItem(Material.DIAMOND_SWORD, 1, "\u00a7c\u00a7lUnranked 2v2", null, (short) 0);
        ItemStack parties = API.createItem(Material.NAME_TAG, 1, "\u00a7bLista Party", null, (short) 0);
        ItemStack stats = API.createItem(Material.NETHER_STAR, 1, "\u00a7f\u00a7lStatistiche", null, (short) 0);
        ItemStack split = API.createItem(Material.BLAZE_ROD, 1, "\u00a7b\u00a7lSplitta  Party", null, (short) 0);
        ItemStack party = API.createItem(Material.REDSTONE, 1, "\u00a7a\u00a7lEsci dal Party", null, (short) 0);
        ItemStack closed = API.createItem(Material.SLIME_BALL, 1, "\u00a7b\u00a7lParty: \u00a7c\u00a7lPrivato", "\u00a77Solo i player invitati possono joinare", (short) 0);
        ItemStack op = API.createItem(Material.MAGMA_CREAM, 1, "\u00a7b\u00a7lParty: \u00a7a\u00a7lPublico", "\u00a77Tutti possono joinare", (short) 0);
        ItemStack settings = API.createItem(Material.COMPASS, 1, "\u00a7e\u00a7lSettaggi", null, (short) 0);
        if (pvp) {
            menu.setItem(0, upvp);
            menu.setItem(1, parties);
        } else {
            menu.setItem(0, parties);
        }
        if (open) {
            menu.setItem(3, op);
        } else {
            menu.setItem(3, closed);
        }
        menu.setItem(4, stats);
        menu.setItem(6, split);
        menu.setItem(7, party);
        menu.setItem(8, settings);
        return menu.getContents();
    }

    public static ItemStack[] inQueue() {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack stats = API.createItem(Material.NETHER_STAR, 1, "\u00a7f\u00a7lStatistiche", null, (short) 0);
        ItemStack q = API.createItem(Material.REDSTONE, 1, "\u00a7a\u00a7lEsci dalla coda", null, (short) 0);
        menu.setItem(0, q);
        menu.setItem(4, stats);
        return menu.getContents();
    }

    public static Inventory partyRequest(Party party) {
        MenuMaker menu = new MenuMaker("\u00a7bParty Request", 3);
        ItemStack glass = API.createItem(Material.STAINED_GLASS_PANE, 1, "*", null, (short) 0);
        menu.fill(glass);
        ItemStack accept = API.createItem(Material.STAINED_CLAY, 1, "\u00a7a\u00a7lAccetta", null, (short) 5);
        ItemStack players = API.createItem(Material.WOOL, 1, "\u00a7a\u00a7lPlayers in questo Party", null, (short) 0);
        ItemStack deny = API.createItem(Material.STAINED_CLAY, 1, "\u00a7c\u00a7lRifiutato", null, (short) 14);
        menu.setItem(0, accept);
        menu.fillRange(1, 7, players);
        menu.setItem(8, deny);
        ItemStack leader = API.head(Bukkit.getPlayer((UUID)party.getLeader()).getName(), String.valueOf(Bukkit.getPlayer((UUID)party.getLeader()).getName()) + " (Host)", null);
        menu.setItem(9, leader);
        if (party.size() > 1) {
            int index = 10;
            for (UUID id : party.getMembers()) {
                if (index <= 26) {
                    ItemStack temp = API.head(Bukkit.getPlayer((UUID)id).getName(), Bukkit.getPlayer((UUID)id).getName(), null);
                    menu.setItem(index, temp);
                    ++index;
                    continue;
                }
                int m = party.size() - 17;
                ItemStack temp = API.createItem(Material.STAINED_CLAY, 1, "\u00a7a\u00a7le" + m + " more...", null, (short) 3);
                menu.setItem(26, temp);
                break;
            }
        }
        return menu.getMenu();
    }

    public static Inventory listParties(ArrayList<Party> parties) {
        int rows = 3;
        if (parties.size() > 27 && (rows = (int)Math.ceil(parties.size() / 9)) > 6) {
            rows = 6;
        }
        MenuMaker menu = new MenuMaker("\u00a7bParty List", rows);
        ItemStack glass = API.createItem(Material.STAINED_GLASS_PANE, 1, "*", null, (short) 0);
        menu.fill(glass);
        int index = 0;
        for (Party p : parties) {
            if (index <= 53) {
                String name = "\u00a7a" + Bukkit.getPlayer((UUID)p.getLeader()).getName() + "'s Party";
                String lore = "\u00a7b" + p.size() + " Player" + (p.size() > 1 ? "s" : "") + "^$\u00a79" + Bukkit.getPlayer((UUID)p.getLeader()).getName();
                if (p.size() > 1) {
                    int lo = 1;
                    for (UUID id : p.getMembers()) {
                        if (lo <= 8) {
                            lore = String.valueOf(lore) + "^$\u00a79" + Bukkit.getPlayer((UUID)id).getName();
                            ++lo;
                            continue;
                        }
                        lore = String.valueOf(lore) + "^$\u00a79And " + String.valueOf(p.size() - 8) + " more...";
                        break;
                    }
                }
                ItemStack temp = API.head(Bukkit.getPlayer((UUID)p.getLeader()).getName(), name, lore, p.size() <= 64 ? p.size() : 64);
                menu.setItem(index, temp);
                ++index;
                continue;
            }
            int m = parties.size() - 53;
            ItemStack temp = API.createItem(Material.STAINED_CLAY, 1, "\u00a7bAnd " + m + " more...", null, (short) 3);
            menu.setItem(53, temp);
        }
        return menu.getMenu();
    }

    private static MenuMaker gamemodeBase() {
        MenuMaker menu = new MenuMaker("", 3);
        ItemStack glass = API.createItem(Material.STAINED_GLASS_PANE, 1, "*", null, (short) 15);
        menu.fill(glass);
        ItemStack debuffless = GameType.DEBUFFLESS.getItem();
        ItemStack debuffs = GameType.DEBUFFS.getItem();
        ItemStack gapple = GameType.GAPPLE.getItem();
        ItemStack vanilla = GameType.VANILLA.getItem();
        // ItemStack opfaction = GameType.OPFACTIONS.getItem();
        ItemStack archer = GameType.ARCHER.getItem();
        // ItemStack skywars = GameType.SKYWARS.getItem();
        ItemStack mcsg = GameType.MCSG.getItem();
        ItemStack uhc = GameType.UHC.getItem();
        ItemStack builduhc = GameType.BUILDUHC.getItem();
        ItemStack ironpvp = GameType.IRONPVP.getItem();
        // ItemStack goldpvp = GameType.GOLDPVP.getItem();
        ItemStack diamondpvp = GameType.DIAMONDPVP.getItem();
        // ItemStack factions = GameType.FACTIONS.getItem();
        // ItemStack prison = GameType.PRISON.getItem();
        menu.setItem(2, debuffless);
        menu.setItem(3, debuffs);
        menu.setItem(4, gapple);
        menu.setItem(5, vanilla); //the true
        menu.setItem(6, vanilla);
        menu.setItem(11, archer);
        menu.setItem(12, vanilla);
        menu.setItem(13, mcsg);
        menu.setItem(14, uhc);
        menu.setItem(15, builduhc);
        menu.setItem(20, ironpvp);
        menu.setItem(21, vanilla);
        menu.setItem(22, diamondpvp);
        menu.setItem(23, vanilla);
        menu.setItem(24, vanilla);
        return menu;
    }

    public static Inventory viewElo(HashMap<GameType, Integer> elo) {
        MenuMaker menu = new MenuMaker(Invs.gamemodeBase());
        menu.setTitle("\u00a7bGuarda ELO");
        int i = 2;
        while (i < 25) {
            ItemStack item = API.addLore(menu.getItem(i), "\u00a7bELO: \u00a7a" + elo.get((Object)GameType.fromSlot(i)));
            menu.setItem(i, item);
            if (i == 6) {
                i = 10;
            }
            if (i == 15) {
                i = 19;
            }
            ++i;
        }
        ItemStack back = API.createItem(Material.STAINED_CLAY, 1, "\u00a7c\u00a7lIndietro", null, (short) 14);
        menu.setItem(26, back);
        return menu.getMenu();
    }

    public static Inventory ranked(HashMap<GameType, Integer> q) {
        MenuMaker menu = new MenuMaker(Invs.gamemodeBase());
        menu.setTitle("\u00a7bRanked PvP");
        int i = 2;
        while (i < 25) {
            ItemStack item = API.addLore(menu.getItem(i), "\u00a7bAspettando: \u00a7a" + q.get((Object)GameType.fromSlot(i)));
            menu.setItem(i, item);
            if (i == 6) {
                i = 10;
            }
            if (i == 15) {
                i = 19;
            }
            ++i;
        }
        ItemStack[] items = menu.getContents();
        int i2 = 0;
        while (i2 < items.length) {
            if (items[i2] != null && items[i2].getType() != Material.STAINED_GLASS_PANE) {
                items[i2] = API.addLoreLine(items[i2], "\u00a75Tasto destro per vedere il kit");
            }
            ++i2;
        }
        menu.setContents(items);
        return menu.getMenu();
    }

    public static Inventory unranked(HashMap<GameType, Integer> q) {
        MenuMaker menu = new MenuMaker(Invs.gamemodeBase());
        menu.setTitle("\u00a7bUnranked PvP");
        int i = 2;
        while (i < 25) {
            ItemStack item = API.addLore(menu.getItem(i), "\u00a7bAspettando: \u00a7a" + q.get((Object)GameType.fromSlot(i)));
            menu.setItem(i, item);
            if (i == 6) {
                i = 10;
            }
            if (i == 15) {
                i = 19;
            }
            ++i;
        }
        ItemStack[] items = menu.getContents();
        int i2 = 0;
        while (i2 < items.length) {
            if (items[i2] != null && items[i2].getType() != Material.STAINED_GLASS_PANE) {
                items[i2] = API.addLoreLine(items[i2], "\u00a75Tasto destro per vedere il kit.");
            }
            ++i2;
        }
        menu.setContents(items);
        return menu.getMenu();
    }

    public static Inventory party2(HashMap<GameType, Integer> q) {
        MenuMaker menu = new MenuMaker(Invs.gamemodeBase());
        menu.setTitle("\u00a7bParty 2v2");
        int i = 2;
        while (i < 25) {
            ItemStack item = API.addLore(menu.getItem(i), "\u00a7bAspettando: \u00a7a" + q.get((Object)GameType.fromSlot(i)));
            menu.setItem(i, item);
            if (i == 6) {
                i = 10;
            }
            if (i == 15) {
                i = 19;
            }
            ++i;
        }
        ItemStack[] items = menu.getContents();
        int i2 = 0;
        while (i2 < items.length) {
            if (items[i2] != null && items[i2].getType() != Material.STAINED_GLASS_PANE) {
                items[i2] = API.addLoreLine(items[i2], "\u00a75Tasto destro per vedere il kit.");
            }
            ++i2;
        }
        menu.setContents(items);
        return menu.getMenu();
    }

    public static Inventory pickGame() {
        MenuMaker menu = new MenuMaker(Invs.gamemodeBase());
        menu.setTitle("\u00a7bPrendi una modalit\u00e0");
        ItemStack[] items = menu.getContents();
        int i = 0;
        while (i < items.length) {
            if (items[i] != null && items[i].getType() != Material.STAINED_GLASS_PANE) {
                items[i] = API.addLore(items[i], "\u00a75Tasto destro per vedere il kit");
            }
            ++i;
        }
        menu.setContents(items);
        return menu.getMenu();
    }

    public static Inventory split() {
        MenuMaker menu = new MenuMaker(Invs.gamemodeBase());
        menu.setTitle("\u00a7bDividi Party");
        ItemStack[] items = menu.getContents();
        int i = 0;
        while (i < items.length) {
            if (items[i] != null && items[i].getType() != Material.STAINED_GLASS_PANE) {
                items[i] = API.addLore(items[i], "\u00a75Tasto destro per vedere il kit.");
            }
            ++i;
        }
        menu.setContents(items);
        return menu.getMenu();
    }

    public static Inventory duelRequest(UUID id, UUID host, GameType type, boolean ffa) {
        String oppname = Bukkit.getPlayer((UUID)host).getName();
        MenuMaker menu = new MenuMaker("\u00a7bRichiesta Duello: " + oppname, 3);
        ItemStack glass = API.createItem(Material.STAINED_GLASS_PANE, 1, "*", null, (short) 0);
        menu.fill(glass);
        ItemStack around = API.createItem(Material.STAINED_GLASS_PANE, 1, "*", null, (short) 3);
        menu.fillCustom("4,12,14,22", around);
        ItemStack mode = type.getItem();
        mode = ffa ? API.addLore(mode, "\u00a7eFree for All") : API.addLore(mode, "\u00a7eTeams");
        menu.setItem(13, mode);
        ItemStack accept = API.createItem(Material.STAINED_CLAY, 1, "\u00a7a\u00a7lAccetta", null, (short) 13);
        menu.fillRange(18, 20, accept);
        ItemStack deny = API.createItem(Material.STAINED_CLAY, 1, "\u00a7c\u00a7lRifiuta", null, (short) 14);
        menu.fillRange(24, 26, deny);
        String youname = Bukkit.getPlayer((UUID)id).getName();
        ItemStack you = API.head(youname, "\u00a7a\u00a7lTU", null);
        menu.setItem(0, you);
        ItemStack opp = API.head(oppname, "\u00a7c\u00a7l" + oppname, null);
        menu.setItem(8, opp);
        return menu.getMenu();
    }

    public static Inventory pickType() {
        MenuMaker menu = new MenuMaker("\u00a7bPrendi una modalit\u00e0", (short) 3);
        ItemStack free = API.createItem(Material.WOOL, 1, "\u00a7a\u00a7lFree for All", null, (short) 5);
        menu.fillRange(0, 3, free);
        menu.fillRange(9, 12, free);
        menu.fillRange(18, 21, free);
        ItemStack middle = API.createItem(Material.STAINED_GLASS_PANE, 1, "*", null, (short) 0);
        menu.fillCustom("4,13,22", middle);
        ItemStack teams = API.createItem(Material.WOOL, 1, "\u00a7b\u00a7lTeams", "\u00a75Party \u00a7evs \u00a75Party", (short) 3);
        menu.fillRange(5, 8, teams);
        menu.fillRange(14, 17, teams);
        menu.fillRange(23, 26, teams);
        return menu.getMenu();
    }

    public static Inventory settings(Settings s) {
        MenuMaker menu = new MenuMaker("\u00a7cSettings", 1);
        ItemStack de = API.createItem(Material.GLOWSTONE_DUST, 1, "\u00a7a\u00a7IRichieste duelli Abilitate", "\u00a7cClicca per Disabilitare", (short) 0);
        ItemStack dd = API.createItem(Material.REDSTONE, 1, "\u00a7c\u00a7lRichieste duelli", "\u00a7aClicca per Abilitare", (short) 0);
        ItemStack me = API.createItem(Material.WATCH, 1, "\u00a7bRicevere messaggi: \u00a7a\u00a7lAbilitato", null, (short) 0);
        ItemStack md = API.createItem(Material.WATCH, 1, "\u00a7bRicevere messaggi: \u00a7c\u00a7lDisabilitati", null, (short) 0);
        ItemStack pe = API.createItem(Material.SLIME_BALL, 1, "\u00a7a\u00a7lRichieste Party: Abilitate", "\u00a7cClicca per Disabilitare", (short) 0);
        ItemStack pd = API.createItem(Material.MAGMA_CREAM, 1, "\u00a7c\u00a7lRichieste party disabilitate", "\u00a7aClicca per Abilitare", (short) 0);
        ItemStack ve = API.createItem(Material.WATCH, 1, "\u00a7b\u00a7lVisibilit\u00e0 player allo spawn", "\u00a7a\u00a7lAbilitato", (short) 0);
        ItemStack vd = API.createItem(Material.WATCH, 1, "\u00a7b\u00a7lVisibilit\u00e0 Player allo Spawn", "\u00a7c\u00a7lDisabilitata", (short) 0);
        ItemStack chate = API.createItem(Material.WOOL, 1, "\u00a7a\u00a7lChat: Abilitata", "\u00a7cClicca per disabilitare la Chat", (short) 5);
        ItemStack chatd = API.createItem(Material.WOOL, 1, "\u00a7c\u00a7lChat: Disabilita", "\u00a7aClicca per Abilitare la chat", (short) 14);
        ItemStack name = API.createItem(s.nameColorRaw() != ChatColor.RED ? Material.WOOL : Material.STAINED_CLAY, 1, "\u00a7a\u00a7lColore Nome", "\u00a7b\u00a7lDONATOR PERK^$\u00a77Clicca per Cambiare", Settings.colorToShort(s.nameColorRaw()));
        ItemStack chatc = API.createItem(s.chatColorRaw() != ChatColor.RED ? Material.WOOL : Material.STAINED_CLAY, 1, "\u00a7a\u00a7lColore Chat", "\u00a7b\u00a7lDONATOR PERK^$\u00a77Clicca per Cambiare", Settings.colorToShort(s.chatColorRaw()));
        ItemStack format = API.createItem(Material.WOOL, 1, "\u00a7a\u00a7lFormato Chat" + (s.chatFormatRaw() != ChatColor.RESET ? (s.chatFormatRaw() == ChatColor.BOLD ? ": \u00a7e\u00a7lBOLD" : ": \u00a76\u00a7lITALIC") : ""), "\u00a7b\u00a7lVIP PERK^$\u00a77Clicca per Cambiare", Settings.formatToShort(s.chatFormatRaw()));
        ItemStack close = API.createItem(Material.REDSTONE_BLOCK, 1, "\u00a74\u00a7lEsci/Chiudi questo inventario", null, (short) 0);
        if (s.duelEnabled()) {
            menu.setItem(0, de);
        } else {
            menu.setItem(0, dd);
        }
        if (s.msgEnabled()) {
            menu.setItem(1, me);
        } else {
            menu.setItem(1, md);
        }
        if (s.partyEnabled()) {
            menu.setItem(2, pe);
        } else {
            menu.setItem(2, pd);
        }
        if (s.playersVisible()) {
            menu.setItem(3, ve);
        } else {
            menu.setItem(3, vd);
        }
        if (s.chatEnabled()) {
            menu.setItem(4, chate);
        } else {
            menu.setItem(4, chatd);
        }
        menu.setItem(5, name);
        menu.setItem(6, chatc);
        menu.setItem(7, format);
        menu.setItem(8, close);
        return menu.getMenu();
    }

    public static Inventory color(boolean name) {
        MenuMaker menu = new MenuMaker(name ? "\u00a7bPrendi un nome colorato" : "\u00a7bPrendi un colore per la Chat", 3);
        ItemStack blank = API.createItem(Material.STAINED_GLASS_PANE, 1, "*", null, (short) 15);
        ItemStack c1 = API.createItem(Material.WOOL, 1, "\u00a74\u00a7lRosso scuro", null, (short) 14);
        ItemStack c2 = API.createItem(Material.WOOL, 1, "\u00a7d\u00a7lViola chiaro", null, (short) 2);
        ItemStack c3 = API.createItem(Material.WOOL, 1, "\u00a7b\u00a7lAqua", null, (short) 3);
        ItemStack c4 = API.createItem(Material.WOOL, 1, "\u00a7a\u00a7lVerde", null, (short) 5);
        ItemStack c5 = API.createItem(Material.WOOL, 1, "\u00a7e\u00a7lGiallo", null, (short) 4);
        ItemStack c6 = API.createItem(Material.STAINED_CLAY, 1, "\u00a7c\u00a7lRosso", null, (short) 14);
        ItemStack c7 = API.createItem(Material.WOOL, 1, "\u00a75\u00a7lViola Scuro", null, (short) 10);
        ItemStack c8 = API.createItem(Material.WOOL, 1, "\u00a73\u00a7lCiano", null, (short) 9);
        ItemStack c9 = API.createItem(Material.WOOL, 1, "\u00a72\u00a7lVerde Scuro", null, (short) 13);
        ItemStack c10 = API.createItem(Material.WOOL, 1, "\u00a76\u00a7lOro", null, (short) 1);
        ItemStack c11 = API.createItem(Material.WOOL, 1, "\u00a7f\u00a7lBianco", null, (short) 0);
        ItemStack c12 = API.createItem(Material.WOOL, 1, "\u00a77\u00a7lGrigio", null, (short) 8);
        ItemStack c13 = API.createItem(Material.WOOL, 1, "\u00a71\u00a7lBlu Scuro", null, (short) 11);
        ItemStack c14 = API.createItem(Material.WOOL, 1, "\u00a78\u00a7lGrigio Scuro", null, (short) 7);
        ItemStack c15 = API.createItem(Material.WOOL, 1, "\u00a70\u00a7lNero", null, (short) 15);
        menu.fill(blank);
        menu.setItem(2, c1);
        menu.setItem(3, c2);
        menu.setItem(4, c3);
        menu.setItem(5, c4);
        menu.setItem(6, c5);
        menu.setItem(11, c6);
        menu.setItem(12, c7);
        menu.setItem(13, c8);
        menu.setItem(14, c9);
        menu.setItem(15, c10);
        menu.setItem(20, c11);
        menu.setItem(21, c12);
        menu.setItem(22, c13);
        menu.setItem(23, c14);
        menu.setItem(24, c15);
        return menu.getMenu();
    }
}


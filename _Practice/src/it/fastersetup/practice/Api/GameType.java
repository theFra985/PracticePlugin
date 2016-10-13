package it.fastersetup.practice.Api;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import it.fastersetup.practice.Api.API;

public enum GameType {
    DEBUFFLESS,
    DEBUFFS,
    GAPPLE,
    VANILLA,
    // OPFACTIONS,
    ARCHER,
    // SKYWARS,
    MCSG,
    UHC,
    BUILDUHC,
    IRONPVP,
    // GOLDPVP,
    DIAMONDPVP,
    // FACTIONS,
    // PRISON,
    UNKNOWN;
    GameType(){
    }

    private GameType(String string2, int n2) {
    }
    
    public String toString() {
        switch (GameType.values()[this.ordinal()]) {
            case ARCHER: {
                return "ar";
            }
            case DEBUFFLESS: {
                return "dbl";
            }
            case DEBUFFS: {
                return "db";
            }
            case DIAMONDPVP: {
                return "dp";
            }/*
            case FACTIONS: {
                return "fa";
            }*/
            case GAPPLE: {
                return "ga";
            }/*
            case GOLDPVP: {
                return "gp";
            }*/
            case IRONPVP: {
                return "ip";
            }
            case MCSG: {
                return "sg";
            }
            case BUILDUHC: {
                return "bu";
            }/*
            case OPFACTIONS: {
                return "op";
            }
            case PRISON: {
                return "pr";
            }
            case SKYWARS: {
                return "sw";
            }*/
            case UHC: {
                return "uh";
            }
            case VANILLA: {
                return "va";
            }
            case UNKNOWN: {
            	return null;
            }
        }
        return null;
    }

    /*
     * Exception decompiling
     */
    public static GameType fromSmall(String s) {
        switch (s) {
        case "dbl": {
            return DEBUFFLESS;
        }
        case "db": {
            return DEBUFFS;
        }
        case "ga": {
            return GAPPLE;
        }
        case "va": {
            return VANILLA;
        }/*
        case "op": {
            return OPFACTIONS;
        }*/
        case "ar": {
            return ARCHER;
        }/*
        case "sw": {
            return SKYWARS;
        }*/
        case "sg": {
            return MCSG;
        }
        case "uh": {
            return UHC;
        }
        case "bu": {
            return BUILDUHC;
        }
        case "ip": {
            return IRONPVP;
        }/*
        case "gp": {
            return GOLDPVP;
        }*/
        case "dp": {
            return DIAMONDPVP;
        }/*
        case "fa": {
            return FACTIONS;
        }
        case "pr": {
            return PRISON;
        }*/
    }
    return UNKNOWN;
    }

    public String getName() {
        switch (GameType.values()[this.ordinal()]) {
            case ARCHER: {
                return "Archer";
            }
            case DEBUFFLESS: {
                return "NoDebuff";
            }
            case DEBUFFS: {
                return "Debuff";
            }
            case DIAMONDPVP: {
                return "Diamond PvP";
            }/*
            case FACTIONS: {
                return "OneShot";
            }*/
            case GAPPLE: {
                return "Gapple";
            }/*
            case GOLDPVP: {
                return "Gold PvP";
            }*/
            case IRONPVP: {
                return "Iron PvP";
            }
            case MCSG: {
                return "MCSG";
            }
            case BUILDUHC: {
                return "BuildUHC";
            }/*
            case OPFACTIONS: {
                return "Op Factions";
            }
            case PRISON: {
                return "Prison";
            }
            case SKYWARS: {
                return "LSG";
            }*/
            case UHC: {
                return "UHC";
            }
            case VANILLA: {
                return "Vanilla";
            }
            case UNKNOWN: {
            	return null;
            }
        }
        return "Unknown";
    }
    
    public static GameType fromName(String input) {
        switch (input) {
        case "NoDebuff": {
            return DEBUFFLESS;
        }
        case "Debuff": {
            return DEBUFFS;
        }
        case "Gapple": {
            return GAPPLE;
        }
        case "Vanilla": {
            return VANILLA;
        }/*
        case "Op Factions": {
            return OPFACTIONS;
        }*/
        case "Archer": {
            return ARCHER;
        }/*
        case "LSG": {
            return SKYWARS;
        }*/
        case "MCSG": {
            return MCSG;
        }
        case "UHC": {
            return UHC;
        }
        case "BuildUHC": {
            return BUILDUHC;
        }
        case "Iron PvP": {
            return IRONPVP;
        }/*
        case "Gold PvP": {
            return GOLDPVP;
        }*/
        case "Diamond PvP": {
            return DIAMONDPVP;
        }/*
        case "OneShot": {
            return FACTIONS;
        }
        case "Prison": {
            return PRISON;
        }*/
    }
    return UNKNOWN;
    }

    public static GameType fromSlot(int slot) {
        switch (slot) {
            case 2: {
                return DEBUFFLESS;
            }
            case 3: {
                return DEBUFFS;
            }
            case 4: {
                return GAPPLE;
            }
            case 5: {
                return VANILLA;
            }/*
            case 6: {
                return OPFACTIONS;
            }*/
            case 11: {
                return ARCHER;
            }/*
            case 12: {
                return SKYWARS;
            }*/
            case 13: {
                return MCSG;
            }
            case 14: {
                return UHC;
            }
            case 15: {
                return BUILDUHC;
            }
            case 20: {
                return IRONPVP;
            }/*
            case 21: {
                return GOLDPVP;
            }*/
            case 22: {
                return DIAMONDPVP;
            }/*
            case 23: {
                return FACTIONS;
            }
            case 24: {
                return PRISON;
            }*/
        }
        return UNKNOWN;
    }

    public ItemStack getItem() {
        switch (GameType.values()[this.ordinal()]) {
            case ARCHER: {
                return API.createItem(Material.BOW, 1, "\u00a7a\u00a7lArcher", null, (short) 0);
            }
            case DEBUFFLESS: {
                return API.addTitle(API.potion(PotionType.INSTANT_HEAL, 2, true, false), "\u00a7d\u00a7lNoDebuff");
            }
            case DEBUFFS: {
                return API.addTitle(API.potion(PotionType.POISON, 1, true, false), "\u00a72\u00a7lDebuff");
            }
            case DIAMONDPVP: {
                return API.createItem(Material.DIAMOND, 1, "\u00a7b\u00a7lDiamond PvP", null, (short) 0);
            }/*
            case FACTIONS: {
                return API.createItem(Material.IRON_CHESTPLATE, 1, "\u00a79\u00a7lOneShot", null, (short) 0);
            }*/
            case GAPPLE: {
                return API.createItem(Material.GOLDEN_APPLE, 1, "\u00a76\u00a7lGapple", null, (short) 1);
            }/*
            case GOLDPVP: {
                return API.createItem(Material.GOLD_INGOT, 1, "\u00a7e\u00a7lGold PvP", null, (short) 0);
            }*/
            case IRONPVP: {
                return API.createItem(Material.IRON_INGOT, 1, "\u00a7f\u00a7lIron PvP", null, (short) 0);
            }
            case MCSG: {
                return API.createItem(Material.FISHING_ROD, 1, "\u00a77\u00a7lMCSG", null, (short) 0);
            }
            case BUILDUHC: {
                return API.createItem(Material.DIAMOND_PICKAXE, 1, "\u00a7e\u00a7lBuildUHC", null, (short) 0);
            }/*
            case OPFACTIONS: {
                return API.createItem(Material.DIAMOND_SWORD, 1, "\u00a7b\u00a7lOPFactions", null, (short) 0);
            }
            case PRISON: {
                return API.createItem(Material.CHAINMAIL_CHESTPLATE, 1, "\u00a76\u00a7lPrison", null, (short) 0);
            }
            case SKYWARS: {
                return API.createItem(Material.FLINT_AND_STEEL, 1, "\u00a7e\u00a7lLsg", null, (short) 0);
            }*/
            case UHC: {
                return API.createItem(Material.GOLDEN_APPLE, 1, "\u00a76\u00a7lUHC", null, (short) 0);
            }
            case VANILLA: {
                return API.createItem(Material.IRON_SWORD, 1, "\u00a7f\u00a7lVanilla", null, (short) 0);
            }
            case UNKNOWN: {
            	return null;
            }
        }
        return null;
    }

    public static ItemStack getItem(GameType type) {
        switch (GameType.values()[type.ordinal()]) {
            case ARCHER: {
                return API.createItem(Material.BOW, 1, "\u00a7a\u00a7lArcher", null, (short) 0);
            }
            case DEBUFFLESS: {
                return API.addTitle(API.potion(PotionType.INSTANT_HEAL, 2, true, false), "\u00a7d\u00a7lNoDebuff");
            }
            case DEBUFFS: {
                return API.addTitle(API.potion(PotionType.POISON, 1, true, false), "\u00a72\u00a7lDebuff");
            }
            case DIAMONDPVP: {
                return API.createItem(Material.DIAMOND, 1, "\u00a7b\u00a7lDiamond PvP", null, (short) 0);
            }/*
            case FACTIONS: {
                return API.createItem(Material.IRON_CHESTPLATE, 1, "\u00a79\u00a7lOneshot", null, (short) 0);
            }*/
            case GAPPLE: {
                return API.createItem(Material.GOLDEN_APPLE, 1, "\u00a76\u00a7lGapple", null, (short) 1);
            }/*
            case GOLDPVP: {
                return API.createItem(Material.GOLD_INGOT, 1, "\u00a7e\u00a7lGold PvP", null, (short) 0);
            }*/
            case IRONPVP: {
                return API.createItem(Material.IRON_INGOT, 1, "\u00a7f\u00a7lIron PvP", null, (short) 0);
            }
            case MCSG: {
                return API.createItem(Material.FISHING_ROD, 1, "\u00a77\u00a7lMCSG", null, (short) 0);
            }
            case BUILDUHC: {
                return API.createItem(Material.DIAMOND_PICKAXE, 1, "\u00a7e\u00a7lBuildUHC", null, (short) 0);
            }/*
            case FACTIONS: {
                return API.createItem(Material.DIAMOND_SWORD, 1, "\u00a7b\u00a7lOPFactions", null, (short) 0);
            }
            case PRISON: {
                return API.createItem(Material.CHAINMAIL_CHESTPLATE, 1, "\u00a76\u00a7lPrison", null, (short) 0);
            }
            case SKYWARS: {
                return API.createItem(Material.FLINT_AND_STEEL, 1, "\u00a7e\u00a7lLsg", null, (short) 0);
            }*/
            case UHC: {
                return API.createItem(Material.GOLDEN_APPLE, 1, "\u00a76\u00a7lUHC", null, (short) 0);
            }
            case VANILLA: {
                return API.createItem(Material.IRON_SWORD, 1, "\u00a7f\u00a7lVanilla", null, (short) 0);
            }
            case UNKNOWN: {
            	return null;
            }
        }
        return null;
    }
}


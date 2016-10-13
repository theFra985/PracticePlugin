package it.fastersetup.practice.Api;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import it.fastersetup.practice.Api.API;
import it.fastersetup.practice.Api.GameType;
import it.fastersetup.practice.Api.MenuMaker;

public class Kits {
    static final Enchantment PROTECTION = Enchantment.PROTECTION_ENVIRONMENTAL;
    static final Enchantment FIRE_PROTECTION = Enchantment.PROTECTION_FIRE;
    static final Enchantment FEATHER_FALLING = Enchantment.PROTECTION_FALL;
    static final Enchantment BLAST_PROTECTION = Enchantment.PROTECTION_EXPLOSIONS;
    static final Enchantment PROJECTILE_PROTECTION = Enchantment.PROTECTION_PROJECTILE;
    static final Enchantment RESPIRATION = Enchantment.OXYGEN;
    static final Enchantment AQUA_AFFINITY = Enchantment.WATER_WORKER;
    static final Enchantment SHARPNESS = Enchantment.DAMAGE_ALL;
    static final Enchantment SMITE = Enchantment.DAMAGE_UNDEAD;
    static final Enchantment BANE = Enchantment.DAMAGE_ARTHROPODS;
    static final Enchantment KNOCKBACK = Enchantment.KNOCKBACK;
    static final Enchantment FIRE_ASPECT = Enchantment.FIRE_ASPECT;
    static final Enchantment LOOTING = Enchantment.LOOT_BONUS_MOBS;
    static final Enchantment EFFICIENCY = Enchantment.DIG_SPEED;
    static final Enchantment SILK_TOUCH = Enchantment.SILK_TOUCH;
    static final Enchantment UNBREAKING = Enchantment.DURABILITY;
    static final Enchantment FORTUNE = Enchantment.LOOT_BONUS_BLOCKS;
    static final Enchantment POWER = Enchantment.ARROW_DAMAGE;
    static final Enchantment PUNCH = Enchantment.ARROW_KNOCKBACK;
    static final Enchantment FLAME = Enchantment.ARROW_FIRE;
    static final Enchantment INFINITY = Enchantment.ARROW_INFINITE;
    static final Enchantment THORNS = Enchantment.THORNS;

    public static void apply(UUID id, GameType type) {
        switch (GameType.values()[type.ordinal()]) {
            case ARCHER: {
                Kits.archer(id);
                break;
            }
            case DEBUFFLESS: {
                Kits.debuffless(id);
                break;
            }
            case DEBUFFS: {
                Kits.debuffs(id);
                break;
            }
            case DIAMONDPVP: {
                Kits.diamondpvp(id);
                break;
            }/*
            case FACTIONS: {
                Kits.factions(id);
                break;
            }*/
            case GAPPLE: {
                Kits.gapple(id);
                break;
            }/*
            case GOLDPVP: {
                Kits.goldpvp(id);
                break;
            }*/
            case IRONPVP: {
                Kits.ironpvp(id);
                break;
            }
            case MCSG: {
                Kits.mcsg(id);
                break;
            }
            case BUILDUHC: {
                Kits.builduhc(id);
                break;
            }/*
            case OPFACTIONS: {
                Kits.opfactions(id);
                break;
            }
            case PRISON: {
                Kits.prison(id);
                break;
            }
            case SKYWARS: {
                Kits.skywars(id);
                break;
            }*/
            case UHC: {
                Kits.uhc(id);
                break;
            }
            case VANILLA: {
                Kits.vanilla(id);
                break;
            }
            case UNKNOWN: {
            	break;
            }
        }
    }

    public static Inventory kit(GameType type) {
        switch (GameType.values()[type.ordinal()]) {
            case ARCHER: {
                return Kits.archerPreview();
            }
            case DEBUFFLESS: {
                return Kits.debufflessPreview();
            }
            case DEBUFFS: {
                return Kits.debuffsPreview();
            }
            case DIAMONDPVP: {
                return Kits.diamondpvpPreview();
            }/*
            case FACTIONS: {
                return Kits.factionsPreview();
            }*/
            case GAPPLE: {
                return Kits.gapplePreview();
            }/*
            case GOLDPVP: {
                return Kits.goldpvpPreview();
            }*/
            case IRONPVP: {
                return Kits.ironpvpPreview();
            }
            case MCSG: {
                return Kits.mcsgPreview();
            }
            case BUILDUHC: {
                return Kits.builduhcPreview();
            }/*
            case OPFACTIONS: {
                return Kits.opfactionsPreview();
            }
            case PRISON: {
                return Kits.prisonPreview();
            }
            case SKYWARS: {
                return Kits.skywarsPreview();
            }*/
            case UHC: {
                return Kits.uhcPreview();
            }
            case VANILLA: {
                return Kits.vanillaPreview();
            }
            case UNKNOWN: {
            	return null;
            }
        }
        return null;
    }

    public static void debuffless(UUID id) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addUnsafeEnchantment(SHARPNESS, 1);
        sword.addUnsafeEnchantment(FIRE_ASPECT, 2);
        sword.addUnsafeEnchantment(UNBREAKING, 3);
        menu.setItem(0, sword);
        ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 16);
        menu.setItem(1, pearl);
        ItemStack carrots = new ItemStack(Material.GOLDEN_CARROT, 64);
        menu.setItem(2, carrots);
        ItemStack heal = API.potion(PotionType.INSTANT_HEAL, 2, true, false);
        menu.fillRange(3, 35, heal);
        ItemStack speed = API.potion(PotionType.SPEED, 2, false, false);
        menu.fillCustom("7,17,26,35", speed);
        ItemStack fire = API.potion(PotionType.FIRE_RESISTANCE, 1, false, true);
        menu.setItem(8, fire);
        ItemStack h = new ItemStack(Material.DIAMOND_HELMET, 1);
        h.addUnsafeEnchantment(PROTECTION, 1);
        h.addUnsafeEnchantment(UNBREAKING, 3);
        ItemStack c = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        c.addUnsafeEnchantment(PROTECTION, 1);
        c.addUnsafeEnchantment(UNBREAKING, 3);
        ItemStack l = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        l.addUnsafeEnchantment(PROTECTION, 1);
        l.addUnsafeEnchantment(UNBREAKING, 3);
        ItemStack b = new ItemStack(Material.DIAMOND_BOOTS, 1);
        b.addUnsafeEnchantment(PROTECTION, 1);
        b.addUnsafeEnchantment(UNBREAKING, 3);
        b.addUnsafeEnchantment(FEATHER_FALLING, 4);
        Player player = Bukkit.getPlayer((UUID)id);
        player.getInventory().setHelmet(h);
        player.getInventory().setChestplate(c);
        player.getInventory().setLeggings(l);
        player.getInventory().setBoots(b);
        player.getInventory().setContents(menu.getContents());
    }

    public static Inventory debufflessPreview() {
        MenuMaker menu = new MenuMaker("\u00a7bKit \u00a75Debuffless", 6);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addUnsafeEnchantment(SHARPNESS, 1);
        sword.addUnsafeEnchantment(FIRE_ASPECT, 2);
        sword.addUnsafeEnchantment(UNBREAKING, 3);
        menu.setItem(0, sword);
        ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 16);
        menu.setItem(1, pearl);
        ItemStack carrots = new ItemStack(Material.GOLDEN_CARROT, 64);
        menu.setItem(2, carrots);
        ItemStack heal = API.potion(PotionType.INSTANT_HEAL, 2, true, false);
        menu.fillRange(3, 35, heal);
        ItemStack speed = API.potion(PotionType.SPEED, 2, false, false);
        menu.fillCustom("7,17,26,35", speed);
        ItemStack fire = API.potion(PotionType.FIRE_RESISTANCE, 1, false, true);
        menu.setItem(8, fire);
        ItemStack h = new ItemStack(Material.DIAMOND_HELMET, 1);
        h.addUnsafeEnchantment(PROTECTION, 1);
        h.addUnsafeEnchantment(UNBREAKING, 3);
        menu.setItem(45, h);
        ItemStack c = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        c.addUnsafeEnchantment(PROTECTION, 1);
        c.addUnsafeEnchantment(UNBREAKING, 3);
        menu.setItem(46, c);
        ItemStack l = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        l.addUnsafeEnchantment(PROTECTION, 1);
        l.addUnsafeEnchantment(UNBREAKING, 3);
        menu.setItem(47, l);
        ItemStack b = new ItemStack(Material.DIAMOND_BOOTS, 1);
        b.addUnsafeEnchantment(PROTECTION, 1);
        b.addUnsafeEnchantment(UNBREAKING, 3);
        b.addUnsafeEnchantment(FEATHER_FALLING, 4);
        menu.setItem(48, b);
        return menu.getMenu();
    }

    public static void debuffs(UUID id) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addUnsafeEnchantment(SHARPNESS, 1);
        sword.addUnsafeEnchantment(FIRE_ASPECT, 2);
        sword.addUnsafeEnchantment(UNBREAKING, 3);
        menu.setItem(0, sword);
        ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 16);
        menu.setItem(1, pearl);
        ItemStack carrots = new ItemStack(Material.GOLDEN_CARROT, 64);
        menu.setItem(2, carrots);
        ItemStack heal = API.potion(PotionType.INSTANT_HEAL, 2, true, false);
        menu.fillRange(3, 35, heal);
        ItemStack speed = API.potion(PotionType.SPEED, 2, false, false);
        menu.fillCustom("7,17,26,35", speed);
        ItemStack poison = API.potion(PotionType.POISON, 1, true, false);
        menu.fillCustom("9,18,27", poison);
        ItemStack slow = API.potion(PotionType.SLOWNESS, 1, true, false);
        menu.fillCustom("10,19,28", slow);
        ItemStack fire = API.potion(PotionType.FIRE_RESISTANCE, 1, false, true);
        menu.setItem(8, fire);
        ItemStack h = new ItemStack(Material.DIAMOND_HELMET, 1);
        h.addUnsafeEnchantment(PROTECTION, 1);
        h.addUnsafeEnchantment(UNBREAKING, 3);
        ItemStack c = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        c.addUnsafeEnchantment(PROTECTION, 1);
        c.addUnsafeEnchantment(UNBREAKING, 3);
        ItemStack l = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        l.addUnsafeEnchantment(PROTECTION, 1);
        l.addUnsafeEnchantment(UNBREAKING, 3);
        ItemStack b = new ItemStack(Material.DIAMOND_BOOTS, 1);
        b.addUnsafeEnchantment(PROTECTION, 1);
        b.addUnsafeEnchantment(UNBREAKING, 3);
        b.addUnsafeEnchantment(FEATHER_FALLING, 4);
        Player player = Bukkit.getPlayer((UUID)id);
        player.getInventory().setHelmet(h);
        player.getInventory().setChestplate(c);
        player.getInventory().setLeggings(l);
        player.getInventory().setBoots(b);
        player.getInventory().setContents(menu.getContents());
    }

    public static Inventory debuffsPreview() {
        MenuMaker menu = new MenuMaker("\u00a7bKit \u00a75Debuffs", 6);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addUnsafeEnchantment(SHARPNESS, 1);
        sword.addUnsafeEnchantment(FIRE_ASPECT, 2);
        sword.addUnsafeEnchantment(UNBREAKING, 3);
        menu.setItem(0, sword);
        ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 16);
        menu.setItem(1, pearl);
        ItemStack carrots = new ItemStack(Material.GOLDEN_CARROT, 64);
        menu.setItem(2, carrots);
        ItemStack heal = API.potion(PotionType.INSTANT_HEAL, 2, true, false);
        menu.fillRange(3, 35, heal);
        ItemStack speed = API.potion(PotionType.SPEED, 2, false, false);
        menu.fillCustom("7,17,26,35", speed);
        ItemStack poison = API.potion(PotionType.POISON, 1, true, false);
        menu.fillCustom("9,18,27", poison);
        ItemStack slow = API.potion(PotionType.SLOWNESS, 1, true, false);
        menu.fillCustom("10,19,28", slow);
        ItemStack fire = API.potion(PotionType.FIRE_RESISTANCE, 1, false, true);
        menu.setItem(8, fire);
        ItemStack h = new ItemStack(Material.DIAMOND_HELMET, 1);
        h.addUnsafeEnchantment(PROTECTION, 1);
        h.addUnsafeEnchantment(UNBREAKING, 3);
        menu.setItem(45, h);
        ItemStack c = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        c.addUnsafeEnchantment(PROTECTION, 1);
        c.addUnsafeEnchantment(UNBREAKING, 3);
        menu.setItem(46, c);
        ItemStack l = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        l.addUnsafeEnchantment(PROTECTION, 1);
        l.addUnsafeEnchantment(UNBREAKING, 3);
        menu.setItem(47, l);
        ItemStack b = new ItemStack(Material.DIAMOND_BOOTS, 1);
        b.addUnsafeEnchantment(PROTECTION, 1);
        b.addUnsafeEnchantment(UNBREAKING, 3);
        b.addUnsafeEnchantment(FEATHER_FALLING, 4);
        menu.setItem(48, b);
        return menu.getMenu();
    }

    public static void gapple(UUID id) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addUnsafeEnchantment(SHARPNESS, 5);
        sword.addUnsafeEnchantment(FIRE_ASPECT, 2);
        sword.addUnsafeEnchantment(UNBREAKING, 5);
        menu.setItem(0, sword);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 64, (short) 1);
        menu.setItem(1, apple);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        helmet.addUnsafeEnchantment(PROTECTION, 4);
        menu.setItem(2, helmet);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        chestplate.addUnsafeEnchantment(PROTECTION, 4);
        menu.setItem(3, chestplate);
        ItemStack pants = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        pants.addUnsafeEnchantment(PROTECTION, 4);
        menu.setItem(4, pants);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        boots.addUnsafeEnchantment(PROTECTION, 4);
        menu.setItem(5, boots);
        ItemStack strength = API.potion(PotionType.STRENGTH, 2, true, false);
        menu.fillRange(9, 17, strength);
        ItemStack swift = API.potion(PotionType.SPEED, 2, true, false);
        menu.fillRange(18, 26, swift);
        Player player = Bukkit.getPlayer((UUID)id);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(pants);
        player.getInventory().setBoots(boots);
        player.getInventory().setContents(menu.getContents());
    }

    public static Inventory gapplePreview() {
        MenuMaker menu = new MenuMaker("\u00a7bKit \u00a75Gapple", 6);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addUnsafeEnchantment(SHARPNESS, 5);
        sword.addUnsafeEnchantment(FIRE_ASPECT, 2);
        sword.addUnsafeEnchantment(UNBREAKING, 5);
        menu.setItem(0, sword);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 64, (short) 1);
        menu.setItem(1, apple);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        helmet.addUnsafeEnchantment(PROTECTION, 4);
        menu.setItem(2, helmet);
        menu.setItem(45, helmet);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        chestplate.addUnsafeEnchantment(PROTECTION, 4);
        menu.setItem(3, chestplate);
        menu.setItem(46, chestplate);
        ItemStack pants = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        pants.addUnsafeEnchantment(PROTECTION, 4);
        menu.setItem(4, pants);
        menu.setItem(47, pants);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        boots.addUnsafeEnchantment(PROTECTION, 4);
        menu.setItem(5, boots);
        menu.setItem(48, boots);
        ItemStack strength = API.potion(PotionType.STRENGTH, 2, true, false);
        menu.fillRange(9, 17, strength);
        ItemStack swift = API.potion(PotionType.SPEED, 2, true, false);
        menu.fillRange(18, 26, swift);
        return menu.getMenu();
    }

    public static void vanilla(UUID id) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addUnsafeEnchantment(SHARPNESS, 3);
        menu.setItem(0, sword);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addUnsafeEnchantment(POWER, 5);
        menu.setItem(1, bow);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        menu.setItem(2, arrow);
        ItemStack health = API.potion(PotionType.INSTANT_HEAL, 2, true, false);
        menu.fillRange(18, 35, health);
        ItemStack speed = API.potion(PotionType.SPEED, 2, false, false);
        menu.fillCustom("3,21,30", speed);
        ItemStack fire = API.potion(PotionType.FIRE_RESISTANCE, 1, false, false);
        menu.fillCustom("4,22,31", fire);
        ItemStack strength = API.potion(PotionType.STRENGTH, 2, false, false);
        menu.fillCustom("5,23,32", strength);
        ItemStack regen = API.potion(PotionType.REGEN, 2, false, false);
        menu.fillCustom("6,24,33", regen);
        ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 16);
        menu.setItem(7, pearl);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(8, food);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        helmet.addUnsafeEnchantment(PROTECTION, 2);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        chestplate.addUnsafeEnchantment(PROTECTION, 2);
        ItemStack pants = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        pants.addUnsafeEnchantment(PROTECTION, 2);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        boots.addUnsafeEnchantment(PROTECTION, 2);
        Player player = Bukkit.getPlayer((UUID)id);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(pants);
        player.getInventory().setBoots(boots);
        player.getInventory().setContents(menu.getContents());
    }

    public static Inventory vanillaPreview() {
        MenuMaker menu = new MenuMaker("\u00a7bKit \u00a75Vanilla", 6);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addUnsafeEnchantment(SHARPNESS, 3);
        menu.setItem(0, sword);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addUnsafeEnchantment(POWER, 5);
        menu.setItem(1, bow);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        menu.setItem(2, arrow);
        ItemStack health = API.potion(PotionType.INSTANT_HEAL, 2, true, false);
        menu.fillRange(18, 35, health);
        ItemStack speed = API.potion(PotionType.SPEED, 2, false, false);
        menu.fillCustom("3,21,30", speed);
        ItemStack fire = API.potion(PotionType.FIRE_RESISTANCE, 1, false, false);
        menu.fillCustom("4,22,31", fire);
        ItemStack strength = API.potion(PotionType.STRENGTH, 2, false, false);
        menu.fillCustom("5,23,32", strength);
        ItemStack regen = API.potion(PotionType.REGEN, 2, false, false);
        menu.fillCustom("6,24,33", regen);
        ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 16);
        menu.setItem(7, pearl);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(8, food);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        helmet.addUnsafeEnchantment(PROTECTION, 2);
        menu.setItem(45, helmet);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        chestplate.addUnsafeEnchantment(PROTECTION, 2);
        menu.setItem(46, chestplate);
        ItemStack pants = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        pants.addUnsafeEnchantment(PROTECTION, 2);
        menu.setItem(47, pants);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        boots.addUnsafeEnchantment(PROTECTION, 2);
        menu.setItem(48, boots);
        return menu.getMenu();
    }

    public static void opfactions(UUID id) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addUnsafeEnchantment(SHARPNESS, 7);
        sword.addUnsafeEnchantment(FIRE_ASPECT, 2);
        sword.addUnsafeEnchantment(UNBREAKING, 7);
        menu.setItem(0, sword);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addUnsafeEnchantment(POWER, 6);
        bow.addUnsafeEnchantment(PUNCH, 5);
        bow.addUnsafeEnchantment(FLAME, 1);
        bow.addUnsafeEnchantment(UNBREAKING, 7);
        menu.setItem(1, bow);
        ItemStack speed = API.potion(PotionType.SPEED, 2, false, false, 8);
        menu.setItem(2, speed);
        ItemStack strength = API.potion(PotionType.STRENGTH, 2, false, false, 8);
        menu.setItem(3, strength);
        ItemStack spsplash = API.potion(PotionType.SPEED, 2, true, false, 4);
        menu.setItem(4, spsplash);
        ItemStack stsplash = API.potion(PotionType.STRENGTH, 2, true, false, 4);
        menu.setItem(5, stsplash);
        ItemStack health = API.potion(PotionType.INSTANT_HEAL, 2, true, false, 16);
        menu.setItem(6, health);
        ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 8, (short) 1);
        menu.setItem(7, gapple);
        ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 16);
        menu.setItem(8, pearl);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 64);
        menu.setItem(9, apple);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(10, food);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        menu.setItem(11, arrow);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        helmet.addUnsafeEnchantment(PROTECTION, 6);
        helmet.addUnsafeEnchantment(UNBREAKING, 7);
        helmet.addUnsafeEnchantment(THORNS, 5);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        chestplate.addUnsafeEnchantment(PROTECTION, 6);
        chestplate.addUnsafeEnchantment(UNBREAKING, 7);
        chestplate.addUnsafeEnchantment(THORNS, 5);
        ItemStack pants = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        pants.addUnsafeEnchantment(PROTECTION, 6);
        pants.addUnsafeEnchantment(UNBREAKING, 7);
        pants.addUnsafeEnchantment(THORNS, 5);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        boots.addUnsafeEnchantment(PROTECTION, 6);
        boots.addUnsafeEnchantment(UNBREAKING, 7);
        boots.addUnsafeEnchantment(THORNS, 5);
        Player player = Bukkit.getPlayer((UUID)id);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(pants);
        player.getInventory().setBoots(boots);
        player.getInventory().setContents(menu.getContents());
    }

    public static Inventory opfactionsPreview() {
        MenuMaker menu = new MenuMaker("\u00a7bKit \u00a75OPFactions", 6);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addUnsafeEnchantment(SHARPNESS, 7);
        sword.addUnsafeEnchantment(FIRE_ASPECT, 2);
        sword.addUnsafeEnchantment(UNBREAKING, 7);
        menu.setItem(0, sword);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addUnsafeEnchantment(POWER, 6);
        bow.addUnsafeEnchantment(PUNCH, 5);
        bow.addUnsafeEnchantment(FLAME, 1);
        bow.addUnsafeEnchantment(UNBREAKING, 7);
        menu.setItem(1, bow);
        ItemStack speed = API.potion(PotionType.SPEED, 2, false, false, 8);
        menu.setItem(2, speed);
        ItemStack strength = API.potion(PotionType.STRENGTH, 2, false, false, 8);
        menu.setItem(3, strength);
        ItemStack spsplash = API.potion(PotionType.SPEED, 2, true, false, 4);
        menu.setItem(4, spsplash);
        ItemStack stsplash = API.potion(PotionType.STRENGTH, 2, true, false, 4);
        menu.setItem(5, stsplash);
        ItemStack health = API.potion(PotionType.INSTANT_HEAL, 2, true, false, 16);
        menu.setItem(6, health);
        ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 8, (short) 1);
        menu.setItem(7, gapple);
        ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 16);
        menu.setItem(8, pearl);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 64);
        menu.setItem(9, apple);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(10, food);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        menu.setItem(11, arrow);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        helmet.addUnsafeEnchantment(PROTECTION, 6);
        helmet.addUnsafeEnchantment(UNBREAKING, 7);
        helmet.addUnsafeEnchantment(THORNS, 5);
        menu.setItem(45, helmet);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        chestplate.addUnsafeEnchantment(PROTECTION, 6);
        chestplate.addUnsafeEnchantment(UNBREAKING, 7);
        chestplate.addUnsafeEnchantment(THORNS, 5);
        menu.setItem(46, chestplate);
        ItemStack pants = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        pants.addUnsafeEnchantment(PROTECTION, 6);
        pants.addUnsafeEnchantment(UNBREAKING, 7);
        pants.addUnsafeEnchantment(THORNS, 5);
        menu.setItem(47, pants);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        boots.addUnsafeEnchantment(PROTECTION, 6);
        boots.addUnsafeEnchantment(UNBREAKING, 7);
        boots.addUnsafeEnchantment(THORNS, 5);
        menu.setItem(48, boots);
        return menu.getMenu();
    }

    public static void archer(UUID id) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addUnsafeEnchantment(INFINITY, 1);
        menu.setItem(0, bow);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(1, food);
        ItemStack arrow = new ItemStack(Material.ARROW, 1);
        menu.setItem(2, arrow);
        ItemStack potion = API.potion(PotionType.SPEED, 2, true, false);
        menu.fillRange(3, 8, potion);
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
        helmet.addUnsafeEnchantment(PROTECTION, 1);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        chestplate.addUnsafeEnchantment(PROTECTION, 1);
        ItemStack pants = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        pants.addUnsafeEnchantment(PROTECTION, 1);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        boots.addUnsafeEnchantment(PROTECTION, 1);
        Player player = Bukkit.getPlayer((UUID)id);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(pants);
        player.getInventory().setBoots(boots);
        player.getInventory().setContents(menu.getContents());
        API.applyPotion(id, PotionEffectType.REGENERATION, Integer.MAX_VALUE, 0);
    }

    public static Inventory archerPreview() {
        MenuMaker menu = new MenuMaker("\u00a7bKit \u00a75Archer", 6);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addUnsafeEnchantment(INFINITY, 1);
        menu.setItem(0, bow);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(1, food);
        ItemStack arrow = new ItemStack(Material.ARROW, 1);
        menu.setItem(2, arrow);
        ItemStack potion = API.potion(PotionType.SPEED, 2, true, false);
        menu.fillRange(3, 8, potion);
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
        helmet.addUnsafeEnchantment(PROTECTION, 1);
        menu.setItem(45, helmet);
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        chestplate.addUnsafeEnchantment(PROTECTION, 1);
        menu.setItem(46, chestplate);
        ItemStack pants = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        pants.addUnsafeEnchantment(PROTECTION, 1);
        menu.setItem(47, pants);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        boots.addUnsafeEnchantment(PROTECTION, 1);
        menu.setItem(48, boots);
        return menu.getMenu();
    }

    public static void skywars(UUID id) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
        sword.addUnsafeEnchantment(SHARPNESS, 3);
        menu.setItem(0, sword);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        menu.setItem(1, bow);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 3);
        menu.setItem(2, apple);
        ItemStack strength = API.potion(PotionType.STRENGTH, 1, false, false);
        menu.setItem(3, strength);
        ItemStack speed = API.potion(PotionType.SPEED, 1, false, false);
        menu.setItem(4, speed);
        ItemStack regen = API.potion(PotionType.REGEN, 1, false, false);
        menu.setItem(5, regen);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        menu.setItem(8, arrow);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        ItemStack pants = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
        Player player = Bukkit.getPlayer((UUID)id);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(pants);
        player.getInventory().setBoots(boots);
        player.getInventory().setContents(menu.getContents());
    }

    public static Inventory skywarsPreview() {
        MenuMaker menu = new MenuMaker("\u00a7bKit \u00a75Skywars", 6);
        ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
        sword.addUnsafeEnchantment(SHARPNESS, 3);
        menu.setItem(0, sword);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        menu.setItem(1, bow);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 3);
        menu.setItem(2, apple);
        ItemStack strength = API.potion(PotionType.STRENGTH, 1, false, false);
        menu.setItem(3, strength);
        ItemStack speed = API.potion(PotionType.SPEED, 1, false, false);
        menu.setItem(4, speed);
        ItemStack regen = API.potion(PotionType.REGEN, 1, false, false);
        menu.setItem(5, regen);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        menu.setItem(8, arrow);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        menu.setItem(45, helmet);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        menu.setItem(46, chestplate);
        ItemStack pants = new ItemStack(Material.CHAINMAIL_LEGGINGS, 1);
        menu.setItem(47, pants);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
        menu.setItem(48, boots);
        return menu.getMenu();
    }

    public static void mcsg(UUID id) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
        menu.setItem(0, sword);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        menu.setItem(1, bow);
        ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
        menu.setItem(2, rod);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 1);
        menu.setItem(3, apple);
        ItemStack fns = new ItemStack(Material.FLINT_AND_STEEL, 1, (short) 63);
        menu.setItem(4, fns);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(7, food);
        ItemStack arrow = new ItemStack(Material.ARROW, 5);
        menu.setItem(8, arrow);
        ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.GOLD_CHESTPLATE, 1);
        ItemStack pants = new ItemStack(Material.IRON_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        Player player = Bukkit.getPlayer((UUID)id);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(pants);
        player.getInventory().setBoots(boots);
        player.getInventory().setContents(menu.getContents());
    }

    public static Inventory mcsgPreview() {
        MenuMaker menu = new MenuMaker("\u00a7bKit \u00a75MCSG", 6);
        ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
        menu.setItem(0, sword);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        menu.setItem(1, bow);
        ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
        menu.setItem(2, rod);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 1);
        menu.setItem(3, apple);
        ItemStack fns = new ItemStack(Material.FLINT_AND_STEEL, 1, (short) 63);
        menu.setItem(4, fns);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(7, food);
        ItemStack arrow = new ItemStack(Material.ARROW, 5);
        menu.setItem(8, arrow);
        ItemStack helmet = new ItemStack(Material.CHAINMAIL_HELMET, 1);
        menu.setItem(45, helmet);
        ItemStack chestplate = new ItemStack(Material.GOLD_CHESTPLATE, 1);
        menu.setItem(46, chestplate);
        ItemStack pants = new ItemStack(Material.IRON_LEGGINGS, 1);
        menu.setItem(47, pants);
        ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
        menu.setItem(48, boots);
        return menu.getMenu();
    }

    public static void uhc(UUID id) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
        sword.addUnsafeEnchantment(SHARPNESS, 2);
        menu.setItem(0, sword);
        ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
        menu.setItem(1, rod);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addUnsafeEnchantment(POWER, 1);
        menu.setItem(2, bow);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 8);
        menu.setItem(3, food);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 1);
        menu.fillRange(4, 7, apple);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        menu.setItem(8, arrow);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        helmet.addUnsafeEnchantment(PROTECTION, 1);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        chestplate.addUnsafeEnchantment(PROTECTION, 2);
        ItemStack pants = new ItemStack(Material.IRON_LEGGINGS, 1);
        pants.addUnsafeEnchantment(PROTECTION, 1);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        boots.addUnsafeEnchantment(PROTECTION, 2);
        Player player = Bukkit.getPlayer((UUID)id);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(pants);
        player.getInventory().setBoots(boots);
        player.getInventory().setContents(menu.getContents());
    }

    public static Inventory uhcPreview() {
        MenuMaker menu = new MenuMaker("\u00a7bKit \u00a75UHC", 6);
        ItemStack sword = new ItemStack(Material.IRON_SWORD, 1);
        sword.addUnsafeEnchantment(SHARPNESS, 2);
        menu.setItem(0, sword);
        ItemStack rod = new ItemStack(Material.FISHING_ROD, 1);
        menu.setItem(1, rod);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addUnsafeEnchantment(POWER, 1);
        menu.setItem(2, bow);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 8);
        menu.setItem(3, food);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 1);
        menu.fillRange(4, 7, apple);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        menu.setItem(8, arrow);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        helmet.addUnsafeEnchantment(PROTECTION, 1);
        menu.setItem(45, helmet);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        chestplate.addUnsafeEnchantment(PROTECTION, 2);
        menu.setItem(46, chestplate);
        ItemStack pants = new ItemStack(Material.IRON_LEGGINGS, 1);
        pants.addUnsafeEnchantment(PROTECTION, 1);
        menu.setItem(47, pants);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        boots.addUnsafeEnchantment(PROTECTION, 2);
        menu.setItem(48, boots);
        return menu.getMenu();
    }

    public static void builduhc(UUID id) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        pick.addUnsafeEnchantment(EFFICIENCY, 10);
        pick.addUnsafeEnchantment(SHARPNESS, 4);
        menu.setItem(0, pick);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 16, (short) 1);
        menu.setItem(1, apple);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(2, food);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        helmet.addUnsafeEnchantment(PROTECTION, 4);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        chestplate.addUnsafeEnchantment(PROTECTION, 4);
        ItemStack pants = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        pants.addUnsafeEnchantment(PROTECTION, 4);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        boots.addUnsafeEnchantment(PROTECTION, 4);
        Player player = Bukkit.getPlayer((UUID)id);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(pants);
        player.getInventory().setBoots(boots);
        player.getInventory().setContents(menu.getContents());
    }

    public static Inventory builduhcPreview() {
        MenuMaker menu = new MenuMaker("\u00a7bKit \u00a75BuildUHC", 6);
        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        pick.addUnsafeEnchantment(EFFICIENCY, 10);
        pick.addUnsafeEnchantment(SHARPNESS, 4);
        menu.setItem(0, pick);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 16, (short) 1);
        menu.setItem(1, apple);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(2, food);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        helmet.addUnsafeEnchantment(PROTECTION, 4);
        menu.setItem(45, helmet);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        chestplate.addUnsafeEnchantment(PROTECTION, 4);
        menu.setItem(46, chestplate);
        ItemStack pants = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        pants.addUnsafeEnchantment(PROTECTION, 4);
        menu.setItem(47, pants);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        boots.addUnsafeEnchantment(PROTECTION, 4);
        menu.setItem(48, boots);
        return menu.getMenu();
    }

    public static void ironpvp(UUID id) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        menu.setItem(0, sword);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        menu.setItem(1, bow);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(2, food);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        menu.setItem(3, arrow);
        ItemStack helmet = new ItemStack(Material.IRON_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        ItemStack pants = new ItemStack(Material.IRON_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
        Player player = Bukkit.getPlayer((UUID)id);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(pants);
        player.getInventory().setBoots(boots);
        player.getInventory().setContents(menu.getContents());
    }

    public static Inventory ironpvpPreview() {
        MenuMaker menu = new MenuMaker("\u00a7bKit \u00a75IronPvP", 6);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        menu.setItem(0, sword);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        menu.setItem(1, bow);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(2, food);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        menu.setItem(3, arrow);
        ItemStack helmet = new ItemStack(Material.IRON_HELMET, 1);
        menu.setItem(45, helmet);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        menu.setItem(46, chestplate);
        ItemStack pants = new ItemStack(Material.IRON_LEGGINGS, 1);
        menu.setItem(47, pants);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
        menu.setItem(48, boots);
        return menu.getMenu();
    }

    public static void goldpvp(UUID id) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
        menu.setItem(0, sword);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        menu.setItem(1, bow);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        menu.setItem(2, arrow);
        ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 8);
        menu.setItem(3, gapple);
        ItemStack helmet = new ItemStack(Material.GOLD_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.GOLD_CHESTPLATE, 1);
        ItemStack pants = new ItemStack(Material.GOLD_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.GOLD_BOOTS, 1);
        Player player = Bukkit.getPlayer((UUID)id);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(pants);
        player.getInventory().setBoots(boots);
        player.getInventory().setContents(menu.getContents());
    }

    public static Inventory goldpvpPreview() {
        MenuMaker menu = new MenuMaker("\u00a7bKit \u00a75GoldPvP", 6);
        ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
        menu.setItem(0, sword);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        menu.setItem(1, bow);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        menu.setItem(2, arrow);
        ItemStack gapple = new ItemStack(Material.GOLDEN_APPLE, 8);
        menu.setItem(3, gapple);
        ItemStack helmet = new ItemStack(Material.GOLD_HELMET, 1);
        menu.setItem(45, helmet);
        ItemStack chestplate = new ItemStack(Material.GOLD_CHESTPLATE, 1);
        menu.setItem(46, chestplate);
        ItemStack pants = new ItemStack(Material.GOLD_LEGGINGS, 1);
        menu.setItem(47, pants);
        ItemStack boots = new ItemStack(Material.GOLD_BOOTS, 1);
        menu.setItem(48, boots);
        return menu.getMenu();
    }

    public static void diamondpvp(UUID id) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        menu.setItem(0, sword);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        menu.setItem(1, bow);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        menu.setItem(2, arrow);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(3, food);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 16);
        menu.setItem(4, apple);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        ItemStack pants = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        Player player = Bukkit.getPlayer((UUID)id);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(pants);
        player.getInventory().setBoots(boots);
        player.getInventory().setContents(menu.getContents());
    }

    public static Inventory diamondpvpPreview() {
        MenuMaker menu = new MenuMaker("\u00a7bKit \u00a75DiamondPvP", 6);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        menu.setItem(0, sword);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        menu.setItem(1, bow);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        menu.setItem(2, arrow);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(3, food);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 16);
        menu.setItem(4, apple);
        ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET, 1);
        menu.setItem(45, helmet);
        ItemStack chestplate = new ItemStack(Material.DIAMOND_CHESTPLATE, 1);
        menu.setItem(46, chestplate);
        ItemStack pants = new ItemStack(Material.DIAMOND_LEGGINGS, 1);
        menu.setItem(47, pants);
        ItemStack boots = new ItemStack(Material.DIAMOND_BOOTS, 1);
        menu.setItem(48, boots);
        return menu.getMenu();
    }

    public static void factions(UUID id) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addUnsafeEnchantment(SHARPNESS, 2);
        menu.setItem(0, sword);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        menu.setItem(1, bow);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        menu.setItem(2, arrow);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(3, food);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 64);
        menu.setItem(4, apple);
        ItemStack helmet = new ItemStack(Material.IRON_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        ItemStack pants = new ItemStack(Material.IRON_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
        Player player = Bukkit.getPlayer((UUID)id);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(pants);
        player.getInventory().setBoots(boots);
        player.getInventory().setContents(menu.getContents());
    }

    public static Inventory factionsPreview() {
        MenuMaker menu = new MenuMaker("\u00a7bKit \u00a75Factions", 6);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD, 1);
        sword.addUnsafeEnchantment(SHARPNESS, 2);
        menu.setItem(0, sword);
        ItemStack bow = new ItemStack(Material.BOW, 1);
        menu.setItem(1, bow);
        ItemStack arrow = new ItemStack(Material.ARROW, 64);
        menu.setItem(2, arrow);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(3, food);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 64);
        menu.setItem(4, apple);
        ItemStack helmet = new ItemStack(Material.IRON_HELMET, 1);
        menu.setItem(45, helmet);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        menu.setItem(46, chestplate);
        ItemStack pants = new ItemStack(Material.IRON_LEGGINGS, 1);
        menu.setItem(47, pants);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
        menu.setItem(48, boots);
        return menu.getMenu();
    }

    public static void prison(UUID id) {
        MenuMaker menu = new MenuMaker("", 4);
        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        pick.addUnsafeEnchantment(EFFICIENCY, 1);
        menu.setItem(0, pick);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 32);
        menu.setItem(1, apple);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(2, food);
        ItemStack helmet = new ItemStack(Material.IRON_HELMET, 1);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        ItemStack pants = new ItemStack(Material.IRON_LEGGINGS, 1);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
        Player player = Bukkit.getPlayer((UUID)id);
        player.getInventory().setHelmet(helmet);
        player.getInventory().setChestplate(chestplate);
        player.getInventory().setLeggings(pants);
        player.getInventory().setBoots(boots);
        player.getInventory().setContents(menu.getContents());
    }

    public static Inventory prisonPreview() {
        MenuMaker menu = new MenuMaker("\u00a7bKit \u00a75Prison", 6);
        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE, 1);
        pick.addUnsafeEnchantment(EFFICIENCY, 1);
        menu.setItem(0, pick);
        ItemStack apple = new ItemStack(Material.GOLDEN_APPLE, 32);
        menu.setItem(1, apple);
        ItemStack food = new ItemStack(Material.COOKED_BEEF, 64);
        menu.setItem(2, food);
        ItemStack helmet = new ItemStack(Material.IRON_HELMET, 1);
        menu.setItem(45, helmet);
        ItemStack chestplate = new ItemStack(Material.IRON_CHESTPLATE, 1);
        menu.setItem(46, chestplate);
        ItemStack pants = new ItemStack(Material.IRON_LEGGINGS, 1);
        menu.setItem(47, pants);
        ItemStack boots = new ItemStack(Material.IRON_BOOTS, 1);
        menu.setItem(48, boots);
        return menu.getMenu();
    }
}


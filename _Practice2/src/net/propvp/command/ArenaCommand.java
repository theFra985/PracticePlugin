/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.World
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package net.propvp.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import net.propvp.Practice;
import net.propvp.game.arena.Arena;
import net.propvp.game.arena.ArenaManager;
import net.propvp.player.InventoryManager;
import net.propvp.util.Cuboid;
import net.propvp.util.CuboidManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class ArenaCommand
implements CommandExecutor {
    private Player player;
    private String[] args;

    /*
     * Exception decompiling
     */
    public boolean onCommand(CommandSender var1_1, Command var2_2, String var3_3, String[] var4_4) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:143)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:385)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:423)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:217)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:162)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:95)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:355)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:768)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:700)
        // org.benf.cfr.reader.Main.doJar(Main.java:134)
        // org.benf.cfr.reader.Main.main(Main.java:189)
        throw new IllegalStateException("Decompilation failed");
    }

    public void help() {
        this.player.sendMessage((Object)ChatColor.RED + " Arena Commands");
        this.player.sendMessage((Object)ChatColor.GRAY + "/arena create <name>");
        this.player.sendMessage((Object)ChatColor.GRAY + "/arena remove <name>");
        this.player.sendMessage((Object)ChatColor.GRAY + "/arena setspawn <name> <1/2>");
        this.player.sendMessage((Object)ChatColor.GRAY + "/arena setregion <name>");
        this.player.sendMessage((Object)ChatColor.GRAY + "/arena check <name>");
        this.player.sendMessage((Object)ChatColor.GRAY + "/arena wand");
        this.player.sendMessage((Object)ChatColor.GRAY + "/arena list");
    }

    public void create() {
        if (this.args.length == 1) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "You didn't specify a name for the arena.");
            return;
        }
        if (Practice.getBackend().getArenaManager().getArena(this.args[1]) != null) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "An arena already exists with that name.");
            return;
        }
        Arena arena = new Arena(this.args[1]);
        Practice.getBackend().getArenaManager().putArena(this.args[1], arena);
        Practice.getBackend().getArenaManager().saveAll();
        this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GRAY + "Created and saved the arena.");
        this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GRAY + "To finish the arena setup, use /practice setspawn <1/2> and /practice setregion where appropriate.");
    }

    public void remove() {
        if (this.args.length == 1) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "You didnt' specify which arena to remove.");
        }
        if (Practice.getBackend().getArenaManager().getArena(this.args[1]) == null) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "There is no arena with that name.");
            return;
        }
        Practice.getBackend().getArenaManager().removeArena(this.args[1]);
        Practice.getBackend().getArenaManager().saveAll();
        this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GRAY + "Removed the arena.");
    }

    public void setspawn() {
        if (this.args.length == 1) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "You didn't specify which arena to modify.");
            return;
        }
        if (Practice.getBackend().getArenaManager().getArena(this.args[1]) == null) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "There is no arena with that name.");
            return;
        }
        if (this.args.length == 2) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "You didn't specify which spawn point (1/2) to set.");
            return;
        }
        List<String> list = Arrays.asList("1", "2");
        if (!list.contains(this.args[2])) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "You entered an invalid spawn point. Choose from 1 or 2.");
            return;
        }
        Arena arena = Practice.getBackend().getArenaManager().getArena(this.args[1]);
        if (this.args[2].equals("1")) {
            arena.setSpawn1(this.player.getLocation());
        } else if (this.args[2].equals("2")) {
            arena.setSpawn2(this.player.getLocation());
        }
        Practice.getBackend().getArenaManager().saveAll();
        this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GRAY + "Set the spawn point and saved the arena.");
    }

    public void setregion() {
        if (this.args.length == 1) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "You didnt' specify which arena to modify.");
        }
        if (Practice.getBackend().getArenaManager().getArena(this.args[1]) == null) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "There is no arena with that name.");
            return;
        }
        if (!CuboidManager.bothPointsSet(this.player)) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "You have not set both locations using the region selector.");
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "Use '/arena wand' to get the wand.");
            return;
        }
        Arena arena = Practice.getBackend().getArenaManager().getArena(this.args[1]);
        arena.setCuboid(CuboidManager.getCuboid(this.player.getUniqueId()));
        arena.save();
        this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GRAY + "Set the region and saved the arena.");
    }

    public void check() {
        if (Practice.getBackend().getArenaManager().getArena(this.args[1]) == null) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GRAY + "That arena does not exist.");
            return;
        }
        Arena arena = Practice.getBackend().getArenaManager().getArena(this.args[1]);
        this.player.sendMessage((Object)ChatColor.RED + " ** Arena Check : " + arena.getName());
        this.player.sendMessage((Object)ChatColor.RED + " * Setup: " + (Object)(arena.isSetup() ? ChatColor.GREEN : ChatColor.RED) + arena.isSetup());
        this.player.sendMessage((Object)ChatColor.RED + " * Spawn1: " + (arena.getSpawn1() == null ? new StringBuilder().append((Object)ChatColor.RED).append("not set").toString() : new StringBuilder().append((Object)ChatColor.GREEN).append("set").toString()));
        this.player.sendMessage((Object)ChatColor.RED + " * Spawn2: " + (arena.getSpawn2() == null ? new StringBuilder().append((Object)ChatColor.RED).append("not set").toString() : new StringBuilder().append((Object)ChatColor.GREEN).append("set").toString()));
        this.player.sendMessage((Object)ChatColor.RED + " * Region: " + (arena.getRegion() == null ? new StringBuilder().append((Object)ChatColor.RED).append("not set").toString() : new StringBuilder().append((Object)ChatColor.GREEN).append("set").toString()));
        this.player.sendMessage((Object)ChatColor.RED + " * ClonedWorld: " + (arena.getClonedWorld() == null ? new StringBuilder().append((Object)ChatColor.RED).append("not set (restart)").toString() : new StringBuilder().append((Object)ChatColor.GREEN).append("set").toString()));
    }

    public void wand() {
        Practice.getBackend().getInventoryManager();
        this.player.getInventory().addItem(new ItemStack[]{InventoryManager.getRegionSelector()});
        this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GRAY + "Left-click for location #1 : Right-click for location #2");
    }

    public void list() {
        if (Practice.getBackend().getArenaManager().getArenas().isEmpty()) {
            this.player.sendMessage((Object)ChatColor.GOLD + "Arena(s): " + (Object)ChatColor.GRAY + "None");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((Object)ChatColor.GOLD).append("Arena(s): ");
        int n = 0;
        int n2 = Practice.getBackend().getArenaManager().getArenas().size();
        for (Map.Entry<String, Arena> entry : Practice.getBackend().getArenaManager().getArenas().entrySet()) {
            if (n == n2 - 1) {
                stringBuilder.append((Object)(entry.getValue().isSetup() ? ChatColor.GREEN : ChatColor.RED)).append(entry.getKey());
            } else {
                stringBuilder.append((Object)(entry.getValue().isSetup() ? ChatColor.GREEN : ChatColor.RED)).append(entry.getKey()).append(", ");
            }
            ++n;
        }
        this.player.sendMessage(stringBuilder.toString());
    }
}


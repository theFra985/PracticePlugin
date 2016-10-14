/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package net.propvp.command;

import java.util.HashMap;
import net.propvp.Practice;
import net.propvp.player.InventoryManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PracticeCommand
implements CommandExecutor {
    private Player player;

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
        this.player.sendMessage((Object)ChatColor.RED + " Practice Commands");
        this.player.sendMessage((Object)ChatColor.GRAY + "/practice spawn");
        this.player.sendMessage((Object)ChatColor.GRAY + "/practice setspawn");
        this.player.sendMessage((Object)ChatColor.GRAY + "/practice seteditor");
        this.player.sendMessage((Object)ChatColor.GRAY + "/practice handbook");
    }

    public void spawn() {
        if (Practice.getBackend().getSpawn() == null) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GRAY + "The spawn location has not been set.");
        } else {
            this.player.teleport(Practice.getBackend().getSpawn());
        }
    }

    public void setspawn() {
        Practice.getBackend().setSpawn(this.player.getLocation());
        this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GRAY + "Spawn location saved.");
    }

    public void seteditor() {
        Practice.getBackend().setEditor(this.player.getLocation());
        this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GRAY + "Editor location saved.");
    }

    public void handbook() {
        Practice.getBackend().getInventoryManager();
        this.player.getInventory().addItem(new ItemStack[]{InventoryManager.getHandBook()});
    }
}


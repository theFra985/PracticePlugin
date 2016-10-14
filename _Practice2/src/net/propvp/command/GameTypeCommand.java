/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 */
package net.propvp.command;

import java.util.Map;
import java.util.Set;
import net.propvp.Practice;
import net.propvp.game.GameManager;
import net.propvp.game.GameMode;
import net.propvp.gui.MenuManager;
import net.propvp.player.PlayerInv;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class GameTypeCommand
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
        this.player.sendMessage((Object)ChatColor.RED + " GameType Commands");
        this.player.sendMessage((Object)ChatColor.GRAY + "/gametype create (name)");
        this.player.sendMessage((Object)ChatColor.GRAY + "/gametype remove (name)");
        this.player.sendMessage((Object)ChatColor.GRAY + "/gametype loadinventory (name)");
        this.player.sendMessage((Object)ChatColor.GRAY + "/gametype setinventory (name)");
        this.player.sendMessage((Object)ChatColor.GRAY + "/gametype setvar (name) (variable) (value)");
        this.player.sendMessage((Object)ChatColor.GRAY + "/gametype variables");
        this.player.sendMessage((Object)ChatColor.GRAY + "/gametype list");
    }

    public void create() {
        if (this.args.length == 1) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "You didn't specify a name for the game-type.");
            return;
        }
        if (this.args[1].length() > 16) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "Game-type titles cannot be longer than 16 characters.");
            return;
        }
        if (this.args[1].length() < 3) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "Game-type titles must be atleast 3 characters.");
            return;
        }
        if (Practice.getBackend().getGameManager().getGameMode(this.args[1]) != null) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "That game-type already exists.");
            return;
        }
        GameMode gameMode = new GameMode(this.args[1]);
        Practice.getBackend().getGameManager().putGameMode(this.args[1], gameMode);
        gameMode.save();
        this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GRAY + "Created and saved the new game-type.");
    }

    public void remove() {
        if (this.args.length == 1) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "You didn't specify which game-type to delete.");
            return;
        }
        if (Practice.getBackend().getGameManager().getGameMode(this.args[1]) == null) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "That game-type does not exist.");
            return;
        }
        Practice.getBackend().getGameManager().removeGameMode(this.args[1]);
        Practice.getBackend().getGameManager().getGameMode(this.args[1]).remove();
        MenuManager.updateMenus();
        this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GRAY + "Removed the game-type.");
    }

    public void loadinventory() {
        if (this.args.length == 1) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "You didn't specify which game-type you want to modify.");
            return;
        }
        if (Practice.getBackend().getGameManager().getGameMode(this.args[1]) == null) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "That game-type does not exist.");
            return;
        }
        GameMode gameMode = Practice.getBackend().getGameManager().getGameMode(this.args[1]);
        if (gameMode.getInventory() != null) {
            if (gameMode.getInventory().getArmorContents() != null) {
                this.player.getInventory().setArmorContents(gameMode.getInventory().getArmorContents());
            }
            if (gameMode.getInventory().getContents() != null) {
                this.player.getInventory().setContents(gameMode.getInventory().getContents());
            }
        }
        this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GRAY + "Loaded the default inventory for the game-type.");
    }

    public void setinventory() {
        if (this.args.length == 1) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "You didn't specify which game-type you want to modify.");
            return;
        }
        if (Practice.getBackend().getGameManager().getGameMode(this.args[1]) == null) {
            this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.RED + "That game-type does not exist.");
            return;
        }
        GameMode gameMode = Practice.getBackend().getGameManager().getGameMode(this.args[1]);
        gameMode.setStartingInventory(PlayerInv.fromPlayerInventory(this.player.getInventory()));
        gameMode.save();
        this.player.sendMessage(String.valueOf(Practice.getPrefix()) + (Object)ChatColor.GRAY + "Updated and saved the game-type inventory.");
    }

    /*
     * Exception decompiling
     */
    public void setvar() {
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

    public void variables() {
        this.player.sendMessage((Object)ChatColor.RED + " Game-type Variables List");
        this.player.sendMessage((Object)ChatColor.GRAY + "displayname <text>");
        this.player.sendMessage((Object)ChatColor.GRAY + "display <material>");
        this.player.sendMessage((Object)ChatColor.GRAY + "editable <true/false>");
        this.player.sendMessage((Object)ChatColor.GRAY + "build <true/false>");
        this.player.sendMessage((Object)ChatColor.GRAY + "regen <true/false>");
        this.player.sendMessage((Object)ChatColor.GRAY + "hunger <true/false>");
        this.player.sendMessage((Object)ChatColor.GRAY + "hitdelay <number>");
    }

    public void list() {
        if (Practice.getBackend().getGameManager().getGameModes().isEmpty()) {
            this.player.sendMessage((Object)ChatColor.GOLD + "Game-type(s): " + (Object)ChatColor.GRAY + "None");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append((Object)ChatColor.GOLD).append("Game-type(s): ");
        int n = 0;
        int n2 = Practice.getBackend().getGameManager().getGameModes().size();
        for (String string : Practice.getBackend().getGameManager().getGameModes().keySet()) {
            if (n == n2 - 1) {
                stringBuilder.append((Object)ChatColor.GREEN).append(string);
                continue;
            }
            stringBuilder.append((Object)ChatColor.GREEN).append(string).append(", ");
        }
        this.player.sendMessage(stringBuilder.toString());
    }
}


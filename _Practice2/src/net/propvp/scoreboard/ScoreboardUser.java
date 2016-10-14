/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.propvp.scoreboard;

import net.propvp.timing.ManualTimer;
import org.bukkit.entity.Player;

public class ScoreboardUser {
    private ManualTimer enderpearlTimer = new ManualTimer(false);
    private ManualTimer combatTimer = new ManualTimer(false);
    private Player player;

    public ScoreboardUser(Player player) {
        this.player = player;
    }

    public Player asPlayer() {
        return this.player;
    }

    public ManualTimer getEnderpearlTimer() {
        return this.enderpearlTimer;
    }

    public ManualTimer getCombatTimer() {
        return this.combatTimer;
    }

    public boolean hasAnyActiveTimers() {
        if (!this.enderpearlTimer.isActive() && !this.combatTimer.isActive()) {
            return false;
        }
        return true;
    }
}


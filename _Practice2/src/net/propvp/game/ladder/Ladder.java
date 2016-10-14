/*
 * Decompiled with CFR 0_118.
 */
package net.propvp.game.ladder;

import java.util.UUID;
import net.propvp.game.GameMode;
import net.propvp.timing.ElapsedTimer;

public interface Ladder {
    public String getName();

    public GameMode getGame();

    public UUID getIdentifier();

    public void addObject(Object var1);

    public void removeObject(Object var1);

    public int getAmountInQueue();

    public int getAmountInMatch();

    public boolean isRanked();

    public ElapsedTimer getTimer(Object var1);
}


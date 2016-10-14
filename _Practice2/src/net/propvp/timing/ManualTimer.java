/*
 * Decompiled with CFR 0_118.
 */
package net.propvp.timing;

import net.propvp.timing.AbstractTimer;

public class ManualTimer
extends AbstractTimer {
    private long timerEnd;

    public ManualTimer(boolean bl) {
        this(bl, 0);
    }

    public ManualTimer(boolean bl, long l) {
        super(bl);
        this.timerEnd = l;
    }

    public void setTimerEnd(long l) {
        this.timerEnd = l;
    }

    public void reset() {
        this.timerEnd = System.currentTimeMillis();
    }

    @Override
    public long getTimerEnd() {
        return this.timerEnd;
    }
}


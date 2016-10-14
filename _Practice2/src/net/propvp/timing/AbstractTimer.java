/*
 * Decompiled with CFR 0_118.
 */
package net.propvp.timing;

import java.text.DecimalFormat;
import net.propvp.timing.CountdownTimer;

public abstract class AbstractTimer
implements CountdownTimer {
    private static DecimalFormat format = new DecimalFormat("#0.0");
    private final boolean prefferedFormat;

    protected AbstractTimer(boolean bl) {
        this.prefferedFormat = bl;
    }

    @Override
    public boolean isActive() {
        if (this.getTimerEnd() > System.currentTimeMillis()) {
            return true;
        }
        return false;
    }

    @Override
    public long getLeftTime() {
        return this.getTimerEnd() - System.currentTimeMillis();
    }

    @Override
    public abstract long getTimerEnd();

    public boolean getPrefferedFormat() {
        return this.prefferedFormat;
    }

    @Override
    public String toString() {
        return this.toString(this.getPrefferedFormat());
    }

    @Override
    public String toString(boolean bl) {
        long l = this.getLeftTime();
        long l2 = l / 1000;
        return bl ? (l2 >= 3600 ? String.format("%02d:%02d:%02d", l2 / 3600, l2 % 3600 / 60, l2 % 60) : String.format("%02d:%02d", l2 / 60, l2 % 60)) : String.valueOf(String.valueOf(String.valueOf(format.format((float)l / 1000.0f)))) + "s";
    }
}


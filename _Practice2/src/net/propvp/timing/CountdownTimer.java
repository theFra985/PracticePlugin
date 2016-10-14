/*
 * Decompiled with CFR 0_118.
 */
package net.propvp.timing;

public interface CountdownTimer {
    public boolean isActive();

    public long getLeftTime();

    public long getTimerEnd();

    public String toString();

    public String toString(boolean var1);
}


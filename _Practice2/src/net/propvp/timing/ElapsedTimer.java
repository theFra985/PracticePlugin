/*
 * Decompiled with CFR 0_118.
 */
package net.propvp.timing;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ElapsedTimer {
    long starts;

    public ElapsedTimer() {
        this.reset();
    }

    public static ElapsedTimer start() {
        return new ElapsedTimer();
    }

    public ElapsedTimer reset() {
        this.starts = System.nanoTime();
        return this;
    }

    public long time() {
        long l = System.nanoTime();
        return l - this.starts;
    }

    public int time(TimeUnit timeUnit) {
        return (int)timeUnit.convert(this.time(), TimeUnit.NANOSECONDS);
    }

    public String toHoursMinutesSeconds() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, this.time(TimeUnit.HOURS));
        calendar.set(12, this.time(TimeUnit.MINUTES));
        calendar.set(13, this.time(TimeUnit.SECONDS));
        return simpleDateFormat.format(calendar.getTime());
    }

    public String toMinutesSecondsMilliseconds() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss.S");
        Calendar calendar = Calendar.getInstance();
        calendar.set(11, this.time(TimeUnit.HOURS));
        calendar.set(12, this.time(TimeUnit.MINUTES));
        calendar.set(13, this.time(TimeUnit.SECONDS));
        calendar.set(14, this.time(TimeUnit.MILLISECONDS));
        return simpleDateFormat.format(calendar.getTime());
    }
}


/*
 * Decompiled with CFR 0_118.
 */
package net.propvp.util;

public class Elo {
    public static final double kFactor = 32.0;
    private int rating;

    public Elo() {
        this(0);
    }

    public Elo(int n) {
        this.rating = n;
    }

    public int getRating() {
        return this.rating;
    }

    public void setRating(double d) {
        this.rating = (int)d;
    }

    public double calculateExpectation(Elo elo) {
        return 1.0 / (1.0 + Math.pow(10.0, (elo.getRating() - this.getRating()) / 400));
    }

    public double newRatingWin(Elo elo) {
        return (double)this.rating + 32.0 * (1.0 - this.calculateExpectation(elo));
    }

    public double newRatingLoss(Elo elo) {
        return (double)this.rating + 32.0 * (0.0 - this.calculateExpectation(elo));
    }
}


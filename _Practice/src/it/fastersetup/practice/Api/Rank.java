/*
 * Decompiled with CFR 0_115.
 */
package it.fastersetup.practice.Api;

public enum Rank {
    DEFAULT,
    DONATOR,
    VIP,
    VIPPLUS,
    SUPPORTER,
    JUSTIN,
    FAMOUS,
    MOD,
    ADMIN,
    OWNER,
    YOUTUBE,
    TWITCH,
    DEV,
    BUILDER;
    Rank(){
    }
    private static /* synthetic */ int[] $SWITCH_TABLE$net$derpcast$pvpcoin$api$Rank;

    private Rank(String string2, int n2) {
    }

    public String toString() {
        switch (Rank.$SWITCH_TABLE$net$derpcast$pvpcoin$api$Rank()[this.ordinal()]) {
            case 2: {
                return "Donator";
            }
            case 3: {
                return "VIP";
            }
            case 4: {
                return "VIP+";
            }
            case 5: {
                return "Supporter";
            }
            case 6: {
                return "Justin";
            }
            case 8: {
                return "Moderator";
            }
            case 9: {
                return "Administrator";
            }
            case 10: {
                return "Owner";
            }
            case 11: {
                return "YouTube";
            }
            case 12: {
                return "Twitch";
            }
            case 13: {
                return "Developer";
            }
            case 14: {
                return "Builder";
            }
            case 7: {
                return "Famous";
            }
        }
        return "Default";
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Rank fromString(String index) {
        String string = index = index.toLowerCase();
        switch (string.hashCode()) {
            case -2004703995: {
                if (string.equals("moderator")) return MOD;
                return DEFAULT;
            }
            case -1281854725: {
                if (string.equals("famous")) return FAMOUS;
                return DEFAULT;
            }
            case -1148684527: {
                if (string.equals("justin")) return JUSTIN;
                return DEFAULT;
            }
            case -991745245: {
                if (string.equals("youtube")) return YOUTUBE;
                return DEFAULT;
            }
            case -860844077: {
                if (string.equals("twitch")) return TWITCH;
                return DEFAULT;
            }
            case -652229939: {
                if (string.equals("administrator")) return ADMIN;
                return DEFAULT;
            }
            case -80681014: {
                if (string.equals("developer")) return DEV;
                return DEFAULT;
            }
            case -19802948: {
                if (string.equals("supporter")) return SUPPORTER;
                return DEFAULT;
            }
            case 116765: {
                if (string.equals("vip")) return VIP;
                return DEFAULT;
            }
            case 3619758: {
                if (string.equals("vip+")) return VIPPLUS;
                return DEFAULT;
            }
            case 106164915: {
                if (string.equals("owner")) return OWNER;
                return DEFAULT;
            }
            case 230944667: {
                if (string.equals("builder")) return BUILDER;
                return DEFAULT;
            }
            case 1838482713: {
                if (!string.equals("donator")) return DEFAULT;
                return DONATOR;
            }
        }
        return DEFAULT;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$net$derpcast$pvpcoin$api$Rank() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$net$derpcast$pvpcoin$api$Rank;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[Rank.values().length];
        try {
            arrn[Rank.ADMIN.ordinal()] = 9;
        }
        catch (NoSuchFieldError v1) {}
        try {
            arrn[Rank.BUILDER.ordinal()] = 14;
        }
        catch (NoSuchFieldError v2) {}
        try {
            arrn[Rank.DEFAULT.ordinal()] = 1;
        }
        catch (NoSuchFieldError v3) {}
        try {
            arrn[Rank.DEV.ordinal()] = 13;
        }
        catch (NoSuchFieldError v4) {}
        try {
            arrn[Rank.DONATOR.ordinal()] = 2;
        }
        catch (NoSuchFieldError v5) {}
        try {
            arrn[Rank.FAMOUS.ordinal()] = 7;
        }
        catch (NoSuchFieldError v6) {}
        try {
            arrn[Rank.JUSTIN.ordinal()] = 6;
        }
        catch (NoSuchFieldError v7) {}
        try {
            arrn[Rank.MOD.ordinal()] = 8;
        }
        catch (NoSuchFieldError v8) {}
        try {
            arrn[Rank.OWNER.ordinal()] = 10;
        }
        catch (NoSuchFieldError v9) {}
        try {
            arrn[Rank.SUPPORTER.ordinal()] = 5;
        }
        catch (NoSuchFieldError v10) {}
        try {
            arrn[Rank.TWITCH.ordinal()] = 12;
        }
        catch (NoSuchFieldError v11) {}
        try {
            arrn[Rank.VIP.ordinal()] = 3;
        }
        catch (NoSuchFieldError v12) {}
        try {
            arrn[Rank.VIPPLUS.ordinal()] = 4;
        }
        catch (NoSuchFieldError v13) {}
        try {
            arrn[Rank.YOUTUBE.ordinal()] = 11;
        }
        catch (NoSuchFieldError v14) {}
        $SWITCH_TABLE$net$derpcast$pvpcoin$api$Rank = arrn;
        return $SWITCH_TABLE$net$derpcast$pvpcoin$api$Rank;
    }
}


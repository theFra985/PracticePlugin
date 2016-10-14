/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package it.fastersetup.practicehook.Api;

import java.util.regex.Pattern;
import org.bukkit.ChatColor;

public class Settings {
    private boolean duel = true;
    private boolean msg = true;
    private boolean party = true;
    private boolean players = true;
    private boolean chat = true;
    private ChatColor name = ChatColor.WHITE;
    private ChatColor chatcolor = ChatColor.GRAY;
    private ChatColor format = ChatColor.RESET;
    private static /* synthetic */ int[] $SWITCH_TABLE$org$bukkit$ChatColor;

    public Settings() {
    }

    public Settings(boolean duel, boolean msg, boolean party, boolean players, boolean chat, ChatColor name, ChatColor chatcolor, ChatColor format) {
        this.duel = duel;
        this.msg = msg;
        this.party = party;
        this.players = players;
        this.chat = chat;
        this.name = name;
        this.chatcolor = chatcolor;
        this.format = format;
    }

    public boolean duelEnabled() {
        return this.duel;
    }

    public void setDuelEnabled(boolean d) {
        this.duel = d;
    }

    public void toggleDuel() {
        this.duel = !this.duel;
    }

    public boolean msgEnabled() {
        return this.msg;
    }

    public void setMsgEnabled(boolean m) {
        this.msg = m;
    }

    public void toggleMsg() {
        this.msg = !this.msg;
    }

    public boolean partyEnabled() {
        return this.party;
    }

    public void setPartyEnabled(boolean p) {
        this.party = p;
    }

    public void toggleParty() {
        this.party = !this.party;
    }

    public boolean playersVisible() {
        return this.players;
    }

    public void setPlayersVisible(boolean v) {
        this.players = v;
    }

    public void togglePlayers() {
        this.players = !this.players;
    }

    public boolean chatEnabled() {
        return this.chat;
    }

    public void setChatEnabled(boolean c) {
        this.chat = c;
    }

    public void toggleChat() {
        this.chat = !this.chat;
    }

    public String nameColor() {
        return this.name.toString();
    }

    public ChatColor nameColorRaw() {
        return this.name;
    }

    public void setNameColor(ChatColor n) {
        this.name = n;
    }

    public String chatColor() {
        return this.chatcolor.toString();
    }

    public ChatColor chatColorRaw() {
        return this.chatcolor;
    }

    public void setChatColor(ChatColor c) {
        this.chatcolor = c;
    }

    public String chatFormat() {
        return this.format.toString();
    }

    public ChatColor chatFormatRaw() {
        return this.format;
    }

    public void setChatFormat(ChatColor f) {
        this.format = f;
    }

    public String toString() {
        return String.valueOf(String.valueOf(this.duel)) + ";" + String.valueOf(this.msg) + ";" + String.valueOf(this.party) + ";" + String.valueOf(this.players) + ";" + String.valueOf(this.chat) + ";" + String.valueOf(this.name.getChar()) + ";" + String.valueOf(this.chatcolor.getChar()) + ";" + String.valueOf(this.format.getChar());
    }

    public static Settings fromString(String input) {
        String[] p = input.split(Pattern.quote(";"));
        if (p.length == 8) {
            return new Settings(Boolean.parseBoolean(p[0]), Boolean.parseBoolean(p[1]), Boolean.parseBoolean(p[2]), Boolean.parseBoolean(p[3]), Boolean.parseBoolean(p[4]), ChatColor.getByChar((String)p[5]), ChatColor.getByChar((String)p[6]), ChatColor.getByChar((String)p[7]));
        }
        return new Settings();
    }

    public static short colorToShort(ChatColor c) {
        switch (Settings.$SWITCH_TABLE$org$bukkit$ChatColor()[c.ordinal()]) {
            case 5: {
                return 14;
            }
            case 13: {
                return 14;
            }
            case 14: {
                return 2;
            }
            case 12: {
                return 3;
            }
            case 11: {
                return 5;
            }
            case 15: {
                return 4;
            }
            case 6: {
                return 10;
            }
            case 4: {
                return 9;
            }
            case 3: {
                return 13;
            }
            case 7: {
                return 1;
            }
            case 16: {
                return 0;
            }
            case 8: {
                return 8;
            }
            case 2: {
                return 11;
            }
            case 9: {
                return 7;
            }
            case 1: {
                return 15;
            }
        }
        return 0;
    }

    public static short formatToShort(ChatColor c) {
        switch (Settings.$SWITCH_TABLE$org$bukkit$ChatColor()[c.ordinal()]) {
            case 22: {
                return 0;
            }
            case 18: {
                return 4;
            }
            case 21: {
                return 1;
            }
        }
        return 0;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$org$bukkit$ChatColor() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$org$bukkit$ChatColor;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[ChatColor.values().length];
        try {
            arrn[ChatColor.AQUA.ordinal()] = 12;
        }
        catch (NoSuchFieldError v1) {}
        try {
            arrn[ChatColor.BLACK.ordinal()] = 1;
        }
        catch (NoSuchFieldError v2) {}
        try {
            arrn[ChatColor.BLUE.ordinal()] = 10;
        }
        catch (NoSuchFieldError v3) {}
        try {
            arrn[ChatColor.BOLD.ordinal()] = 18;
        }
        catch (NoSuchFieldError v4) {}
        try {
            arrn[ChatColor.DARK_AQUA.ordinal()] = 4;
        }
        catch (NoSuchFieldError v5) {}
        try {
            arrn[ChatColor.DARK_BLUE.ordinal()] = 2;
        }
        catch (NoSuchFieldError v6) {}
        try {
            arrn[ChatColor.DARK_GRAY.ordinal()] = 9;
        }
        catch (NoSuchFieldError v7) {}
        try {
            arrn[ChatColor.DARK_GREEN.ordinal()] = 3;
        }
        catch (NoSuchFieldError v8) {}
        try {
            arrn[ChatColor.DARK_PURPLE.ordinal()] = 6;
        }
        catch (NoSuchFieldError v9) {}
        try {
            arrn[ChatColor.DARK_RED.ordinal()] = 5;
        }
        catch (NoSuchFieldError v10) {}
        try {
            arrn[ChatColor.GOLD.ordinal()] = 7;
        }
        catch (NoSuchFieldError v11) {}
        try {
            arrn[ChatColor.GRAY.ordinal()] = 8;
        }
        catch (NoSuchFieldError v12) {}
        try {
            arrn[ChatColor.GREEN.ordinal()] = 11;
        }
        catch (NoSuchFieldError v13) {}
        try {
            arrn[ChatColor.ITALIC.ordinal()] = 21;
        }
        catch (NoSuchFieldError v14) {}
        try {
            arrn[ChatColor.LIGHT_PURPLE.ordinal()] = 14;
        }
        catch (NoSuchFieldError v15) {}
        try {
            arrn[ChatColor.MAGIC.ordinal()] = 17;
        }
        catch (NoSuchFieldError v16) {}
        try {
            arrn[ChatColor.RED.ordinal()] = 13;
        }
        catch (NoSuchFieldError v17) {}
        try {
            arrn[ChatColor.RESET.ordinal()] = 22;
        }
        catch (NoSuchFieldError v18) {}
        try {
            arrn[ChatColor.STRIKETHROUGH.ordinal()] = 19;
        }
        catch (NoSuchFieldError v19) {}
        try {
            arrn[ChatColor.UNDERLINE.ordinal()] = 20;
        }
        catch (NoSuchFieldError v20) {}
        try {
            arrn[ChatColor.WHITE.ordinal()] = 16;
        }
        catch (NoSuchFieldError v21) {}
        try {
            arrn[ChatColor.YELLOW.ordinal()] = 15;
        }
        catch (NoSuchFieldError v22) {}
        $SWITCH_TABLE$org$bukkit$ChatColor = arrn;
        return $SWITCH_TABLE$org$bukkit$ChatColor;
    }
}


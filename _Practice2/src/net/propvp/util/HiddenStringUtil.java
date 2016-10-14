/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 */
package net.propvp.util;

import java.nio.charset.Charset;
import org.bukkit.ChatColor;

public class HiddenStringUtil {
    private static final String SEQUENCE_HEADER = (Object)ChatColor.RESET + (Object)ChatColor.UNDERLINE + (Object)ChatColor.RESET;
    private static final String SEQUENCE_FOOTER = (Object)ChatColor.RESET + (Object)ChatColor.ITALIC + (Object)ChatColor.RESET;

    public static String encodeString(String string) {
        return HiddenStringUtil.quote(HiddenStringUtil.stringToColors(string));
    }

    public static boolean hasHiddenString(String string) {
        if (string == null) {
            return false;
        }
        if (string.indexOf(SEQUENCE_HEADER) > -1 && string.indexOf(SEQUENCE_FOOTER) > -1) {
            return true;
        }
        return false;
    }

    public static String extractHiddenString(String string) {
        return HiddenStringUtil.colorsToString(HiddenStringUtil.extract(string));
    }

    public static String replaceHiddenString(String string, String string2) {
        if (string == null) {
            return null;
        }
        int n = string.indexOf(SEQUENCE_HEADER);
        int n2 = string.indexOf(SEQUENCE_FOOTER);
        if (n < 0 || n2 < 0) {
            return null;
        }
        return String.valueOf(string.substring(0, n + SEQUENCE_HEADER.length())) + HiddenStringUtil.stringToColors(string2) + string.substring(n2, string.length());
    }

    private static String quote(String string) {
        if (string == null) {
            return null;
        }
        return String.valueOf(SEQUENCE_HEADER) + string + SEQUENCE_FOOTER;
    }

    private static String extract(String string) {
        if (string == null) {
            return null;
        }
        int n = string.indexOf(SEQUENCE_HEADER);
        int n2 = string.indexOf(SEQUENCE_FOOTER);
        if (n < 0 || n2 < 0) {
            return null;
        }
        return string.substring(n + SEQUENCE_HEADER.length(), n2);
    }

    private static String stringToColors(String string) {
        if (string == null) {
            return null;
        }
        byte[] arrby = string.getBytes(Charset.forName("UTF-8"));
        char[] arrc = new char[arrby.length * 4];
        int n = 0;
        while (n < arrby.length) {
            char[] arrc2 = HiddenStringUtil.byteToHex(arrby[n]);
            arrc[n * 4] = 167;
            arrc[n * 4 + 1] = arrc2[0];
            arrc[n * 4 + 2] = 167;
            arrc[n * 4 + 3] = arrc2[1];
            ++n;
        }
        return new String(arrc);
    }

    private static String colorsToString(String string) {
        if (string == null) {
            return null;
        }
        if ((string = string.toLowerCase().replace("\u00a7", "")).length() % 2 != 0) {
            string = string.substring(0, string.length() / 2 * 2);
        }
        char[] arrc = string.toCharArray();
        byte[] arrby = new byte[arrc.length / 2];
        int n = 0;
        while (n < arrc.length) {
            arrby[n / 2] = HiddenStringUtil.hexToByte(arrc[n], arrc[n + 1]);
            n += 2;
        }
        return new String(arrby, Charset.forName("UTF-8"));
    }

    private static int hexToUnsignedInt(char c) {
        if (c >= '0' && c <= '9') {
            return c - 48;
        }
        if (c >= 'a' && c <= 'f') {
            return c - 87;
        }
        throw new IllegalArgumentException("Invalid hex char: out of range");
    }

    private static char unsignedIntToHex(int n) {
        if (n >= 0 && n <= 9) {
            return (char)(n + 48);
        }
        if (n >= 10 && n <= 15) {
            return (char)(n + 87);
        }
        throw new IllegalArgumentException("Invalid hex int: out of range");
    }

    private static byte hexToByte(char c, char c2) {
        return (byte)((HiddenStringUtil.hexToUnsignedInt(c) << 4 | HiddenStringUtil.hexToUnsignedInt(c2)) + -128);
    }

    private static char[] byteToHex(byte by) {
        int n = by - -128;
        return new char[]{HiddenStringUtil.unsignedIntToHex(n >> 4 & 15), HiddenStringUtil.unsignedIntToHex(n & 15)};
    }
}


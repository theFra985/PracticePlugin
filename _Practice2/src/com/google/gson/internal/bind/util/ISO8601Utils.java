/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson.internal.bind.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class ISO8601Utils {
    private static final String UTC_ID = "UTC";
    private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");

    public static String format(Date date) {
        return ISO8601Utils.format(date, false, TIMEZONE_UTC);
    }

    public static String format(Date date, boolean bl) {
        return ISO8601Utils.format(date, bl, TIMEZONE_UTC);
    }

    public static String format(Date date, boolean bl, TimeZone timeZone) {
        int n;
        GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone, Locale.US);
        gregorianCalendar.setTime(date);
        int n2 = "yyyy-MM-ddThh:mm:ss".length();
        n2 += bl ? ".sss".length() : 0;
        StringBuilder stringBuilder = new StringBuilder(n2 += timeZone.getRawOffset() == 0 ? "Z".length() : "+hh:mm".length());
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(1), "yyyy".length());
        stringBuilder.append('-');
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(2) + 1, "MM".length());
        stringBuilder.append('-');
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(5), "dd".length());
        stringBuilder.append('T');
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(11), "hh".length());
        stringBuilder.append(':');
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(12), "mm".length());
        stringBuilder.append(':');
        ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(13), "ss".length());
        if (bl) {
            stringBuilder.append('.');
            ISO8601Utils.padInt(stringBuilder, gregorianCalendar.get(14), "sss".length());
        }
        if ((n = timeZone.getOffset(gregorianCalendar.getTimeInMillis())) != 0) {
            int n3 = Math.abs(n / 60000 / 60);
            int n4 = Math.abs(n / 60000 % 60);
            stringBuilder.append(n < 0 ? '-' : '+');
            ISO8601Utils.padInt(stringBuilder, n3, "hh".length());
            stringBuilder.append(':');
            ISO8601Utils.padInt(stringBuilder, n4, "mm".length());
        } else {
            stringBuilder.append('Z');
        }
        return stringBuilder.toString();
    }

    public static Date parse(String string, ParsePosition parsePosition) {
        Throwable throwable = null;
        try {
            int n;
            int n2 = parsePosition.getIndex();
            int n3 = ISO8601Utils.parseInt(string, n2, n2 += 4);
            if (ISO8601Utils.checkOffset(string, n2, '-')) {
                ++n2;
            }
            int n4 = ISO8601Utils.parseInt(string, n2, n2 += 2);
            if (ISO8601Utils.checkOffset(string, n2, '-')) {
                ++n2;
            }
            int n5 = ISO8601Utils.parseInt(string, n2, n2 += 2);
            int n6 = 0;
            int n7 = 0;
            int n8 = 0;
            int n9 = 0;
            boolean bl = ISO8601Utils.checkOffset(string, n2, 'T');
            if (!bl && string.length() <= n2) {
                GregorianCalendar gregorianCalendar = new GregorianCalendar(n3, n4 - 1, n5);
                parsePosition.setIndex(n2);
                return gregorianCalendar.getTime();
            }
            if (bl) {
                char c;
                n6 = ISO8601Utils.parseInt(string, ++n2, n2 += 2);
                if (ISO8601Utils.checkOffset(string, n2, ':')) {
                    ++n2;
                }
                n7 = ISO8601Utils.parseInt(string, n2, n2 += 2);
                if (ISO8601Utils.checkOffset(string, n2, ':')) {
                    ++n2;
                }
                if (string.length() > n2 && (c = string.charAt(n2)) != 'Z' && c != '+' && c != '-') {
                    int n10 = n2;
                    n8 = ISO8601Utils.parseInt(string, n10, n2 += 2);
                    if (n8 > 59 && n8 < 63) {
                        n8 = 59;
                    }
                    if (ISO8601Utils.checkOffset(string, n2, '.')) {
                        n = ISO8601Utils.indexOfNonDigit(string, ++n2 + 1);
                        int n11 = Math.min(n, n2 + 3);
                        int n12 = ISO8601Utils.parseInt(string, n2, n11);
                        switch (n11 - n2) {
                            case 2: {
                                n9 = n12 * 10;
                                break;
                            }
                            case 1: {
                                n9 = n12 * 100;
                                break;
                            }
                            default: {
                                n9 = n12;
                            }
                        }
                        n2 = n;
                    }
                }
            }
            if (string.length() <= n2) {
                throw new IllegalArgumentException("No time zone indicator");
            }
            TimeZone timeZone = null;
            n = string.charAt(n2);
            if (n == 90) {
                timeZone = TIMEZONE_UTC;
                ++n2;
            } else if (n == 43 || n == 45) {
                String string2 = string.substring(n2);
                string2 = string2.length() >= 5 ? string2 : string2 + "00";
                n2 += string2.length();
                if ("+0000".equals(string2) || "+00:00".equals(string2)) {
                    timeZone = TIMEZONE_UTC;
                } else {
                    String string3;
                    String string4 = "GMT" + string2;
                    timeZone = TimeZone.getTimeZone(string4);
                    String string5 = timeZone.getID();
                    if (!string5.equals(string4) && !(string3 = string5.replace(":", "")).equals(string4)) {
                        throw new IndexOutOfBoundsException("Mismatching time zone indicator: " + string4 + " given, resolves to " + timeZone.getID());
                    }
                }
            } else {
                throw new IndexOutOfBoundsException("Invalid time zone indicator '" + (char)n + "'");
            }
            GregorianCalendar gregorianCalendar = new GregorianCalendar(timeZone);
            gregorianCalendar.setLenient(false);
            gregorianCalendar.set(1, n3);
            gregorianCalendar.set(2, n4 - 1);
            gregorianCalendar.set(5, n5);
            gregorianCalendar.set(11, n6);
            gregorianCalendar.set(12, n7);
            gregorianCalendar.set(13, n8);
            gregorianCalendar.set(14, n9);
            parsePosition.setIndex(n2);
            return gregorianCalendar.getTime();
        }
        catch (IndexOutOfBoundsException var3_4) {
            throwable = var3_4;
        }
        catch (NumberFormatException var3_5) {
            throwable = var3_5;
        }
        catch (IllegalArgumentException var3_6) {
            throwable = var3_6;
        }
        String string6 = string == null ? null : "" + '\"' + string + "'";
        String string7 = throwable.getMessage();
        if (string7 == null || string7.isEmpty()) {
            string7 = "(" + throwable.getClass().getName() + ")";
        }
        ParseException parseException = new ParseException("Failed to parse date [" + string6 + "]: " + string7, parsePosition.getIndex());
        parseException.initCause(throwable);
        throw parseException;
    }

    private static boolean checkOffset(String string, int n, char c) {
        return n < string.length() && string.charAt(n) == c;
    }

    private static int parseInt(String string, int n, int n2) {
        int n3;
        if (n < 0 || n2 > string.length() || n > n2) {
            throw new NumberFormatException(string);
        }
        int n4 = n;
        int n5 = 0;
        if (n4 < n2) {
            if ((n3 = Character.digit(string.charAt(n4++), 10)) < 0) {
                throw new NumberFormatException("Invalid number: " + string.substring(n, n2));
            }
            n5 = - n3;
        }
        while (n4 < n2) {
            if ((n3 = Character.digit(string.charAt(n4++), 10)) < 0) {
                throw new NumberFormatException("Invalid number: " + string.substring(n, n2));
            }
            n5 *= 10;
            n5 -= n3;
        }
        return - n5;
    }

    private static void padInt(StringBuilder stringBuilder, int n, int n2) {
        String string = Integer.toString(n);
        for (int i = n2 - string.length(); i > 0; --i) {
            stringBuilder.append('0');
        }
        stringBuilder.append(string);
    }

    private static int indexOfNonDigit(String string, int n) {
        for (int i = n; i < string.length(); ++i) {
            char c = string.charAt(i);
            if (c >= '0' && c <= '9') continue;
            return i;
        }
        return string.length();
    }
}


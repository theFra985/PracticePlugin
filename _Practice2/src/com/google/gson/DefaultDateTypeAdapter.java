/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.util.ISO8601Utils;
import java.lang.reflect.Type;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Locale;

final class DefaultDateTypeAdapter
implements JsonSerializer<java.util.Date>,
JsonDeserializer<java.util.Date> {
    private final DateFormat enUsFormat;
    private final DateFormat localFormat;

    DefaultDateTypeAdapter() {
        this(DateFormat.getDateTimeInstance(2, 2, Locale.US), DateFormat.getDateTimeInstance(2, 2));
    }

    DefaultDateTypeAdapter(String string) {
        this(new SimpleDateFormat(string, Locale.US), new SimpleDateFormat(string));
    }

    DefaultDateTypeAdapter(int n) {
        this(DateFormat.getDateInstance(n, Locale.US), DateFormat.getDateInstance(n));
    }

    public DefaultDateTypeAdapter(int n, int n2) {
        this(DateFormat.getDateTimeInstance(n, n2, Locale.US), DateFormat.getDateTimeInstance(n, n2));
    }

    DefaultDateTypeAdapter(DateFormat dateFormat, DateFormat dateFormat2) {
        this.enUsFormat = dateFormat;
        this.localFormat = dateFormat2;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public JsonElement serialize(java.util.Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        DateFormat dateFormat = this.localFormat;
        synchronized (dateFormat) {
            String string = this.enUsFormat.format(date);
            return new JsonPrimitive(string);
        }
    }

    @Override
    public java.util.Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        if (!(jsonElement instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }
        java.util.Date date = this.deserializeToDate(jsonElement);
        if (type == java.util.Date.class) {
            return date;
        }
        if (type == Timestamp.class) {
            return new Timestamp(date.getTime());
        }
        if (type == Date.class) {
            return new Date(date.getTime());
        }
        throw new IllegalArgumentException(this.getClass() + " cannot deserialize to " + type);
    }

    private java.util.Date deserializeToDate(JsonElement jsonElement) {
        DateFormat dateFormat = this.localFormat;
        synchronized (dateFormat) {
            try {
                return this.localFormat.parse(jsonElement.getAsString());
            }
            catch (ParseException var3_3) {
                try {
                    return this.enUsFormat.parse(jsonElement.getAsString());
                }
                catch (ParseException var3_4) {
                    try {
                        return ISO8601Utils.parse(jsonElement.getAsString(), new ParsePosition(0));
                    }
                    catch (ParseException var3_5) {
                        throw new JsonSyntaxException(jsonElement.getAsString(), var3_5);
                    }
                }
            }
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DefaultDateTypeAdapter.class.getSimpleName());
        stringBuilder.append('(').append(this.localFormat.getClass().getSimpleName()).append(')');
        return stringBuilder.toString();
    }
}


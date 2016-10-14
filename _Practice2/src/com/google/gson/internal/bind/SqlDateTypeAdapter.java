/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class SqlDateTypeAdapter
extends TypeAdapter<java.sql.Date> {
    public static final TypeAdapterFactory FACTORY = new TypeAdapterFactory(){

        @Override
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
            return typeToken.getRawType() == java.sql.Date.class ? new SqlDateTypeAdapter() : null;
        }
    };
    private final DateFormat format = new SimpleDateFormat("MMM d, yyyy");

    @Override
    public synchronized java.sql.Date read(JsonReader jsonReader) {
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }
        try {
            long l = this.format.parse(jsonReader.nextString()).getTime();
            return new java.sql.Date(l);
        }
        catch (ParseException var2_3) {
            throw new JsonSyntaxException(var2_3);
        }
    }

    @Override
    public synchronized void write(JsonWriter jsonWriter, java.sql.Date date) {
        jsonWriter.value(date == null ? null : this.format.format(date));
    }

}


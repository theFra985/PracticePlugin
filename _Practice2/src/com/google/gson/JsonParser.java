/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public final class JsonParser {
    public JsonElement parse(String string) {
        return this.parse(new StringReader(string));
    }

    public JsonElement parse(Reader reader) {
        try {
            JsonReader jsonReader = new JsonReader(reader);
            JsonElement jsonElement = this.parse(jsonReader);
            if (!jsonElement.isJsonNull() && jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonSyntaxException("Did not consume the entire document.");
            }
            return jsonElement;
        }
        catch (MalformedJsonException var2_3) {
            throw new JsonSyntaxException(var2_3);
        }
        catch (IOException var2_4) {
            throw new JsonIOException(var2_4);
        }
        catch (NumberFormatException var2_5) {
            throw new JsonSyntaxException(var2_5);
        }
    }

    public JsonElement parse(JsonReader jsonReader) {
        boolean bl = jsonReader.isLenient();
        jsonReader.setLenient(true);
        try {
            JsonElement jsonElement = Streams.parse(jsonReader);
            return jsonElement;
        }
        catch (StackOverflowError var3_4) {
            throw new JsonParseException("Failed parsing JSON source: " + jsonReader + " to Json", var3_4);
        }
        catch (OutOfMemoryError var3_5) {
            throw new JsonParseException("Failed parsing JSON source: " + jsonReader + " to Json", var3_5);
        }
        finally {
            jsonReader.setLenient(bl);
        }
    }
}


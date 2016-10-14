/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson.internal.bind;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class JsonTreeReader
extends JsonReader {
    private static final Reader UNREADABLE_READER = new Reader(){

        @Override
        public int read(char[] arrc, int n, int n2) {
            throw new AssertionError();
        }

        @Override
        public void close() {
            throw new AssertionError();
        }
    };
    private static final Object SENTINEL_CLOSED = new Object();
    private final List<Object> stack = new ArrayList<Object>();

    public JsonTreeReader(JsonElement jsonElement) {
        super(UNREADABLE_READER);
        this.stack.add(jsonElement);
    }

    @Override
    public void beginArray() {
        this.expect(JsonToken.BEGIN_ARRAY);
        JsonArray jsonArray = (JsonArray)this.peekStack();
        this.stack.add(jsonArray.iterator());
    }

    @Override
    public void endArray() {
        this.expect(JsonToken.END_ARRAY);
        this.popStack();
        this.popStack();
    }

    @Override
    public void beginObject() {
        this.expect(JsonToken.BEGIN_OBJECT);
        JsonObject jsonObject = (JsonObject)this.peekStack();
        this.stack.add(jsonObject.entrySet().iterator());
    }

    @Override
    public void endObject() {
        this.expect(JsonToken.END_OBJECT);
        this.popStack();
        this.popStack();
    }

    @Override
    public boolean hasNext() {
        JsonToken jsonToken = this.peek();
        return jsonToken != JsonToken.END_OBJECT && jsonToken != JsonToken.END_ARRAY;
    }

    @Override
    public JsonToken peek() {
        if (this.stack.isEmpty()) {
            return JsonToken.END_DOCUMENT;
        }
        Object object = this.peekStack();
        if (object instanceof Iterator) {
            boolean bl = this.stack.get(this.stack.size() - 2) instanceof JsonObject;
            Iterator iterator = (Iterator)object;
            if (iterator.hasNext()) {
                if (bl) {
                    return JsonToken.NAME;
                }
                this.stack.add(iterator.next());
                return this.peek();
            }
            return bl ? JsonToken.END_OBJECT : JsonToken.END_ARRAY;
        }
        if (object instanceof JsonObject) {
            return JsonToken.BEGIN_OBJECT;
        }
        if (object instanceof JsonArray) {
            return JsonToken.BEGIN_ARRAY;
        }
        if (object instanceof JsonPrimitive) {
            JsonPrimitive jsonPrimitive = (JsonPrimitive)object;
            if (jsonPrimitive.isString()) {
                return JsonToken.STRING;
            }
            if (jsonPrimitive.isBoolean()) {
                return JsonToken.BOOLEAN;
            }
            if (jsonPrimitive.isNumber()) {
                return JsonToken.NUMBER;
            }
            throw new AssertionError();
        }
        if (object instanceof JsonNull) {
            return JsonToken.NULL;
        }
        if (object == SENTINEL_CLOSED) {
            throw new IllegalStateException("JsonReader is closed");
        }
        throw new AssertionError();
    }

    private Object peekStack() {
        return this.stack.get(this.stack.size() - 1);
    }

    private Object popStack() {
        return this.stack.remove(this.stack.size() - 1);
    }

    private void expect(JsonToken jsonToken) {
        if (this.peek() != jsonToken) {
            throw new IllegalStateException("Expected " + (Object)((Object)jsonToken) + " but was " + (Object)((Object)this.peek()));
        }
    }

    @Override
    public String nextName() {
        this.expect(JsonToken.NAME);
        Iterator iterator = (Iterator)this.peekStack();
        Map.Entry entry = (Map.Entry)iterator.next();
        this.stack.add(entry.getValue());
        return (String)entry.getKey();
    }

    @Override
    public String nextString() {
        JsonToken jsonToken = this.peek();
        if (jsonToken != JsonToken.STRING && jsonToken != JsonToken.NUMBER) {
            throw new IllegalStateException("Expected " + (Object)((Object)JsonToken.STRING) + " but was " + (Object)((Object)jsonToken));
        }
        return ((JsonPrimitive)this.popStack()).getAsString();
    }

    @Override
    public boolean nextBoolean() {
        this.expect(JsonToken.BOOLEAN);
        return ((JsonPrimitive)this.popStack()).getAsBoolean();
    }

    @Override
    public void nextNull() {
        this.expect(JsonToken.NULL);
        this.popStack();
    }

    @Override
    public double nextDouble() {
        JsonToken jsonToken = this.peek();
        if (jsonToken != JsonToken.NUMBER && jsonToken != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + (Object)((Object)JsonToken.NUMBER) + " but was " + (Object)((Object)jsonToken));
        }
        double d = ((JsonPrimitive)this.peekStack()).getAsDouble();
        if (!this.isLenient() && (Double.isNaN(d) || Double.isInfinite(d))) {
            throw new NumberFormatException("JSON forbids NaN and infinities: " + d);
        }
        this.popStack();
        return d;
    }

    @Override
    public long nextLong() {
        JsonToken jsonToken = this.peek();
        if (jsonToken != JsonToken.NUMBER && jsonToken != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + (Object)((Object)JsonToken.NUMBER) + " but was " + (Object)((Object)jsonToken));
        }
        long l = ((JsonPrimitive)this.peekStack()).getAsLong();
        this.popStack();
        return l;
    }

    @Override
    public int nextInt() {
        JsonToken jsonToken = this.peek();
        if (jsonToken != JsonToken.NUMBER && jsonToken != JsonToken.STRING) {
            throw new IllegalStateException("Expected " + (Object)((Object)JsonToken.NUMBER) + " but was " + (Object)((Object)jsonToken));
        }
        int n = ((JsonPrimitive)this.peekStack()).getAsInt();
        this.popStack();
        return n;
    }

    @Override
    public void close() {
        this.stack.clear();
        this.stack.add(SENTINEL_CLOSED);
    }

    @Override
    public void skipValue() {
        if (this.peek() == JsonToken.NAME) {
            this.nextName();
        } else {
            this.popStack();
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public void promoteNameToValue() {
        this.expect(JsonToken.NAME);
        Iterator iterator = (Iterator)this.peekStack();
        Map.Entry entry = (Map.Entry)iterator.next();
        this.stack.add(entry.getValue());
        this.stack.add(new JsonPrimitive((String)entry.getKey()));
    }

}


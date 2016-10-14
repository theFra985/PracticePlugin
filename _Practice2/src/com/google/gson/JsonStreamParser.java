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
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

public final class JsonStreamParser
implements Iterator<JsonElement> {
    private final JsonReader parser;
    private final Object lock;

    public JsonStreamParser(String string) {
        this(new StringReader(string));
    }

    public JsonStreamParser(Reader reader) {
        this.parser = new JsonReader(reader);
        this.parser.setLenient(true);
        this.lock = new Object();
    }

    @Override
    public JsonElement next() {
        if (!this.hasNext()) {
            throw new NoSuchElementException();
        }
        try {
            return Streams.parse(this.parser);
        }
        catch (StackOverflowError var1_1) {
            throw new JsonParseException("Failed parsing JSON source to Json", var1_1);
        }
        catch (OutOfMemoryError var1_2) {
            throw new JsonParseException("Failed parsing JSON source to Json", var1_2);
        }
        catch (JsonParseException var1_3 /* !! */ ) {
            throw var1_3 /* !! */ .getCause() instanceof EOFException ? new NoSuchElementException() : var1_3 /* !! */ ;
        }
    }

    @Override
    public boolean hasNext() {
        Object object = this.lock;
        synchronized (object) {
            try {
                return this.parser.peek() != JsonToken.END_DOCUMENT;
            }
            catch (MalformedJsonException var2_2) {
                throw new JsonSyntaxException(var2_2);
            }
            catch (IOException var2_3) {
                throw new JsonIOException(var2_3);
            }
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}


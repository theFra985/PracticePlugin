/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson.internal;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Writer;

public final class Streams {
    private Streams() {
        throw new UnsupportedOperationException();
    }

    public static JsonElement parse(JsonReader jsonReader) {
        boolean bl = true;
        try {
            jsonReader.peek();
            bl = false;
            return TypeAdapters.JSON_ELEMENT.read(jsonReader);
        }
        catch (EOFException var2_2) {
            if (bl) {
                return JsonNull.INSTANCE;
            }
            throw new JsonSyntaxException(var2_2);
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

    public static void write(JsonElement jsonElement, JsonWriter jsonWriter) {
        TypeAdapters.JSON_ELEMENT.write(jsonWriter, jsonElement);
    }

    public static Writer writerForAppendable(Appendable appendable) {
        return appendable instanceof Writer ? (Writer)appendable : new AppendableWriter(appendable);
    }

    private static final class AppendableWriter
    extends Writer {
        private final Appendable appendable;
        private final CurrentWrite currentWrite = new CurrentWrite();

        AppendableWriter(Appendable appendable) {
            this.appendable = appendable;
        }

        @Override
        public void write(char[] arrc, int n, int n2) {
            this.currentWrite.chars = arrc;
            this.appendable.append(this.currentWrite, n, n + n2);
        }

        @Override
        public void write(int n) {
            this.appendable.append((char)n);
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() {
        }

        static class CurrentWrite
        implements CharSequence {
            char[] chars;

            CurrentWrite() {
            }

            @Override
            public int length() {
                return this.chars.length;
            }

            @Override
            public char charAt(int n) {
                return this.chars[n];
            }

            @Override
            public CharSequence subSequence(int n, int n2) {
                return new String(this.chars, n, n2 - n);
            }
        }

    }

}


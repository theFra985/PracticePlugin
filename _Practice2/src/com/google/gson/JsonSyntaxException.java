/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson;

import com.google.gson.JsonParseException;

public final class JsonSyntaxException
extends JsonParseException {
    private static final long serialVersionUID = 1;

    public JsonSyntaxException(String string) {
        super(string);
    }

    public JsonSyntaxException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public JsonSyntaxException(Throwable throwable) {
        super(throwable);
    }
}


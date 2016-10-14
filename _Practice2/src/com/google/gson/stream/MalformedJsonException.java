/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson.stream;

import java.io.IOException;

public final class MalformedJsonException
extends IOException {
    private static final long serialVersionUID = 1;

    public MalformedJsonException(String string) {
        super(string);
    }

    public MalformedJsonException(String string, Throwable throwable) {
        super(string);
        this.initCause(throwable);
    }

    public MalformedJsonException(Throwable throwable) {
        this.initCause(throwable);
    }
}


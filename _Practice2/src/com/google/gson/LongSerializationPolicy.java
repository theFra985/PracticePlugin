/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public enum LongSerializationPolicy {
    DEFAULT{

        @Override
        public JsonElement serialize(Long l) {
            return new JsonPrimitive(l);
        }
    }
    ,
    STRING{

        @Override
        public JsonElement serialize(Long l) {
            return new JsonPrimitive(String.valueOf(l));
        }
    };
    

    private LongSerializationPolicy() {
    }

    public abstract JsonElement serialize(Long var1);

}


/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

final class TypeAdapterRuntimeTypeWrapper<T>
extends TypeAdapter<T> {
    private final Gson context;
    private final TypeAdapter<T> delegate;
    private final Type type;

    TypeAdapterRuntimeTypeWrapper(Gson gson, TypeAdapter<T> typeAdapter, Type type) {
        this.context = gson;
        this.delegate = typeAdapter;
        this.type = type;
    }

    @Override
    public T read(JsonReader jsonReader) {
        return this.delegate.read(jsonReader);
    }

    @Override
    public void write(JsonWriter jsonWriter, T t) {
        TypeAdapter typeAdapter = this.delegate;
        Type type = this.getRuntimeTypeIfMoreSpecific(this.type, t);
        if (type != this.type) {
            TypeAdapter typeAdapter2 = this.context.getAdapter(TypeToken.get(type));
            typeAdapter = !(typeAdapter2 instanceof ReflectiveTypeAdapterFactory.Adapter) ? typeAdapter2 : (!(this.delegate instanceof ReflectiveTypeAdapterFactory.Adapter) ? this.delegate : typeAdapter2);
        }
        typeAdapter.write(jsonWriter, t);
    }

    private Type getRuntimeTypeIfMoreSpecific(Type class_, Object object) {
        if (object != null && (class_ == Object.class || class_ instanceof TypeVariable || class_ instanceof Class)) {
            class_ = object.getClass();
        }
        return class_;
    }
}


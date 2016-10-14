/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.reflect.TypeToken;
import java.lang.annotation.Annotation;

public final class JsonAdapterAnnotationTypeAdapterFactory
implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;

    public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        JsonAdapter jsonAdapter = typeToken.getRawType().getAnnotation(JsonAdapter.class);
        if (jsonAdapter == null) {
            return null;
        }
        return JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(this.constructorConstructor, gson, typeToken, jsonAdapter);
    }

    static TypeAdapter<?> getTypeAdapter(ConstructorConstructor constructorConstructor, Gson gson, TypeToken<?> typeToken, JsonAdapter jsonAdapter) {
        TypeAdapter typeAdapter2;
        TypeAdapter typeAdapter2;
        Class class_ = jsonAdapter.value();
        if (TypeAdapter.class.isAssignableFrom(class_)) {
            Class class_2 = class_;
            typeAdapter2 = (TypeAdapter)constructorConstructor.get(TypeToken.get(class_2)).construct();
        } else if (TypeAdapterFactory.class.isAssignableFrom(class_)) {
            Class class_3 = class_;
            typeAdapter2 = ((TypeAdapterFactory)constructorConstructor.get(TypeToken.get(class_3)).construct()).create(gson, typeToken);
        } else {
            throw new IllegalArgumentException("@JsonAdapter value must be TypeAdapter or TypeAdapterFactory reference.");
        }
        if (typeAdapter2 != null) {
            typeAdapter2 = typeAdapter2.nullSafe();
        }
        return typeAdapter2;
    }
}


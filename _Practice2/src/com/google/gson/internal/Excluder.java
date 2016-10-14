/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson.internal;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class Excluder
implements TypeAdapterFactory,
Cloneable {
    private static final double IGNORE_VERSIONS = -1.0;
    public static final Excluder DEFAULT = new Excluder();
    private double version = -1.0;
    private int modifiers = 136;
    private boolean serializeInnerClasses = true;
    private boolean requireExpose;
    private List<ExclusionStrategy> serializationStrategies = Collections.emptyList();
    private List<ExclusionStrategy> deserializationStrategies = Collections.emptyList();

    protected Excluder clone() {
        try {
            return (Excluder)super.clone();
        }
        catch (CloneNotSupportedException var1_1) {
            throw new AssertionError(var1_1);
        }
    }

    public Excluder withVersion(double d) {
        Excluder excluder = this.clone();
        excluder.version = d;
        return excluder;
    }

    public /* varargs */ Excluder withModifiers(int ... arrn) {
        Excluder excluder = this.clone();
        excluder.modifiers = 0;
        for (int n : arrn) {
            excluder.modifiers |= n;
        }
        return excluder;
    }

    public Excluder disableInnerClassSerialization() {
        Excluder excluder = this.clone();
        excluder.serializeInnerClasses = false;
        return excluder;
    }

    public Excluder excludeFieldsWithoutExposeAnnotation() {
        Excluder excluder = this.clone();
        excluder.requireExpose = true;
        return excluder;
    }

    public Excluder withExclusionStrategy(ExclusionStrategy exclusionStrategy, boolean bl, boolean bl2) {
        Excluder excluder = this.clone();
        if (bl) {
            excluder.serializationStrategies = new ArrayList<ExclusionStrategy>(this.serializationStrategies);
            excluder.serializationStrategies.add(exclusionStrategy);
        }
        if (bl2) {
            excluder.deserializationStrategies = new ArrayList<ExclusionStrategy>(this.deserializationStrategies);
            excluder.deserializationStrategies.add(exclusionStrategy);
        }
        return excluder;
    }

    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        Class<T> class_ = typeToken.getRawType();
        final boolean bl = this.excludeClass(class_, true);
        final boolean bl2 = this.excludeClass(class_, false);
        if (!bl && !bl2) {
            return null;
        }
        return new TypeAdapter<T>(){
            private TypeAdapter<T> delegate;

            @Override
            public T read(JsonReader jsonReader) {
                if (bl2) {
                    jsonReader.skipValue();
                    return null;
                }
                return this.delegate().read(jsonReader);
            }

            @Override
            public void write(JsonWriter jsonWriter, T t) {
                if (bl) {
                    jsonWriter.nullValue();
                    return;
                }
                this.delegate().write(jsonWriter, t);
            }

            private TypeAdapter<T> delegate() {
                TypeAdapter<T> typeAdapter = this.delegate;
                TypeAdapter<T> typeAdapter2 = typeAdapter != null ? typeAdapter : (this.delegate = gson.getDelegateAdapter(Excluder.this, typeToken));
                return typeAdapter2;
            }
        };
    }

    public boolean excludeField(Field field, boolean bl) {
        Object object;
        if ((this.modifiers & field.getModifiers()) != 0) {
            return true;
        }
        if (this.version != -1.0 && !this.isValidVersion(field.getAnnotation(Since.class), field.getAnnotation(Until.class))) {
            return true;
        }
        if (field.isSynthetic()) {
            return true;
        }
        if (this.requireExpose && ((object = field.getAnnotation(Expose.class)) == null || (bl ? !object.serialize() : !object.deserialize()))) {
            return true;
        }
        if (!this.serializeInnerClasses && this.isInnerClass(field.getType())) {
            return true;
        }
        if (this.isAnonymousOrLocal(field.getType())) {
            return true;
        }
        Object object2 = object = bl ? this.serializationStrategies : this.deserializationStrategies;
        if (!object.isEmpty()) {
            FieldAttributes fieldAttributes = new FieldAttributes(field);
            Iterator iterator = object.iterator();
            while (iterator.hasNext()) {
                ExclusionStrategy exclusionStrategy = (ExclusionStrategy)iterator.next();
                if (!exclusionStrategy.shouldSkipField(fieldAttributes)) continue;
                return true;
            }
        }
        return false;
    }

    public boolean excludeClass(Class<?> class_, boolean bl) {
        if (this.version != -1.0 && !this.isValidVersion(class_.getAnnotation(Since.class), class_.getAnnotation(Until.class))) {
            return true;
        }
        if (!this.serializeInnerClasses && this.isInnerClass(class_)) {
            return true;
        }
        if (this.isAnonymousOrLocal(class_)) {
            return true;
        }
        List<ExclusionStrategy> list = bl ? this.serializationStrategies : this.deserializationStrategies;
        for (ExclusionStrategy exclusionStrategy : list) {
            if (!exclusionStrategy.shouldSkipClass(class_)) continue;
            return true;
        }
        return false;
    }

    private boolean isAnonymousOrLocal(Class<?> class_) {
        return !Enum.class.isAssignableFrom(class_) && (class_.isAnonymousClass() || class_.isLocalClass());
    }

    private boolean isInnerClass(Class<?> class_) {
        return class_.isMemberClass() && !this.isStatic(class_);
    }

    private boolean isStatic(Class<?> class_) {
        return (class_.getModifiers() & 8) != 0;
    }

    private boolean isValidVersion(Since since, Until until) {
        return this.isValidSince(since) && this.isValidUntil(until);
    }

    private boolean isValidSince(Since since) {
        double d;
        if (since != null && (d = since.value()) > this.version) {
            return false;
        }
        return true;
    }

    private boolean isValidUntil(Until until) {
        double d;
        if (until != null && (d = until.value()) <= this.version) {
            return false;
        }
        return true;
    }

}


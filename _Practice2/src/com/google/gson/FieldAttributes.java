/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson;

import com.google.gson.internal.$Gson$Preconditions;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;

public final class FieldAttributes {
    private final Field field;

    public FieldAttributes(Field field) {
        $Gson$Preconditions.checkNotNull(field);
        this.field = field;
    }

    public Class<?> getDeclaringClass() {
        return this.field.getDeclaringClass();
    }

    public String getName() {
        return this.field.getName();
    }

    public Type getDeclaredType() {
        return this.field.getGenericType();
    }

    public Class<?> getDeclaredClass() {
        return this.field.getType();
    }

    public <T extends Annotation> T getAnnotation(Class<T> class_) {
        return this.field.getAnnotation(class_);
    }

    public Collection<Annotation> getAnnotations() {
        return Arrays.asList(this.field.getAnnotations());
    }

    public boolean hasModifier(int n) {
        return (this.field.getModifiers() & n) != 0;
    }

    Object get(Object object) {
        return this.field.get(object);
    }

    boolean isSynthetic() {
        return this.field.isSynthetic();
    }
}


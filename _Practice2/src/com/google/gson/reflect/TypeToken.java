/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson.reflect;

import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

public class TypeToken<T> {
    final Class<? super T> rawType;
    final Type type;
    final int hashCode;

    protected TypeToken() {
        this.type = TypeToken.getSuperclassTypeParameter(this.getClass());
        this.rawType = $Gson$Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }

    TypeToken(Type type) {
        this.type = $Gson$Types.canonicalize($Gson$Preconditions.checkNotNull(type));
        this.rawType = $Gson$Types.getRawType(this.type);
        this.hashCode = this.type.hashCode();
    }

    static Type getSuperclassTypeParameter(Class<?> class_) {
        Type type = class_.getGenericSuperclass();
        if (type instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterizedType = (ParameterizedType)type;
        return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
    }

    public final Class<? super T> getRawType() {
        return this.rawType;
    }

    public final Type getType() {
        return this.type;
    }

    @Deprecated
    public boolean isAssignableFrom(Class<?> class_) {
        return this.isAssignableFrom((Type)class_);
    }

    @Deprecated
    public boolean isAssignableFrom(Type type) {
        if (type == null) {
            return false;
        }
        if (this.type.equals(type)) {
            return true;
        }
        if (this.type instanceof Class) {
            return this.rawType.isAssignableFrom($Gson$Types.getRawType(type));
        }
        if (this.type instanceof ParameterizedType) {
            return TypeToken.isAssignableFrom(type, (ParameterizedType)this.type, new HashMap<String, Type>());
        }
        if (this.type instanceof GenericArrayType) {
            return this.rawType.isAssignableFrom($Gson$Types.getRawType(type)) && TypeToken.isAssignableFrom(type, (GenericArrayType)this.type);
        }
        throw TypeToken.buildUnexpectedTypeError(this.type, Class.class, ParameterizedType.class, GenericArrayType.class);
    }

    @Deprecated
    public boolean isAssignableFrom(TypeToken<?> typeToken) {
        return this.isAssignableFrom(typeToken.getType());
    }

    private static boolean isAssignableFrom(Type type, GenericArrayType genericArrayType) {
        Type type2 = genericArrayType.getGenericComponentType();
        if (type2 instanceof ParameterizedType) {
            Type type3 = type;
            if (type instanceof GenericArrayType) {
                type3 = ((GenericArrayType)type).getGenericComponentType();
            } else if (type instanceof Class) {
                Class class_ = (Class)type;
                while (class_.isArray()) {
                    class_ = class_.getComponentType();
                }
                type3 = class_;
            }
            return TypeToken.isAssignableFrom(type3, (ParameterizedType)type2, new HashMap<String, Type>());
        }
        return true;
    }

    private static boolean isAssignableFrom(Type type, ParameterizedType parameterizedType, Map<String, Type> map) {
        Object object;
        if (type == null) {
            return false;
        }
        if (parameterizedType.equals(type)) {
            return true;
        }
        Class class_ = $Gson$Types.getRawType(type);
        ParameterizedType parameterizedType2 = null;
        if (type instanceof ParameterizedType) {
            parameterizedType2 = (ParameterizedType)type;
        }
        if (parameterizedType2 != null) {
            object = parameterizedType2.getActualTypeArguments();
            TypeVariable<Class<?>>[] arrtypeVariable = class_.getTypeParameters();
            for (int i = 0; i < object.length; ++i) {
                Type type2 = object[i];
                TypeVariable typeVariable = arrtypeVariable[i];
                while (type2 instanceof TypeVariable) {
                    TypeVariable typeVariable2 = (TypeVariable)type2;
                    type2 = map.get(typeVariable2.getName());
                }
                map.put(typeVariable.getName(), type2);
            }
            if (TypeToken.typeEquals(parameterizedType2, parameterizedType, map)) {
                return true;
            }
        }
        for (Type type2 : class_.getGenericInterfaces()) {
            if (!TypeToken.isAssignableFrom(type2, parameterizedType, new HashMap<String, Type>(map))) continue;
            return true;
        }
        object = class_.getGenericSuperclass();
        return TypeToken.isAssignableFrom((Type)object, parameterizedType, new HashMap<String, Type>(map));
    }

    private static boolean typeEquals(ParameterizedType parameterizedType, ParameterizedType parameterizedType2, Map<String, Type> map) {
        if (parameterizedType.getRawType().equals(parameterizedType2.getRawType())) {
            Type[] arrtype = parameterizedType.getActualTypeArguments();
            Type[] arrtype2 = parameterizedType2.getActualTypeArguments();
            for (int i = 0; i < arrtype.length; ++i) {
                if (TypeToken.matches(arrtype[i], arrtype2[i], map)) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    private static /* varargs */ AssertionError buildUnexpectedTypeError(Type type, Class<?> ... arrclass) {
        StringBuilder stringBuilder = new StringBuilder("Unexpected type. Expected one of: ");
        for (Class class_ : arrclass) {
            stringBuilder.append(class_.getName()).append(", ");
        }
        stringBuilder.append("but got: ").append(type.getClass().getName()).append(", for type token: ").append(type.toString()).append('.');
        return new AssertionError((Object)stringBuilder.toString());
    }

    private static boolean matches(Type type, Type type2, Map<String, Type> map) {
        return type2.equals(type) || type instanceof TypeVariable && type2.equals(map.get(((TypeVariable)type).getName()));
    }

    public final int hashCode() {
        return this.hashCode;
    }

    public final boolean equals(Object object) {
        return object instanceof TypeToken && $Gson$Types.equals(this.type, ((TypeToken)object).type);
    }

    public final String toString() {
        return $Gson$Types.typeToString(this.type);
    }

    public static TypeToken<?> get(Type type) {
        return new TypeToken<T>(type);
    }

    public static <T> TypeToken<T> get(Class<T> class_) {
        return new TypeToken<T>(class_);
    }
}


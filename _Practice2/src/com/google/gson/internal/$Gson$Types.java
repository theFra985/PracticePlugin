/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson.internal;

import com.google.gson.internal.$Gson$Preconditions;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

public final class $Gson$Types {
    static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

    private $Gson$Types() {
        throw new UnsupportedOperationException();
    }

    public static /* varargs */ ParameterizedType newParameterizedTypeWithOwner(Type type, Type type2, Type ... arrtype) {
        return new ParameterizedTypeImpl(type, type2, arrtype);
    }

    public static GenericArrayType arrayOf(Type type) {
        return new GenericArrayTypeImpl(type);
    }

    public static WildcardType subtypeOf(Type type) {
        return new WildcardTypeImpl(new Type[]{type}, EMPTY_TYPE_ARRAY);
    }

    public static WildcardType supertypeOf(Type type) {
        return new WildcardTypeImpl(new Type[]{Object.class}, new Type[]{type});
    }

    public static Type canonicalize(Type type) {
        if (type instanceof Class) {
            Type type2 = (Class)type;
            return type2.isArray() ? new GenericArrayTypeImpl($Gson$Types.canonicalize(type2.getComponentType())) : type2;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            return new ParameterizedTypeImpl(parameterizedType.getOwnerType(), parameterizedType.getRawType(), parameterizedType.getActualTypeArguments());
        }
        if (type instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType)type;
            return new GenericArrayTypeImpl(genericArrayType.getGenericComponentType());
        }
        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType)type;
            return new WildcardTypeImpl(wildcardType.getUpperBounds(), wildcardType.getLowerBounds());
        }
        return type;
    }

    public static Class<?> getRawType(Type type) {
        if (type instanceof Class) {
            return (Class)type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type;
            Type type2 = parameterizedType.getRawType();
            $Gson$Preconditions.checkArgument(type2 instanceof Class);
            return (Class)type2;
        }
        if (type instanceof GenericArrayType) {
            Type type3 = ((GenericArrayType)type).getGenericComponentType();
            return Array.newInstance($Gson$Types.getRawType(type3), 0).getClass();
        }
        if (type instanceof TypeVariable) {
            return Object.class;
        }
        if (type instanceof WildcardType) {
            return $Gson$Types.getRawType(((WildcardType)type).getUpperBounds()[0]);
        }
        String string = type == null ? "null" : type.getClass().getName();
        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or GenericArrayType, but <" + type + "> is of type " + string);
    }

    static boolean equal(Object object, Object object2) {
        return object == object2 || object != null && object.equals(object2);
    }

    public static boolean equals(Type type, Type type2) {
        if (type == type2) {
            return true;
        }
        if (type instanceof Class) {
            return type.equals(type2);
        }
        if (type instanceof ParameterizedType) {
            if (!(type2 instanceof ParameterizedType)) {
                return false;
            }
            ParameterizedType parameterizedType = (ParameterizedType)type;
            ParameterizedType parameterizedType2 = (ParameterizedType)type2;
            return $Gson$Types.equal(parameterizedType.getOwnerType(), parameterizedType2.getOwnerType()) && parameterizedType.getRawType().equals(parameterizedType2.getRawType()) && Arrays.equals(parameterizedType.getActualTypeArguments(), parameterizedType2.getActualTypeArguments());
        }
        if (type instanceof GenericArrayType) {
            if (!(type2 instanceof GenericArrayType)) {
                return false;
            }
            GenericArrayType genericArrayType = (GenericArrayType)type;
            GenericArrayType genericArrayType2 = (GenericArrayType)type2;
            return $Gson$Types.equals(genericArrayType.getGenericComponentType(), genericArrayType2.getGenericComponentType());
        }
        if (type instanceof WildcardType) {
            if (!(type2 instanceof WildcardType)) {
                return false;
            }
            WildcardType wildcardType = (WildcardType)type;
            WildcardType wildcardType2 = (WildcardType)type2;
            return Arrays.equals(wildcardType.getUpperBounds(), wildcardType2.getUpperBounds()) && Arrays.equals(wildcardType.getLowerBounds(), wildcardType2.getLowerBounds());
        }
        if (type instanceof TypeVariable) {
            if (!(type2 instanceof TypeVariable)) {
                return false;
            }
            TypeVariable typeVariable = (TypeVariable)type;
            TypeVariable typeVariable2 = (TypeVariable)type2;
            return typeVariable.getGenericDeclaration() == typeVariable2.getGenericDeclaration() && typeVariable.getName().equals(typeVariable2.getName());
        }
        return false;
    }

    static int hashCodeOrZero(Object object) {
        return object != null ? object.hashCode() : 0;
    }

    public static String typeToString(Type type) {
        return type instanceof Class ? ((Class)type).getName() : type.toString();
    }

    static Type getGenericSupertype(Type type, Class<?> object, Class<?> class_) {
        Class class_2;
        if (class_ == object) {
            return type;
        }
        if (class_.isInterface()) {
            class_2 = object.getInterfaces();
            int n = class_2.length;
            for (int i = 0; i < n; ++i) {
                if (class_2[i] == class_) {
                    return object.getGenericInterfaces()[i];
                }
                if (!class_.isAssignableFrom(class_2[i])) continue;
                return $Gson$Types.getGenericSupertype(object.getGenericInterfaces()[i], class_2[i], class_);
            }
        }
        if (!object.isInterface()) {
            while (object != Object.class) {
                class_2 = object.getSuperclass();
                if (class_2 == class_) {
                    return object.getGenericSuperclass();
                }
                if (class_.isAssignableFrom(class_2)) {
                    return $Gson$Types.getGenericSupertype(object.getGenericSuperclass(), class_2, class_);
                }
                object = class_2;
            }
        }
        return class_;
    }

    static Type getSupertype(Type type, Class<?> class_, Class<?> class_2) {
        $Gson$Preconditions.checkArgument(class_2.isAssignableFrom(class_));
        return $Gson$Types.resolve(type, class_, $Gson$Types.getGenericSupertype(type, class_, class_2));
    }

    public static Type getArrayComponentType(Type type) {
        return type instanceof GenericArrayType ? ((GenericArrayType)type).getGenericComponentType() : ((Class)type).getComponentType();
    }

    public static Type getCollectionElementType(Type type, Class<?> class_) {
        Type type2 = $Gson$Types.getSupertype(type, class_, Collection.class);
        if (type2 instanceof WildcardType) {
            type2 = ((WildcardType)type2).getUpperBounds()[0];
        }
        if (type2 instanceof ParameterizedType) {
            return ((ParameterizedType)type2).getActualTypeArguments()[0];
        }
        return Object.class;
    }

    public static Type[] getMapKeyAndValueTypes(Type type, Class<?> class_) {
        if (type == Properties.class) {
            return new Type[]{String.class, String.class};
        }
        Type type2 = $Gson$Types.getSupertype(type, class_, Map.class);
        if (type2 instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type2;
            return parameterizedType.getActualTypeArguments();
        }
        return new Type[]{Object.class, Object.class};
    }

    public static Type resolve(Type type, Class<?> class_, Type type2) {
        while (type2 instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable)type2;
            type2 = $Gson$Types.resolveTypeVariable(type, class_, typeVariable);
            if (type2 != typeVariable) continue;
            return type2;
        }
        if (type2 instanceof Class && ((Class)type2).isArray()) {
            Type type3;
            Class class_2 = (Class)type2;
            Class class_3 = class_2.getComponentType();
            return class_3 == (type3 = $Gson$Types.resolve(type, class_, class_3)) ? class_2 : $Gson$Types.arrayOf(type3);
        }
        if (type2 instanceof GenericArrayType) {
            Type type4;
            GenericArrayType genericArrayType = (GenericArrayType)type2;
            Type type5 = genericArrayType.getGenericComponentType();
            return type5 == (type4 = $Gson$Types.resolve(type, class_, type5)) ? genericArrayType : $Gson$Types.arrayOf(type4);
        }
        if (type2 instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType)type2;
            Type type6 = parameterizedType.getOwnerType();
            Type type7 = $Gson$Types.resolve(type, class_, type6);
            boolean bl = type7 != type6;
            Type[] arrtype = parameterizedType.getActualTypeArguments();
            int n = arrtype.length;
            for (int i = 0; i < n; ++i) {
                Type type8 = $Gson$Types.resolve(type, class_, arrtype[i]);
                if (type8 == arrtype[i]) continue;
                if (!bl) {
                    arrtype = (Type[])arrtype.clone();
                    bl = true;
                }
                arrtype[i] = type8;
            }
            return bl ? $Gson$Types.newParameterizedTypeWithOwner(type7, parameterizedType.getRawType(), arrtype) : parameterizedType;
        }
        if (type2 instanceof WildcardType) {
            Type type9;
            WildcardType wildcardType = (WildcardType)type2;
            Type[] arrtype = wildcardType.getLowerBounds();
            Type[] arrtype2 = wildcardType.getUpperBounds();
            if (arrtype.length == 1) {
                Type type10 = $Gson$Types.resolve(type, class_, arrtype[0]);
                if (type10 != arrtype[0]) {
                    return $Gson$Types.supertypeOf(type10);
                }
            } else if (arrtype2.length == 1 && (type9 = $Gson$Types.resolve(type, class_, arrtype2[0])) != arrtype2[0]) {
                return $Gson$Types.subtypeOf(type9);
            }
            return wildcardType;
        }
        return type2;
    }

    static Type resolveTypeVariable(Type type, Class<?> class_, TypeVariable<?> typeVariable) {
        Class class_2 = $Gson$Types.declaringClassOf(typeVariable);
        if (class_2 == null) {
            return typeVariable;
        }
        Type type2 = $Gson$Types.getGenericSupertype(type, class_, class_2);
        if (type2 instanceof ParameterizedType) {
            int n = $Gson$Types.indexOf(class_2.getTypeParameters(), typeVariable);
            return ((ParameterizedType)type2).getActualTypeArguments()[n];
        }
        return typeVariable;
    }

    private static int indexOf(Object[] arrobject, Object object) {
        for (int i = 0; i < arrobject.length; ++i) {
            if (!object.equals(arrobject[i])) continue;
            return i;
        }
        throw new NoSuchElementException();
    }

    private static Class<?> declaringClassOf(TypeVariable<?> typeVariable) {
        Object obj = typeVariable.getGenericDeclaration();
        return obj instanceof Class ? (Class)obj : null;
    }

    static void checkNotPrimitive(Type type) {
        $Gson$Preconditions.checkArgument(!(type instanceof Class) || !((Class)type).isPrimitive());
    }

    private static final class WildcardTypeImpl
    implements WildcardType,
    Serializable {
        private final Type upperBound;
        private final Type lowerBound;
        private static final long serialVersionUID = 0;

        public WildcardTypeImpl(Type[] arrtype, Type[] arrtype2) {
            $Gson$Preconditions.checkArgument(arrtype2.length <= 1);
            $Gson$Preconditions.checkArgument(arrtype.length == 1);
            if (arrtype2.length == 1) {
                $Gson$Preconditions.checkNotNull(arrtype2[0]);
                $Gson$Types.checkNotPrimitive(arrtype2[0]);
                $Gson$Preconditions.checkArgument(arrtype[0] == Object.class);
                this.lowerBound = $Gson$Types.canonicalize(arrtype2[0]);
                this.upperBound = Object.class;
            } else {
                $Gson$Preconditions.checkNotNull(arrtype[0]);
                $Gson$Types.checkNotPrimitive(arrtype[0]);
                this.lowerBound = null;
                this.upperBound = $Gson$Types.canonicalize(arrtype[0]);
            }
        }

        @Override
        public Type[] getUpperBounds() {
            return new Type[]{this.upperBound};
        }

        @Override
        public Type[] getLowerBounds() {
            Type[] arrtype;
            if (this.lowerBound != null) {
                Type[] arrtype2 = new Type[1];
                arrtype = arrtype2;
                arrtype2[0] = this.lowerBound;
            } else {
                arrtype = $Gson$Types.EMPTY_TYPE_ARRAY;
            }
            return arrtype;
        }

        public boolean equals(Object object) {
            return object instanceof WildcardType && $Gson$Types.equals(this, (WildcardType)object);
        }

        public int hashCode() {
            return (this.lowerBound != null ? 31 + this.lowerBound.hashCode() : 1) ^ 31 + this.upperBound.hashCode();
        }

        public String toString() {
            if (this.lowerBound != null) {
                return "? super " + $Gson$Types.typeToString(this.lowerBound);
            }
            if (this.upperBound == Object.class) {
                return "?";
            }
            return "? extends " + $Gson$Types.typeToString(this.upperBound);
        }
    }

    private static final class GenericArrayTypeImpl
    implements GenericArrayType,
    Serializable {
        private final Type componentType;
        private static final long serialVersionUID = 0;

        public GenericArrayTypeImpl(Type type) {
            this.componentType = $Gson$Types.canonicalize(type);
        }

        @Override
        public Type getGenericComponentType() {
            return this.componentType;
        }

        public boolean equals(Object object) {
            return object instanceof GenericArrayType && $Gson$Types.equals(this, (GenericArrayType)object);
        }

        public int hashCode() {
            return this.componentType.hashCode();
        }

        public String toString() {
            return $Gson$Types.typeToString(this.componentType) + "[]";
        }
    }

    private static final class ParameterizedTypeImpl
    implements ParameterizedType,
    Serializable {
        private final Type ownerType;
        private final Type rawType;
        private final Type[] typeArguments;
        private static final long serialVersionUID = 0;

        public /* varargs */ ParameterizedTypeImpl(Type type, Type type2, Type ... arrtype) {
            if (type2 instanceof Class) {
                Class class_ = (Class)type2;
                boolean bl = Modifier.isStatic(class_.getModifiers()) || class_.getEnclosingClass() == null;
                $Gson$Preconditions.checkArgument(type != null || bl);
            }
            this.ownerType = type == null ? null : $Gson$Types.canonicalize(type);
            this.rawType = $Gson$Types.canonicalize(type2);
            this.typeArguments = (Type[])arrtype.clone();
            for (int i = 0; i < this.typeArguments.length; ++i) {
                $Gson$Preconditions.checkNotNull(this.typeArguments[i]);
                $Gson$Types.checkNotPrimitive(this.typeArguments[i]);
                this.typeArguments[i] = $Gson$Types.canonicalize(this.typeArguments[i]);
            }
        }

        @Override
        public Type[] getActualTypeArguments() {
            return (Type[])this.typeArguments.clone();
        }

        @Override
        public Type getRawType() {
            return this.rawType;
        }

        @Override
        public Type getOwnerType() {
            return this.ownerType;
        }

        public boolean equals(Object object) {
            return object instanceof ParameterizedType && $Gson$Types.equals(this, (ParameterizedType)object);
        }

        public int hashCode() {
            return Arrays.hashCode(this.typeArguments) ^ this.rawType.hashCode() ^ $Gson$Types.hashCodeOrZero(this.ownerType);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder(30 * (this.typeArguments.length + 1));
            stringBuilder.append($Gson$Types.typeToString(this.rawType));
            if (this.typeArguments.length == 0) {
                return stringBuilder.toString();
            }
            stringBuilder.append("<").append($Gson$Types.typeToString(this.typeArguments[0]));
            for (int i = 1; i < this.typeArguments.length; ++i) {
                stringBuilder.append(", ").append($Gson$Types.typeToString(this.typeArguments[i]));
            }
            return stringBuilder.append(">").toString();
        }
    }

}


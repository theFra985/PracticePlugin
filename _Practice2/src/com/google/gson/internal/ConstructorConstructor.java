/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson.internal;

import com.google.gson.InstanceCreator;
import com.google.gson.JsonIOException;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.UnsafeAllocator;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

public final class ConstructorConstructor {
    private final Map<Type, InstanceCreator<?>> instanceCreators;

    public ConstructorConstructor(Map<Type, InstanceCreator<?>> map) {
        this.instanceCreators = map;
    }

    public <T> ObjectConstructor<T> get(TypeToken<T> typeToken) {
        final Type type = typeToken.getType();
        Class<T> class_ = typeToken.getRawType();
        final InstanceCreator instanceCreator = this.instanceCreators.get(type);
        if (instanceCreator != null) {
            return new ObjectConstructor<T>(){

                @Override
                public T construct() {
                    return instanceCreator.createInstance(type);
                }
            };
        }
        final InstanceCreator instanceCreator2 = this.instanceCreators.get(class_);
        if (instanceCreator2 != null) {
            return new ObjectConstructor<T>(){

                @Override
                public T construct() {
                    return instanceCreator2.createInstance(type);
                }
            };
        }
        ObjectConstructor<T> objectConstructor = this.newDefaultConstructor(class_);
        if (objectConstructor != null) {
            return objectConstructor;
        }
        ObjectConstructor<T> objectConstructor2 = this.newDefaultImplementationConstructor(type, class_);
        if (objectConstructor2 != null) {
            return objectConstructor2;
        }
        return this.newUnsafeAllocator(type, class_);
    }

    private <T> ObjectConstructor<T> newDefaultConstructor(Class<? super T> class_) {
        try {
            final Constructor<T> constructor = class_.getDeclaredConstructor(new Class[0]);
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            return new ObjectConstructor<T>(){

                @Override
                public T construct() {
                    try {
                        Object[] arrobject = null;
                        return constructor.newInstance(arrobject);
                    }
                    catch (InstantiationException var1_2) {
                        throw new RuntimeException("Failed to invoke " + constructor + " with no args", var1_2);
                    }
                    catch (InvocationTargetException var1_3) {
                        throw new RuntimeException("Failed to invoke " + constructor + " with no args", var1_3.getTargetException());
                    }
                    catch (IllegalAccessException var1_4) {
                        throw new AssertionError(var1_4);
                    }
                }
            };
        }
        catch (NoSuchMethodException var2_3) {
            return null;
        }
    }

    private <T> ObjectConstructor<T> newDefaultImplementationConstructor(final Type type, Class<? super T> class_) {
        if (Collection.class.isAssignableFrom(class_)) {
            if (SortedSet.class.isAssignableFrom(class_)) {
                return new ObjectConstructor<T>(){

                    @Override
                    public T construct() {
                        return (T)new TreeSet();
                    }
                };
            }
            if (EnumSet.class.isAssignableFrom(class_)) {
                return new ObjectConstructor<T>(){

                    @Override
                    public T construct() {
                        if (type instanceof ParameterizedType) {
                            Type type2 = ((ParameterizedType)type).getActualTypeArguments()[0];
                            if (type2 instanceof Class) {
                                return (T)EnumSet.noneOf((Class)type2);
                            }
                            throw new JsonIOException("Invalid EnumSet type: " + type.toString());
                        }
                        throw new JsonIOException("Invalid EnumSet type: " + type.toString());
                    }
                };
            }
            if (Set.class.isAssignableFrom(class_)) {
                return new ObjectConstructor<T>(){

                    @Override
                    public T construct() {
                        return (T)new LinkedHashSet();
                    }
                };
            }
            if (Queue.class.isAssignableFrom(class_)) {
                return new ObjectConstructor<T>(){

                    @Override
                    public T construct() {
                        return (T)new LinkedList();
                    }
                };
            }
            return new ObjectConstructor<T>(){

                @Override
                public T construct() {
                    return (T)new ArrayList();
                }
            };
        }
        if (Map.class.isAssignableFrom(class_)) {
            if (ConcurrentNavigableMap.class.isAssignableFrom(class_)) {
                return new ObjectConstructor<T>(){

                    @Override
                    public T construct() {
                        return (T)new ConcurrentSkipListMap();
                    }
                };
            }
            if (ConcurrentMap.class.isAssignableFrom(class_)) {
                return new ObjectConstructor<T>(){

                    @Override
                    public T construct() {
                        return (T)new ConcurrentHashMap();
                    }
                };
            }
            if (SortedMap.class.isAssignableFrom(class_)) {
                return new ObjectConstructor<T>(){

                    @Override
                    public T construct() {
                        return (T)new TreeMap();
                    }
                };
            }
            if (type instanceof ParameterizedType && !String.class.isAssignableFrom(TypeToken.get(((ParameterizedType)type).getActualTypeArguments()[0]).getRawType())) {
                return new ObjectConstructor<T>(){

                    @Override
                    public T construct() {
                        return (T)new LinkedHashMap();
                    }
                };
            }
            return new ObjectConstructor<T>(){

                @Override
                public T construct() {
                    return (T)new LinkedTreeMap();
                }
            };
        }
        return null;
    }

    private <T> ObjectConstructor<T> newUnsafeAllocator(final Type type, final Class<? super T> class_) {
        return new ObjectConstructor<T>(){
            private final UnsafeAllocator unsafeAllocator;

            @Override
            public T construct() {
                try {
                    Object t = this.unsafeAllocator.newInstance(class_);
                    return t;
                }
                catch (Exception var1_2) {
                    throw new RuntimeException("Unable to invoke no-args constructor for " + type + ". " + "Register an InstanceCreator with Gson for this type may fix this problem.", var1_2);
                }
            }
        };
    }

    public String toString() {
        return this.instanceCreators.toString();
    }

}


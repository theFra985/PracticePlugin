/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson.internal;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class UnsafeAllocator {
    public abstract <T> T newInstance(Class<T> var1);

    public static UnsafeAllocator create() {
        try {
            Class<?> class_ = Class.forName("sun.misc.Unsafe");
            Field field = class_.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            final Object object = field.get(null);
            final Method method = class_.getMethod("allocateInstance", Class.class);
            return new UnsafeAllocator(){

                @Override
                public <T> T newInstance(Class<T> class_) {
                    return (T)method.invoke(object, class_);
                }
            };
        }
        catch (Exception var0_1) {
            try {
                Method method = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
                method.setAccessible(true);
                final int n = (Integer)method.invoke(null, Object.class);
                final Method method2 = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, Integer.TYPE);
                method2.setAccessible(true);
                return new UnsafeAllocator(){

                    @Override
                    public <T> T newInstance(Class<T> class_) {
                        return (T)method2.invoke(null, class_, n);
                    }
                };
            }
            catch (Exception var0_3) {
                try {
                    final Method method = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
                    method.setAccessible(true);
                    return new UnsafeAllocator(){

                        @Override
                        public <T> T newInstance(Class<T> class_) {
                            return (T)method.invoke(null, class_, Object.class);
                        }
                    };
                }
                catch (Exception var0_5) {
                    return new UnsafeAllocator(){

                        @Override
                        public <T> T newInstance(Class<T> class_) {
                            throw new UnsupportedOperationException("Cannot allocate " + class_);
                        }
                    };
                }
            }
        }
    }

}


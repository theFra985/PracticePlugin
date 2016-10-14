/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package mkremins.fanciful;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.lang.Validate;

public final class ArrayWrapper<E> {
    private E[] _array;

    @SafeVarargs
    public /* varargs */ ArrayWrapper(E ... arrE) {
        this.setArray(arrE);
    }

    public E[] getArray() {
        return this._array;
    }

    public void setArray(E[] arrE) {
        Validate.notNull(arrE, (String)"The array must not be null.");
        this._array = arrE;
    }

    public boolean equals(Object object) {
        if (!(object instanceof ArrayWrapper)) {
            return false;
        }
        return Arrays.equals(this._array, ((ArrayWrapper)object)._array);
    }

    public int hashCode() {
        return Arrays.hashCode(this._array);
    }

    public static <T> T[] toArray(Iterable<? extends T> iterable, Class<T> class_) {
        Object object2;
        int n = -1;
        if (iterable instanceof Collection) {
            object2 = (Object[])iterable;
            n = object2.size();
        }
        if (n < 0) {
            n = 0;
            for (Object object2 : iterable) {
                ++n;
            }
        }
        object2 = (Object[])Array.newInstance(class_, n);
        int n2 = 0;
        for (T t : iterable) {
            object2[n2++] = t;
        }
        return object2;
    }
}


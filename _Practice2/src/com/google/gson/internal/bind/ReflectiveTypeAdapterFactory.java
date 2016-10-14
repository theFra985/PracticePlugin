/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson.internal.bind;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class ReflectiveTypeAdapterFactory
implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    private final FieldNamingStrategy fieldNamingPolicy;
    private final Excluder excluder;

    public ReflectiveTypeAdapterFactory(ConstructorConstructor constructorConstructor, FieldNamingStrategy fieldNamingStrategy, Excluder excluder) {
        this.constructorConstructor = constructorConstructor;
        this.fieldNamingPolicy = fieldNamingStrategy;
        this.excluder = excluder;
    }

    public boolean excludeField(Field field, boolean bl) {
        return ReflectiveTypeAdapterFactory.excludeField(field, bl, this.excluder);
    }

    static boolean excludeField(Field field, boolean bl, Excluder excluder) {
        return !excluder.excludeClass(field.getType(), bl) && !excluder.excludeField(field, bl);
    }

    private List<String> getFieldNames(Field field) {
        return ReflectiveTypeAdapterFactory.getFieldName(this.fieldNamingPolicy, field);
    }

    static List<String> getFieldName(FieldNamingStrategy fieldNamingStrategy, Field field) {
        SerializedName serializedName = field.getAnnotation(SerializedName.class);
        LinkedList<String> linkedList = new LinkedList<String>();
        if (serializedName == null) {
            linkedList.add(fieldNamingStrategy.translateName(field));
        } else {
            linkedList.add(serializedName.value());
            for (String string : serializedName.alternate()) {
                linkedList.add(string);
            }
        }
        return linkedList;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Class<T> class_ = typeToken.getRawType();
        if (!Object.class.isAssignableFrom(class_)) {
            return null;
        }
        ObjectConstructor<T> objectConstructor = this.constructorConstructor.get(typeToken);
        return new Adapter<T>(objectConstructor, this.getBoundFields(gson, typeToken, class_));
    }

    private BoundField createBoundField(final Gson gson, final Field field, String string, final TypeToken<?> typeToken, boolean bl, boolean bl2) {
        final boolean bl3 = Primitives.isPrimitive(typeToken.getRawType());
        return new BoundField(string, bl, bl2){
            final TypeAdapter<?> typeAdapter;

            @Override
            void write(JsonWriter jsonWriter, Object object) {
                Object object2 = field.get(object);
                TypeAdapterRuntimeTypeWrapper typeAdapterRuntimeTypeWrapper = new TypeAdapterRuntimeTypeWrapper(gson, this.typeAdapter, typeToken.getType());
                typeAdapterRuntimeTypeWrapper.write(jsonWriter, (Object)object2);
            }

            @Override
            void read(JsonReader jsonReader, Object object) {
                Object obj = this.typeAdapter.read(jsonReader);
                if (obj != null || !bl3) {
                    field.set(object, obj);
                }
            }

            @Override
            public boolean writeField(Object object) {
                if (!this.serialized) {
                    return false;
                }
                Object object2 = field.get(object);
                return object2 != object;
            }
        };
    }

    TypeAdapter<?> getFieldAdapter(Gson gson, Field field, TypeToken<?> typeToken) {
        TypeAdapter typeAdapter;
        JsonAdapter jsonAdapter = field.getAnnotation(JsonAdapter.class);
        if (jsonAdapter != null && (typeAdapter = JsonAdapterAnnotationTypeAdapterFactory.getTypeAdapter(this.constructorConstructor, gson, typeToken, jsonAdapter)) != null) {
            return typeAdapter;
        }
        return gson.getAdapter(typeToken);
    }

    private Map<String, BoundField> getBoundFields(Gson gson, TypeToken<?> typeToken, Class<?> class_) {
        LinkedHashMap<String, BoundField> linkedHashMap = new LinkedHashMap<String, BoundField>();
        if (class_.isInterface()) {
            return linkedHashMap;
        }
        Type type = typeToken.getType();
        while (class_ != Object.class) {
            Field[] arrfield;
            for (Field field : arrfield = class_.getDeclaredFields()) {
                boolean bl = this.excludeField(field, true);
                boolean bl2 = this.excludeField(field, false);
                if (!bl && !bl2) continue;
                field.setAccessible(true);
                Type type2 = $Gson$Types.resolve(typeToken.getType(), class_, field.getGenericType());
                List<String> list = this.getFieldNames(field);
                BoundField boundField = null;
                for (int i = 0; i < list.size(); ++i) {
                    String string = list.get(i);
                    if (i != 0) {
                        bl = false;
                    }
                    BoundField boundField2 = this.createBoundField(gson, field, string, TypeToken.get(type2), bl, bl2);
                    BoundField boundField3 = linkedHashMap.put(string, boundField2);
                    if (boundField != null) continue;
                    boundField = boundField3;
                }
                if (boundField == null) continue;
                throw new IllegalArgumentException(type + " declares multiple JSON fields named " + boundField.name);
            }
            typeToken = TypeToken.get($Gson$Types.resolve(typeToken.getType(), class_, class_.getGenericSuperclass()));
            class_ = typeToken.getRawType();
        }
        return linkedHashMap;
    }

    public static final class Adapter<T>
    extends TypeAdapter<T> {
        private final ObjectConstructor<T> constructor;
        private final Map<String, BoundField> boundFields;

        Adapter(ObjectConstructor<T> objectConstructor, Map<String, BoundField> map) {
            this.constructor = objectConstructor;
            this.boundFields = map;
        }

        @Override
        public T read(JsonReader jsonReader) {
            if (jsonReader.peek() == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            T t = this.constructor.construct();
            try {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    String string = jsonReader.nextName();
                    BoundField boundField = this.boundFields.get(string);
                    if (boundField == null || !boundField.deserialized) {
                        jsonReader.skipValue();
                        continue;
                    }
                    boundField.read(jsonReader, t);
                }
            }
            catch (IllegalStateException var3_4) {
                throw new JsonSyntaxException(var3_4);
            }
            catch (IllegalAccessException var3_5) {
                throw new AssertionError(var3_5);
            }
            jsonReader.endObject();
            return t;
        }

        @Override
        public void write(JsonWriter jsonWriter, T t) {
            if (t == null) {
                jsonWriter.nullValue();
                return;
            }
            jsonWriter.beginObject();
            try {
                for (BoundField boundField : this.boundFields.values()) {
                    if (!boundField.writeField(t)) continue;
                    jsonWriter.name(boundField.name);
                    boundField.write(jsonWriter, t);
                }
            }
            catch (IllegalAccessException var3_4) {
                throw new AssertionError(var3_4);
            }
            jsonWriter.endObject();
        }
    }

    static abstract class BoundField {
        final String name;
        final boolean serialized;
        final boolean deserialized;

        protected BoundField(String string, boolean bl, boolean bl2) {
            this.name = string;
            this.serialized = bl;
            this.deserialized = bl2;
        }

        abstract boolean writeField(Object var1);

        abstract void write(JsonWriter var1, Object var2);

        abstract void read(JsonReader var1, Object var2);
    }

}


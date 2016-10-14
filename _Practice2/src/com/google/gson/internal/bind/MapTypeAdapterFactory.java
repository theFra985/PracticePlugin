/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public final class MapTypeAdapterFactory
implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;
    final boolean complexMapKeySerialization;

    public MapTypeAdapterFactory(ConstructorConstructor constructorConstructor, boolean bl) {
        this.constructorConstructor = constructorConstructor;
        this.complexMapKeySerialization = bl;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        Class<T> class_ = typeToken.getRawType();
        if (!Map.class.isAssignableFrom(class_)) {
            return null;
        }
        Class class_2 = $Gson$Types.getRawType(type);
        Type[] arrtype = $Gson$Types.getMapKeyAndValueTypes(type, class_2);
        TypeAdapter typeAdapter = this.getKeyAdapter(gson, arrtype[0]);
        TypeAdapter typeAdapter2 = gson.getAdapter(TypeToken.get(arrtype[1]));
        ObjectConstructor<T> objectConstructor = this.constructorConstructor.get(typeToken);
        Adapter adapter = new Adapter(gson, arrtype[0], typeAdapter, arrtype[1], typeAdapter2, objectConstructor);
        return adapter;
    }

    private TypeAdapter<?> getKeyAdapter(Gson gson, Type type) {
        return type == Boolean.TYPE || type == Boolean.class ? TypeAdapters.BOOLEAN_AS_STRING : gson.getAdapter(TypeToken.get(type));
    }

    private final class Adapter<K, V>
    extends TypeAdapter<Map<K, V>> {
        private final TypeAdapter<K> keyTypeAdapter;
        private final TypeAdapter<V> valueTypeAdapter;
        private final ObjectConstructor<? extends Map<K, V>> constructor;

        public Adapter(Gson gson, Type type, TypeAdapter<K> typeAdapter, Type type2, TypeAdapter<V> typeAdapter2, ObjectConstructor<? extends Map<K, V>> objectConstructor) {
            this.keyTypeAdapter = new TypeAdapterRuntimeTypeWrapper<K>(gson, typeAdapter, type);
            this.valueTypeAdapter = new TypeAdapterRuntimeTypeWrapper<V>(gson, typeAdapter2, type2);
            this.constructor = objectConstructor;
        }

        @Override
        public Map<K, V> read(JsonReader jsonReader) {
            JsonToken jsonToken = jsonReader.peek();
            if (jsonToken == JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            }
            Map<K, V> map = this.constructor.construct();
            if (jsonToken == JsonToken.BEGIN_ARRAY) {
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    jsonReader.beginArray();
                    K k = this.keyTypeAdapter.read(jsonReader);
                    V v = this.valueTypeAdapter.read(jsonReader);
                    V v2 = map.put(k, v);
                    if (v2 != null) {
                        throw new JsonSyntaxException("duplicate key: " + k);
                    }
                    jsonReader.endArray();
                }
                jsonReader.endArray();
            } else {
                jsonReader.beginObject();
                while (jsonReader.hasNext()) {
                    V v;
                    JsonReaderInternalAccess.INSTANCE.promoteNameToValue(jsonReader);
                    K k = this.keyTypeAdapter.read(jsonReader);
                    V v3 = map.put(k, v = this.valueTypeAdapter.read(jsonReader));
                    if (v3 == null) continue;
                    throw new JsonSyntaxException("duplicate key: " + k);
                }
                jsonReader.endObject();
            }
            return map;
        }

        @Override
        public void write(JsonWriter jsonWriter, Map<K, V> map) {
            if (map == null) {
                jsonWriter.nullValue();
                return;
            }
            if (!MapTypeAdapterFactory.this.complexMapKeySerialization) {
                jsonWriter.beginObject();
                for (Map.Entry<K, V> entry : map.entrySet()) {
                    jsonWriter.name(String.valueOf(entry.getKey()));
                    this.valueTypeAdapter.write(jsonWriter, entry.getValue());
                }
                jsonWriter.endObject();
                return;
            }
            boolean bl = false;
            ArrayList<JsonElement> arrayList = new ArrayList<JsonElement>(map.size());
            ArrayList<V> arrayList2 = new ArrayList<V>(map.size());
            for (Map.Entry<K, V> object2 : map.entrySet()) {
                JsonElement jsonElement = this.keyTypeAdapter.toJsonTree(object2.getKey());
                arrayList.add(jsonElement);
                arrayList2.add(object2.getValue());
                bl |= jsonElement.isJsonArray() || jsonElement.isJsonObject();
            }
            if (bl) {
                jsonWriter.beginArray();
                for (int i = 0; i < arrayList.size(); ++i) {
                    jsonWriter.beginArray();
                    Streams.write((JsonElement)arrayList.get(i), jsonWriter);
                    this.valueTypeAdapter.write(jsonWriter, arrayList2.get(i));
                    jsonWriter.endArray();
                }
                jsonWriter.endArray();
            } else {
                jsonWriter.beginObject();
                for (int i = 0; i < arrayList.size(); ++i) {
                    JsonElement jsonElement = (JsonElement)arrayList.get(i);
                    jsonWriter.name(this.keyToString(jsonElement));
                    this.valueTypeAdapter.write(jsonWriter, arrayList2.get(i));
                }
                jsonWriter.endObject();
            }
        }

        private String keyToString(JsonElement jsonElement) {
            if (jsonElement.isJsonPrimitive()) {
                JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
                if (jsonPrimitive.isNumber()) {
                    return String.valueOf(jsonPrimitive.getAsNumber());
                }
                if (jsonPrimitive.isBoolean()) {
                    return Boolean.toString(jsonPrimitive.getAsBoolean());
                }
                if (jsonPrimitive.isString()) {
                    return jsonPrimitive.getAsString();
                }
                throw new AssertionError();
            }
            if (jsonElement.isJsonNull()) {
                return "null";
            }
            throw new AssertionError();
        }
    }

}


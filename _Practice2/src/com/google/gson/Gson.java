/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSyntaxException;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.SqlDateTypeAdapter;
import com.google.gson.internal.bind.TimeTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

public final class Gson {
    static final boolean DEFAULT_JSON_NON_EXECUTABLE = false;
    static final boolean DEFAULT_LENIENT = false;
    static final boolean DEFAULT_PRETTY_PRINT = false;
    static final boolean DEFAULT_ESCAPE_HTML = true;
    static final boolean DEFAULT_SERIALIZE_NULLS = false;
    static final boolean DEFAULT_COMPLEX_MAP_KEYS = false;
    static final boolean DEFAULT_SPECIALIZE_FLOAT_VALUES = false;
    private static final String JSON_NON_EXECUTABLE_PREFIX = ")]}'\n";
    private final ThreadLocal<Map<TypeToken<?>, FutureTypeAdapter<?>>> calls = new ThreadLocal();
    private final Map<TypeToken<?>, TypeAdapter<?>> typeTokenCache = Collections.synchronizedMap(new HashMap());
    private final List<TypeAdapterFactory> factories;
    private final ConstructorConstructor constructorConstructor;
    private final boolean serializeNulls;
    private final boolean htmlSafe;
    private final boolean generateNonExecutableJson;
    private final boolean prettyPrinting;
    private final boolean lenient;
    final JsonDeserializationContext deserializationContext;
    final JsonSerializationContext serializationContext;

    public Gson() {
        this(Excluder.DEFAULT, FieldNamingPolicy.IDENTITY, Collections.emptyMap(), false, false, false, true, false, false, false, LongSerializationPolicy.DEFAULT, Collections.emptyList());
    }

    Gson(Excluder excluder, FieldNamingStrategy fieldNamingStrategy, Map<Type, InstanceCreator<?>> map, boolean bl, boolean bl2, boolean bl3, boolean bl4, boolean bl5, boolean bl6, boolean bl7, LongSerializationPolicy longSerializationPolicy, List<TypeAdapterFactory> list) {
        this.deserializationContext = new JsonDeserializationContext(){

            @Override
            public <T> T deserialize(JsonElement jsonElement, Type type) {
                return Gson.this.fromJson(jsonElement, type);
            }
        };
        this.serializationContext = new JsonSerializationContext(){

            @Override
            public JsonElement serialize(Object object) {
                return Gson.this.toJsonTree(object);
            }

            @Override
            public JsonElement serialize(Object object, Type type) {
                return Gson.this.toJsonTree(object, type);
            }
        };
        this.constructorConstructor = new ConstructorConstructor(map);
        this.serializeNulls = bl;
        this.generateNonExecutableJson = bl3;
        this.htmlSafe = bl4;
        this.prettyPrinting = bl5;
        this.lenient = bl6;
        ArrayList<TypeAdapterFactory> arrayList = new ArrayList<TypeAdapterFactory>();
        arrayList.add(TypeAdapters.JSON_ELEMENT_FACTORY);
        arrayList.add(ObjectTypeAdapter.FACTORY);
        arrayList.add(excluder);
        arrayList.addAll(list);
        arrayList.add(TypeAdapters.STRING_FACTORY);
        arrayList.add(TypeAdapters.INTEGER_FACTORY);
        arrayList.add(TypeAdapters.BOOLEAN_FACTORY);
        arrayList.add(TypeAdapters.BYTE_FACTORY);
        arrayList.add(TypeAdapters.SHORT_FACTORY);
        TypeAdapter<Number> typeAdapter = Gson.longAdapter(longSerializationPolicy);
        arrayList.add(TypeAdapters.newFactory(Long.TYPE, Long.class, typeAdapter));
        arrayList.add(TypeAdapters.newFactory(Double.TYPE, Double.class, this.doubleAdapter(bl7)));
        arrayList.add(TypeAdapters.newFactory(Float.TYPE, Float.class, this.floatAdapter(bl7)));
        arrayList.add(TypeAdapters.NUMBER_FACTORY);
        arrayList.add(TypeAdapters.ATOMIC_INTEGER_FACTORY);
        arrayList.add(TypeAdapters.ATOMIC_BOOLEAN_FACTORY);
        arrayList.add(TypeAdapters.newFactory(AtomicLong.class, Gson.atomicLongAdapter(typeAdapter)));
        arrayList.add(TypeAdapters.newFactory(AtomicLongArray.class, Gson.atomicLongArrayAdapter(typeAdapter)));
        arrayList.add(TypeAdapters.ATOMIC_INTEGER_ARRAY_FACTORY);
        arrayList.add(TypeAdapters.CHARACTER_FACTORY);
        arrayList.add(TypeAdapters.STRING_BUILDER_FACTORY);
        arrayList.add(TypeAdapters.STRING_BUFFER_FACTORY);
        arrayList.add(TypeAdapters.newFactory(BigDecimal.class, TypeAdapters.BIG_DECIMAL));
        arrayList.add(TypeAdapters.newFactory(BigInteger.class, TypeAdapters.BIG_INTEGER));
        arrayList.add(TypeAdapters.URL_FACTORY);
        arrayList.add(TypeAdapters.URI_FACTORY);
        arrayList.add(TypeAdapters.UUID_FACTORY);
        arrayList.add(TypeAdapters.CURRENCY_FACTORY);
        arrayList.add(TypeAdapters.LOCALE_FACTORY);
        arrayList.add(TypeAdapters.INET_ADDRESS_FACTORY);
        arrayList.add(TypeAdapters.BIT_SET_FACTORY);
        arrayList.add(DateTypeAdapter.FACTORY);
        arrayList.add(TypeAdapters.CALENDAR_FACTORY);
        arrayList.add(TimeTypeAdapter.FACTORY);
        arrayList.add(SqlDateTypeAdapter.FACTORY);
        arrayList.add(TypeAdapters.TIMESTAMP_FACTORY);
        arrayList.add(ArrayTypeAdapter.FACTORY);
        arrayList.add(TypeAdapters.CLASS_FACTORY);
        arrayList.add(new CollectionTypeAdapterFactory(this.constructorConstructor));
        arrayList.add(new MapTypeAdapterFactory(this.constructorConstructor, bl2));
        arrayList.add(new JsonAdapterAnnotationTypeAdapterFactory(this.constructorConstructor));
        arrayList.add(TypeAdapters.ENUM_FACTORY);
        arrayList.add(new ReflectiveTypeAdapterFactory(this.constructorConstructor, fieldNamingStrategy, excluder));
        this.factories = Collections.unmodifiableList(arrayList);
    }

    private TypeAdapter<Number> doubleAdapter(boolean bl) {
        if (bl) {
            return TypeAdapters.DOUBLE;
        }
        return new TypeAdapter<Number>(){

            @Override
            public Double read(JsonReader jsonReader) {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return jsonReader.nextDouble();
            }

            @Override
            public void write(JsonWriter jsonWriter, Number number) {
                if (number == null) {
                    jsonWriter.nullValue();
                    return;
                }
                double d = number.doubleValue();
                Gson.checkValidFloatingPoint(d);
                jsonWriter.value(number);
            }
        };
    }

    private TypeAdapter<Number> floatAdapter(boolean bl) {
        if (bl) {
            return TypeAdapters.FLOAT;
        }
        return new TypeAdapter<Number>(){

            @Override
            public Float read(JsonReader jsonReader) {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return Float.valueOf((float)jsonReader.nextDouble());
            }

            @Override
            public void write(JsonWriter jsonWriter, Number number) {
                if (number == null) {
                    jsonWriter.nullValue();
                    return;
                }
                float f = number.floatValue();
                Gson.checkValidFloatingPoint(f);
                jsonWriter.value(number);
            }
        };
    }

    static void checkValidFloatingPoint(double d) {
        if (Double.isNaN(d) || Double.isInfinite(d)) {
            throw new IllegalArgumentException("" + d + " is not a valid double value as per JSON specification. To override this" + " behavior, use GsonBuilder.serializeSpecialFloatingPointValues() method.");
        }
    }

    private static TypeAdapter<Number> longAdapter(LongSerializationPolicy longSerializationPolicy) {
        if (longSerializationPolicy == LongSerializationPolicy.DEFAULT) {
            return TypeAdapters.LONG;
        }
        return new TypeAdapter<Number>(){

            @Override
            public Number read(JsonReader jsonReader) {
                if (jsonReader.peek() == JsonToken.NULL) {
                    jsonReader.nextNull();
                    return null;
                }
                return jsonReader.nextLong();
            }

            @Override
            public void write(JsonWriter jsonWriter, Number number) {
                if (number == null) {
                    jsonWriter.nullValue();
                    return;
                }
                jsonWriter.value(number.toString());
            }
        };
    }

    private static TypeAdapter<AtomicLong> atomicLongAdapter(final TypeAdapter<Number> typeAdapter) {
        return new TypeAdapter<AtomicLong>(){

            @Override
            public void write(JsonWriter jsonWriter, AtomicLong atomicLong) {
                typeAdapter.write(jsonWriter, atomicLong.get());
            }

            @Override
            public AtomicLong read(JsonReader jsonReader) {
                Number number = (Number)typeAdapter.read(jsonReader);
                return new AtomicLong(number.longValue());
            }
        }.nullSafe();
    }

    private static TypeAdapter<AtomicLongArray> atomicLongArrayAdapter(final TypeAdapter<Number> typeAdapter) {
        return new TypeAdapter<AtomicLongArray>(){

            @Override
            public void write(JsonWriter jsonWriter, AtomicLongArray atomicLongArray) {
                jsonWriter.beginArray();
                int n = atomicLongArray.length();
                for (int i = 0; i < n; ++i) {
                    typeAdapter.write(jsonWriter, atomicLongArray.get(i));
                }
                jsonWriter.endArray();
            }

            @Override
            public AtomicLongArray read(JsonReader jsonReader) {
                ArrayList<Long> arrayList = new ArrayList<Long>();
                jsonReader.beginArray();
                while (jsonReader.hasNext()) {
                    long l = ((Number)typeAdapter.read(jsonReader)).longValue();
                    arrayList.add(l);
                }
                jsonReader.endArray();
                int n = arrayList.size();
                AtomicLongArray atomicLongArray = new AtomicLongArray(n);
                for (int i = 0; i < n; ++i) {
                    atomicLongArray.set(i, (Long)arrayList.get(i));
                }
                return atomicLongArray;
            }
        }.nullSafe();
    }

    public <T> TypeAdapter<T> getAdapter(TypeToken<T> typeToken) {
        FutureTypeAdapter futureTypeAdapter;
        TypeAdapter typeAdapter = this.typeTokenCache.get(typeToken);
        if (typeAdapter != null) {
            return typeAdapter;
        }
        Map map = this.calls.get();
        boolean bl = false;
        if (map == null) {
            map = new HashMap();
            this.calls.set(map);
            bl = true;
        }
        if ((futureTypeAdapter = map.get(typeToken)) != null) {
            return futureTypeAdapter;
        }
        try {
            FutureTypeAdapter<T> futureTypeAdapter2 = new FutureTypeAdapter<T>();
            map.put(typeToken, futureTypeAdapter2);
            for (TypeAdapterFactory typeAdapterFactory : this.factories) {
                TypeAdapter<T> typeAdapter2 = typeAdapterFactory.create(this, typeToken);
                if (typeAdapter2 == null) continue;
                futureTypeAdapter2.setDelegate(typeAdapter2);
                this.typeTokenCache.put(typeToken, typeAdapter2);
                TypeAdapter<T> typeAdapter3 = typeAdapter2;
                return typeAdapter3;
            }
            throw new IllegalArgumentException("GSON cannot handle " + typeToken);
        }
        finally {
            map.remove(typeToken);
            if (bl) {
                this.calls.remove();
            }
        }
    }

    public <T> TypeAdapter<T> getDelegateAdapter(TypeAdapterFactory typeAdapterFactory, TypeToken<T> typeToken) {
        boolean bl = false;
        if (!this.factories.contains(typeAdapterFactory)) {
            bl = true;
        }
        for (TypeAdapterFactory typeAdapterFactory2 : this.factories) {
            if (!bl) {
                if (typeAdapterFactory2 != typeAdapterFactory) continue;
                bl = true;
                continue;
            }
            TypeAdapter<T> typeAdapter = typeAdapterFactory2.create(this, typeToken);
            if (typeAdapter == null) continue;
            return typeAdapter;
        }
        throw new IllegalArgumentException("GSON cannot serialize " + typeToken);
    }

    public <T> TypeAdapter<T> getAdapter(Class<T> class_) {
        return this.getAdapter(TypeToken.get(class_));
    }

    public JsonElement toJsonTree(Object object) {
        if (object == null) {
            return JsonNull.INSTANCE;
        }
        return this.toJsonTree(object, object.getClass());
    }

    public JsonElement toJsonTree(Object object, Type type) {
        JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
        this.toJson(object, type, jsonTreeWriter);
        return jsonTreeWriter.get();
    }

    public String toJson(Object object) {
        if (object == null) {
            return this.toJson(JsonNull.INSTANCE);
        }
        return this.toJson(object, object.getClass());
    }

    public String toJson(Object object, Type type) {
        StringWriter stringWriter = new StringWriter();
        this.toJson(object, type, stringWriter);
        return stringWriter.toString();
    }

    public void toJson(Object object, Appendable appendable) {
        if (object != null) {
            this.toJson(object, object.getClass(), appendable);
        } else {
            this.toJson((JsonElement)JsonNull.INSTANCE, appendable);
        }
    }

    public void toJson(Object object, Type type, Appendable appendable) {
        try {
            JsonWriter jsonWriter = this.newJsonWriter(Streams.writerForAppendable(appendable));
            this.toJson(object, type, jsonWriter);
        }
        catch (IOException var4_5) {
            throw new JsonIOException(var4_5);
        }
    }

    public void toJson(Object object, Type type, JsonWriter jsonWriter) {
        TypeAdapter typeAdapter = this.getAdapter(TypeToken.get(type));
        boolean bl = jsonWriter.isLenient();
        jsonWriter.setLenient(true);
        boolean bl2 = jsonWriter.isHtmlSafe();
        jsonWriter.setHtmlSafe(this.htmlSafe);
        boolean bl3 = jsonWriter.getSerializeNulls();
        jsonWriter.setSerializeNulls(this.serializeNulls);
        try {
            typeAdapter.write(jsonWriter, (Object)object);
        }
        catch (IOException var8_8) {
            throw new JsonIOException(var8_8);
        }
        finally {
            jsonWriter.setLenient(bl);
            jsonWriter.setHtmlSafe(bl2);
            jsonWriter.setSerializeNulls(bl3);
        }
    }

    public String toJson(JsonElement jsonElement) {
        StringWriter stringWriter = new StringWriter();
        this.toJson(jsonElement, (Appendable)stringWriter);
        return stringWriter.toString();
    }

    public void toJson(JsonElement jsonElement, Appendable appendable) {
        try {
            JsonWriter jsonWriter = this.newJsonWriter(Streams.writerForAppendable(appendable));
            this.toJson(jsonElement, jsonWriter);
        }
        catch (IOException var3_4) {
            throw new RuntimeException(var3_4);
        }
    }

    public JsonWriter newJsonWriter(Writer writer) {
        if (this.generateNonExecutableJson) {
            writer.write(")]}'\n");
        }
        JsonWriter jsonWriter = new JsonWriter(writer);
        if (this.prettyPrinting) {
            jsonWriter.setIndent("  ");
        }
        jsonWriter.setSerializeNulls(this.serializeNulls);
        return jsonWriter;
    }

    public JsonReader newJsonReader(Reader reader) {
        JsonReader jsonReader = new JsonReader(reader);
        jsonReader.setLenient(this.lenient);
        return jsonReader;
    }

    public void toJson(JsonElement jsonElement, JsonWriter jsonWriter) {
        boolean bl = jsonWriter.isLenient();
        jsonWriter.setLenient(true);
        boolean bl2 = jsonWriter.isHtmlSafe();
        jsonWriter.setHtmlSafe(this.htmlSafe);
        boolean bl3 = jsonWriter.getSerializeNulls();
        jsonWriter.setSerializeNulls(this.serializeNulls);
        try {
            Streams.write(jsonElement, jsonWriter);
        }
        catch (IOException var6_6) {
            throw new JsonIOException(var6_6);
        }
        finally {
            jsonWriter.setLenient(bl);
            jsonWriter.setHtmlSafe(bl2);
            jsonWriter.setSerializeNulls(bl3);
        }
    }

    public <T> T fromJson(String string, Class<T> class_) {
        T t = this.fromJson(string, (Type)class_);
        return Primitives.wrap(class_).cast(t);
    }

    public <T> T fromJson(String string, Type type) {
        if (string == null) {
            return null;
        }
        StringReader stringReader = new StringReader(string);
        T t = this.fromJson((Reader)stringReader, type);
        return t;
    }

    public <T> T fromJson(Reader reader, Class<T> class_) {
        JsonReader jsonReader = this.newJsonReader(reader);
        T t = this.fromJson(jsonReader, class_);
        Gson.assertFullConsumption(t, jsonReader);
        return Primitives.wrap(class_).cast(t);
    }

    public <T> T fromJson(Reader reader, Type type) {
        JsonReader jsonReader = this.newJsonReader(reader);
        T t = this.fromJson(jsonReader, type);
        Gson.assertFullConsumption(t, jsonReader);
        return t;
    }

    private static void assertFullConsumption(Object object, JsonReader jsonReader) {
        try {
            if (object != null && jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw new JsonIOException("JSON document was not fully consumed.");
            }
        }
        catch (MalformedJsonException var2_2) {
            throw new JsonSyntaxException(var2_2);
        }
        catch (IOException var2_3) {
            throw new JsonIOException(var2_3);
        }
    }

    public <T> T fromJson(JsonReader jsonReader, Type type) {
        boolean bl = true;
        boolean bl2 = jsonReader.isLenient();
        jsonReader.setLenient(true);
        try {
            Object obj;
            jsonReader.peek();
            bl = false;
            TypeToken typeToken = TypeToken.get(type);
            TypeAdapter typeAdapter = this.getAdapter(typeToken);
            Object obj2 = obj = typeAdapter.read(jsonReader);
            return (T)obj2;
        }
        catch (EOFException var5_6) {
            if (bl) {
                T t = null;
                return t;
            }
            throw new JsonSyntaxException(var5_6);
        }
        catch (IllegalStateException var5_7) {
            throw new JsonSyntaxException(var5_7);
        }
        catch (IOException var5_8) {
            throw new JsonSyntaxException(var5_8);
        }
        finally {
            jsonReader.setLenient(bl2);
        }
    }

    public <T> T fromJson(JsonElement jsonElement, Class<T> class_) {
        T t = this.fromJson(jsonElement, (Type)class_);
        return Primitives.wrap(class_).cast(t);
    }

    public <T> T fromJson(JsonElement jsonElement, Type type) {
        if (jsonElement == null) {
            return null;
        }
        return this.fromJson(new JsonTreeReader(jsonElement), type);
    }

    public String toString() {
        return "{serializeNulls:" + this.serializeNulls + "factories:" + this.factories + ",instanceCreators:" + this.constructorConstructor + "}";
    }

    static class FutureTypeAdapter<T>
    extends TypeAdapter<T> {
        private TypeAdapter<T> delegate;

        FutureTypeAdapter() {
        }

        public void setDelegate(TypeAdapter<T> typeAdapter) {
            if (this.delegate != null) {
                throw new AssertionError();
            }
            this.delegate = typeAdapter;
        }

        @Override
        public T read(JsonReader jsonReader) {
            if (this.delegate == null) {
                throw new IllegalStateException();
            }
            return this.delegate.read(jsonReader);
        }

        @Override
        public void write(JsonWriter jsonWriter, T t) {
            if (this.delegate == null) {
                throw new IllegalStateException();
            }
            this.delegate.write(jsonWriter, t);
        }
    }

}


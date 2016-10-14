/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 *  org.bukkit.configuration.serialization.ConfigurationSerialization
 */
package mkremins.fanciful;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.stream.JsonWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

public abstract class TextualComponent
implements Cloneable {
    static {
        ConfigurationSerialization.registerClass(ArbitraryTextTypeComponent.class);
        ConfigurationSerialization.registerClass(ComplexTextTypeComponent.class);
    }

    public String toString() {
        return this.getReadableString();
    }

    public abstract String getKey();

    public abstract String getReadableString();

    public abstract TextualComponent clone();

    public abstract void writeJson(JsonWriter var1);

    static TextualComponent deserialize(Map<String, Object> map) {
        if (map.containsKey("key") && map.size() == 2 && map.containsKey("value")) {
            return ArbitraryTextTypeComponent.deserialize(map);
        }
        if (map.size() >= 2 && map.containsKey("key") && !map.containsKey("value")) {
            return ComplexTextTypeComponent.deserialize(map);
        }
        return null;
    }

    static boolean isTextKey(String string) {
        if (!(string.equals("translate") || string.equals("text") || string.equals("score") || string.equals("selector"))) {
            return false;
        }
        return true;
    }

    static boolean isTranslatableText(TextualComponent textualComponent) {
        if (textualComponent instanceof ComplexTextTypeComponent && ((ComplexTextTypeComponent)textualComponent).getKey().equals("translate")) {
            return true;
        }
        return false;
    }

    public static TextualComponent rawText(String string) {
        return new ArbitraryTextTypeComponent("text", string);
    }

    public static TextualComponent localizedText(String string) {
        return new ArbitraryTextTypeComponent("translate", string);
    }

    private static void throwUnsupportedSnapshot() {
        throw new UnsupportedOperationException("This feature is only supported in snapshot releases.");
    }

    public static TextualComponent objectiveScore(String string) {
        return TextualComponent.objectiveScore("*", string);
    }

    public static TextualComponent objectiveScore(String string, String string2) {
        TextualComponent.throwUnsupportedSnapshot();
        return new ComplexTextTypeComponent("score", (Map<String, String>)ImmutableMap.builder().put((Object)"name", (Object)string).put((Object)"objective", (Object)string2).build());
    }

    public static TextualComponent selector(String string) {
        TextualComponent.throwUnsupportedSnapshot();
        return new ArbitraryTextTypeComponent("selector", string);
    }

    private static final class ArbitraryTextTypeComponent
    extends TextualComponent
    implements ConfigurationSerializable {
        private String _key;
        private String _value;

        public ArbitraryTextTypeComponent(String string, String string2) {
            this.setKey(string);
            this.setValue(string2);
        }

        @Override
        public String getKey() {
            return this._key;
        }

        public void setKey(String string) {
            Preconditions.checkArgument((boolean)(string != null && !string.isEmpty()), (Object)"The key must be specified.");
            this._key = string;
        }

        public String getValue() {
            return this._value;
        }

        public void setValue(String string) {
            Preconditions.checkArgument((boolean)(string != null), (Object)"The value must be specified.");
            this._value = string;
        }

        @Override
        public TextualComponent clone() {
            return new ArbitraryTextTypeComponent(this.getKey(), this.getValue());
        }

        @Override
        public void writeJson(JsonWriter jsonWriter) {
            jsonWriter.name(this.getKey()).value(this.getValue());
        }

        public Map<String, Object> serialize() {
            return new HashMap<String, Object>(){};
        }

        public static ArbitraryTextTypeComponent deserialize(Map<String, Object> map) {
            return new ArbitraryTextTypeComponent(map.get("key").toString(), map.get("value").toString());
        }

        @Override
        public String getReadableString() {
            return this.getValue();
        }

    }

    private static final class ComplexTextTypeComponent
    extends TextualComponent
    implements ConfigurationSerializable {
        private String _key;
        private Map<String, String> _value;

        public ComplexTextTypeComponent(String string, Map<String, String> map) {
            this.setKey(string);
            this.setValue(map);
        }

        @Override
        public String getKey() {
            return this._key;
        }

        public void setKey(String string) {
            Preconditions.checkArgument((boolean)(string != null && !string.isEmpty()), (Object)"The key must be specified.");
            this._key = string;
        }

        public Map<String, String> getValue() {
            return this._value;
        }

        public void setValue(Map<String, String> map) {
            Preconditions.checkArgument((boolean)(map != null), (Object)"The value must be specified.");
            this._value = map;
        }

        @Override
        public TextualComponent clone() {
            return new ComplexTextTypeComponent(this.getKey(), this.getValue());
        }

        @Override
        public void writeJson(JsonWriter jsonWriter) {
            jsonWriter.name(this.getKey());
            jsonWriter.beginObject();
            for (Map.Entry<String, String> entry : this._value.entrySet()) {
                jsonWriter.name(entry.getKey()).value(entry.getValue());
            }
            jsonWriter.endObject();
        }

        public Map<String, Object> serialize() {
            return new HashMap<String, Object>(){};
        }

        public static ComplexTextTypeComponent deserialize(Map<String, Object> map) {
            String string = null;
            HashMap<String, String> hashMap = new HashMap<String, String>();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().equals("key")) {
                    string = (String)entry.getValue();
                    continue;
                }
                if (!entry.getKey().startsWith("value.")) continue;
                hashMap.put(entry.getKey().substring(6), entry.getValue().toString());
            }
            return new ComplexTextTypeComponent(string, hashMap);
        }

        @Override
        public String getReadableString() {
            return this.getKey();
        }

    }

}


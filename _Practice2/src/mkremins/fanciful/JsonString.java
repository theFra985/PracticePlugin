/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 */
package mkremins.fanciful;

import com.google.gson.stream.JsonWriter;
import java.util.HashMap;
import java.util.Map;
import mkremins.fanciful.JsonRepresentedObject;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

final class JsonString
implements JsonRepresentedObject,
ConfigurationSerializable {
    private String _value;

    public JsonString(CharSequence charSequence) {
        this._value = charSequence == null ? null : charSequence.toString();
    }

    @Override
    public void writeJson(JsonWriter jsonWriter) {
        jsonWriter.value(this.getValue());
    }

    public String getValue() {
        return this._value;
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("stringValue", this._value);
        return hashMap;
    }

    public static JsonString deserialize(Map<String, Object> map) {
        return new JsonString(map.get("stringValue").toString());
    }

    public String toString() {
        return this._value;
    }
}


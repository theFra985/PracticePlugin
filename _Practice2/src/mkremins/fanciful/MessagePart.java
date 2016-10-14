/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.BiMap
 *  com.google.common.collect.ImmutableBiMap
 *  com.google.common.collect.ImmutableBiMap$Builder
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 *  org.bukkit.configuration.serialization.ConfigurationSerialization
 */
package mkremins.fanciful;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import mkremins.fanciful.FancyMessage;
import mkremins.fanciful.JsonRepresentedObject;
import mkremins.fanciful.JsonString;
import mkremins.fanciful.TextualComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

final class MessagePart
implements JsonRepresentedObject,
ConfigurationSerializable,
Cloneable {
    ChatColor color = ChatColor.WHITE;
    ArrayList<ChatColor> styles = new ArrayList();
    String clickActionName = null;
    String clickActionData = null;
    String hoverActionName = null;
    JsonRepresentedObject hoverActionData = null;
    TextualComponent text = null;
    String insertionData = null;
    ArrayList<JsonRepresentedObject> translationReplacements = new ArrayList();
    static final BiMap<ChatColor, String> stylesToNames;
    private static /* synthetic */ int[] $SWITCH_TABLE$org$bukkit$ChatColor;

    static {
        ImmutableBiMap.Builder builder = ImmutableBiMap.builder();
        ChatColor[] arrchatColor = ChatColor.values();
        int n = arrchatColor.length;
        int n2 = 0;
        while (n2 < n) {
            ChatColor chatColor = arrchatColor[n2];
            if (chatColor.isFormat()) {
                String string;
                switch (MessagePart.$SWITCH_TABLE$org$bukkit$ChatColor()[chatColor.ordinal()]) {
                    case 17: {
                        string = "obfuscated";
                        break;
                    }
                    case 20: {
                        string = "underlined";
                        break;
                    }
                    default: {
                        string = chatColor.name().toLowerCase();
                    }
                }
                builder.put((Object)chatColor, (Object)string);
            }
            ++n2;
        }
        stylesToNames = builder.build();
        ConfigurationSerialization.registerClass(MessagePart.class);
    }

    MessagePart(TextualComponent textualComponent) {
        this.text = textualComponent;
    }

    MessagePart() {
        this.text = null;
    }

    boolean hasText() {
        if (this.text != null) {
            return true;
        }
        return false;
    }

    public MessagePart clone() {
        MessagePart messagePart = (MessagePart)super.clone();
        messagePart.styles = (ArrayList)this.styles.clone();
        if (this.hoverActionData instanceof JsonString) {
            messagePart.hoverActionData = new JsonString(((JsonString)this.hoverActionData).getValue());
        } else if (this.hoverActionData instanceof FancyMessage) {
            messagePart.hoverActionData = ((FancyMessage)this.hoverActionData).clone();
        }
        messagePart.translationReplacements = (ArrayList)this.translationReplacements.clone();
        return messagePart;
    }

    @Override
    public void writeJson(JsonWriter jsonWriter) {
        try {
            jsonWriter.beginObject();
            this.text.writeJson(jsonWriter);
            jsonWriter.name("color").value(this.color.name().toLowerCase());
            for (ChatColor object2 : this.styles) {
                jsonWriter.name((String)stylesToNames.get((Object)object2)).value(true);
            }
            if (this.clickActionName != null && this.clickActionData != null) {
                jsonWriter.name("clickEvent").beginObject().name("action").value(this.clickActionName).name("value").value(this.clickActionData).endObject();
            }
            if (this.hoverActionName != null && this.hoverActionData != null) {
                jsonWriter.name("hoverEvent").beginObject().name("action").value(this.hoverActionName).name("value");
                this.hoverActionData.writeJson(jsonWriter);
                jsonWriter.endObject();
            }
            if (this.insertionData != null) {
                jsonWriter.name("insertion").value(this.insertionData);
            }
            if (this.translationReplacements.size() > 0 && this.text != null && TextualComponent.isTranslatableText(this.text)) {
                jsonWriter.name("with").beginArray();
                for (JsonRepresentedObject jsonRepresentedObject : this.translationReplacements) {
                    jsonRepresentedObject.writeJson(jsonWriter);
                }
                jsonWriter.endArray();
            }
            jsonWriter.endObject();
        }
        catch (IOException var2_6) {
            Bukkit.getLogger().log(Level.WARNING, "A problem occured during writing of JSON string", var2_6);
        }
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("text", this.text);
        hashMap.put("styles", this.styles);
        hashMap.put("color", Character.valueOf(this.color.getChar()));
        hashMap.put("hoverActionName", this.hoverActionName);
        hashMap.put("hoverActionData", this.hoverActionData);
        hashMap.put("clickActionName", this.clickActionName);
        hashMap.put("clickActionData", this.clickActionData);
        hashMap.put("insertion", this.insertionData);
        hashMap.put("translationReplacements", this.translationReplacements);
        return hashMap;
    }

    public static MessagePart deserialize(Map<String, Object> map) {
        MessagePart messagePart = new MessagePart((TextualComponent)map.get("text"));
        messagePart.styles = (ArrayList)map.get("styles");
        messagePart.color = ChatColor.getByChar((String)map.get("color").toString());
        messagePart.hoverActionName = (String)map.get("hoverActionName");
        messagePart.hoverActionData = (JsonRepresentedObject)map.get("hoverActionData");
        messagePart.clickActionName = (String)map.get("clickActionName");
        messagePart.clickActionData = (String)map.get("clickActionData");
        messagePart.insertionData = (String)map.get("insertion");
        messagePart.translationReplacements = (ArrayList)map.get("translationReplacements");
        return messagePart;
    }

    static /* synthetic */ int[] $SWITCH_TABLE$org$bukkit$ChatColor() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$org$bukkit$ChatColor;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[ChatColor.values().length];
        try {
            arrn[ChatColor.AQUA.ordinal()] = 12;
        }
        catch (NoSuchFieldError v1) {}
        try {
            arrn[ChatColor.BLACK.ordinal()] = 1;
        }
        catch (NoSuchFieldError v2) {}
        try {
            arrn[ChatColor.BLUE.ordinal()] = 10;
        }
        catch (NoSuchFieldError v3) {}
        try {
            arrn[ChatColor.BOLD.ordinal()] = 18;
        }
        catch (NoSuchFieldError v4) {}
        try {
            arrn[ChatColor.DARK_AQUA.ordinal()] = 4;
        }
        catch (NoSuchFieldError v5) {}
        try {
            arrn[ChatColor.DARK_BLUE.ordinal()] = 2;
        }
        catch (NoSuchFieldError v6) {}
        try {
            arrn[ChatColor.DARK_GRAY.ordinal()] = 9;
        }
        catch (NoSuchFieldError v7) {}
        try {
            arrn[ChatColor.DARK_GREEN.ordinal()] = 3;
        }
        catch (NoSuchFieldError v8) {}
        try {
            arrn[ChatColor.DARK_PURPLE.ordinal()] = 6;
        }
        catch (NoSuchFieldError v9) {}
        try {
            arrn[ChatColor.DARK_RED.ordinal()] = 5;
        }
        catch (NoSuchFieldError v10) {}
        try {
            arrn[ChatColor.GOLD.ordinal()] = 7;
        }
        catch (NoSuchFieldError v11) {}
        try {
            arrn[ChatColor.GRAY.ordinal()] = 8;
        }
        catch (NoSuchFieldError v12) {}
        try {
            arrn[ChatColor.GREEN.ordinal()] = 11;
        }
        catch (NoSuchFieldError v13) {}
        try {
            arrn[ChatColor.ITALIC.ordinal()] = 21;
        }
        catch (NoSuchFieldError v14) {}
        try {
            arrn[ChatColor.LIGHT_PURPLE.ordinal()] = 14;
        }
        catch (NoSuchFieldError v15) {}
        try {
            arrn[ChatColor.MAGIC.ordinal()] = 17;
        }
        catch (NoSuchFieldError v16) {}
        try {
            arrn[ChatColor.RED.ordinal()] = 13;
        }
        catch (NoSuchFieldError v17) {}
        try {
            arrn[ChatColor.RESET.ordinal()] = 22;
        }
        catch (NoSuchFieldError v18) {}
        try {
            arrn[ChatColor.STRIKETHROUGH.ordinal()] = 19;
        }
        catch (NoSuchFieldError v19) {}
        try {
            arrn[ChatColor.UNDERLINE.ordinal()] = 20;
        }
        catch (NoSuchFieldError v20) {}
        try {
            arrn[ChatColor.WHITE.ordinal()] = 16;
        }
        catch (NoSuchFieldError v21) {}
        try {
            arrn[ChatColor.YELLOW.ordinal()] = 15;
        }
        catch (NoSuchFieldError v22) {}
        $SWITCH_TABLE$org$bukkit$ChatColor = arrn;
        return $SWITCH_TABLE$org$bukkit$ChatColor;
    }
}


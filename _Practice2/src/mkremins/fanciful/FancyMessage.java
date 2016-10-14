/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.BiMap
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.serialization.ConfigurationSerializable
 *  org.bukkit.configuration.serialization.ConfigurationSerialization
 *  org.bukkit.entity.Player
 */
package mkremins.fanciful;

import com.google.common.collect.BiMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import mkremins.fanciful.ArrayWrapper;
import mkremins.fanciful.JsonRepresentedObject;
import mkremins.fanciful.JsonString;
import mkremins.fanciful.MessagePart;
import mkremins.fanciful.TextualComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;

public class FancyMessage
implements JsonRepresentedObject,
Cloneable,
Iterable<MessagePart>,
ConfigurationSerializable {
    private List<MessagePart> messageParts = new ArrayList<MessagePart>();
    private String jsonString;
    private boolean dirty;
    private static JsonParser _stringParser;

    static {
        ConfigurationSerialization.registerClass(FancyMessage.class);
        _stringParser = new JsonParser();
    }

    public FancyMessage clone() {
        FancyMessage fancyMessage = (FancyMessage)super.clone();
        fancyMessage.messageParts = new ArrayList<MessagePart>(this.messageParts.size());
        int n = 0;
        while (n < this.messageParts.size()) {
            fancyMessage.messageParts.add(n, this.messageParts.get(n).clone());
            ++n;
        }
        fancyMessage.dirty = false;
        fancyMessage.jsonString = null;
        return fancyMessage;
    }

    public FancyMessage(String string) {
        this(TextualComponent.rawText(string));
    }

    public FancyMessage(TextualComponent textualComponent) {
        this.messageParts.add(new MessagePart(textualComponent));
        this.jsonString = null;
        this.dirty = false;
    }

    public FancyMessage() {
        this((TextualComponent)null);
    }

    public FancyMessage text(String string) {
        MessagePart messagePart = this.latest();
        messagePart.text = TextualComponent.rawText(string);
        this.dirty = true;
        return this;
    }

    public FancyMessage text(TextualComponent textualComponent) {
        MessagePart messagePart = this.latest();
        messagePart.text = textualComponent;
        this.dirty = true;
        return this;
    }

    public FancyMessage color(ChatColor chatColor) {
        if (!chatColor.isColor()) {
            throw new IllegalArgumentException(String.valueOf(chatColor.name()) + " is not a color");
        }
        this.latest().color = chatColor;
        this.dirty = true;
        return this;
    }

    public /* varargs */ FancyMessage style(ChatColor ... arrchatColor) {
        ChatColor[] arrchatColor2 = arrchatColor;
        int n = arrchatColor2.length;
        int n2 = 0;
        while (n2 < n) {
            ChatColor chatColor = arrchatColor2[n2];
            if (!chatColor.isFormat()) {
                throw new IllegalArgumentException(String.valueOf(chatColor.name()) + " is not a style");
            }
            ++n2;
        }
        this.latest().styles.addAll(Arrays.asList(arrchatColor));
        this.dirty = true;
        return this;
    }

    public FancyMessage file(String string) {
        this.onClick("open_file", string);
        return this;
    }

    public FancyMessage link(String string) {
        this.onClick("open_url", string);
        return this;
    }

    public FancyMessage suggest(String string) {
        this.onClick("suggest_command", string);
        return this;
    }

    public FancyMessage insert(String string) {
        this.latest().insertionData = string;
        this.dirty = true;
        return this;
    }

    public FancyMessage command(String string) {
        this.onClick("run_command", string);
        return this;
    }

    public FancyMessage achievementTooltip(String string) {
        this.onHover("show_achievement", new JsonString("achievement." + string));
        return this;
    }

    public FancyMessage tooltip(String string) {
        this.onHover("show_text", new JsonString(string));
        return this;
    }

    public FancyMessage tooltip(Iterable<String> iterable) {
        this.tooltip(ArrayWrapper.toArray(iterable, String.class));
        return this;
    }

    public /* varargs */ FancyMessage tooltip(String ... arrstring) {
        StringBuilder stringBuilder = new StringBuilder();
        int n = 0;
        while (n < arrstring.length) {
            stringBuilder.append(arrstring[n]);
            if (n != arrstring.length - 1) {
                stringBuilder.append('\n');
            }
            ++n;
        }
        this.tooltip(stringBuilder.toString());
        return this;
    }

    public FancyMessage formattedTooltip(FancyMessage fancyMessage) {
        for (MessagePart messagePart : fancyMessage.messageParts) {
            if (messagePart.clickActionData != null && messagePart.clickActionName != null) {
                throw new IllegalArgumentException("The tooltip text cannot have click data.");
            }
            if (messagePart.hoverActionData == null || messagePart.hoverActionName == null) continue;
            throw new IllegalArgumentException("The tooltip text cannot have a tooltip.");
        }
        this.onHover("show_text", fancyMessage);
        return this;
    }

    public /* varargs */ FancyMessage formattedTooltip(FancyMessage ... arrfancyMessage) {
        if (arrfancyMessage.length < 1) {
            this.onHover(null, null);
            return this;
        }
        FancyMessage fancyMessage = new FancyMessage();
        fancyMessage.messageParts.clear();
        int n = 0;
        while (n < arrfancyMessage.length) {
            try {
                for (MessagePart messagePart : arrfancyMessage[n]) {
                    if (messagePart.clickActionData != null && messagePart.clickActionName != null) {
                        throw new IllegalArgumentException("The tooltip text cannot have click data.");
                    }
                    if (messagePart.hoverActionData != null && messagePart.hoverActionName != null) {
                        throw new IllegalArgumentException("The tooltip text cannot have a tooltip.");
                    }
                    if (!messagePart.hasText()) continue;
                    fancyMessage.messageParts.add(messagePart.clone());
                }
                if (n != arrfancyMessage.length - 1) {
                    fancyMessage.messageParts.add(new MessagePart(TextualComponent.rawText("\n")));
                }
            }
            catch (CloneNotSupportedException var4_5) {
                Bukkit.getLogger().log(Level.WARNING, "Failed to clone object", var4_5);
                return this;
            }
            ++n;
        }
        return this.formattedTooltip(fancyMessage.messageParts.isEmpty() ? null : fancyMessage);
    }

    public FancyMessage formattedTooltip(Iterable<FancyMessage> iterable) {
        return this.formattedTooltip(ArrayWrapper.toArray(iterable, FancyMessage.class));
    }

    public /* varargs */ FancyMessage translationReplacements(String ... arrstring) {
        String[] arrstring2 = arrstring;
        int n = arrstring2.length;
        int n2 = 0;
        while (n2 < n) {
            String string = arrstring2[n2];
            this.latest().translationReplacements.add(new JsonString(string));
            ++n2;
        }
        this.dirty = true;
        return this;
    }

    public /* varargs */ FancyMessage translationReplacements(FancyMessage ... arrfancyMessage) {
        FancyMessage[] arrfancyMessage2 = arrfancyMessage;
        int n = arrfancyMessage2.length;
        int n2 = 0;
        while (n2 < n) {
            FancyMessage fancyMessage = arrfancyMessage2[n2];
            this.latest().translationReplacements.add(fancyMessage);
            ++n2;
        }
        this.dirty = true;
        return this;
    }

    public FancyMessage translationReplacements(Iterable<FancyMessage> iterable) {
        return this.translationReplacements(ArrayWrapper.toArray(iterable, FancyMessage.class));
    }

    public FancyMessage then(String string) {
        return this.then(TextualComponent.rawText(string));
    }

    public FancyMessage then(TextualComponent textualComponent) {
        if (!this.latest().hasText()) {
            throw new IllegalStateException("previous message part has no text");
        }
        this.messageParts.add(new MessagePart(textualComponent));
        this.dirty = true;
        return this;
    }

    public FancyMessage then() {
        if (!this.latest().hasText()) {
            throw new IllegalStateException("previous message part has no text");
        }
        this.messageParts.add(new MessagePart());
        this.dirty = true;
        return this;
    }

    @Override
    public void writeJson(JsonWriter jsonWriter) {
        if (this.messageParts.size() == 1) {
            this.latest().writeJson(jsonWriter);
        } else {
            jsonWriter.beginObject().name("text").value("").name("extra").beginArray();
            for (MessagePart messagePart : this) {
                messagePart.writeJson(jsonWriter);
            }
            jsonWriter.endArray().endObject();
        }
    }

    public String toJSONString() {
        if (!this.dirty && this.jsonString != null) {
            return this.jsonString;
        }
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);
        try {
            this.writeJson(jsonWriter);
            jsonWriter.close();
        }
        catch (IOException var3_3) {
            throw new RuntimeException("invalid message");
        }
        this.jsonString = stringWriter.toString();
        this.dirty = false;
        return this.jsonString;
    }

    public void send(Player player) {
        this.send((CommandSender)player, this.toJSONString());
    }

    private void send(CommandSender commandSender, String string) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(this.toOldMessageFormat());
            return;
        }
        Player player = (Player)commandSender;
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), (String)("tellraw " + player.getName() + " " + string));
    }

    public void send(CommandSender commandSender) {
        this.send(commandSender, this.toJSONString());
    }

    public void send(Iterable<? extends CommandSender> iterable) {
        String string = this.toJSONString();
        for (CommandSender commandSender : iterable) {
            this.send(commandSender, string);
        }
    }

    public String toOldMessageFormat() {
        StringBuilder stringBuilder = new StringBuilder();
        for (MessagePart messagePart : this) {
            stringBuilder.append((Object)(messagePart.color == null ? "" : messagePart.color));
            for (ChatColor chatColor : messagePart.styles) {
                stringBuilder.append((Object)chatColor);
            }
            stringBuilder.append(messagePart.text);
        }
        return stringBuilder.toString();
    }

    private MessagePart latest() {
        return this.messageParts.get(this.messageParts.size() - 1);
    }

    private void onClick(String string, String string2) {
        MessagePart messagePart = this.latest();
        messagePart.clickActionName = string;
        messagePart.clickActionData = string2;
        this.dirty = true;
    }

    private void onHover(String string, JsonRepresentedObject jsonRepresentedObject) {
        MessagePart messagePart = this.latest();
        messagePart.hoverActionName = string;
        messagePart.hoverActionData = jsonRepresentedObject;
        this.dirty = true;
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("messageParts", this.messageParts);
        return hashMap;
    }

    public static FancyMessage deserialize(Map<String, Object> map) {
        FancyMessage fancyMessage = new FancyMessage();
        fancyMessage.messageParts = (List)map.get("messageParts");
        fancyMessage.jsonString = map.containsKey("JSON") ? map.get("JSON").toString() : null;
        fancyMessage.dirty = !map.containsKey("JSON");
        return fancyMessage;
    }

    @Override
    public Iterator<MessagePart> iterator() {
        return this.messageParts.iterator();
    }

    public static FancyMessage deserialize(String string) {
        JsonObject jsonObject = _stringParser.parse(string).getAsJsonObject();
        JsonArray jsonArray = jsonObject.getAsJsonArray("extra");
        FancyMessage fancyMessage = new FancyMessage();
        fancyMessage.messageParts.clear();
        for (JsonElement jsonElement : jsonArray) {
            MessagePart messagePart = new MessagePart();
            JsonObject jsonObject2 = jsonElement.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : jsonObject2.entrySet()) {
                Object object2;
                if (TextualComponent.isTextKey(entry.getKey())) {
                    object2 = new HashMap<String, String>();
                    object2.put((String)"key", (String)entry.getKey());
                    if (entry.getValue().isJsonPrimitive()) {
                        object2.put((String)"value", (String)entry.getValue().getAsString());
                    } else {
                        for (Map.Entry<String, JsonElement> entry2 : entry.getValue().getAsJsonObject().entrySet()) {
                            object2.put("value." + entry2.getKey(), entry2.getValue().getAsString());
                        }
                    }
                    messagePart.text = TextualComponent.deserialize(object2);
                    continue;
                }
                if (MessagePart.stylesToNames.inverse().containsKey((Object)entry.getKey())) {
                    if (!entry.getValue().getAsBoolean()) continue;
                    messagePart.styles.add((ChatColor)MessagePart.stylesToNames.inverse().get((Object)entry.getKey()));
                    continue;
                }
                if (entry.getKey().equals("color")) {
                    messagePart.color = ChatColor.valueOf((String)entry.getValue().getAsString().toUpperCase());
                    continue;
                }
                if (entry.getKey().equals("clickEvent")) {
                    object2 = entry.getValue().getAsJsonObject();
                    messagePart.clickActionName = object2.get("action").getAsString();
                    messagePart.clickActionData = object2.get("value").getAsString();
                    continue;
                }
                if (entry.getKey().equals("hoverEvent")) {
                    object2 = entry.getValue().getAsJsonObject();
                    messagePart.hoverActionName = object2.get("action").getAsString();
                    if (object2.get("value").isJsonPrimitive()) {
                        messagePart.hoverActionData = new JsonString(object2.get("value").getAsString());
                        continue;
                    }
                    messagePart.hoverActionData = FancyMessage.deserialize(object2.get("value").toString());
                    continue;
                }
                if (entry.getKey().equals("insertion")) {
                    messagePart.insertionData = entry.getValue().getAsString();
                    continue;
                }
                if (!entry.getKey().equals("with")) continue;
                for (Object object2 : entry.getValue().getAsJsonArray()) {
                    if (object2.isJsonPrimitive()) {
                        messagePart.translationReplacements.add(new JsonString(object2.getAsString()));
                        continue;
                    }
                    messagePart.translationReplacements.add(FancyMessage.deserialize(object2.toString()));
                }
            }
            fancyMessage.messageParts.add(messagePart);
        }
        return fancyMessage;
    }
}


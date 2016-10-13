package it.fastersetup.practice;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import it.fastersetup.practice.Main;
import it.fastersetup.practice.Api.ConfigLoader;
import it.fastersetup.practice.Api.Settings;

public class UserData {
    private ConfigLoader config;
    private String chat;
    private Main plugin;

    public UserData(Main pl) {
        this.plugin = pl;
        pl.getConfigLoader().setDefault("chatformat", "<prefix><sp><nc><name> &r&6&l> <cc><m>");
        this.chat = ChatColor.translateAlternateColorCodes((char)'&', (String)pl.getConfigLoader().getConfig().getString("chatformat"));
        this.config = new ConfigLoader(pl, "playersettings.yml");
        this.config.generateFile();
    }

    public Settings getSettings(UUID id) {
        if (this.config.getConfig().contains(id.toString())) {
            return Settings.fromString(this.config.getConfig().getString(id.toString()));
        }
        return new Settings();
    }

    public void saveSettings(UUID id, Settings settings) {
        this.config.set(id.toString(), settings.toString());
    }

    public String getChatFormat(UUID id) {
        String base = this.chat;
        Settings s = this.plugin.data.getSettings(id);
        String prefix = this.plugin.perm.getPrefix(this.plugin.ranks.getRank(id));
        base = UserData.replace(base, "prefix", prefix);
        base = UserData.replace(base, "sp", !prefix.isEmpty() ? " " : "");
        base = UserData.replace(base, "nc", s.nameColor());
        base = UserData.replace(base, "name", "%1$s");
        base = UserData.replace(base, "cc", String.valueOf(s.chatColor()) + (s.chatFormatRaw() != ChatColor.RESET ? s.chatFormat() : ""));
        base = UserData.replace(base, "m", "%2$s");
        return base;
    }

    private static String replace(String input, String ch, String replace) {
        return input.replaceAll(Pattern.quote("<" + ch + ">"), Matcher.quoteReplacement(replace));
    }
}


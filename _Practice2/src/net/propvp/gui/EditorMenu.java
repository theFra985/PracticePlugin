/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 */
package net.propvp.gui;

import java.util.Collection;
import java.util.Map;
import net.propvp.Practice;
import net.propvp.game.GameManager;
import net.propvp.game.GameMode;
import net.propvp.player.EditorManager;
import net.propvp.util.IconMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class EditorMenu {
    private static IconMenu menu;

    public static void init() {
        menu = new IconMenu((Object)ChatColor.GOLD + "Kit Editing", 18, optionClickEvent -> {
            GameMode gameMode = Practice.getBackend().getGameManager().getGameMode(optionClickEvent.getName());
            Practice.getBackend().getEditorManager().beginEditing(optionClickEvent.getPlayer(), gameMode);
            optionClickEvent.getPlayer().closeInventory();
        }
        , null);
        EditorMenu.update();
    }

    public static IconMenu getMenu() {
        return menu;
    }

    public static void update() {
        int n = 0;
        for (GameMode gameMode : Practice.getBackend().getGameManager().getGameModes().values()) {
            if (!gameMode.isEditable()) continue;
            menu.setOption(n, gameMode.getDisplay(), ChatColor.translateAlternateColorCodes((char)'&', (String)gameMode.getDisplayName()), "");
            ++n;
        }
    }
}


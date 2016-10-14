/*
 * Decompiled with CFR 0_118.
 */
package net.propvp.gui;

import net.propvp.gui.EditorMenu;
import net.propvp.gui.PartyMenu;
import net.propvp.gui.RankedMenu;
import net.propvp.gui.UnrankedMenu;

public class MenuManager {
    public static void createMenus() {
        RankedMenu.init();
        UnrankedMenu.init();
        PartyMenu.init();
        EditorMenu.init();
    }

    public static void updateMenus() {
        RankedMenu.update();
        UnrankedMenu.update();
        PartyMenu.update();
        EditorMenu.update();
    }
}


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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.propvp.Practice;
import net.propvp.game.GameManager;
import net.propvp.game.GameMode;
import net.propvp.game.ladder.Ladder;
import net.propvp.game.ladder.TvT;
import net.propvp.party.Party;
import net.propvp.player.DataManager;
import net.propvp.player.PlayerData;
import net.propvp.util.HiddenStringUtil;
import net.propvp.util.IconMenu;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PartyMenu {
    private static IconMenu menu;

    public static void init() {
        menu = new IconMenu((Object)ChatColor.BLUE + "Party Queues", 18, optionClickEvent -> {
            Player player = optionClickEvent.getPlayer();
            if (Practice.getBackend().getQueueStatus() != 1) {
                player.sendMessage((Object)ChatColor.RED + "Tried adding you to the queue but the queue system is down.");
                return;
            }
            PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
            Party party = playerData.getParty();
            if (party == null) {
                player.sendMessage((Object)ChatColor.RED + "You do not have a party and cannot join this queue.");
                return;
            }
            if (party.getMembers().size() < 0) {
                player.sendMessage((Object)ChatColor.RED + "You must have atleast 2 players in your party to join this queue.");
                return;
            }
            if (optionClickEvent.getName() != null) {
                player.closeInventory();
                Practice.getBackend().getGameManager().addToQueue(optionClickEvent.getUUID(), party);
                PartyMenu.update();
            }
        }
        , null);
        PartyMenu.update();
    }

    public static IconMenu getMenu() {
        return menu;
    }

    public static void update() {
        int n = 0;
        Collection<Ladder> collection = Practice.getBackend().getGameManager().getQueues().values();
        for (Ladder ladder : collection) {
            GameMode gameMode = ladder.getGame();
            if (!(ladder instanceof TvT)) continue;
            menu.setOption(n, gameMode.getDisplay(), ChatColor.translateAlternateColorCodes((char)'&', (String)gameMode.getDisplayName()), HiddenStringUtil.encodeString(ladder.getIdentifier().toString()), (Object)ChatColor.YELLOW + "In queue: " + (Object)ChatColor.GREEN + ladder.getAmountInQueue(), (Object)ChatColor.YELLOW + "In match: " + (Object)ChatColor.GREEN + ladder.getAmountInMatch());
            ++n;
        }
    }
}


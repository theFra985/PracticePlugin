/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.propvp.game.duel;

import java.util.UUID;
import net.propvp.Practice;
import net.propvp.game.GameMode;
import net.propvp.game.arena.Arena;
import net.propvp.player.DataManager;
import net.propvp.player.PlayerData;
import org.bukkit.entity.Player;

public class DuelInfo {
    private UUID identifier;
    private GameMode gameMode;
    private Arena arena;
    private Player player1;
    private Player player2;
    private boolean party;

    public DuelInfo(GameMode gameMode, Arena arena, Player player, Player player2) {
        this.gameMode = gameMode;
        this.arena = arena;
        this.player1 = player;
        this.player2 = player2;
        PlayerData playerData = Practice.getBackend().getDataManager().getData(player);
        if (playerData.hasParty()) {
            this.party = true;
        }
    }

    public UUID getIdentifier() {
        return this.identifier;
    }

    public GameMode getGameMode() {
        return this.gameMode;
    }

    public Arena getArena() {
        return this.arena;
    }

    public Player getPlayer1() {
        return this.player1;
    }

    public Player getPlayer2() {
        return this.player2;
    }

    public boolean isParty() {
        return this.party;
    }
}


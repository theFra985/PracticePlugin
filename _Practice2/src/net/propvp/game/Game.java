/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.propvp.game;

import java.util.List;
import net.propvp.game.GameMode;
import net.propvp.game.Team;
import net.propvp.timing.ElapsedTimer;
import org.bukkit.entity.Player;

public interface Game {
    public void addSpectator(Player var1);

    public GameMode getGameMode();

    public List<Player> getPlayers();

    public List<Player> getSpectators();

    public void removeSpectator(Player var1);

    public Team getTeamOfPlayer(Player var1);

    public Team getRival(Team var1);

    public ElapsedTimer getTimer();

    public boolean hasStarted();
}


/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.propvp.game;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.propvp.party.Party;
import org.bukkit.entity.Player;

public class Team {
    private String name;
    private Object object;
    private Map<Player, Boolean> players;

    public Team(Object object) {
        this.object = object;
        this.players = new HashMap<Player, Boolean>();
        if (object instanceof Player) {
            Player player = (Player)object;
            this.name = player.getName();
            this.players.put(player, true);
        } else if (object instanceof Party) {
            Party party = (Party)object;
            this.name = "team_" + party.getLeader().getName();
            this.players.put(party.getLeader(), true);
            for (Player player : party.getMembers()) {
                this.players.put(player, true);
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public Object getObject() {
        return this.object;
    }

    public boolean isParty() {
        return this.object instanceof Party;
    }

    public Map<Player, Boolean> getPlayers() {
        return this.players;
    }

    public int amountLeft() {
        return (int)this.players.entrySet().stream().filter(entry -> (Boolean)entry.getValue()).count();
    }
}


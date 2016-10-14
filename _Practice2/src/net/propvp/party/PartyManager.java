/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package net.propvp.party;

import java.util.HashMap;
import java.util.Map;
import net.propvp.party.Party;
import org.bukkit.entity.Player;

public class PartyManager {
    private Map<Player, Party> parties = new HashMap<Player, Party>();

    public void addSet(Player player, Party party) {
        this.parties.put(player, party);
    }

    public void removeSet(Player player) {
        this.parties.remove((Object)player);
    }

    public boolean isParty(Player player) {
        return this.parties.containsKey((Object)player);
    }

    public Party getParty(Player player) {
        return this.parties.get((Object)player);
    }
}


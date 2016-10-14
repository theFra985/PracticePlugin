/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Cancellable
 *  org.bukkit.event.Event
 *  org.bukkit.event.HandlerList
 */
package net.propvp.event;

import net.propvp.party.Party;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PartyKickEvent
extends Event
implements Cancellable {
    private Player initiator;
    private Player kicked;
    private Party party;
    private boolean cancelled;
    private static final HandlerList handlers = new HandlerList();

    public PartyKickEvent(Player player, Player player2, Party party) {
        this.initiator = player;
        this.kicked = player2;
        this.party = party;
    }

    public Player getInitiator() {
        return this.initiator;
    }

    public Player getKicked() {
        return this.kicked;
    }

    public Party getParty() {
        return this.party;
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(boolean bl) {
        this.cancelled = bl;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}


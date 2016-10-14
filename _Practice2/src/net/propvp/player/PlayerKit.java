/*
 * Decompiled with CFR 0_118.
 */
package net.propvp.player;

import java.util.UUID;
import net.propvp.player.PlayerInv;

public class PlayerKit {
    private String name;
    private UUID uuid;
    private PlayerInv inv;

    public PlayerKit(String string, PlayerInv playerInv) {
        this.name = string;
        this.uuid = UUID.randomUUID();
        this.inv = playerInv;
    }

    public String getName() {
        return this.name;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void setName(String string) {
        this.name = string;
    }

    public PlayerInv getInv() {
        return this.inv;
    }

    public void setInv(PlayerInv playerInv) {
        this.inv = playerInv;
    }
}


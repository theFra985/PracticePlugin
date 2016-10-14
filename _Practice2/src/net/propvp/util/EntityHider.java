/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.ProtocolManager
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.events.PacketListener
 *  com.comphenix.protocol.reflect.StructureModifier
 *  com.google.common.base.Preconditions
 *  com.google.common.collect.HashBasedTable
 *  com.google.common.collect.Table
 *  org.bukkit.Bukkit
 *  org.bukkit.Chunk
 *  org.bukkit.Server
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.HandlerList
 *  org.bukkit.event.Listener
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.world.ChunkUnloadEvent
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.PluginManager
 */
package net.propvp.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.reflect.StructureModifier;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class EntityHider
implements Listener {
    protected Table<Integer, Integer, Boolean> observerEntityMap = HashBasedTable.create();
    private static final PacketType[] ENTITY_PACKETS = new PacketType[]{PacketType.Play.Server.ENTITY_EQUIPMENT, PacketType.Play.Server.BED, PacketType.Play.Server.ANIMATION, PacketType.Play.Server.NAMED_ENTITY_SPAWN, PacketType.Play.Server.COLLECT, PacketType.Play.Server.SPAWN_ENTITY, PacketType.Play.Server.SPAWN_ENTITY_LIVING, PacketType.Play.Server.SPAWN_ENTITY_PAINTING, PacketType.Play.Server.SPAWN_ENTITY_EXPERIENCE_ORB, PacketType.Play.Server.ENTITY_VELOCITY, PacketType.Play.Server.REL_ENTITY_MOVE, PacketType.Play.Server.ENTITY_LOOK, PacketType.Play.Server.ENTITY_MOVE_LOOK, PacketType.Play.Server.ENTITY_MOVE_LOOK, PacketType.Play.Server.ENTITY_TELEPORT, PacketType.Play.Server.ENTITY_HEAD_ROTATION, PacketType.Play.Server.ENTITY_STATUS, PacketType.Play.Server.ATTACH_ENTITY, PacketType.Play.Server.ENTITY_METADATA, PacketType.Play.Server.ENTITY_EFFECT, PacketType.Play.Server.REMOVE_ENTITY_EFFECT, PacketType.Play.Server.BLOCK_BREAK_ANIMATION};
    private ProtocolManager manager;
    private Listener bukkitListener;
    private PacketAdapter protocolListener;
    protected final Policy policy;
    private static /* synthetic */ int[] $SWITCH_TABLE$net$propvp$util$EntityHider$Policy;

    public EntityHider(Plugin plugin, Policy policy) {
        Preconditions.checkNotNull((Object)plugin, (Object)"plugin cannot be NULL.");
        this.policy = policy;
        this.manager = ProtocolLibrary.getProtocolManager();
        this.bukkitListener = this.constructBukkit();
        plugin.getServer().getPluginManager().registerEvents(this.bukkitListener, plugin);
        this.protocolListener = this.constructProtocol(plugin);
        this.manager.addPacketListener((PacketListener)this.protocolListener);
    }

    protected boolean setVisibility(Player player, int n, boolean bl) {
        switch (EntityHider.$SWITCH_TABLE$net$propvp$util$EntityHider$Policy()[this.policy.ordinal()]) {
            case 2: {
                return !this.setMembership(player, n, !bl);
            }
            case 1: {
                return this.setMembership(player, n, bl);
            }
        }
        throw new IllegalArgumentException("Unknown policy: " + (Object)((Object)this.policy));
    }

    protected boolean setMembership(Player player, int n, boolean bl) {
        if (bl) {
            if (this.observerEntityMap.put((Object)player.getEntityId(), (Object)n, (Object)true) != null) {
                return true;
            }
            return false;
        }
        if (this.observerEntityMap.remove((Object)player.getEntityId(), (Object)n) != null) {
            return true;
        }
        return false;
    }

    protected boolean getMembership(Player player, int n) {
        return this.observerEntityMap.contains((Object)player.getEntityId(), (Object)n);
    }

    protected boolean isVisible(Player player, int n) {
        boolean bl = this.getMembership(player, n);
        return this.policy == Policy.WHITELIST ? bl : !bl;
    }

    protected void removeEntity(Entity entity, boolean bl) {
        int n = entity.getEntityId();
        for (Map map : this.observerEntityMap.rowMap().values()) {
            map.remove(n);
        }
    }

    protected void removePlayer(Player player) {
        this.observerEntityMap.rowMap().remove(player.getEntityId());
    }

    private Listener constructBukkit() {
        return new Listener(){

            @EventHandler
            public void onEntityDeath(EntityDeathEvent entityDeathEvent) {
                EntityHider.this.removeEntity((Entity)entityDeathEvent.getEntity(), true);
            }

            @EventHandler
            public void onChunkUnload(ChunkUnloadEvent chunkUnloadEvent) {
                Entity[] arrentity = chunkUnloadEvent.getChunk().getEntities();
                int n = arrentity.length;
                int n2 = 0;
                while (n2 < n) {
                    Entity entity = arrentity[n2];
                    EntityHider.this.removeEntity(entity, false);
                    ++n2;
                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent playerQuitEvent) {
                EntityHider.this.removePlayer(playerQuitEvent.getPlayer());
            }
        };
    }

    private PacketAdapter constructProtocol(Plugin plugin) {
        return new PacketAdapter(plugin, ENTITY_PACKETS){

            public void onPacketSending(PacketEvent packetEvent) {
                int n = (Integer)packetEvent.getPacket().getIntegers().read(0);
                if (!EntityHider.this.isVisible(packetEvent.getPlayer(), n)) {
                    packetEvent.setCancelled(true);
                }
            }
        };
    }

    public final boolean toggleEntity(Player player, Entity entity) {
        if (this.isVisible(player, entity.getEntityId())) {
            return this.hideEntity(player, entity);
        }
        return !this.showEntity(player, entity);
    }

    public final boolean showFadedEntity(Player player, Entity entity) {
        boolean bl;
        this.validate(player, entity);
        boolean bl2 = bl = !this.setVisibility(player, entity.getEntityId(), true);
        if (this.manager != null && bl) {
            this.manager.updateEntity(entity, Arrays.asList(new Player[]{player}));
        }
        return bl;
    }

    public final boolean showEntity(Player player, Entity entity) {
        boolean bl;
        this.validate(player, entity);
        boolean bl2 = bl = !this.setVisibility(player, entity.getEntityId(), true);
        if (this.manager != null && bl) {
            this.manager.updateEntity(entity, Arrays.asList(new Player[]{player}));
        }
        return bl;
    }

    public final boolean hideEntity(Player player, Entity entity) {
        this.validate(player, entity);
        boolean bl = this.setVisibility(player, entity.getEntityId(), false);
        if (bl) {
            PacketContainer packetContainer = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
            packetContainer.getIntegerArrays().write(0, (Object)new int[]{entity.getEntityId()});
            try {
                this.manager.sendServerPacket(player, packetContainer);
            }
            catch (InvocationTargetException var5_5) {
                throw new RuntimeException("Cannot send server packet.", var5_5);
            }
        }
        return bl;
    }

    public final boolean canSee(Player player, Entity entity) {
        this.validate(player, entity);
        return this.isVisible(player, entity.getEntityId());
    }

    private void validate(Player player, Entity entity) {
        Preconditions.checkNotNull((Object)player, (Object)"observer cannot be NULL.");
        Preconditions.checkNotNull((Object)entity, (Object)"entity cannot be NULL.");
    }

    public void showAllPlayers(Player player) {
        for (Player player2 : Bukkit.getOnlinePlayers()) {
            this.showEntity(player, (Entity)player2);
        }
    }

    public Policy getPolicy() {
        return this.policy;
    }

    public void close() {
        if (this.manager != null) {
            HandlerList.unregisterAll((Listener)this.bukkitListener);
            this.manager.removePacketListener((PacketListener)this.protocolListener);
            this.manager = null;
        }
    }

    static /* synthetic */ int[] $SWITCH_TABLE$net$propvp$util$EntityHider$Policy() {
        int[] arrn;
        int[] arrn2 = $SWITCH_TABLE$net$propvp$util$EntityHider$Policy;
        if (arrn2 != null) {
            return arrn2;
        }
        arrn = new int[Policy.values().length];
        try {
            arrn[Policy.BLACKLIST.ordinal()] = 2;
        }
        catch (NoSuchFieldError v1) {}
        try {
            arrn[Policy.WHITELIST.ordinal()] = 1;
        }
        catch (NoSuchFieldError v2) {}
        $SWITCH_TABLE$net$propvp$util$EntityHider$Policy = arrn;
        return $SWITCH_TABLE$net$propvp$util$EntityHider$Policy;
    }

    public static enum Policy {
        WHITELIST,
        BLACKLIST;
        

        private Policy(String string2, int n2) {
        }
    }

}


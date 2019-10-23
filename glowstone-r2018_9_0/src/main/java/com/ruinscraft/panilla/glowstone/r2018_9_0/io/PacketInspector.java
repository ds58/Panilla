package com.ruinscraft.panilla.glowstone.r2018_9_0.io;

import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.EntityNbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.LegacyEntityNbtNotPermittedException;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import org.bukkit.Bukkit;

import java.util.UUID;

public class PacketInspector implements IPacketInspector {

    @Override
    public void checkPacketPlayInSetCreativeSlot(Object _packet) throws NbtNotPermittedException {

    }

    @Override
    public void checkPacketPlayOutSetSlot(Object _packet) throws NbtNotPermittedException {

    }

    @Override
    public void checkPacketPlayOutSpawnEntity(Object _packet) throws EntityNbtNotPermittedException, LegacyEntityNbtNotPermittedException {

    }

    @Override
    public void sendPacketPlayOutSetSlotAir(IPanillaPlayer player, int slot) {

    }

    @Override
    public void removeEntity(UUID entityId) {
        Bukkit.getEntity(entityId).remove();
    }

    @Override
    public void removeEntityLegacy(int entityId) {
        throw new IllegalStateException("Cannot use #removeEntityLegacy on 1.12");
    }

}

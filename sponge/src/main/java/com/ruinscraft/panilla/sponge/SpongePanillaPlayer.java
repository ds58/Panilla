package com.ruinscraft.panilla.sponge;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.exception.PacketException;
import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class SpongePanillaPlayer implements IPanillaPlayer {

    private final Player handle;

    public SpongePanillaPlayer(Player handle) {
        this.handle = handle;
    }

    @Override
    public Object getHandle() {
        return handle;
    }

    @Override
    public String getName() {
        return handle.getName();
    }

    @Override
    public String getCurrentWorldName() {
        return handle.getWorld().getName();
    }

    @Override
    public boolean hasPermission(String node) {
        return handle.hasPermission(node);
    }

    @Override
    public void sendMessage(String message) {
        handle.sendMessage(Text.of(message));
    }

    @Override
    public boolean canBypassChecks(PacketException e) {
        if (e.getFailedNbt().result == NbtCheck.NbtCheckResult.CRITICAL) {
            return false;
        }
        return PanillaPlugin.get().getPanillaConfig().disabledWorlds.contains(getCurrentWorldName())
                || hasPermission(IPanilla.PERMISSION_BYPASS);
    }

}

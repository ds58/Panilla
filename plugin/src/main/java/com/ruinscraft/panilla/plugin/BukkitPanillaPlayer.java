package com.ruinscraft.panilla.plugin;

import com.ruinscraft.panilla.api.IPanillaPlayer;
import org.bukkit.entity.Player;

public class BukkitPanillaPlayer implements IPanillaPlayer {

    private final Player handle;

    public BukkitPanillaPlayer(Player handle) {
        this.handle = handle;
    }

    @Override
    public Player getHandle() {
        return handle;
    }

    @Override
    public String getName() {
        return handle.getName();
    }

    @Override
    public boolean isOp() {
        return handle.isOp();
    }

    @Override
    public boolean hasPermission(String node) {
        return handle.hasPermission(node);
    }

    @Override
    public void sendMessage(String message) {
        handle.sendMessage(message);
    }

}

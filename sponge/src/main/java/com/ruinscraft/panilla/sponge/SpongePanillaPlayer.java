package com.ruinscraft.panilla.sponge;

import com.ruinscraft.panilla.api.IPanillaPlayer;
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
    public boolean isOp() {
        return false;   // TODO:
    }

    @Override
    public boolean hasPermission(String node) {
        return handle.hasPermission(node);
    }

    @Override
    public void sendMessage(String message) {
        handle.sendMessage(Text.of(message));
    }

}

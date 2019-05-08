package com.ruinscraft.panilla.forge112.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.PanillaLogger;
import com.ruinscraft.panilla.api.io.IPlayerInjector;
import com.ruinscraft.panilla.api.io.PlayerInbound;
import com.ruinscraft.panilla.api.io.PlayerOutbound;
import io.netty.channel.Channel;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetworkManager;

import java.lang.reflect.Field;

public class PlayerInjector implements IPlayerInjector {

    private final IPanilla panilla;
    private final PanillaLogger panillaLogger;

    public PlayerInjector(IPanilla panilla, PanillaLogger panillaLogger) {
        this.panilla = panilla;
        this.panillaLogger = panillaLogger;
    }

    private static Channel getPlayerChannel(IPanillaPlayer player) throws IllegalArgumentException {
        EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player.getHandle();
        try {
            Field channelField = NetworkManager.class.getField("connection");
            channelField.setAccessible(true);
            Channel channel = (Channel) channelField.get(entityPlayerMP.connection.netManager);
            return channel;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void register(final IPanillaPlayer player) {
        Channel channel = getPlayerChannel(player);

        /* Register inbound */
        if (channel.pipeline().get(PANILLA_CHANNEL_IN) == null) {
            PlayerInbound inbound = new PlayerInbound(player, panilla, panillaLogger);
            channel.pipeline().addBefore(MINECRAFT_CHANNEL_DPLX, PANILLA_CHANNEL_IN, inbound);
        }

        /* Register outbound */
        if (channel.pipeline().get(PANILLA_CHANNEL_OUT) == null) {
            PlayerOutbound outbound = new PlayerOutbound(player, panilla, panillaLogger);
            channel.pipeline().addBefore(MINECRAFT_CHANNEL_DPLX, PANILLA_CHANNEL_OUT, outbound);
        }
    }

    @Override
    public void unregister(final IPanillaPlayer player) {
        Channel channel = getPlayerChannel(player);

        /* Unregister inbound */
        if (channel.pipeline().get(PANILLA_CHANNEL_IN) != null) {
            channel.pipeline().remove(PANILLA_CHANNEL_IN);
        }

        /* Unregister outbound */
        if (channel.pipeline().get(PANILLA_CHANNEL_OUT) != null) {
            channel.pipeline().remove(PANILLA_CHANNEL_OUT);
        }
    }

}

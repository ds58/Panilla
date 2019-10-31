package com.ruinscraft.panilla.api.io;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.IPanillaLogger;
import com.ruinscraft.panilla.api.IPanillaPlayer;
import com.ruinscraft.panilla.api.config.PTranslations;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

public class PanillaExceptionHandler extends ChannelDuplexHandler {

    private final IPanilla panilla;
    private final IPanillaPlayer player;

    public PanillaExceptionHandler(IPanilla panilla, IPanillaPlayer player) {
        this.panilla = panilla;
        this.player = player;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        IPanillaLogger panillaLogger = panilla.getPanillaLogger();
        PTranslations pTranslations = panilla.getPTranslations();

        panillaLogger.log(pTranslations.getTranslation("preventedKick"), true);

        panilla.getContainerCleaner().clean(player);
    }

}

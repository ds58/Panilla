package com.ruinscraft.panilla.api;

import java.util.Collection;
import java.util.logging.Logger;

public interface IPanillaPlatform {

    String translateColorCodes(String string);

    // TODO: move this
    @Deprecated
    boolean isValidPotionColor(int bgr);

    Collection<IPanillaPlayer> getOnlinePlayers();

    Logger getLogger();

    void runTaskLater(long delay, Runnable task);

}

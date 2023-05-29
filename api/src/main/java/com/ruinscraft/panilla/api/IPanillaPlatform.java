package com.ruinscraft.panilla.api;

import java.util.Collection;
import java.util.logging.Logger;

public interface IPanillaPlatform {

    String translateColorCodes(String string);

    boolean isValidPotionColor(int bgr);

    Collection<IPanillaPlayer> getOnlinePlayers();

    Logger getLogger();

}

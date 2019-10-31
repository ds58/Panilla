package com.ruinscraft.panilla.api;

import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PTranslations;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.io.IPlayerInjector;

public interface IPanilla {

    PConfig getPConfig();

    PTranslations getPTranslations();

    IPanillaLogger getPanillaLogger();

    IProtocolConstants getProtocolConstants();

    IPlayerInjector getPlayerInjector();

    IPacketInspector getPacketInspector();

    IContainerCleaner getContainerCleaner();

    IEnchantments getEnchantments();

}

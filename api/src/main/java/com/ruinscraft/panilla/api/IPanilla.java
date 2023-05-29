package com.ruinscraft.panilla.api;

import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PLocale;
import com.ruinscraft.panilla.api.io.IPacketInspector;
import com.ruinscraft.panilla.api.io.IPlayerInjector;

public interface IPanilla {

    String PERMISSION_BYPASS = "panilla.bypass";

    PanillaLogger getPanlliaLogger();

    PConfig getPanillaConfig();

    PLocale getPanillaLocale();

    IPanillaPlatform getPlatform();

    IProtocolConstants getProtocolConstants();

    IPlayerInjector getPlayerInjector();

    IPacketInspector getPacketInspector();

    IContainerCleaner getContainerCleaner();

    IEnchantments getEnchantments();

}

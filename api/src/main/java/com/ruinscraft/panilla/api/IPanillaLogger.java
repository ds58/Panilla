package com.ruinscraft.panilla.api;

import java.io.IOException;

import com.ruinscraft.panilla.api.exception.PacketException;

public interface IPanillaLogger {

	void loadLocale(String localeFile) throws IOException;
	void warn(Object _player, PacketException e);
	
}

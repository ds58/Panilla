package com.ruinscraft.panilla.api;

public interface IPanillaLogger {

	void all(String message);
	void chat(String message);
	void console(String message);
	String generateWarning(Object _player, Exception exception);
	
}

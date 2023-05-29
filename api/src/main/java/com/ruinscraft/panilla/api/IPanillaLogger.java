package com.ruinscraft.panilla.api;

public interface IPanillaLogger {

    void log(String translationKey, boolean colorize);

    void info(String message, boolean colorize);

    void warning(String message, boolean colorize);

}

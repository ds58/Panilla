package com.ruinscraft.panilla.sponge;

import com.ruinscraft.panilla.api.IPanillaLogger;

import java.util.logging.Logger;

public class SpongePanillaLogger implements IPanillaLogger {

    private final Logger jLogger;

    public SpongePanillaLogger(Logger jLogger) {
        this.jLogger = jLogger;
    }

    @Override
    public void log(String translationKey, boolean colorize) {
        // TODO:
    }

    @Override
    public void info(String message, boolean colorize) {
        if (colorize) {
            message = colorize(message);
        }

        jLogger.info(message);
    }

    @Override
    public void warning(String message, boolean colorize) {
        if (colorize) {
            message = colorize(message);
        }

        jLogger.warning(message);
    }

    private String colorize(String string) {
        return string;  // TODO:
    }

}

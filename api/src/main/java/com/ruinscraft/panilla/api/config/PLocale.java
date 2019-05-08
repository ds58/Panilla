package com.ruinscraft.panilla.api.config;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class PLocale {

    private final Map<String, String> translations;

    public PLocale(Map<String, String> translations) {
        this.translations = translations;
    }

    public String getTranslation(String key) {
        return translations.get(key);
    }

    public static void saveDefaultLocaleFile(String localeFileName, File dataDir) throws IOException {


    }

}

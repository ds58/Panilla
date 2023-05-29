package com.ruinscraft.panilla.api.config;

import java.util.Map;

public class PLocale {

    private final Map<String, String> translations;
    private final Map<String, String> englishDefaults;

    public PLocale(Map<String, String> translations, Map<String, String> englishDefaults) {
        this.translations = translations;
        this.englishDefaults = englishDefaults;
    }

    public String getTranslation(String key) {
        String translation = translations.get(key);

        if (translation == null) {
            // use a default translation
            translation = englishDefaults.get(key);
        }

        return translation;
    }

}

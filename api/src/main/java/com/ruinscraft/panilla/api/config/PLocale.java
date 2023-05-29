package com.ruinscraft.panilla.api.config;

import java.util.Map;

public class PLocale {

    private final Map<String, String> translations;

    public PLocale(Map<String, String> translations) {
        this.translations = translations;
    }

    public String getTranslation(String key) {
        return translations.get(key);
    }

}

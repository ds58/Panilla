package com.ruinscraft.panilla.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PTranslations {

    private final String languageKey;
    private final Map<String, String> translations;

    public PTranslations(String languageKey, Map<String, String> translations) {
        this.languageKey = languageKey;
        this.translations = translations;
    }

    public static PTranslations get(String languageKey) throws IOException {
        try (InputStream inputStream = PTranslations.class.getResourceAsStream("/messages_" + languageKey + ".properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);

            Map<String, String> translations = new HashMap<>();

            for (Object key : properties.keySet()) {
                String value = properties.getProperty(key.toString());
                translations.put(key.toString(), value);
            }

            return new PTranslations(languageKey, translations);
        }
    }

    public String getLanguageKey() {
        return languageKey;
    }

    public String getTranslation(String key, String... replacements) {
        String unformatted = translations.get(key);
        if (unformatted == null) {
            return "unknown translation: " + key;
        }
        String formatted = String.format(translations.get(key), replacements);
        return formatted;
    }

}

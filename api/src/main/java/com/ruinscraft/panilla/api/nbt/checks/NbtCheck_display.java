package com.ruinscraft.panilla.api.nbt.checks;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

public class NbtCheck_display extends NbtCheck {

    private static final JsonParser PARSER = new JsonParser();

    public NbtCheck_display() {
        super("display", PStrictness.LENIENT);
    }

    private static String createTextFromJsonArray(JsonArray jsonArray) {
        StringBuilder text = new StringBuilder();

        for (JsonElement jsonElement : jsonArray) {
            text.append(jsonElement.getAsJsonObject().get("text").getAsString());
        }

        return text.toString();
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        INbtTagCompound display = tag.getCompound(getName());

        if (display.hasKeyOfType("Name", NbtDataType.STRING)) {
            String name = display.getString("Name");

            // check for Json array
            if (name.startsWith("[{")) {
                try {
                    JsonElement jsonElement = PARSER.parse(name);
                    JsonArray jsonArray = jsonElement.getAsJsonArray();

                    name = createTextFromJsonArray(jsonArray);
                } catch (Exception e) {
                    // could not parse Json
                }
            }

            // check for Json object
            else if (name.startsWith("{")) {
                try {
                    JsonElement jsonElement = PARSER.parse(name);
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    JsonArray jsonArray = jsonObject.getAsJsonArray("extra");

                    name = createTextFromJsonArray(jsonArray);
                } catch (Exception e) {
                    // could not parse Json
                    return NbtCheckResult.CRITICAL;  // can cause crashes
                }
            }

            final int maxNameLength;

            // if strict, use anvil length
            if (panilla.getPConfig().strictness.ordinal() >= PStrictness.STRICT.ordinal()) {
                maxNameLength = panilla.getProtocolConstants().maxAnvilRenameChars();
            } else {
                maxNameLength = panilla.getProtocolConstants().NOT_PROTOCOL_maxItemNameLength();
            }

            if (name.length() > maxNameLength) {
                return NbtCheckResult.CRITICAL; // can cause crashes
            }
        }

        if (display.hasKeyOfType("Lore", NbtDataType.LIST)) {
            INbtTagList lore = display.getList("Lore");

            if (lore.size() > panilla.getProtocolConstants().NOT_PROTOCOL_maxLoreLines()) {
                return NbtCheckResult.CRITICAL; // can cause crashes
            }

            for (int i = 0; i < lore.size(); i++) {
                String line = lore.getString(i);

                if (line.length() > panilla.getProtocolConstants().NOT_PROTOCOL_maxLoreLineLength()) {
                    return NbtCheckResult.CRITICAL; // can cause crashes
                }
            }
        }

        return NbtCheckResult.PASS;
    }

}

package com.ruinscraft.panilla.api.nbt.checks.paper1_20_6;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.NbtDataType;
import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;

import static com.ruinscraft.panilla.api.nbt.checks.NbtCheck_display.PARSER;

public class NbtCheck_CustomName extends NbtCheck {

    public NbtCheck_CustomName() {
        super("minecraft:custom_name", PStrictness.LENIENT);
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
        if (tag.hasKeyOfType(getName(), NbtDataType.STRING)) {
            String name = tag.getString(getName());

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

                    if (jsonArray != null) {
                        name = createTextFromJsonArray(jsonArray);
                    }
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

        return NbtCheckResult.PASS;
    }

}
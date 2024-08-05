package com.ruinscraft.panilla.api.nbt.checks.paper1_20_6;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;
import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;

import java.nio.charset.StandardCharsets;

import static com.ruinscraft.panilla.api.nbt.checks.NbtCheck_pages.MOJANG_CRASH_TRANSLATIONS;
import static com.ruinscraft.panilla.api.nbt.checks.NbtCheck_pages.createCharMap;

public class NbtCheck_WrittenBookContent extends NbtCheck {
    public NbtCheck_WrittenBookContent() {
        super("minecraft:written_book_content", PStrictness.AVERAGE);
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        tag = tag.getCompound("minecraft:written_book_content");

        final int titleLength = tag.getCompound("title").getString("raw").length();

        if (panilla.getPConfig().strictness == PStrictness.STRICT) {
            if (titleLength > panilla.getProtocolConstants().maxBookTitleLength()) {
                return NbtCheckResult.CRITICAL;
            }
        } else {
            if (titleLength > panilla.getProtocolConstants().NOT_PROTOCOL_maxItemNameLength()) {
                return NbtCheckResult.CRITICAL;
            }
        }

        if (tag.getString("author").length() > panilla.getProtocolConstants().maxUsernameLength())
            return NbtCheckResult.FAIL;

        INbtTagList pages = tag.getList("pages", NbtDataType.COMPOUND);

        if (pages.size() > panilla.getProtocolConstants().maxBookPages()) {
            return NbtCheckResult.CRITICAL; // too many pages
        }

        // iterate through book pages
        for (int i = 0; i < pages.size(); i++) {
            String page = pages.getCompound(i).getString("raw");
            String pageContent;

            if (page.startsWith("{")) {
                JsonObject data = new JsonParser().parse(page).getAsJsonObject();

                if (data.has("text")) {
                    pageContent = data.get("text").getAsString();
                } else {
                    pageContent = "";
                }

                if (data.has("hoverEvent")) {
                    if (data.get("hoverEvent").getAsString().getBytes(StandardCharsets.UTF_8).length > 32000) {
                        return NbtCheckResult.CRITICAL;
                    }
                }
            } else {
                pageContent = pages.getString(i);
            }

            final String pageNoSpaces = pageContent.replace(" ", "");

            // check char map
            short[] charMap = createCharMap(pageNoSpaces);
            int uniqueChars = 0;

            for (short count : charMap) {
                if (count > 0) {
                    uniqueChars++;
                }
            }

            if (uniqueChars > 180) { // 180 is a reasonable guess for max characters on a book page. It does depend on character size
                return NbtCheckResult.CRITICAL; // most likely a "crash" book
            }

            // check if contains mojang crash translations
            if (pageNoSpaces.startsWith("{\"translate\"")) {
                // Prevent a stack overflow
                if (pageNoSpaces.contains("[[[[[[[[[")) {
                    return NbtCheckResult.CRITICAL;
                }

                // Prevent Mojang crash translations
                for (String crashTranslation : MOJANG_CRASH_TRANSLATIONS) {
                    String translationJson = String.format("{\"translate\":\"%s\"}", crashTranslation);

                    if (pageContent.equals(translationJson)) {
                        return NbtCheckResult.CRITICAL;
                    }
                }
            }
        }

        return NbtCheckResult.PASS;
    }
}

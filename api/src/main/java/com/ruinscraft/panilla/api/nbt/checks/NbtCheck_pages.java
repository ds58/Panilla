package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

public class NbtCheck_pages extends NbtCheck {

    // translations in the game which (intentionally) crash the user
    private static final String[] MOJANG_CRASH_TRANSLATIONS = new String[] {
            "translation.test.invalid",
            "translation.test.invalid2"
    };

    public NbtCheck_pages() {
        super("pages", PStrictness.LENIENT);
    }

    @Override
    public boolean check(INbtTagCompound tag, String nmsItemClassName, IProtocolConstants protocolConstants, PConfig config) {
        INbtTagList pages = tag.getList("pages", NbtDataType.STRING);

        for (int i = 0; i < pages.size(); i++) {
            final String page = pages.getString(i);
            final String noSpaces = page.replace(" ", "");

            if (!noSpaces.startsWith("{\"translate\"")) {
                continue;
            }

            for (String crashTranslation : MOJANG_CRASH_TRANSLATIONS) {
                String check = String.format("{\"translate\":\"%s\"}", crashTranslation);

                if (page.equalsIgnoreCase(check)) {
                    return false;
                }
            }
        }

        return true;
    }

}

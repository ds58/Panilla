package com.ruinscraft.panilla.api.nbt.checks.paper1_20_6;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;
import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;

import java.util.Base64;
import java.util.UUID;
import java.util.regex.Matcher;

import static com.ruinscraft.panilla.api.nbt.checks.NbtCheck_SkullOwner.URL_MATCHER;
import static com.ruinscraft.panilla.api.nbt.checks.NbtCheck_SkullOwner.minecraftSerializableUuid;

public class NbtCheck_SkullOwner1_20_6 extends NbtCheck {

    public NbtCheck_SkullOwner1_20_6() {
        super("minecraft:profile", PStrictness.LENIENT);
    }

    @Override
    public NbtCheck.NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        INbtTagCompound skullOwner = tag.getCompound("minecraft:profile");

        if (skullOwner.hasKey("name")) {
            String name = skullOwner.getString("name");

            if (name.length() > 64) {
                return NbtCheckResult.CRITICAL;
            }
        }

        // id or Id UUID string
        if (skullOwner.hasKeyOfType("id", NbtDataType.STRING)) {
            String uuidString = skullOwner.getString("id");

            try {
                // Ensure valid UUID
                UUID.fromString(uuidString);
            } catch (Exception e) {
                return NbtCheckResult.CRITICAL;
            }
        }
        if (skullOwner.hasKeyOfType("Id", NbtDataType.STRING)) {
            String uuidString = skullOwner.getString("Id");

            try {
                // Ensure valid UUID
                UUID.fromString(uuidString);
            } catch (Exception e) {
                return NbtCheckResult.CRITICAL;
            }
        }

        // id or Id int array
        if (skullOwner.hasKeyOfType("Id", NbtDataType.INT_ARRAY)) {
            int[] ints = skullOwner.getIntArray("Id");

            try {
                UUID check = minecraftSerializableUuid(ints);
            } catch (Exception e) {
                return NbtCheckResult.CRITICAL;
            }
        }
        if (skullOwner.hasKeyOfType("id", NbtDataType.INT_ARRAY)) {
            int[] ints = skullOwner.getIntArray("id");

            try {
                UUID check = minecraftSerializableUuid(ints);
            } catch (Exception e) {
                return NbtCheckResult.CRITICAL;
            }
        }

        if (panilla.getPConfig().preventMinecraftEducationSkulls) {
            if (skullOwner.hasKey("properties")) {
                INbtTagList properties = skullOwner.getList("properties");
                for (int i = 0; i < properties.size(); i++) {
                    INbtTagCompound entry = properties.getCompound(i);
                    if (!"textures".equals(entry.getString("name"))) continue;

                    String b64 = entry.getString("value");
                    String decoded;

                    try {
                        decoded = new String(Base64.getDecoder().decode(b64));
                    } catch (IllegalArgumentException e) {
                        panilla.getPanillaLogger().warning("Invalid head texture", false);
                        return NbtCheckResult.CRITICAL;
                    }

                    // all lowercase, no parentheses or spaces
                    decoded = decoded.trim()
                        .replace(" ", "")
                        .replace("\"", "")
                        .toLowerCase();

                    Matcher matcher = URL_MATCHER.matcher(decoded);

                    // example: {textures:{SKIN:{url:https://education.minecraft.net/wp-content/uploads/deezcord.png}}}
                    // may contain multiple url tags
                    while (matcher.find()) {
                        String url = decoded.substring(matcher.end() + 1);

                        if (url.startsWith("http://textures.minecraft.net") ||
                            url.startsWith("https://textures.minecraft.net")) {
                            continue;
                        } else {
                            return NbtCheckResult.FAIL;
                        }
                    }
                }
            }
        }

        return NbtCheckResult.PASS;
    }

}

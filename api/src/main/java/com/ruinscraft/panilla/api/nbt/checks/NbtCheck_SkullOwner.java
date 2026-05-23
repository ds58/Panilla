package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

import java.util.Base64;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NbtCheck_SkullOwner extends NbtCheck {

    public static final Pattern URL_MATCHER = Pattern.compile("url");

    public NbtCheck_SkullOwner() {
        super("SkullOwner", PStrictness.LENIENT);
    }

    public static UUID minecraftSerializableUuid(final int[] ints) {
        return new UUID((long) ints[0] << 32 | ((long) ints[1] & 0xFFFFFFFFL), (long) ints[2] << 32 | ((long) ints[3] & 0xFFFFFFFFL));
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        INbtTagCompound skullOwner = tag.getCompound("SkullOwner");

        if (skullOwner.hasKey("Name")) {
            String name = skullOwner.getString("Name");

            if (name.length() > 64) {
                return NbtCheckResult.CRITICAL;
            }
        }

        if (skullOwner.hasKey("UUID")) {
            String uuidString = skullOwner.getString("UUID");

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
        } else if (skullOwner.hasKeyOfType("Id", NbtDataType.INT_ARRAY)) {
            int[] ints = skullOwner.getIntArray("Id");

            try {
                UUID check = minecraftSerializableUuid(ints);
            } catch (Exception e) {
                return NbtCheckResult.CRITICAL;
            }
        }

        if (panilla.getPConfig().preventMinecraftEducationSkulls) {
            if (skullOwner.hasKey("Properties")) {
                INbtTagCompound properties = skullOwner.getCompound("Properties");

                if (properties.hasKey("textures")) {
                    INbtTagList textures = properties.getList("textures", NbtDataType.COMPOUND);

                    for (int i = 0; i < textures.size(); i++) {
                        INbtTagCompound entry = textures.getCompound(i);

                        if (entry.hasKey("Value")) {
                            String b64 = entry.getString("Value");
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
            }
        }

        return NbtCheckResult.PASS;
    }

}

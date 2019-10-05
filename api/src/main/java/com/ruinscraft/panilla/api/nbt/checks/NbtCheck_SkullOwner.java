package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NbtCheck_SkullOwner extends NbtCheck {

    private static final Pattern URL_MATCHER = Pattern.compile("url");

    public NbtCheck_SkullOwner() {
        super("SkullOwner", PStrictness.AVERAGE);
    }

    @Override
    public boolean check(INbtTagCompound tag, String nmsItemClassName, IPanilla panilla) {
        if (panilla.getPanillaConfig().preventMinecraftEducationSkulls) {
            INbtTagCompound skullOwner = tag.getCompound("SkullOwner");

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
                                panilla.getPlatform().getLogger().info("Invalid head texture");
                                return false;
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
                                    return false;
                                }
                            }

                            return true;
                        }
                    }
                }
            }
        }

        return true;
    }

}

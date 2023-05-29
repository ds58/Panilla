package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

import java.util.Base64;

public class NbtCheck_SkullOwner extends NbtCheck {

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

                            try {
                                String decoded = new String(Base64.getDecoder().decode(b64));
                                decoded = decoded.trim().replace(" ", "");

                                // example: {"textures":{"SKIN":{"url":"https://education.minecraft.net/wp-content/uploads/deezcord.png"}}}
                                int urlIndex = decoded.indexOf("url") + 6;

                                if (decoded.substring(urlIndex).startsWith("http://textures.minecraft.net")
                                        || decoded.substring(urlIndex).startsWith("https://textures.minecraft.net")) {
                                    return false;
                                } else {
                                    return true;
                                }
                            } catch (IllegalArgumentException e) {
                                panilla.getPlatform().getLogger().info("Invalid head texture");
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }

}

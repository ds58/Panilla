package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

import java.util.HashMap;
import java.util.Map;

public final class NbtChecks {

    private static final Map<String, NbtCheck> checks = new HashMap<>();

    static {
        register(new NbtCheck_Unbreakable());
        register(new NbtCheck_CanDestroy());
        register(new NbtCheck_CanPlaceOn());
        register(new NbtCheck_BlockEntityTag());
        register(new NbtCheck_BlockStateTag());
        register(new NbtCheck_ench());
        register(new NbtCheck_Enchantments());
        register(new NbtCheck_StoredEnchantments());
        register(new NbtCheck_RepairCost());
        register(new NbtCheck_AttributeModifiers());
        register(new NbtCheck_CustomPotionEffects());
        register(new NbtCheck_Potion());
        register(new NbtCheck_CustomPotionColor());
        register(new NbtCheck_display());
        register(new NbtCheck_HideFlags());
        register(new NbtCheck_resolved());
        register(new NbtCheck_generation());
        register(new NbtCheck_author());
        register(new NbtCheck_title());
        register(new NbtCheck_pages());
        register(new NbtCheck_SkullOwner());
        register(new NbtCheck_Explosion());
        register(new NbtCheck_Fireworks());
        register(new NbtCheck_EntityTag());
        register(new NbtCheck_BucketVariantTag());
        register(new NbtCheck_map());
        register(new NbtCheck_map_scale_direction());
        register(new NbtCheck_Decorations());
        register(new NbtCheck_Effects());
        register(new NbtCheck_CustomModelData());
    }

    public static void register(NbtCheck check) {
        checks.put(check.getName(), check);
    }

    public static NbtCheck get(String tag) {
        return checks.get(tag);
    }

    public static boolean exists(String tag) {
        return checks.containsKey(tag);
    }

    public static void checkPacketPlayIn(INbtTagCompound tag, String nmsItemClassName, String nmsPacketClassName,
                                         IProtocolConstants protocolConstants, PConfig config) throws NbtNotPermittedException {

        String failedNbt = checkAll(tag, nmsItemClassName, protocolConstants, config);

        if (failedNbt != null) {
            throw new NbtNotPermittedException(nmsPacketClassName, true, failedNbt);
        }
    }

    public static void checkPacketPlayOut(INbtTagCompound tag, String nmsItemClassName, String nmsPacketClassName,
                                          IProtocolConstants protocolConstants, PConfig config) throws NbtNotPermittedException {

        String failedNbt = checkAll(tag, nmsItemClassName, protocolConstants, config);

        if (failedNbt != null) {
            throw new NbtNotPermittedException(nmsPacketClassName, false, failedNbt);
        }
    }

    public static String checkAll(INbtTagCompound tag, String nmsItemClassName,
                                  IProtocolConstants protocolConstants, PConfig config) {
        int nonMinecraftKeys = 0;

        for (String key : tag.getKeys()) {
            if (config.nbtWhitelist.contains(key)) {
                continue;
            }

            NbtCheck check = checks.get(key);

            if (check == null) {
                nonMinecraftKeys++;
                continue;
            }

            if (check.getTolerance().lvl > config.strictness.lvl) {
                continue;
            }

            if (!check.check(tag, nmsItemClassName, protocolConstants, config)) {
                return key;
            }
        }

        if (nonMinecraftKeys > config.maxNonMinecraftNbtKeys) {
            for (String key : tag.getKeys()) {
                if (checks.get(key) == null) {
                    return key;
                }
            }
        }

        return null;
    }

}

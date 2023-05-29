package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.exception.NbtNotPermittedException;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

import java.util.HashMap;
import java.util.Map;

public final class NbtChecks {

    private static final Map<String, NbtCheck> checks = new HashMap<>();

    static {
        // vanilla
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

        // non-vanilla
        register(new NbtCheck_weBrushJson());
    }

    private static void register(NbtCheck check) {
        checks.put(check.getName(), check);
    }

    public static Map<String, NbtCheck> getChecks() {
        return checks;
    }

    public static void checkPacketPlayIn(int slot, INbtTagCompound tag, String nmsItemClassName, String nmsPacketClassName,
                                         IPanilla panilla) throws NbtNotPermittedException {
        FailedNbt failedNbt = checkAll(tag, nmsItemClassName, panilla);

        if (FailedNbt.fails(failedNbt)) {
            throw new NbtNotPermittedException(nmsPacketClassName, false, failedNbt, slot);
        }
    }

    public static void checkPacketPlayOut(int slot, INbtTagCompound tag, String nmsItemClassName, String nmsPacketClassName,
                                          IPanilla panilla) throws NbtNotPermittedException {
        FailedNbt failedNbt = checkAll(tag, nmsItemClassName, panilla);

        if (FailedNbt.fails(failedNbt)) {
            throw new NbtNotPermittedException(nmsPacketClassName, false, failedNbt, slot);
        }
    }

    private static boolean tagMeetsKeyThreshold(INbtTagCompound tag, IPanilla panilla) {
        int maxNonMinecraftKeys = panilla.getPConfig().maxNonMinecraftNbtKeys;

        if (tag.getNonMinecraftKeys().size() > maxNonMinecraftKeys) {
            return false;
        }

        for (String key : tag.getKeys()) {
            if (tag.hasKeyOfType(key, NbtDataType.COMPOUND)) {
                INbtTagCompound subTag = tag.getCompound(key);

                if (!tagMeetsKeyThreshold(subTag, panilla)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static FailedNbt checkAll(INbtTagCompound tag, String nmsItemClassName, IPanilla panilla) {
        if (!tagMeetsKeyThreshold(tag, panilla)) {
            return FailedNbt.FAIL_KEY_THRESHOLD;
        }

        for (String key : tag.getKeys()) {
            if (panilla.getPConfig().nbtWhitelist.contains(key)) {
                continue;
            }

            if (tag.hasKeyOfType(key, NbtDataType.LIST)) {
                INbtTagList list = tag.getList(key);

                if (list.size() > 128) {
                    return new FailedNbt((key), NbtCheck.NbtCheckResult.CRITICAL);
                }
            }

            NbtCheck check = checks.get(key);

            if (check == null) {
                // a non-minecraft NBT tag
                continue;
            }

            if (check.getTolerance().lvl > panilla.getPConfig().strictness.lvl) {
                continue;
            }

            NbtCheck.NbtCheckResult result = check.check(tag, nmsItemClassName, panilla);

            if (result != NbtCheck.NbtCheckResult.PASS) {
                return new FailedNbt(key, result);
            }
        }

        return FailedNbt.NOFAIL;
    }

}

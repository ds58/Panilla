package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

public class NbtCheck_EntityTag extends NbtCheck {

    private static final String[] ARMOR_STAND_TAGS = new String[]{"NoGravity", "ShowArms", "NoBasePlate", "Small", "Rotation", "Marker", "Pose", "Invisible"};

    public NbtCheck_EntityTag() {
        super("EntityTag", PStrictness.AVERAGE);
    }

    private static FailedNbt checkItems(INbtTagList items, String nmsItemClassName, IPanilla panilla) {
        for (int i = 0; i < items.size(); i++) {
            INbtTagCompound item = items.getCompound(i);

            if (item.hasKey("tag")) {
                FailedNbt failedNbt = NbtChecks.checkAll(item.getCompound("tag"), nmsItemClassName, panilla);

                if (FailedNbt.fails(failedNbt)) {
                    return failedNbt;
                }
            }
        }

        return FailedNbt.NOFAIL;
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String nmsItemClassName, IPanilla panilla) {
        INbtTagCompound entityTag = tag.getCompound(getName());

        if (panilla.getPConfig().strictness == PStrictness.STRICT) {
            for (String armorStandTag : ARMOR_STAND_TAGS) {
                if (entityTag.hasKey(armorStandTag)) {
                    return NbtCheckResult.FAIL;
                }
            }
        }

        if (entityTag.hasKey("Invulnerable")) {
            return NbtCheckResult.FAIL;
        }

        if (entityTag.hasKey("Motion")) {
            return NbtCheckResult.FAIL;
        }

        if (entityTag.hasKey("ArmorItems")) {
            INbtTagList items = entityTag.getList("ArmorItems", NbtDataType.COMPOUND);

            FailedNbt failedNbt = checkItems(items, nmsItemClassName, panilla);

            if (FailedNbt.fails(failedNbt)) {
                return failedNbt.result;
            }
        }

        if (entityTag.hasKey("HandItems")) {
            INbtTagList items = entityTag.getList("HandItems", NbtDataType.COMPOUND);

            FailedNbt failedNbt = checkItems(items, nmsItemClassName, panilla);

            if (FailedNbt.fails(failedNbt)) {
                return failedNbt.result;
            }
        }

        boolean hasIdTag = entityTag.hasKey("id");

        if (hasIdTag) {
            if (panilla.getPConfig().strictness == PStrictness.STRICT) {
                return NbtCheckResult.FAIL;
            }

            // check for massive slime spawn eggs
            if (entityTag.hasKey("Size")) {
                if (entityTag.getInt("Size") > panilla.getProtocolConstants().maxSlimeSize()) {
                    return NbtCheckResult.CRITICAL;
                }
            }

            // blow tags are mostly for EntityAreaEffectCloud
            // see nms.EntityAreaEffectCloud
            if (entityTag.hasKey("Age")) {
                return NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("Duration")) {
                return NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("WaitTime")) {
                return NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("ReapplicationDelay")) {
                return NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("DurationOnUse")) {
                return NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("RadiusOnUse")) {
                return NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("RadiusPerTick")) {
                return NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("Radius")) {
                return NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("Particle") && panilla.getPConfig().strictness == PStrictness.STRICT) {
                return NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("Color") && panilla.getPConfig().strictness == PStrictness.STRICT) {
                return NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("Potion") && panilla.getPConfig().strictness == PStrictness.STRICT) {
                return NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("Effects")) {
                return NbtCheckResult.FAIL;
            }
        }

        return NbtCheckResult.PASS;
    }

}

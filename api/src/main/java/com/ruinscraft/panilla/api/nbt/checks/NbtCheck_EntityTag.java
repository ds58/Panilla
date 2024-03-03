package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.exception.FailedNbt;
import com.ruinscraft.panilla.api.exception.FailedNbtList;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

import java.util.Locale;
import java.util.UUID;

public class NbtCheck_EntityTag extends NbtCheck {

    private static final String[] ARMOR_STAND_TAGS = new String[]{"NoGravity", "ShowArms", "NoBasePlate", "Small", "Rotation", "Marker", "Pose", "Invisible"};

    public NbtCheck_EntityTag() {
        super("EntityTag", PStrictness.LENIENT);
    }

    private static FailedNbt checkItems(INbtTagList items, String nmsItemClassName, IPanilla panilla) {
        FailedNbt failedNbt = null;

        for (int i = 0; i < items.size(); i++) {
            INbtTagCompound item = items.getCompound(i);

            if (item.hasKey("tag")) {
                FailedNbtList failedNbtList = NbtChecks.checkAll(item.getCompound("tag"), nmsItemClassName, panilla);

                if (failedNbtList.containsCritical()) {
                    return failedNbtList.getCritical();
                }

                failedNbt = failedNbtList.findFirstNonCritical();
            }
        }

        return failedNbt;
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String itemName, IPanilla panilla) {
        NbtCheckResult result = NbtCheckResult.PASS;

        PStrictness strictness = panilla.getPConfig().strictness;

        INbtTagCompound entityTag = tag.getCompound(getName());

        if (strictness == PStrictness.STRICT) {
            for (String armorStandTag : ARMOR_STAND_TAGS) {
                if (entityTag.hasKey(armorStandTag)) {
                    result = NbtCheckResult.FAIL;
                }
            }
        }

        if (entityTag.hasKey("CustomName")) {
            String customName = entityTag.getString("CustomName");
            if (customName.length() > panilla.getProtocolConstants().NOT_PROTOCOL_maxEntityTagCustomNameLength()) {
                return NbtCheckResult.CRITICAL;
            }
            try {
                panilla.getPacketInspector().validateBaseComponentParse(customName);
            } catch (Exception e) {
                return NbtCheckResult.CRITICAL;
            }
        }

        if (entityTag.hasKey("UUID")) {
            String uuid = entityTag.getString("UUID");

            try {
                UUID.fromString(uuid);
            } catch (Exception e) {
                return NbtCheckResult.CRITICAL;
            }
        }

        if (entityTag.hasKey("ExplosionPower")) {
            result = NbtCheckResult.FAIL;
        }

        if (entityTag.hasKey("Invulnerable")) {
            result = NbtCheckResult.FAIL;
        }

        if (entityTag.hasKey("Motion")) {
            result = NbtCheckResult.FAIL;
        }

        if (entityTag.hasKey("power")) {
            return NbtCheckResult.CRITICAL;
        }

        if (entityTag.hasKey("PuffState")) {
            result = NbtCheckResult.FAIL;
        }

        if (entityTag.hasKeyOfType("AbsorptionAmount", NbtDataType.STRING)) {
            result = NbtCheckResult.FAIL;
        }

        if (entityTag.hasKeyOfType("Health", NbtDataType.FLOAT)) {
            return NbtCheckResult.FAIL;
        }

        if (entityTag.hasKey("ArmorItems")) {
            INbtTagList items = entityTag.getList("ArmorItems", NbtDataType.COMPOUND);

            FailedNbt failedNbt = checkItems(items, itemName, panilla);

            if (FailedNbt.fails(failedNbt)) {
                if (failedNbt.result == NbtCheckResult.CRITICAL) {
                    return NbtCheckResult.CRITICAL;
                } else {
                    result = NbtCheckResult.FAIL;
                }
            }
        }

        if (entityTag.hasKey("HandItems")) {
            INbtTagList items = entityTag.getList("HandItems", NbtDataType.COMPOUND);

            FailedNbt failedNbt = checkItems(items, itemName, panilla);

            if (FailedNbt.fails(failedNbt)) {
                if (failedNbt.result == NbtCheckResult.CRITICAL) {
                    return NbtCheckResult.CRITICAL;
                } else {
                    result = NbtCheckResult.FAIL;
                }
            }
        }

        boolean hasIdTag = entityTag.hasKey("id");

        if (hasIdTag) {
            if (strictness == PStrictness.STRICT) {
                result = NbtCheckResult.FAIL;
            }

            String id = entityTag.getString("id");
            String normalizedId = id.toLowerCase(Locale.US);

            // prevent lightning bolt eggs
            if (normalizedId.contains("lightning")) {
                result = NbtCheckResult.FAIL;
            }

            // prevent troll elder guardian eggs
            if (normalizedId.contains("elder_guardian")) {
                result = NbtCheckResult.FAIL;
            }

            // check for massive slime spawn eggs
            if (entityTag.hasKeyOfType("Size", NbtDataType.INT)) {
                if (entityTag.getInt("Size") > panilla.getProtocolConstants().maxSlimeSize()) {
                    return NbtCheckResult.CRITICAL;
                }
            }

            // below tags are mostly for EntityAreaEffectCloud
            // see nms.EntityAreaEffectCloud
            if (entityTag.hasKey("Age")) {
                return NbtCheckResult.CRITICAL;
            }

            if (entityTag.hasKeyOfType("Duration", NbtDataType.INT)) {
                int duration = entityTag.getInt("Duration");
                // Block area effect clouds with a duration longer than 15 seconds
                if (duration > (20 * 15)) {
                    return NbtCheckResult.CRITICAL;
                }
            }

            if (entityTag.hasKeyOfType("WaitTime", NbtDataType.INT)) {
                int waitTime = entityTag.getInt("WaitTime");
                // Block area effect clouds with a wait time longer than 5 seconds
                if (waitTime > (20 * 5)) {
                    result = NbtCheckResult.FAIL;
                }
            }

            if (entityTag.hasKey("ReapplicationDelay")) {
                result = NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("DurationOnUse")) {
                result = NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("RadiusOnUse")) {
                result = NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("RadiusPerTick")) {
                result = NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("Radius")) {
                result = NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("Particle")) {
                result = NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("Color")) {
                result = NbtCheckResult.FAIL;
            }

            if (entityTag.hasKey("Potion")) {
                result = NbtCheckResult.FAIL;
            }

            if (entityTag.hasKeyOfType("Effects", NbtDataType.LIST)) {
                INbtTagList effectsList = entityTag.getList("Effects");
                NbtCheckResult effectsResult = checkEffectsTag(effectsList);
                if (effectsResult == NbtCheckResult.CRITICAL) {
                    return NbtCheckResult.CRITICAL;
                } else if (effectsResult == NbtCheckResult.FAIL) {
                    result = NbtCheckResult.FAIL;
                }
            }

            if (entityTag.hasKeyOfType("effects", NbtDataType.LIST)) {
                INbtTagList effectsList = entityTag.getList("effects");
                NbtCheckResult effectsResult = checkEffectsTag(effectsList);
                if (effectsResult == NbtCheckResult.CRITICAL) {
                    return NbtCheckResult.CRITICAL;
                } else if (effectsResult == NbtCheckResult.FAIL) {
                    result = NbtCheckResult.FAIL;
                }
            }
        }

        return result;
    }

    private static NbtCheckResult checkEffectsTag(INbtTagList effectsList) {
        for (int i = 0; i < effectsList.size(); i++) {
            INbtTagCompound effect = effectsList.getCompound(i);

            if (effect.hasKeyOfType("amplifier", NbtDataType.BYTE)) {
                short amplifier = effect.getByte("amplifier");
                if (amplifier > 32) {
                    return NbtCheckResult.CRITICAL;
                }
            }
        }

        return NbtCheckResult.PASS;
    }

}

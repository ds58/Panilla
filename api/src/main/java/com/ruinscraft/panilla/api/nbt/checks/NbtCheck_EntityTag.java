package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IProtocolConstants;
import com.ruinscraft.panilla.api.config.PConfig;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;
import com.ruinscraft.panilla.api.nbt.INbtTagList;
import com.ruinscraft.panilla.api.nbt.NbtDataType;

public class NbtCheck_EntityTag extends NbtCheck {

    public NbtCheck_EntityTag() {
        super("EntityTag", PStrictness.AVERAGE);
    }

    private static final String[] ARMOR_STAND_TAGS = new String[]{"NoGravity", "ShowArms", "NoBasePlate", "Small", "Rotation", "Marker", "Pose", "Invisible"};

    @Override
    public boolean check(INbtTagCompound tag, String nmsItemClassName, IProtocolConstants protocolConstants, PConfig config) {
        INbtTagCompound entityTag = tag.getCompound(getName());

        if (config.strictness == PStrictness.STRICT) {
            for (String armorStandTag : ARMOR_STAND_TAGS) {
                if (entityTag.hasKey(armorStandTag)) return false;
            }
        }

        if (entityTag.hasKey("Invulnerable")) {
            return false;
        }

        if (entityTag.hasKey("Motion")) {
            return false;
        }

        if (entityTag.hasKey("Size")) {
            if (entityTag.getInt("Size") > protocolConstants.maxSlimeSize()) {
                return false;
            }
        }

        if (entityTag.hasKey("ArmorItems")) {
            INbtTagList items = entityTag.getList("ArmorItems", NbtDataType.COMPOUND);

            if (!checkItems(items, nmsItemClassName, protocolConstants, config)) {
                return false;
            }
        }

        if (entityTag.hasKey("HandItems")) {
            INbtTagList items = entityTag.getList("HandItems", NbtDataType.COMPOUND);

            if (!checkItems(items, nmsItemClassName, protocolConstants, config)) {
                return false;
            }
        }

        return true;
    }

    private static boolean checkItems(INbtTagList items, String nmsItemClassName, IProtocolConstants protocolConstants, PConfig config) {
        for (int i = 0; i < items.size(); i++) {
            INbtTagCompound item = items.getCompound(i);

            if (item.hasKey("tag")) {
                String failedNbt = NbtChecks.checkAll(item.getCompound("tag"), nmsItemClassName, protocolConstants, config);

                if (failedNbt != null) return false;
            }
        }

        return true;
    }

}

package com.ruinscraft.panilla.api.nbt;

public enum NbtDataType {

    END(0x0), BYTE(0x1), SHORT(0x2), INT(0x3), LONG(0x4), FLOAT(0x5), DOUBLE(0x6), BYTE_ARRAY(0x7),
    STRING(0x8), LIST(0x9), COMPOUND(0xA), INT_ARRAY(0xB);

    public final int id;

    NbtDataType(int id) {
        this.id = id;
    }

    public static NbtDataType fromId(int id) {
        for (NbtDataType type : values()) {
            if (type.id == id) {
                return type;
            }
        }

        return null;
    }

}

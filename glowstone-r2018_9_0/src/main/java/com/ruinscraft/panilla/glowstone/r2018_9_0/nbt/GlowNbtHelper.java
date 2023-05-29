package com.ruinscraft.panilla.glowstone.r2018_9_0.nbt;

import net.glowstone.inventory.GlowMetaItem;
import net.glowstone.util.nbt.CompoundTag;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class GlowNbtHelper {

    private static boolean canAccessNbt;
    private static Method writeNbtMethod;   // Reads NBT values from a GlowMetaItem into a CompoundTag
    // See https://github.com/GlowstoneMC/Glowstone/blob/a12eebbf8aad46f4d46ff4af86c59a55a39ad49e/src/main/java/net/glowstone/inventory/GlowMetaItem.java#L178

    private static Method readNbtMethod;    // writes NBT values to a GlowMetaItem from a CompoundTag
    // See https://github.com/GlowstoneMC/Glowstone/blob/a12eebbf8aad46f4d46ff4af86c59a55a39ad49e/src/main/java/net/glowstone/inventory/GlowMetaItem.java#L155

    static {
        try {
            writeNbtMethod = GlowMetaItem.class.getMethod("writeNbt", CompoundTag.class);
            readNbtMethod = GlowMetaItem.class.getMethod("readNbt", CompoundTag.class);
            canAccessNbt = true;
        } catch (NoSuchMethodException e) {
            canAccessNbt = false;
        }
    }

    public static CompoundTag getNbt(GlowMetaItem glowMetaItem) {
        CompoundTag tag = new CompoundTag();

        if (canAccessNbt) {
            try {
                writeNbtMethod.invoke(glowMetaItem, tag);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return tag;
    }

    public static void applyNbt(GlowMetaItem glowMetaItem, CompoundTag tag) {
        if (canAccessNbt) {
            try {
                readNbtMethod.invoke(glowMetaItem, tag);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}

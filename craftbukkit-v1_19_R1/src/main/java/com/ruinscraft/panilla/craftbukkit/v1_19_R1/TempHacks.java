package com.ruinscraft.panilla.craftbukkit.v1_19_R1;

import com.ruinscraft.panilla.api.IPanillaPlayer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class TempHacks {

    public static EntityPlayer getEntityPlayerFromPanillaPlayer(IPanillaPlayer panillaPlayer) {
        // PanillaPlayer->getHandle()->CraftPlayer->getHandle()->EntityPlayer
        Object craftPlayerHandle = panillaPlayer.getHandle();

        System.out.println(craftPlayerHandle.getClass().getCanonicalName());

        try {
            Method craftPlayerGetHandleMethod = craftPlayerHandle.getClass().getDeclaredMethod("getHandle");
            Object o = craftPlayerGetHandleMethod.invoke(craftPlayerHandle);
            System.out.println(o.getClass().getCanonicalName());
            return (EntityPlayer) o;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean paperChunkRewrite;

    static {
        try {
            WorldServer.class.getField("P");
            paperChunkRewrite = false;
        } catch (NoSuchFieldException e) {
            paperChunkRewrite = true;
        }
    }

    public static boolean isPaperChunkRewrite() {
        return paperChunkRewrite;
    }

}

package com.ruinscraft.panilla.api.exception;

import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;

public class FailedNbt {

    public static FailedNbt NOFAIL = new FailedNbt(null, NbtCheck.NbtCheckResult.PASS);
    public final String key;
    public final NbtCheck.NbtCheckResult result;

    public FailedNbt(String key, NbtCheck.NbtCheckResult result) {
        this.key = key;
        this.result = result;
    }

    public static boolean passes(FailedNbt failedNbt) {
        if (failedNbt == null) {
            return true;
        } else if (failedNbt.equals(NOFAIL)) {
            return true;
        } else {
            return failedNbt.result == NbtCheck.NbtCheckResult.PASS;
        }
    }

    public static boolean fails(FailedNbt failedNbt) {
        return !passes(failedNbt);
    }

}

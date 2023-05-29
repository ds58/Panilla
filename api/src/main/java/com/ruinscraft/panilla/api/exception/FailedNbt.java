package com.ruinscraft.panilla.api.exception;

import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;

public class FailedNbt {

    public static FailedNbt NO_FAIL = new FailedNbt(null, NbtCheck.NbtCheckResult.PASS);
    public static FailedNbt FAIL_KEY_THRESHOLD = new FailedNbt(null, NbtCheck.NbtCheckResult.CRITICAL);

    public final String key;
    public final NbtCheck.NbtCheckResult result;

    public FailedNbt(String key, NbtCheck.NbtCheckResult result) {
        this.key = key;
        this.result = result;
    }

    public static boolean passes(FailedNbt failedNbt) {
        if (failedNbt == null) {
            return true;
        } else if (failedNbt.equals(NO_FAIL)) {
            return true;
        } else {
            return failedNbt.result == NbtCheck.NbtCheckResult.PASS;
        }
    }

    public static boolean fails(FailedNbt failedNbt) {
        return !passes(failedNbt);
    }

    public static boolean failsThreshold(FailedNbt failedNbt) {
        return failedNbt.equals(FAIL_KEY_THRESHOLD);
    }

}

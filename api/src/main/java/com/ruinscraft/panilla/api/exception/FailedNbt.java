package com.ruinscraft.panilla.api.exception;

import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;

public class FailedNbt {

    public final String key;
    public final NbtCheck.NbtCheckResult result;

    public FailedNbt(String key, NbtCheck.NbtCheckResult result) {
        this.key = key;
        this.result = result;
    }

}

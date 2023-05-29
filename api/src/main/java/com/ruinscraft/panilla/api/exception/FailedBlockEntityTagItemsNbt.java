package com.ruinscraft.panilla.api.exception;

import com.ruinscraft.panilla.api.nbt.checks.NbtCheck;

import java.util.HashMap;
import java.util.Map;

public class FailedBlockEntityTagItemsNbt extends FailedNbt {

    private Map<Integer, FailedNbt> failedItems = new HashMap<>();

    public FailedBlockEntityTagItemsNbt(String key, NbtCheck.NbtCheckResult result) {
        super(key, result);
    }

    public FailedBlockEntityTagItemsNbt(String key, NbtCheck.NbtCheckResult result, Map<Integer, FailedNbt> failedItems) {
        this(key, result);
        this.failedItems = failedItems;
    }

    public Map<Integer, FailedNbt> getFailedItems() {
        return failedItems;
    }

    public boolean critical() {
        for (Map.Entry<Integer, FailedNbt> entry : failedItems.entrySet()) {
            if (entry.getValue().result == NbtCheck.NbtCheckResult.CRITICAL) {
                return true;
            }
        }
        return false;
    }

}

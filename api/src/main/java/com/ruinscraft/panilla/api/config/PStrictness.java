package com.ruinscraft.panilla.api.config;

public enum PStrictness {

    LENIENT(0), AVERAGE(1), STRICT(2);

    public final int lvl;

    PStrictness(int lvl) {
        this.lvl = lvl;
    }

}

package com.ruinscraft.panilla.api.nbt.checks;

import com.ruinscraft.panilla.api.IPanilla;
import com.ruinscraft.panilla.api.config.PStrictness;
import com.ruinscraft.panilla.api.nbt.INbtTagCompound;

public class NbtCheck_BlockStateTag extends NbtCheck {

    // introduced in 1.14
    public NbtCheck_BlockStateTag() {
        super("BlockStateTag", PStrictness.STRICT);
    }

    @Override
    public NbtCheckResult check(INbtTagCompound tag, String nmsItemClassName, IPanilla panilla) {
        return NbtCheckResult.FAIL;   // TODO: test variations of BlockStateTag to see what is potentially malicious
    }

}

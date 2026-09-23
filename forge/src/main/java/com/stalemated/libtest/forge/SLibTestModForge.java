package com.stalemated.libtest.forge;

import net.minecraftforge.fml.common.Mod;

import com.stalemated.libtest.SLibTestMod;

@Mod(SLibTestMod.MOD_ID)
public final class SLibTestModForge {
    public SLibTestModForge() {
        SLibTestMod.init();
    }
}

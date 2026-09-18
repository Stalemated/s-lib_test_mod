package com.stalemated.libtest.forge;

import net.minecraftforge.fml.common.Mod;

import com.stalemated.libtest.ExampleMod;

@Mod(ExampleMod.MOD_ID)
public final class ExampleModForge {
    public ExampleModForge() {
        // Run our common setup.
        ExampleMod.init();
    }
}

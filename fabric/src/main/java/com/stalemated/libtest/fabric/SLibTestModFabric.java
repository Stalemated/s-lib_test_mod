package com.stalemated.libtest.fabric;

import net.fabricmc.api.ModInitializer;

import com.stalemated.libtest.SLibTestMod;

public final class SLibTestModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SLibTestMod.init();
    }
}

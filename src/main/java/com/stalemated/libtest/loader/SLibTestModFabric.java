package com.stalemated.libtest.loader;

//? if fabric {
import com.stalemated.libtest.SLibTestMod;
import net.fabricmc.api.ModInitializer;

public final class SLibTestModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SLibTestMod.init();
    }
}
//?}

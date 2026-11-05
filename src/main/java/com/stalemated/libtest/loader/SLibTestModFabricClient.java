package com.stalemated.libtest.loader;

//? if fabric {
import com.stalemated.libtest.SLibTestModClient;
import net.fabricmc.api.ClientModInitializer;

public final class SLibTestModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SLibTestModClient.init();
    }
}
//?}

package com.stalemated.libtest.loader;

//? if fabric {
import com.stalemated.libtest.SLibTestModClient;
import com.stalemated.libtest.client.TestModTooltips;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public final class SLibTestModFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SLibTestModClient.init();
        //? if <1.21 {
        ItemTooltipCallback.EVENT.register((stack, flag, lines) -> TestModTooltips.append(stack, lines));
        //?} else {
        /*ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> TestModTooltips.append(stack, lines));
        *///?}
    }
}
//?}

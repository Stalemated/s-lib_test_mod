package com.stalemated.libtest.mixin.client;

import com.stalemated.lib.mixin.RequiresMod;
import com.stalemated.libtest.SLibTestMod;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@RequiresMod("yet_another_config_lib_v3")
@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin {
    @Inject(method = "init", at = @At("TAIL"))
    private void s_lib_test_mod$logConditionalMixin(CallbackInfo ci) {
        SLibTestMod.LOGGER.info("YACL is loaded, so the conditional mixin applied");
    }
}

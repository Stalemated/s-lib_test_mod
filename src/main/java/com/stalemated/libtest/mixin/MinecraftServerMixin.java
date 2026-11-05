package com.stalemated.libtest.mixin;

import com.stalemated.libtest.SLibTestMod;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = "loadLevel", at = @At("HEAD"))
    private void s_lib_test_mod$logLevelLoad(CallbackInfo ci) {
        SLibTestMod.LOGGER.info("{} sees the level loading", SLibTestMod.MOD_ID);
    }
}

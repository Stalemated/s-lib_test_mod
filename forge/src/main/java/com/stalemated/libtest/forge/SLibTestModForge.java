package com.stalemated.libtest.forge;

import com.stalemated.libtest.SLibTestMod;
import com.stalemated.libtest.forge.client.SLibTestModForgeClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

@Mod(SLibTestMod.MOD_ID)
public final class SLibTestModForge {
    public SLibTestModForge() {
        SLibTestMod.init();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> SLibTestModForgeClient::init);
    }
}

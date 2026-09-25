package com.stalemated.libtest.neoforge;

import com.stalemated.libtest.SLibTestMod;
import com.stalemated.libtest.neoforge.client.SLibTestModNeoForgeClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(SLibTestMod.MOD_ID)
public final class SLibTestModNeoForge {

    public SLibTestModNeoForge(ModContainer container) {
        SLibTestMod.init();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            SLibTestModNeoForgeClient.init(container);
        }
    }
}

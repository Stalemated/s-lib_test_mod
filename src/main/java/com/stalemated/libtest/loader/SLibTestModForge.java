package com.stalemated.libtest.loader;

//? if forge {
/*import com.stalemated.libtest.SLibTestMod;
import com.stalemated.libtest.SLibTestModClient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(SLibTestMod.MOD_ID)
public final class SLibTestModForge {
    public SLibTestModForge() {
        SLibTestMod.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            SLibTestModClient.init();
        }
    }
}
*///?}

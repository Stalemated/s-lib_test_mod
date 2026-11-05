package com.stalemated.libtest.loader;

//? if neoforge {
/*import com.stalemated.libtest.SLibTestMod;
import com.stalemated.libtest.SLibTestModClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(SLibTestMod.MOD_ID)
public final class SLibTestModNeoForge {
    public SLibTestModNeoForge(IEventBus modBus, Dist dist) {
        SLibTestMod.init();
        if (dist == Dist.CLIENT) {
            SLibTestModClient.init();
        }
    }
}
*///?}

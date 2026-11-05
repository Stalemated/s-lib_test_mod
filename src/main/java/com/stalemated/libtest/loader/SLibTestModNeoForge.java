package com.stalemated.libtest.loader;

//? if neoforge {
/*import com.stalemated.libtest.SLibTestMod;
import com.stalemated.libtest.SLibTestModClient;
import com.stalemated.libtest.client.TestModTooltips;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@Mod(SLibTestMod.MOD_ID)
public final class SLibTestModNeoForge {
    public SLibTestModNeoForge(IEventBus modBus, Dist dist) {
        SLibTestMod.init();
        if (dist == Dist.CLIENT) {
            SLibTestModClient.init();
            NeoForge.EVENT_BUS.addListener((ItemTooltipEvent event) -> TestModTooltips.append(event.getItemStack(), event.getToolTip()));
        }
    }
}
*///?}

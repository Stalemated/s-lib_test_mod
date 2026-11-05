package com.stalemated.libtest.loader;

//? if forge {
/*import com.stalemated.libtest.SLibTestMod;
import com.stalemated.libtest.SLibTestModClient;
import com.stalemated.libtest.client.TestModTooltips;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(SLibTestMod.MOD_ID)
public final class SLibTestModForge {
    public SLibTestModForge() {
        SLibTestMod.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            SLibTestModClient.init();
            MinecraftForge.EVENT_BUS.addListener((ItemTooltipEvent event) -> TestModTooltips.append(event.getItemStack(), event.getToolTip()));
        }
    }
}
*///?}

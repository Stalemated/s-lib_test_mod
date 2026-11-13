package com.stalemated.libtest;

import com.stalemated.lib.client.SLibConfigScreens;
import com.stalemated.libtest.client.TestModTooltips;
import com.stalemated.libtest.config.TestModConfigRegistry;
import com.stalemated.libtest.gui.screen.TestModConfigScreen;

//? if fabric {
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
//?} elif forge {
/*import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
*///?} elif neoforge {
/*import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
*///?}

public final class SLibTestModClient //? if fabric
implements ClientModInitializer
{
    //? if fabric {
    @Override
    public void onInitializeClient() {
        init();
    }
    //?} else {
    /*private SLibTestModClient() {
    }
    *///?}

    static void init() {
        SLibConfigScreens.register(SLibTestMod.MOD_ID, () -> TestModConfigScreen::create);
        TestModTooltips.reload(TestModConfigRegistry.CONFIG.get());
        TestModConfigRegistry.CONFIG.onChange(TestModTooltips::reload);
        //? if fabric && <1.21 {
        ItemTooltipCallback.EVENT.register((stack, flag, lines) -> TestModTooltips.append(stack, lines));
        //?} elif fabric {
        /*ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> TestModTooltips.append(stack, lines));
        *///?} elif forge {
        /*MinecraftForge.EVENT_BUS.addListener((ItemTooltipEvent event) -> TestModTooltips.append(event.getItemStack(), event.getToolTip()));
        *///?} elif neoforge {
        /*NeoForge.EVENT_BUS.addListener((ItemTooltipEvent event) -> TestModTooltips.append(event.getItemStack(), event.getToolTip()));
        *///?}
    }
}

package com.stalemated.libtest;

import com.mojang.blaze3d.platform.InputConstants;
import com.stalemated.lib.client.ClientPlatformHelper;
import com.stalemated.lib.client.SLibConfigScreens;
import com.stalemated.libtest.client.TestModTooltips;
import com.stalemated.libtest.client.WeaponStatsTooltip;
import com.stalemated.libtest.config.TestModConfigRegistry;
import com.stalemated.libtest.gui.screen.TestModConfigScreen;
import net.minecraft.client.KeyMapping;

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
        ClientPlatformHelper.registerKeyBinding(pingKey());
        SLibConfigScreens.register(SLibTestMod.MOD_ID, () -> TestModConfigScreen::create);
        TestModTooltips.reload(TestModConfigRegistry.CONFIG.get());
        TestModConfigRegistry.CONFIG.onChange(TestModTooltips::reload);
        //? if fabric && <1.21 {
        ItemTooltipCallback.EVENT.register((stack, flag, lines) -> TestModTooltips.append(stack, lines));
        ItemTooltipCallback.EVENT.register((stack, flag, lines) -> WeaponStatsTooltip.append(stack, lines));
        //?} elif fabric {
        /*ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> TestModTooltips.append(stack, lines));
        ItemTooltipCallback.EVENT.register((stack, context, flag, lines) -> WeaponStatsTooltip.append(stack, lines));
        *///?} elif forge {
        /*MinecraftForge.EVENT_BUS.addListener((ItemTooltipEvent event) -> TestModTooltips.append(event.getItemStack(), event.getToolTip()));
        MinecraftForge.EVENT_BUS.addListener((ItemTooltipEvent event) -> WeaponStatsTooltip.append(event.getItemStack(), event.getToolTip()));
        *///?} elif neoforge {
        /*NeoForge.EVENT_BUS.addListener((ItemTooltipEvent event) -> TestModTooltips.append(event.getItemStack(), event.getToolTip()));
        NeoForge.EVENT_BUS.addListener((ItemTooltipEvent event) -> WeaponStatsTooltip.append(event.getItemStack(), event.getToolTip()));
        *///?}
    }

    private static KeyMapping pingKey() {
        //? if <1.21.9 {
        return new KeyMapping("key.s_lib_test_mod.ping", InputConstants.UNKNOWN.getValue(), "key.categories.misc");
        //?} else
        //return new KeyMapping("key.s_lib_test_mod.ping", InputConstants.UNKNOWN.getValue(), KeyMapping.Category.MISC);
    }
}

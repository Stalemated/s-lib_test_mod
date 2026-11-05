package com.stalemated.libtest;

import com.stalemated.lib.client.SLibConfigScreens;
import com.stalemated.libtest.client.TestModTooltips;
import com.stalemated.libtest.config.TestModConfigs;
import com.stalemated.libtest.gui.screen.TestModConfigScreen;

public final class SLibTestModClient {
    private SLibTestModClient() {
    }

    public static void init() {
        SLibConfigScreens.register(SLibTestMod.MOD_ID, TestModConfigScreen::create);
        TestModTooltips.reload(TestModConfigs.CONFIG.get());
        TestModConfigs.CONFIG.onChange(TestModTooltips::reload);
    }
}

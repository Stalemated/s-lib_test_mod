package com.stalemated.libtest;

import com.stalemated.lib.client.SLibConfigScreens;
import com.stalemated.libtest.gui.screen.TestModConfigScreen;

public final class SLibTestModClient {
    private SLibTestModClient() {
    }

    public static void init() {
        SLibConfigScreens.register(SLibTestMod.MOD_ID, TestModConfigScreen::create);
    }
}

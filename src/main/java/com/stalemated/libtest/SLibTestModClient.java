package com.stalemated.libtest;

import com.stalemated.lib.client.SLibConfigScreens;
import com.stalemated.libtest.config.SLibTestModConfigScreen;

public final class SLibTestModClient {
    private SLibTestModClient() {
    }

    public static void init() {
        SLibConfigScreens.register(SLibTestMod.MOD_ID, SLibTestModConfigScreen::create);
    }
}

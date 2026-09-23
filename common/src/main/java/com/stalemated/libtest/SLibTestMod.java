package com.stalemated.libtest;

import com.stalemated.libtest.config.TestConfigManager;

public final class SLibTestMod {
    public static final String MOD_ID = "s_lib_test_mod";

    public static void init() {
        TestConfigManager.init();
    }
}

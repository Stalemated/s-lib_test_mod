package com.stalemated.libtest;

import com.stalemated.libtest.config.TestModConfigs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SLibTestMod {
    public static final String MOD_ID = "s_lib_test_mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private SLibTestMod() {
    }

    public static void init() {
        TestModConfigs.register();
    }
}

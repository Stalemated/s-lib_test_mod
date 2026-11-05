package com.stalemated.libtest.config;

import com.stalemated.lib.config.SLibConfig;
import com.stalemated.lib.config.SyncedConfigManager;
import com.stalemated.libtest.SLibTestMod;

public final class SLibTestModConfigs {
    public static final SyncedConfigManager<SLibTestModConfig> CONFIG = SLibConfig.synced(SLibTestMod.MOD_ID, SLibTestModConfig.class).build();

    private SLibTestModConfigs() {
    }

    public static void register() {
        CONFIG.register();
    }
}

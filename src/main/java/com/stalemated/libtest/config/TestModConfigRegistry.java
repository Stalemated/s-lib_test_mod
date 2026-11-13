package com.stalemated.libtest.config;

import com.stalemated.lib.config.LocalConfigManager;
import com.stalemated.lib.config.SLibConfig;
import com.stalemated.lib.config.SyncedConfigManager;
import com.stalemated.lib.config.permissions.ServerConfigPermissions;
import com.stalemated.lib.util.io.PathUtils;
import com.stalemated.libtest.SLibTestMod;
import com.stalemated.libtest.config.model.TestModConfig;
import com.stalemated.libtest.config.model.TestModConfigLocal;

public final class TestModConfigRegistry {
    public static final SyncedConfigManager<TestModConfig> CONFIG = SLibConfig.synced(SLibTestMod.MOD_ID, TestModConfig.class)
            .configPath(PathUtils.buildPath("s-lib_test_mod", "config.json5"))
            .permissionCheck(ServerConfigPermissions.OP_ONLY)
            .onInform((player, config) -> SLibTestMod.LOGGER.info("{} sent clientPreferences '{}' and serverInformedInt {}",
                    player.getName().getString(), config.clientPreferences, config.serverInformedInt))
            .onChange(config -> SLibTestMod.LOGGER.info("Synced config changed: testInt {}, testFloat {}, testString '{}'",
                    config.testInt, config.testFloat, config.testString))
            .onLoadFailed(failure -> SLibTestMod.LOGGER.warn("Synced config {} failed to load: {} {}",
                    failure.file(), failure.reason(), failure.rejectedValues()))
            .build();

    public static final LocalConfigManager<TestModConfigLocal> LOCAL = SLibConfig.local(SLibTestMod.MOD_ID, TestModConfigLocal.class)
            .configPath(PathUtils.buildPath("s-lib_test_mod", "config_local.json5"))
            .onChange(config -> SLibTestMod.LOGGER.info("Local config changed: testInt {}, primaryColor {}, favoriteWeapon {}",
                    config.testInt, config.primaryColor, config.favoriteWeapon))
            .onLoadFailed(failure -> SLibTestMod.LOGGER.warn("Local config {} failed to load: {} {}",
                    failure.file(), failure.reason(), failure.rejectedValues()))
            .build();

    private TestModConfigRegistry() {
    }

    public static void register() {
        CONFIG.register();
        LOCAL.register();
    }
}

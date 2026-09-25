package com.stalemated.libtest.config;

import com.stalemated.lib.config.SLibConfig;
import com.stalemated.lib.config.manager.LocalConfigManager;
import com.stalemated.lib.config.manager.SyncedConfigManager;
import com.stalemated.lib.util.io.PathUtils;
import com.stalemated.libtest.SLibTestMod;
import com.stalemated.libtest.config.model.TestModConfig;
import com.stalemated.libtest.config.model.TestModConfigLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * S-Lib Config Module
 * <p>
 * Demonstrates how to register, initialize, and listen to config events for both
 * {@link SyncedConfigManager} (network-synced) and {@link LocalConfigManager} (local).
 * <p>
 * A minimum viable config would be to register your {@link SyncedConfigManager} or {@link LocalConfigManager}
 * during mod initialization (e.g., inside your mod initializer / common setup)
 * or in the case of a ConfigManager class like this one, leaving your init() method empty.
 */
public class TestConfigManager {
    public static final Logger LOGGER = LoggerFactory.getLogger("S-Lib Test Mod");

    /**
     * Network-Synced Config Managers
     * Syncs server-authoritative fields to connected clients via S2C packets
     * and receives client preference informs via C2S packets.
     */
    public static final SyncedConfigManager<TestModConfig> MANAGER = SLibConfig.syncedBuilder(TestModConfig.class)
            .configPath(PathUtils.buildPath("s-lib_test_mod", "config.json5"))
            .logger(LOGGER)
            .modId(SLibTestMod.MOD_ID)
            .register();

    /**
     * Local Config Manager.
     * Operates exclusively on the local client or server environment with zero network communication.
     */
    public static final LocalConfigManager<TestModConfigLocal> MANAGER_LOCAL = SLibConfig.localBuilder(TestModConfigLocal.class)
            .configPath(PathUtils.buildPath("s-lib_test_mod", "config_local.json5"))
            .logger(LOGGER)
            .modId(SLibTestMod.MOD_ID)
            .register();

    /**
     * Registers lifecycle listeners for both managers.
     * Call this method during mod initialization (e.g., inside your mod initializer / common setup).
     */
    public static void init() {
        
        // SyncedConfigManager Lifecycle Listeners
        // region

        // Triggered on the CLIENT whenever the active config is synchronized or updated (load, S2C packet, disconnect, or local edit).
        MANAGER.onConfigSynced(config -> LOGGER.info(
                "Active config updated (State: {})! testInt: {}, testFloat: {}",
                MANAGER.getConnectionState(), config.testInt, config.testFloat
        ));

        // Triggered on the SERVER when a connected player sends client-side informed preferences (C2S).
        MANAGER.onConfigInformed((player, config) -> LOGGER.info(
                "C2S informed preference received from player '{}'. clientPreferences: {}",
                player.getName().getString(), config.clientPreferences
        ));

        // Triggered whenever the synced config is successfully written to disk.
        MANAGER.onConfigSaved(config -> LOGGER.info(
                "Config successfully saved to disk. testInt: {}, testFloat: {}",
                config.testInt, config.testFloat
        ));

        // Triggered whenever the synced config is loaded or reloaded from disk.
        MANAGER.onConfigLoaded(config -> LOGGER.info(
                "Config successfully loaded from disk. testInt: {}, testFloat: {}",
                config.testInt, config.testFloat
        ));
        // endregion

        // LocalConfigManager Lifecycle Listeners
        // region

        // Triggered whenever the local config is successfully written to disk.
        MANAGER_LOCAL.onConfigSaved(config -> LOGGER.info(
                "Local config saved to disk. testInt: {}, primaryColor: {}, favoriteWeapon: {}",
                config.testInt, config.primaryColor, config.favoriteWeapon
        ));

        // Triggered whenever the local config is loaded or reloaded from disk.
        MANAGER_LOCAL.onConfigLoaded(config -> LOGGER.info(
                "Local config loaded from disk. testInt: {}, primaryColor: {}, favoriteWeapon: {}",
                config.testInt, config.primaryColor, config.favoriteWeapon
        ));
        // endregion
    }

    /**
     * @return The currently active synced config instance (server-synced or client-active).
     */
    public static TestModConfig getActiveConfig() {
        return MANAGER.getActiveConfig();
    }

    /**
     * @return The currently active local config instance.
     */
    public static TestModConfigLocal getActiveLocalConfig() {
        return MANAGER_LOCAL.getActiveConfig();
    }
}

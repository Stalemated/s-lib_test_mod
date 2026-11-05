package com.stalemated.libtest.config;

import com.stalemated.lib.compat.yacl.builder.YaclCategoryBuilder;
import com.stalemated.lib.compat.yacl.builder.YaclScreenBuilder;
import com.stalemated.lib.config.ConfigManager;
import com.stalemated.libtest.SLibTestMod;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class SLibTestModConfigScreen {
    private SLibTestModConfigScreen() {
    }

    public static Screen create(Screen parent) {
        return builder(parent, SLibTestModConfigs.CONFIG).generateScreen();
    }

    static YaclScreenBuilder builder(Screen parent, ConfigManager<SLibTestModConfig> config) {
        return YaclScreenBuilder.create(parent, Component.translatable("s_lib_test_mod.config.title"), SLibTestMod.MOD_ID)
                .category(config, "general", YaclCategoryBuilder::autoGenerate);
    }
}

package com.stalemated.libtest.gui.screen;

import com.stalemated.lib.compat.yacl.builder.YaclCategoryBuilder;
import com.stalemated.lib.compat.yacl.builder.YaclScreenBuilder;
import com.stalemated.lib.config.ConfigManager;
import com.stalemated.libtest.SLibTestMod;
import com.stalemated.libtest.config.model.TestModConfig;
import com.stalemated.libtest.config.nested.DummyTooltipEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class TooltipEntryScreen {
    private TooltipEntryScreen() {
    }

    public static Screen edit(Screen parent, ConfigManager<TestModConfig> config) {
        return open(parent, config.editListElement(c -> c.tooltipEntries, config.get().tooltipEntries.get(0), DummyTooltipEntry::new));
    }

    public static Screen add(Screen parent, ConfigManager<TestModConfig> config) {
        return open(parent, config.addListElement(c -> c.tooltipEntries, DummyTooltipEntry::new));
    }

    static YaclScreenBuilder builder(Screen parent, ConfigManager<DummyTooltipEntry> manager, DummyTooltipEntry preview) {
        return YaclScreenBuilder.create(parent, Component.translatable("s_lib_test_mod.tooltip_entry.title"), SLibTestMod.MOD_ID)
                .livePreview(manager, preview, entry -> SLibTestMod.LOGGER.info("Previewing tooltip entry '{}'", entry.comment))
                .category(manager, "tooltip_entry", YaclCategoryBuilder::autoGenerate);
    }

    private static Screen open(Screen parent, ConfigManager<DummyTooltipEntry> manager) {
        return builder(parent, manager, manager.cloneConfig(manager.get())).generateScreen();
    }
}

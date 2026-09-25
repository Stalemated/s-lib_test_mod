package com.stalemated.libtest.gui.screen;

import com.stalemated.lib.compat.yacl.controller.builder.AdvancedColorControllerBuilder;
import com.stalemated.lib.compat.yacl.controller.builder.ItemOrTagControllerBuilder;
import com.stalemated.lib.config.permissions.ClientConfigPermissions;
import com.stalemated.lib.util.color.ColorUtils;
import com.stalemated.libtest.config.TestConfigManager;
import com.stalemated.libtest.config.model.TestEnum;
import com.stalemated.libtest.config.model.TestModConfig;
import com.stalemated.libtest.config.model.TestModConfigLocal;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.awt.Color;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Config Screen for S-Lib Test Mod.
 * <p>
 * Displays two distinct categories (tabs):
 * <ol>
 *     <li><b>Synced Config:</b> Bound to {@link TestModConfig}, demonstrating server-authoritative
 *     sync, C2S client-informed preferences, and local overrides.</li>
 *     <li><b>Local Config:</b> Bound to {@link TestModConfigLocal}, demonstrating purely client-side
 *     execution where {@code @Sync} annotations remain inert.</li>
 * </ol>
 */
public class TestModConfigScreen {

    public static Screen create(Screen parent) {
        boolean canEditSynced = ClientConfigPermissions.OP_OR_SP.get();

        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("s_lib_test_mod.config_screen.title"))
                .category(createSyncedCategory(canEditSynced))
                .category(createLocalCategory())
                .build()
                .generateScreen(parent);
    }

    // Tab 1: Synced Config Category
    // region

    private static ConfigCategory createSyncedCategory(boolean canEditSynced) {
        Supplier<TestModConfig> configSupplier = TestConfigManager::getActiveConfig;
        BiConsumer<String, Object> updater = TestConfigManager.MANAGER::updateOption;

        return ConfigCategory.createBuilder()
                .name(Text.translatable("s_lib_test_mod.category.synced"))
                .tooltip(Text.translatable("s_lib_test_mod.category.synced.desc"))
                .group(createSyncedPrimitivesGroup(canEditSynced, configSupplier, updater))
                .group(createSyncedAdaptersGroup(canEditSynced, configSupplier, updater))
                .group(createSyncedNetworkModesGroup(configSupplier, updater))
                .group(createSyncedNestedCategoryGroup(canEditSynced, configSupplier, updater))
                .build();
    }

    private static OptionGroup createSyncedPrimitivesGroup(
            boolean canEdit,
            Supplier<TestModConfig> cfg,
            BiConsumer<String, Object> updater
    ) {
        return OptionGroup.createBuilder()
                .name(Text.translatable("s_lib_test_mod.group.primitives"))
                .description(OptionDescription.of(Text.translatable("s_lib_test_mod.group.primitives.desc")))
                .option(createIntOption("testInt", canEdit, -100, 1000, 1,
                        cfg, c -> c.testInt, 50, updater))
                .option(createFloatOption("testFloat", canEdit, 0.0f, 5.0f, 0.1f,
                        cfg, c -> c.testFloat, 2.5f, updater))
                .option(createDoubleOption("testDouble", canEdit, -50.0, 50.0, 0.5,
                        cfg, c -> c.testDouble, 25.0, updater))
                .option(createBooleanOption("testBoolean", canEdit,
                        cfg, c -> c.testBoolean, true, updater))
                .option(createStringOption("testString", canEdit,
                        cfg, c -> c.testString, "Hello World!", updater::accept))
                .option(createEnumOption("testEnum", canEdit, TestEnum.class,
                        cfg, c -> c.testEnum, TestEnum.OPTION_B, updater))
                .build();
    }

    private static OptionGroup createSyncedAdaptersGroup(
            boolean canEdit,
            Supplier<TestModConfig> cfg,
            BiConsumer<String, Object> updater
    ) {
        return OptionGroup.createBuilder()
                .name(Text.translatable("s_lib_test_mod.group.adapters"))
                .description(OptionDescription.of(Text.translatable("s_lib_test_mod.group.adapters.desc")))
                .option(createColorOption("primaryColor", canEdit, false,
                        cfg, c -> c.primaryColor, new Color(70, 130, 180), updater))
                .option(createColorOption("translucentAccent", canEdit, true,
                        cfg, c -> c.translucentAccent, new Color(255, 105, 180, 128), updater))
                .option(createTextColorOption("chatHighlightColor", canEdit,
                        cfg, c -> c.chatHighlightColor, TextColor.fromFormatting(Formatting.GOLD), updater))
                .option(createTextColorOption("customRgbTextColor", canEdit,
                        cfg, c -> c.customRgbTextColor, TextColor.fromRgb(0x55FF55), updater))
                .option(createStringOption("itemFilterRegex", canEdit,
                        cfg, c -> c.itemFilterRegex.pattern(), "minecraft:.*_pickaxe",
                        (key, str) -> {
                            try {
                                updater.accept(key, Pattern.compile(str));
                            } catch (Exception ignored) {}
                        }))
                .option(createItemOrTagOption("favoriteWeapon", canEdit,
                        cfg, c -> c.favoriteWeapon.toString(), "minecraft:netherite_sword",
                        (key, idStr) -> {
                            Identifier id = Identifier.tryParse(idStr);
                            if (id != null) updater.accept(key, id);
                        }))
                .option(createStringOption("soundEffectId", canEdit,
                        cfg, c -> c.soundEffectId.toString(), "minecraft:entity.player.levelup",
                        (key, idStr) -> {
                            Identifier id = Identifier.tryParse(idStr);
                            if (id != null) updater.accept(key, id);
                        }))
                .option(createStringOption("playerProfileId", canEdit,
                        cfg, c -> c.playerProfileId.toString(), "00000000-0000-0000-0000-000000000001",
                        (key, uuidStr) -> {
                            try {
                                updater.accept(key, UUID.fromString(uuidStr));
                            } catch (Exception ignored) {}
                        }))
                .build();
    }

    private static OptionGroup createSyncedNetworkModesGroup(
            Supplier<TestModConfig> cfg,
            BiConsumer<String, Object> updater
    ) {
        return OptionGroup.createBuilder()
                .name(Text.translatable("s_lib_test_mod.group.network_modes"))
                .description(OptionDescription.of(Text.translatable("s_lib_test_mod.group.network_modes.desc")))
                // INFORM_SERVER options: always editable by client, sent to server via C2S
                .option(createStringOption("clientPreferences", true,
                        cfg, c -> c.clientPreferences, "high_performance", updater::accept))
                .option(createIntOption("serverInformedInt", true, 0, 500, 10,
                        cfg, c -> c.serverInformedInt, 100, updater))
                // NONE options: client-local, not affected by server permissions
                .option(createBooleanOption("localClientToggle", true,
                        cfg, c -> c.localClientToggle, true, updater))
                .option(createDoubleOption("localDouble", true, 0.5, 1.5, 0.05,
                        cfg, c -> c.localDouble, 0.8, updater))
                .build();
    }

    private static OptionGroup createSyncedNestedCategoryGroup(
            boolean canEdit,
            Supplier<TestModConfig> cfg,
            BiConsumer<String, Object> updater
    ) {
        return OptionGroup.createBuilder()
                .name(Text.translatable("s_lib_test_mod.group.nested_category"))
                .description(OptionDescription.of(Text.translatable("s_lib_test_mod.group.nested_category.desc")))
                .option(createBooleanOption("visualSettings.enableOverlay", canEdit,
                        cfg, c -> c.visualSettings.enableOverlay, true, updater))
                .option(createIntOption("visualSettings.hudScale", canEdit, 1, 5, 1,
                        cfg, c -> c.visualSettings.hudScale, 2, updater))
                .option(createColorOption("visualSettings.overlayBackground", canEdit, true,
                        cfg, c -> c.visualSettings.overlayBackground, new Color(0, 0, 0, 180), updater))
                .option(createDoubleOption("visualSettings.subSettings.animationSpeed", canEdit, 0.1, 10.0, 0.1,
                        cfg, c -> c.visualSettings.subSettings.animationSpeed, 1.0, updater))
                .option(createStringOption("visualSettings.subSettings.themeName", canEdit,
                        cfg, c -> c.visualSettings.subSettings.themeName, "modern_dark", updater::accept))
                .option(createBooleanOption("visualSettings.subSettings.showDebugBounds", canEdit,
                        cfg, c -> c.visualSettings.subSettings.showDebugBounds, false, updater))
                .build();
    }

    // endregion

    // Tab 2: Local Config Category
    // region

    private static ConfigCategory createLocalCategory() {
        Supplier<TestModConfigLocal> configSupplier = TestConfigManager::getActiveLocalConfig;
        BiConsumer<String, Object> updater = TestConfigManager.MANAGER_LOCAL::updateOption;

        return ConfigCategory.createBuilder()
                .name(Text.translatable("s_lib_test_mod.category.local"))
                .tooltip(Text.translatable("s_lib_test_mod.category.local.desc"))
                .group(createLocalPrimitivesGroup(configSupplier, updater))
                .group(createLocalAdaptersGroup(configSupplier, updater))
                .group(createLocalNetworkModesGroup(configSupplier, updater))
                .group(createLocalNestedCategoryGroup(configSupplier, updater))
                .build();
    }

    private static OptionGroup createLocalPrimitivesGroup(
            Supplier<TestModConfigLocal> cfg,
            BiConsumer<String, Object> updater
    ) {
        return OptionGroup.createBuilder()
                .name(Text.translatable("s_lib_test_mod.group.primitives"))
                .description(OptionDescription.of(Text.translatable("s_lib_test_mod.group.primitives.desc")))
                .option(createIntOption("testInt", true, -100, 1000, 1,
                        cfg, c -> c.testInt, 50, updater))
                .option(createFloatOption("testFloat", true, 0.0f, 5.0f, 0.1f,
                        cfg, c -> c.testFloat, 2.5f, updater))
                .option(createDoubleOption("testDouble", true, -50.0, 50.0, 0.5,
                        cfg, c -> c.testDouble, 25.0, updater))
                .option(createBooleanOption("testBoolean", true,
                        cfg, c -> c.testBoolean, true, updater))
                .option(createStringOption("testString", true,
                        cfg, c -> c.testString, "Hello World!", updater::accept))
                .option(createEnumOption("testEnum", true, TestEnum.class,
                        cfg, c -> c.testEnum, TestEnum.OPTION_B, updater))
                .build();
    }

    private static OptionGroup createLocalAdaptersGroup(
            Supplier<TestModConfigLocal> cfg,
            BiConsumer<String, Object> updater
    ) {
        return OptionGroup.createBuilder()
                .name(Text.translatable("s_lib_test_mod.group.adapters"))
                .description(OptionDescription.of(Text.translatable("s_lib_test_mod.group.adapters.desc")))
                .option(createColorOption("primaryColor", true, false,
                        cfg, c -> c.primaryColor, new Color(70, 130, 180), updater))
                .option(createColorOption("translucentAccent", true, true,
                        cfg, c -> c.translucentAccent, new Color(255, 105, 180, 128), updater))
                .option(createTextColorOption("chatHighlightColor", true,
                        cfg, c -> c.chatHighlightColor, TextColor.fromFormatting(Formatting.GOLD), updater))
                .option(createTextColorOption("customRgbTextColor", true,
                        cfg, c -> c.customRgbTextColor, TextColor.fromRgb(0x55FF55), updater))
                .option(createStringOption("itemFilterRegex", true,
                        cfg, c -> c.itemFilterRegex.pattern(), "minecraft:.*_pickaxe",
                        (key, str) -> {
                            try {
                                updater.accept(key, Pattern.compile(str));
                            } catch (Exception ignored) {}
                        }))
                .option(createItemOrTagOption("favoriteWeapon", true,
                        cfg, c -> c.favoriteWeapon.toString(), "minecraft:netherite_sword",
                        (key, idStr) -> {
                            Identifier id = Identifier.tryParse(idStr);
                            if (id != null) updater.accept(key, id);
                        }))
                .option(createStringOption("soundEffectId", true,
                        cfg, c -> c.soundEffectId.toString(), "minecraft:entity.player.levelup",
                        (key, idStr) -> {
                            Identifier id = Identifier.tryParse(idStr);
                            if (id != null) updater.accept(key, id);
                        }))
                .option(createStringOption("playerProfileId", true,
                        cfg, c -> c.playerProfileId.toString(), "00000000-0000-0000-0000-000000000001",
                        (key, uuidStr) -> {
                            try {
                                updater.accept(key, UUID.fromString(uuidStr));
                            } catch (Exception ignored) {}
                        }))
                .build();
    }

    private static OptionGroup createLocalNetworkModesGroup(
            Supplier<TestModConfigLocal> cfg,
            BiConsumer<String, Object> updater
    ) {
        return OptionGroup.createBuilder()
                .name(Text.translatable("s_lib_test_mod.group.network_modes"))
                .description(OptionDescription.of(Text.translatable("s_lib_test_mod.group.network_modes.desc")))
                // In local config, all options are purely local regardless of their @Sync annotation
                .option(createIntOption("serverDrivenInt", true, 0, 500, 10,
                        cfg, c -> c.serverDrivenInt, 100, updater))
                .option(createStringOption("clientPreferences", true,
                        cfg, c -> c.clientPreferences, "high_performance", updater::accept))
                .option(createBooleanOption("localClientToggle", true,
                        cfg, c -> c.localClientToggle, true, updater))
                .build();
    }

    private static OptionGroup createLocalNestedCategoryGroup(
            Supplier<TestModConfigLocal> cfg,
            BiConsumer<String, Object> updater
    ) {
        return OptionGroup.createBuilder()
                .name(Text.translatable("s_lib_test_mod.group.nested_category"))
                .description(OptionDescription.of(Text.translatable("s_lib_test_mod.group.nested_category.desc")))
                .option(createBooleanOption("visualSettings.enableOverlay", true,
                        cfg, c -> c.visualSettings.enableOverlay, true, updater))
                .option(createIntOption("visualSettings.hudScale", true, 1, 5, 1,
                        cfg, c -> c.visualSettings.hudScale, 2, updater))
                .option(createColorOption("visualSettings.overlayBackground", true, true,
                        cfg, c -> c.visualSettings.overlayBackground, new Color(0, 0, 0, 180), updater))
                .option(createDoubleOption("visualSettings.subSettings.animationSpeed", true, 0.1, 10.0, 0.1,
                        cfg, c -> c.visualSettings.subSettings.animationSpeed, 1.0, updater))
                .option(createStringOption("visualSettings.subSettings.themeName", true,
                        cfg, c -> c.visualSettings.subSettings.themeName, "modern_dark", updater::accept))
                .option(createBooleanOption("visualSettings.subSettings.showDebugBounds", true,
                        cfg, c -> c.visualSettings.subSettings.showDebugBounds, false, updater))
                .build();
    }

    // endregion

    // Generic Helper Option Builders
    // region

    private static <C> Option<Boolean> createBooleanOption(
            String configKey,
            boolean canEdit,
            Supplier<C> configSupplier,
            Function<C, Boolean> getter,
            boolean defaultValue,
            BiConsumer<String, Object> updater
    ) {
        String langKey = "s_lib_test_mod.option." + configKey.replace('.', '_');
        return Option.<Boolean>createBuilder()
                .name(Text.translatable(langKey))
                .description(OptionDescription.of(
                        Text.translatable(langKey + ".desc"),
                        canEdit ? Text.empty() : Text.translatable("s_lib_test_mod.config_screen.op_required")
                ))
                .binding(
                        defaultValue,
                        () -> getter.apply(configSupplier.get()),
                        val -> updater.accept(configKey, val)
                )
                .controller(TickBoxControllerBuilder::create)
                .available(canEdit)
                .build();
    }

    private static <C> Option<Integer> createIntOption(
            String configKey,
            boolean canEdit,
            int min,
            int max,
            int step,
            Supplier<C> configSupplier,
            Function<C, Integer> getter,
            int defaultValue,
            BiConsumer<String, Object> updater
    ) {
        String langKey = "s_lib_test_mod.option." + configKey.replace('.', '_');
        return Option.<Integer>createBuilder()
                .name(Text.translatable(langKey))
                .description(OptionDescription.of(
                        Text.translatable(langKey + ".desc"),
                        canEdit ? Text.empty() : Text.translatable("s_lib_test_mod.config_screen.op_required")
                ))
                .binding(
                        defaultValue,
                        () -> getter.apply(configSupplier.get()),
                        val -> updater.accept(configKey, val)
                )
                .controller(opt -> IntegerSliderControllerBuilder.create(opt).range(min, max).step(step))
                .available(canEdit)
                .build();
    }

    private static <C> Option<Float> createFloatOption(
            String configKey,
            boolean canEdit,
            float min,
            float max,
            float step,
            Supplier<C> configSupplier,
            Function<C, Float> getter,
            float defaultValue,
            BiConsumer<String, Object> updater
    ) {
        String langKey = "s_lib_test_mod.option." + configKey.replace('.', '_');
        return Option.<Float>createBuilder()
                .name(Text.translatable(langKey))
                .description(OptionDescription.of(
                        Text.translatable(langKey + ".desc"),
                        canEdit ? Text.empty() : Text.translatable("s_lib_test_mod.config_screen.op_required")
                ))
                .binding(
                        defaultValue,
                        () -> getter.apply(configSupplier.get()),
                        val -> updater.accept(configKey, val)
                )
                .controller(opt -> FloatSliderControllerBuilder.create(opt).range(min, max).step(step))
                .available(canEdit)
                .build();
    }

    private static <C> Option<Double> createDoubleOption(
            String configKey,
            boolean canEdit,
            double min,
            double max,
            double step,
            Supplier<C> configSupplier,
            Function<C, Double> getter,
            double defaultValue,
            BiConsumer<String, Object> updater
    ) {
        String langKey = "s_lib_test_mod.option." + configKey.replace('.', '_');
        return Option.<Double>createBuilder()
                .name(Text.translatable(langKey))
                .description(OptionDescription.of(
                        Text.translatable(langKey + ".desc"),
                        canEdit ? Text.empty() : Text.translatable("s_lib_test_mod.config_screen.op_required")
                ))
                .binding(
                        defaultValue,
                        () -> getter.apply(configSupplier.get()),
                        val -> updater.accept(configKey, val)
                )
                .controller(opt -> DoubleSliderControllerBuilder.create(opt).range(min, max).step(step))
                .available(canEdit)
                .build();
    }

    private static <C> Option<String> createStringOption(
            String configKey,
            boolean canEdit,
            Supplier<C> configSupplier,
            Function<C, String> getter,
            String defaultValue,
            BiConsumer<String, String> updater
    ) {
        String langKey = "s_lib_test_mod.option." + configKey.replace('.', '_');
        return Option.<String>createBuilder()
                .name(Text.translatable(langKey))
                .description(OptionDescription.of(
                        Text.translatable(langKey + ".desc"),
                        canEdit ? Text.empty() : Text.translatable("s_lib_test_mod.config_screen.op_required")
                ))
                .binding(
                        defaultValue,
                        () -> getter.apply(configSupplier.get()),
                        val -> updater.accept(configKey, val)
                )
                .controller(StringControllerBuilder::create)
                .available(canEdit)
                .build();
    }

    private static <C, E extends Enum<E>> Option<E> createEnumOption(
            String configKey,
            boolean canEdit,
            Class<E> enumClass,
            Supplier<C> configSupplier,
            Function<C, E> getter,
            E defaultValue,
            BiConsumer<String, Object> updater
    ) {
        String langKey = "s_lib_test_mod.option." + configKey.replace('.', '_');
        return Option.<E>createBuilder()
                .name(Text.translatable(langKey))
                .description(OptionDescription.of(
                        Text.translatable(langKey + ".desc"),
                        canEdit ? Text.empty() : Text.translatable("s_lib_test_mod.config_screen.op_required")
                ))
                .binding(
                        defaultValue,
                        () -> getter.apply(configSupplier.get()),
                        val -> updater.accept(configKey, val)
                )
                .controller(opt -> EnumControllerBuilder.create(opt).enumClass(enumClass))
                .available(canEdit)
                .build();
    }

    private static <C> Option<Color> createColorOption(
            String configKey,
            boolean canEdit,
            boolean allowAlpha,
            Supplier<C> configSupplier,
            Function<C, Color> getter,
            Color defaultValue,
            BiConsumer<String, Object> updater
    ) {
        String langKey = "s_lib_test_mod.option." + configKey.replace('.', '_');
        return Option.<Color>createBuilder()
                .name(Text.translatable(langKey))
                .description(OptionDescription.of(
                        Text.translatable(langKey + ".desc"),
                        canEdit ? Text.empty() : Text.translatable("s_lib_test_mod.config_screen.op_required")
                ))
                .binding(
                        defaultValue,
                        () -> getter.apply(configSupplier.get()),
                        val -> updater.accept(configKey, val)
                )
                .controller(opt -> ColorControllerBuilder.create(opt).allowAlpha(allowAlpha))
                .available(canEdit)
                .build();
    }

    private static <C> Option<String> createTextColorOption(
            String configKey,
            boolean canEdit,
            Supplier<C> configSupplier,
            Function<C, TextColor> getter,
            TextColor defaultValue,
            BiConsumer<String, Object> updater
    ) {
        String langKey = "s_lib_test_mod.option." + configKey.replace('.', '_');
        String defaultStr = defaultValue != null ? defaultValue.getName() : "white";

        return Option.<String>createBuilder()
                .name(Text.translatable(langKey))
                .description(OptionDescription.of(
                        Text.translatable(langKey + ".desc"),
                        canEdit ? Text.empty() : Text.translatable("s_lib_test_mod.config_screen.op_required")
                ))
                .binding(
                        defaultStr,
                        () -> {
                            TextColor tc = getter.apply(configSupplier.get());
                            return tc != null ? tc.getName() : defaultStr;
                        },
                        val -> {
                            TextColor tc = ColorUtils.resolveTextColor(val);
                            if (tc != null) updater.accept(configKey, tc);
                        }
                )
                .controller(opt -> AdvancedColorControllerBuilder.create(opt).alpha(false))
                .available(canEdit)
                .build();
    }

    private static <C> Option<String> createItemOrTagOption(
            String configKey,
            boolean canEdit,
            Supplier<C> configSupplier,
            Function<C, String> getter,
            String defaultValue,
            BiConsumer<String, String> updater
    ) {
        String langKey = "s_lib_test_mod.option." + configKey.replace('.', '_');
        return Option.<String>createBuilder()
                .name(Text.translatable(langKey))
                .description(OptionDescription.of(
                        Text.translatable(langKey + ".desc"),
                        canEdit ? Text.empty() : Text.translatable("s_lib_test_mod.config_screen.op_required")
                ))
                .binding(
                        defaultValue,
                        () -> getter.apply(configSupplier.get()),
                        val -> updater.accept(configKey, val)
                )
                .controller(ItemOrTagControllerBuilder::create)
                .available(canEdit)
                .build();
    }
    // endregion
}

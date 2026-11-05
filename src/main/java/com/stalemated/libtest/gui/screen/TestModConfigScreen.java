package com.stalemated.libtest.gui.screen;

import com.stalemated.lib.compat.yacl.builder.NestListBinding;
import com.stalemated.lib.compat.yacl.builder.YaclCategoryBuilder;
import com.stalemated.lib.compat.yacl.builder.YaclScreenBuilder;
import com.stalemated.lib.compat.yacl.registry.YaclControllerRegistry;
import com.stalemated.lib.config.ConfigManager;
import com.stalemated.libtest.SLibTestMod;
import com.stalemated.libtest.config.TestModConfigs;
import com.stalemated.libtest.config.model.TestModConfig;
import com.stalemated.libtest.config.model.TestModConfigLocal;
import com.stalemated.libtest.config.nested.DummyEnchantmentRules;
import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TestModConfigScreen {
    private static final List<String> CLIENT_PREFERENCES = List.of("high_performance", "balanced", "high_quality");
    private static final String NUMBER = "\\s*(-?\\d+(?:\\.\\d+)?(?:[eE]-?\\d+)?)\\s*";
    private static final Pattern VECTOR = Pattern.compile(NUMBER + "," + NUMBER + "," + NUMBER);

    private TestModConfigScreen() {
    }

    public static Screen create(Screen parent) {
        return builder(parent, TestModConfigs.CONFIG, TestModConfigs.LOCAL, EnchantmentIds::current).generateScreen();
    }

    static YaclScreenBuilder builder(
            Screen parent,
            ConfigManager<TestModConfig> synced,
            ConfigManager<TestModConfigLocal> local,
            Supplier<Collection<String>> enchantmentIds
    ) {
        return YaclScreenBuilder.create(parent, Component.translatable("s_lib_test_mod.config.title"), SLibTestMod.MOD_ID)
                .controllerRegistry(controllers())
                .category(synced, "synced", category -> category
                        .group("primitives", group -> group
                                .include("testInt", "testLong", "testFloat", "testDouble", "sliderStep005", "sliderStep0002",
                                        "percentage", "testBoolean", "testString", "testEnum"))
                        .group("adapters", group -> group
                                .include("primaryColor", "translucentAccent", "chatHighlightColor", "customRgbTextColor",
                                        "itemFilterRegex", "favoriteWeapon", "soundEffectId", "playerProfileId", "spawnOffset")
                                .configureOption("playerProfileId", option -> option.available(false)))
                        .listOption("colorPalette")
                        .listOption("statusIndicators")
                        .group("list_elements", group -> group
                                .listElement("statusIndicators", 0, "first_status_indicator", TextColor.fromRgb(0x55FF55)))
                        .listOption("itemBlacklist")
                        .listOption("allowedDimensions")
                        .mapPairsOption("translationAliases")
                        .group("network_modes", group -> group
                                .dropdown("clientPreferences", CLIENT_PREFERENCES)
                                .include("serverInformedInt", "localClientToggle", "localDouble"))
                        .nestedMap("enchantmentRules")
                        .nestedList("tooltipEntries"))
                .category(synced, "visual_settings", category -> category.nested("visualSettings"))
                .category(synced, "enchantment_allowed", category -> category
                        .nestedMapLists("enchantmentRules", allowedEnchantments(enchantmentIds)))
                .category(synced, "tooltip_entries", category -> category
                        .group("tooltip_tools", group -> group
                                .option(button("edit_first_entry", !synced.get().tooltipEntries.isEmpty(),
                                        screen -> TooltipEntryScreen.edit(screen, synced)))
                                .option(button("add_entry", true, screen -> TooltipEntryScreen.add(screen, synced))))
                        .nestedList("tooltipEntries"))
                .category(local, "local", YaclCategoryBuilder::autoGenerate);
    }

    private static ButtonOption button(String key, boolean available, UnaryOperator<Screen> opened) {
        return ButtonOption.createBuilder()
                .name(Component.translatable("s_lib_test_mod.option." + key))
                .description(OptionDescription.of(Component.translatable("s_lib_test_mod.option." + key + ".desc")))
                .available(available)
                .action((screen, option) -> show(opened.apply(screen)))
                .text(Component.empty())
                .build();
    }

    private static void show(Screen screen) {
        //? if <26.2 {
        Minecraft.getInstance().setScreen(screen);
        //?} else {
        /*Minecraft.getInstance().gui.setScreen(screen);
        *///?}
    }

    private static NestListBinding<DummyEnchantmentRules> allowedEnchantments(Supplier<Collection<String>> enchantmentIds) {
        return NestListBinding.of(
                        (DummyEnchantmentRules rules) -> rules.allowed,
                        (DummyEnchantmentRules rules, List<String> allowed) -> rules.allowed = allowed)
                .withKeys(enchantmentIds, DummyEnchantmentRules::new)
                .withEntryNames(id -> Component.translatable("s_lib_test_mod.option.enchantment_allowed.entry", id))
                .withDescription(Component.translatable("s_lib_test_mod.option.enchantment_allowed.desc"));
    }

    private static YaclControllerRegistry controllers() {
        YaclControllerRegistry registry = YaclControllerRegistry.createDefault();
        registry.registerAdapted(Vector3f.class, TestModConfigScreen::formatVector, TestModConfigScreen::parseVector, "0.0,0.0,0.0",
                (info, option) -> StringControllerBuilder.create(option));
        return registry;
    }

    private static String formatVector(Vector3f vector) {
        return vector.x() + "," + vector.y() + "," + vector.z();
    }

    private static Vector3f parseVector(String text) {
        Matcher matcher = VECTOR.matcher(text);
        if (!matcher.matches()) {
            SLibTestMod.LOGGER.warn("A Vector3f must be written as x,y,z, got '{}'", text);
            return null;
        }
        return new Vector3f(Float.parseFloat(matcher.group(1)), Float.parseFloat(matcher.group(2)), Float.parseFloat(matcher.group(3)));
    }
}

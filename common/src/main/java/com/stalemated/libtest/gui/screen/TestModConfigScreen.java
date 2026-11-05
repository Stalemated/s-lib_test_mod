package com.stalemated.libtest.gui.screen;

import com.stalemated.lib.compat.yacl.builder.NestListBinding;
import com.stalemated.lib.compat.yacl.builder.YaclScreenBuilder;
import com.stalemated.lib.compat.yacl.registry.OptionAvailabilityRule;
import com.stalemated.libtest.SLibTestMod;
import com.stalemated.libtest.config.TestConfigManager;
import com.stalemated.libtest.config.model.TestModConfig;
import com.stalemated.libtest.config.model.TestModConfigLocal;
import com.stalemated.libtest.config.nested.DummyEnchantmentRules;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

/**
 * Config Screen for S-Lib Test Mod demonstrating comprehensive YACL integration.
 * <p>
 * Displays 7 distinct categories (tabs):
 * <ol>
 *     <li><b>Synced Config:</b> Bound to {@link TestModConfig}, demonstrating server-authoritative
 *     sync, C2S client-informed preferences, and local overrides, ordered 1 through 7.</li>
 *     <li><b>Local Config:</b> Bound to {@link TestModConfigLocal}, demonstrating purely client-side
 *     execution where {@code @Sync} annotations remain inert, ordered 1 through 7.</li>
 *     <li><b>Auto-Generated Showcase:</b> Generated via {@code .autoGenerate()} to showcase zero-boilerplate
 *     screen generation directly from the config schema.</li>
 *     <li><b>Visual Settings:</b> Dedicated projection tab for the nested POJO category via {@code categoryFromNest()}.</li>
 *     <li><b>Enchantment Rules:</b> Dedicated projection tab for the nested POJO map via {@code categoryFromNestMap()}.</li>
 *     <li><b>Custom Tooltips:</b> Dedicated projection tab for the nested POJO list via {@code categoryFromNestList()}.</li>
 *     <li><b>Enchantment Allowed Lists:</b> Dedicated projection tab for inner list properties of map entries via {@code categoryFromNestMapList()}.</li>
 * </ol>
 */
public class TestModConfigScreen {

    public static Screen create(Screen parent) {
        return YaclScreenBuilder.create(parent, Text.translatable("s_lib_test_mod.config_screen.title"))
                .modId(SLibTestMod.MOD_ID)
                .availabilityRule(OptionAvailabilityRule.DEFAULT)

                // Tab 1: Synced Config Category (1..7 strict POJO order)
                .category(TestConfigManager.MANAGER, cat -> cat
                        .name(Text.translatable("s_lib_test_mod.category.synced"))
                        .tooltip(Text.translatable("s_lib_test_mod.category.synced.desc"))
                        // 1. Primitives
                        .group("primitives", g -> g
                                .name(Text.translatable("s_lib_test_mod.group.primitives"))
                                .description(Text.translatable("s_lib_test_mod.group.primitives.desc"))
                                .include("testInt", "testLong", "testFloat", "testDouble", "testBoolean", "testString", "testEnum")
                        )
                        // 2. Adapters
                        .group("adapters", g -> g
                                .name(Text.translatable("s_lib_test_mod.group.adapters"))
                                .description(Text.translatable("s_lib_test_mod.group.adapters.desc"))
                                .include("primaryColor", "translucentAccent", "chatHighlightColor", "customRgbTextColor",
                                         "itemFilterRegex", "favoriteWeapon", "soundEffectId", "playerProfileId")
                        )
                        // 3. Native Leaf Collections
                        .listOption("colorPalette")
                        .listOption("statusIndicators")
                        .listOption("itemBlacklist")
                        .listOption("allowedDimensions")
                        .mapPairsOption("translationAliases")
                        // 4. Network & Sync Modes
                        .group("network_modes", g -> g
                                .name(Text.translatable("s_lib_test_mod.group.network_modes"))
                                .description(Text.translatable("s_lib_test_mod.group.network_modes.desc"))
                                .include("clientPreferences", "serverInformedInt", "localClientToggle", "localDouble")
                        )
                        // 5. Unrestricted Enchanting Simulation (Map with Nested POJOs)
                        .mapNestGroups("enchantmentRules")
                        // 6. Custom Tooltip API Simulation (List with Nested POJOs)
                        .listNestGroups("tooltipEntries")
                        // 7. Hierarchical Nested Category
                        .nestedGroup("visualSettings", g -> g
                                .name(Text.translatable("s_lib_test_mod.group.visualSettings"))
                                .description(Text.translatable("s_lib_test_mod.group.visualSettings.desc"))
                        )
                )

                // Tab 2: Local Config Category (1..7 strict POJO order)
                .category(TestConfigManager.MANAGER_LOCAL, cat -> cat
                        .name(Text.translatable("s_lib_test_mod.category.local"))
                        .tooltip(Text.translatable("s_lib_test_mod.category.local.desc"))
                        // 1. Primitives
                        .group("primitives", g -> g
                                .name(Text.translatable("s_lib_test_mod.group.primitives"))
                                .description(Text.translatable("s_lib_test_mod.group.primitives.desc"))
                                .include("testInt", "testLong", "testFloat", "testDouble", "testBoolean", "testString", "testEnum")
                        )
                        // 2. Adapters
                        .group("adapters", g -> g
                                .name(Text.translatable("s_lib_test_mod.group.adapters"))
                                .description(Text.translatable("s_lib_test_mod.group.adapters.desc"))
                                .include("primaryColor", "translucentAccent", "chatHighlightColor", "customRgbTextColor",
                                         "itemFilterRegex", "favoriteWeapon", "soundEffectId", "playerProfileId")
                        )
                        // 3. Native Leaf Collections
                        .listOption("colorPalette")
                        .listOption("statusIndicators")
                        .listOption("itemBlacklist")
                        .listOption("allowedDimensions")
                        .mapPairsOption("translationAliases")
                        // 4. Network & Sync Modes
                        .group("network_modes", g -> g
                                .name(Text.translatable("s_lib_test_mod.group.network_modes"))
                                .description(Text.translatable("s_lib_test_mod.group.network_modes.desc"))
                                .include("serverDrivenInt", "clientPreferences", "localClientToggle")
                        )
                        // 5. Unrestricted Enchanting Simulation (Map with Nested POJOs)
                        .mapNestGroups("enchantmentRules")
                        // 6. Custom Tooltip API Simulation (List with Nested POJOs)
                        .listNestGroups("tooltipEntries")
                        // 7. Hierarchical Nested Category
                        .nestedGroup("visualSettings", g -> g
                                .name(Text.translatable("s_lib_test_mod.group.visualSettings"))
                                .description(Text.translatable("s_lib_test_mod.group.visualSettings.desc"))
                        )
                )

                // Tab 3: Auto-Generated Showcase Tab
                .category(TestConfigManager.MANAGER_LOCAL, cat -> cat
                        .name(Text.translatable("s_lib_test_mod.category.autogen"))
                        .tooltip(Text.translatable("s_lib_test_mod.category.autogen.desc"))
                        .autoGenerate()
                )

                // Tab 4: Dedicated Tab for Nested Category (visualSettings)
                .categoryFromNest(
                        "visual_settings",
                        "visualSettings",
                        TestConfigManager.MANAGER,
                        cat -> cat.tooltip(Text.translatable("s_lib_test_mod.category.visual_settings.desc"))
                )

                // Tab 5: Dedicated Tab for Nested Map (enchantmentRules)
                .categoryFromNestMap(
                        "enchantment_rules",
                        "enchantmentRules",
                        TestConfigManager.MANAGER,
                        cat -> cat.tooltip(Text.translatable("s_lib_test_mod.category.enchantment_rules.desc"))
                )

                // Tab 6: Dedicated Tab for Nested List (tooltipEntries)
                .categoryFromNestList(
                        "tooltip_entries",
                        "tooltipEntries",
                        TestConfigManager.MANAGER,
                        cat -> cat.tooltip(Text.translatable("s_lib_test_mod.category.tooltip_entries.desc"))
                )

                // Tab 7: Dedicated Tab for Enchantment Allowed List Projection (UE-style demo)
                .categoryFromNestMapList(
                        "enchantment_allowed",
                        "enchantmentRules",
                        TestConfigManager.MANAGER,
                        NestListBinding.of(
                                (DummyEnchantmentRules rules) -> rules.allowed,
                                (DummyEnchantmentRules rules, List<String> list) -> rules.allowed = list
                        ),
                        cat -> cat.tooltip(Text.translatable("s_lib_test_mod.category.enchantment_allowed.desc"))
                )
                .generateScreen();
    }
}

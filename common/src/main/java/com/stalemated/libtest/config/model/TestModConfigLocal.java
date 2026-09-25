package com.stalemated.libtest.config.model;

import com.stalemated.lib.config.annotation.*;
import com.stalemated.lib.config.manager.LocalConfigManager;
import com.stalemated.lib.config.network.SyncMode;
import com.stalemated.libtest.config.nested.CategorySettings;
import com.stalemated.libtest.config.nested.DummyEnchantmentRules;
import com.stalemated.libtest.config.nested.DummyTooltipEntry;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.awt.Color;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This is an example of a synced config model, using a {@link LocalConfigManager}.
 * <p>
 * Features:
 * <li>If the config file is missing or empty, it regenerates
 * with default values and comments.</li>
 * <li>New config keys are added automatically with their default values.</li>
 * <li>Keys that do not exist are automatically deleted.</li>
 * <li>If manual edits introduce invalid syntax or corrupt the file, S-Lib creates
 * a backup copy in the config directory.</li>
 * <li>Keys with the {@link RangeInt}, {@link RangeFloat}, or {@link RangeDouble} annotations are
 * automatically clamped to their set limits upon loading.</li>
 * <li>Keys with the {@link Ignore} annotation are completely ignored and do not appear in the config file.</li>
 * <li>Local-Only Execution:
 * <ul>
 *     <li>It operates strictly on the local machine (client or dedicated server).</li>
 *     <li>All {@code @Sync} annotations are inert; NO network packets (S2C or C2S) are ever sent or received.</li>
 *     <li>Server configs cannot override this file, and client settings cannot alter the server.</li>
 * </ul></li>
 */
public class TestModConfigLocal {

    @Comment("""
            ====================================================================================================
            1. Primitives & Numeric Constraints
            ====================================================================================================
            Demonstrates basic primitive types, numeric boundaries (@Range annotations), enums, and @Ignore.
            
            Integer with range bounds [-100 to 1000]
            """)
    @RangeInt(min = -100, max = 1000)
    public int testInt = 50;

    @Comment("Floating-point value with range bounds [0.0 to 5.0]")
    @RangeFloat(min = 0.0f, max = 5.0f)
    public float testFloat = 2.5f;

    @Comment("Double-precision floating-point value with range bounds [-50.0 to 50.0]")
    @RangeDouble(min = -50.0, max = 50.0)
    public double testDouble = 25.0;

    @Comment("Basic boolean toggle")
    public boolean testBoolean = true;

    @Comment("Standard text string option")
    public String testString = "Hello World!";

    @Comment("Enum option (serialized by name: OPTION_A, OPTION_B, OPTION_C)")
    public TestEnum testEnum = TestEnum.OPTION_B;

    @Comment("Ignored field demonstrating @Ignore (excluded from serialization, disk, and network)")
    @Ignore
    public String ignoredField = "This field is ignored by S-Lib";

    @Comment("""
            ====================================================================================================
            2. Built-in Type Adapters
            ====================================================================================================
            Demonstrates S-Lib's built-in adapters for AWT Color, Minecraft TextColor, Pattern, Identifier, and UUID.
            
            Standard opaque AWT Color (RGB)
            """)
    public Color primaryColor = new Color(70, 130, 180); // Steel Blue

    @Comment("AWT Color with transparency / alpha channel (RGBA)")
    public Color translucentAccent = new Color(255, 105, 180, 128); // Semi-transparent Hot Pink

    @Comment("Named Minecraft TextColor (canonical color name e.g. 'gold')")
    public TextColor chatHighlightColor = TextColor.fromFormatting(Formatting.GOLD);

    @Comment("Custom RGB Minecraft TextColor (formatted as #RRGGBB hex)")
    public TextColor customRgbTextColor = TextColor.fromRgb(0x55FF55);

    @Comment("Compiled regex Pattern for item/entity filtering")
    public Pattern itemFilterRegex = Pattern.compile("minecraft:.*_pickaxe");

    @Comment("Direct Minecraft Item/Block identifier binding (stored as raw namespaced string)")
    public Identifier favoriteWeapon = Identifier.of("minecraft", "netherite_sword");

    @Comment("Namespaced Identifier for sound events or custom registries")
    public Identifier soundEffectId = Identifier.of("minecraft", "entity.player.levelup");

    @Comment("Standard UUID string serialization")
    public UUID playerProfileId = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Comment("""
            ====================================================================================================
            3. Native Leaf Collections (No @Nest Needed)
            ====================================================================================================
            S-Lib automatically detects collections of primitives and built-in adapter types as leaves.
            
            Palette list of AWT Colors
            """)
    public List<Color> colorPalette = new ArrayList<>(List.of(
            new Color(255, 0, 0),
            new Color(0, 255, 0),
            new Color(0, 0, 255)
    ));

    @Comment("List of Minecraft TextColors (supports both named and hex colors)")
    public List<TextColor> statusIndicators = new ArrayList<>(List.of(
            Objects.requireNonNull(TextColor.fromFormatting(Formatting.GREEN)),
            Objects.requireNonNull(TextColor.fromFormatting(Formatting.YELLOW)),
            Objects.requireNonNull(TextColor.fromFormatting(Formatting.RED))
    ));

    @Comment("List of registered Minecraft Item/Block identifiers")
    public List<Identifier> itemBlacklist = new ArrayList<>(List.of(
            Identifier.of("minecraft", "tnt"),
            Identifier.of("minecraft", "lava_bucket")
    ));

    @Comment("List of dimension identifiers")
    public List<Identifier> allowedDimensions = new ArrayList<>(List.of(
            Identifier.of("minecraft", "overworld"),
            Identifier.of("minecraft", "the_nether"),
            Identifier.of("minecraft", "the_end")
    ));

    @Comment("Key-value translation aliases map with native primitive string leaves")
    public Map<String, String> translationAliases = new TreeMap<>(Map.of(
            "gui.confirm", "Acknowledge",
            "gui.dismiss", "Decline"
    ));

    @Comment("""
            ====================================================================================================
            4. Network & Sync Modes (Inert Local Test)
            ====================================================================================================
            In LocalConfigManager, @Sync annotations MUST HAVE ZERO EFFECT.
            
            Marked as OVERRIDE_CLIENT: Under LocalConfigManager, this must NOT accept server overrides.
            """)
    @Sync(SyncMode.OVERRIDE_CLIENT)
    @RangeInt(min = 0, max = 500)
    public int serverDrivenInt = 100;

    @Comment("Marked as INFORM_SERVER: Under LocalConfigManager, this must NOT dispatch a C2S packet.")
    @Sync(SyncMode.INFORM_SERVER)
    public String clientPreferences = "high_performance";

    @Comment("Marked as NONE: Standard local behavior.")
    @Sync(SyncMode.NONE)
    public boolean localClientToggle = true;

    @Comment("""
            ====================================================================================================
            5. Unrestricted Enchanting Simulation (Map with Nested POJOs via @Nest)
            ====================================================================================================
            Simulates a complex map mapping enchantment identifiers to nested rule objects.
            """)
    @Nest
    public Map<String, DummyEnchantmentRules> enchantmentRules = new TreeMap<>(Map.of(
            "minecraft:mending", new DummyEnchantmentRules(
                    List.of("minecraft:infinity"),
                    List.of("minecraft:unbreaking"),
                    1,
                    true
            ),
            "minecraft:sharpness", new DummyEnchantmentRules(
                    List.of("minecraft:smite", "minecraft:bane_of_arthropods"),
                    List.of(),
                    2,
                    false
            )
    ));

    @Comment("""
            ====================================================================================================
            6. Custom Tooltip API Simulation (List with Nested POJOs via @Nest)
            ====================================================================================================
            Simulates a list of complex tooltip entries using nested POJOs with enums, colors, and constraints.
            """)
    @Nest
    @Sync(SyncMode.NONE)
    public List<DummyTooltipEntry> tooltipEntries = new ArrayList<>(List.of(
            new DummyTooltipEntry()
    ));

    @Comment("""
            ====================================================================================================
            7. Hierarchical Nested Categories (@Nest POJOs)
            ====================================================================================================
            Demonstrates multi-level nested categories with UI/Visual preferences and sub-categories.
            """)
    @Nest
    public CategorySettings visualSettings = new CategorySettings();
}

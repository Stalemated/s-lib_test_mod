package com.stalemated.libtest.config.model;

import com.google.gson.annotations.SerializedName;
import com.stalemated.lib.config.annotation.Comment;
import com.stalemated.lib.config.annotation.EnabledIf;
import com.stalemated.lib.config.annotation.Format;
import com.stalemated.lib.config.annotation.Ignore;
import com.stalemated.lib.config.annotation.ItemDropdown;
import com.stalemated.lib.config.annotation.Nest;
import com.stalemated.lib.config.annotation.RangeDouble;
import com.stalemated.lib.config.annotation.RangeFloat;
import com.stalemated.lib.config.annotation.RangeInt;
import com.stalemated.lib.config.annotation.RangeLong;
import com.stalemated.lib.config.annotation.SharedDescription;
import com.stalemated.lib.config.annotation.Sync;
import com.stalemated.lib.config.annotation.Title;
import com.stalemated.lib.config.network.SyncMode;
import com.stalemated.lib.util.ResourceIds;
import com.stalemated.libtest.config.nested.CategorySettings;
import com.stalemated.libtest.config.nested.DummyEnchantmentRules;
import com.stalemated.libtest.config.nested.DummyTooltipEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Pattern;

@Sync(SyncMode.OVERRIDE_CLIENT)
public class TestModConfig {

    @Title("primitives")
    @Comment("""
            ====================================================================================================
            1. Primitives & Numeric Constraints
            ====================================================================================================
            Demonstrates basic primitive types, numeric boundaries (@Range annotations), enums, and @Ignore.

            Integer with range bounds [-100 to 1000]
            """)
    @RangeInt(min = -100, max = 1000)
    public int testInt = 50;

    @Comment("Long integer with range bounds [-10000 to 100000]")
    @RangeLong(min = -10000L, max = 100000L, step = 500L)
    public long testLong = 25000L;

    @Comment("Floating-point value with range bounds [0.0 to 5.0]")
    @RangeFloat(min = 0.0f, max = 5.0f)
    public float testFloat = 2.5f;

    @Comment("Double-precision floating-point value with range bounds [-50.0 to 50.0], enabled while testEnum is OPTION_A or OPTION_C")
    @EnabledIf(value = "testEnum", equals = {"OPTION_A", "OPTION_C"})
    @RangeDouble(min = -50.0, max = 50.0)
    public double testDouble = 25.0;

    @Comment("Slider with a 0.05 step [0.0 to 1.0]")
    @RangeFloat(min = 0, max = 1, step = 0.05f)
    public float sliderStep005 = 0.5f;

    @Comment("Slider with a 0.002 step [0.0 to 0.1]")
    @RangeFloat(min = 0, max = 0.1f, step = 0.002f)
    public float sliderStep0002 = 0.05f;

    @Comment("Percentage shown through @Format [0 to 100]")
    @RangeFloat(min = 0, max = 100, step = 1)
    @Format("%.0f%%")
    public float percentage = 75;

    @Comment("Basic boolean toggle")
    public boolean testBoolean = true;

    @Comment("Standard text string option (also read from the old key legacyString)")
    @SerializedName(value = "testString", alternate = {"legacyString"})
    public String testString = "Hello World!";

    @Comment("Enum option (serialized by name: OPTION_A, OPTION_B, OPTION_C)")
    public TestEnum testEnum = TestEnum.OPTION_B;

    @Comment("Ignored field demonstrating @Ignore (excluded from serialization, disk, and network)")
    @Ignore
    public String ignoredField = "This field is ignored by S-Lib";

    @Title("adapters")
    @Comment("""
            ====================================================================================================
            2. Built-in Type Adapters
            ====================================================================================================
            Demonstrates S-Lib's built-in adapters for AWT Color, Minecraft TextColor, Pattern, Identifier, and UUID.

            Standard opaque AWT Color (RGB)
            """)
    @SharedDescription("color")
    public Color primaryColor = new Color(70, 130, 180);

    @Comment("AWT Color with transparency / alpha channel (RGBA)")
    @SharedDescription("color")
    public Color translucentAccent = new Color(255, 105, 180, 128);

    @Comment("Named Minecraft TextColor (canonical color name e.g. 'gold')")
    public TextColor chatHighlightColor = TextColor.fromLegacyFormat(ChatFormatting.GOLD);

    @Comment("Custom RGB Minecraft TextColor (formatted as #RRGGBB hex)")
    public TextColor customRgbTextColor = TextColor.fromRgb(0x55FF55);

    @Comment("Compiled regex Pattern for item/entity filtering")
    public Pattern itemFilterRegex = Pattern.compile("minecraft:.*_pickaxe");

    @Comment("Direct Minecraft Item/Block identifier binding (uses Item dropdown without #tags)")
    @ItemDropdown(allowTags = false)
    public ResourceLocation favoriteWeapon = ResourceIds.of("minecraft", "netherite_sword");

    @Comment("Namespaced Identifier for sound events or custom registries")
    public ResourceLocation soundEffectId = ResourceIds.of("minecraft", "entity.player.levelup");

    @Comment("Standard UUID string serialization")
    public UUID playerProfileId = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Comment("Vector with a controller registered by the config screen (x,y,z)")
    public Vector3f spawnOffset = new Vector3f(0, 1.5f, 0);

    @Title("collections")
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
            Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.GREEN)),
            Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.YELLOW)),
            Objects.requireNonNull(TextColor.fromLegacyFormat(ChatFormatting.RED))
    ));

    @Comment("List of registered Minecraft Item or #Tag strings (supports items, #tags, and * wildcards)")
    @ItemDropdown
    public List<String> itemBlacklist = new ArrayList<>(List.of(
            "minecraft:tnt",
            "minecraft:lava_bucket",
            "#minecraft:logs"
    ));

    @Comment("List of dimension identifiers")
    public List<ResourceLocation> allowedDimensions = new ArrayList<>(List.of(
            ResourceIds.of("minecraft", "overworld"),
            ResourceIds.of("minecraft", "the_nether"),
            ResourceIds.of("minecraft", "the_end")
    ));

    @Comment("Key-value translation aliases map with native primitive string leaves")
    public Map<String, String> translationAliases = new TreeMap<>(Map.of(
            "gui.confirm", "Acknowledge",
            "gui.dismiss", "Decline"
    ));

    @Title("network_modes")
    @Comment("""
            ====================================================================================================
            4. Network & Sync Modes
            ====================================================================================================
            Demonstrates other network sync modes under SyncedConfigManager.

            Client-informed setting: Sent from client to server via C2S packet on connect (e.g. client preferences)
            """)
    @Sync(SyncMode.INFORM_SERVER)
    public String clientPreferences = "high_performance";

    @Comment("Client-informed setting: Sent from client to server via C2S packet on connect")
    @Sync(SyncMode.INFORM_SERVER)
    @RangeInt(min = 0, max = 500)
    public int serverInformedInt = 100;

    @Comment("Local-only setting in a synced config: Not synchronized over network, retained per-client")
    @Sync(SyncMode.NONE)
    public boolean localClientToggle = true;

    @Comment("Local-only setting in a synced config: Not synchronized over network, retained per-client")
    @Sync(SyncMode.NONE)
    @RangeDouble(min = 0.5, max = 1.5)
    public double localDouble = 0.8;

    @Title("enchantment_rules")
    @Comment("""
            ====================================================================================================
            5. Unrestricted Enchanting Simulation (Map with Nested POJOs via @Nest)
            ====================================================================================================
            Simulates a complex map mapping enchantment identifiers to nested rule objects.
            """)
    @Nest
    public Map<String, DummyEnchantmentRules> enchantmentRules = new TreeMap<>(Map.of(
            "minecraft:mending", new DummyEnchantmentRules(
                    List.of(ResourceIds.of("minecraft", "infinity")),
                    List.of(ResourceIds.of("minecraft", "unbreaking")),
                    1,
                    true
            ),
            "minecraft:sharpness", new DummyEnchantmentRules(
                    List.of(ResourceIds.of("minecraft", "smite"), ResourceIds.of("minecraft", "bane_of_arthropods")),
                    List.of(),
                    2,
                    false
            )
    ));

    @Title("tooltip_entries")
    @Comment("""
            ====================================================================================================
            6. Custom Tooltip API Simulation (List with Nested POJOs via @Nest)
            ====================================================================================================
            Simulates a list of complex tooltip entries using nested POJOs with enums, colors, and constraints.
            """)
    @Nest
    @Sync(SyncMode.NONE)
    public List<DummyTooltipEntry> tooltipEntries = new ArrayList<>(List.of(
            new DummyTooltipEntry(),
            new DummyTooltipEntry("Datapack tag test", "regex:#s_lib_test_mod:tooltip_.*", List.of("Tagged by a datapack"))
    ));

    @Title("visualSettings")
    @Comment("""
            ====================================================================================================
            7. Hierarchical Nested Categories (@Nest POJOs)
            ====================================================================================================
            Demonstrates multi-level nested categories with UI/Visual preferences and sub-categories.
            """)
    @Nest
    public CategorySettings visualSettings = new CategorySettings();
}

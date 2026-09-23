package com.stalemated.libtest.config.nested;

import com.stalemated.lib.config.annotation.Comment;
import com.stalemated.lib.config.annotation.Nest;
import com.stalemated.lib.config.annotation.RangeDouble;
import com.stalemated.lib.config.annotation.RangeInt;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.awt.Color;

/**
 * Reusable nested category demonstrating multi-level nested POJOs via @Nest.
 */
public class CategorySettings {

    @Comment("Enable custom HUD overlay rendering")
    public boolean enableOverlay = true;

    @Comment("HUD scaling factor [1 - 5]")
    @RangeInt(min = 1, max = 5)
    public int hudScale = 2;

    @Comment("Semi-transparent background color for the HUD overlay (#AARRGGBB)")
    public Color overlayBackground = new Color(0, 0, 0, 180);

    @Comment("Default text color for HUD widgets")
    public TextColor hudTextColor = TextColor.fromFormatting(Formatting.AQUA);

    @Comment("Sub-nested category verifying multi-level hierarchical nesting")
    @Nest
    public SubCategory subSettings = new SubCategory();

    public CategorySettings() {}

    public static class SubCategory {
        @Comment("HUD element animation speed multiplier [0.1 - 10.0]")
        @RangeDouble(min = 0.1, max = 10.0)
        public double animationSpeed = 1.0;

        @Comment("Active visual theme profile")
        public String themeName = "modern_dark";

        @Comment("Show debug wireframe bounds around HUD widgets")
        public boolean showDebugBounds = false;

        public SubCategory() {}
    }
}

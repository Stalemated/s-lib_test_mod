package com.stalemated.libtest.config.nested;

import com.stalemated.lib.config.annotation.Comment;
import com.stalemated.lib.config.annotation.RangeInt;
import net.minecraft.text.TextColor;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Data model simulating a custom tooltip entry (inspired by Custom Tooltip API).
 * Demonstrates a complex nested POJO containing enums, identifiers, UUIDs, colors, text colors, and numeric bounds.
 */
public class DummyTooltipEntry {

    @Comment("Descriptive label or comment for this tooltip entry")
    public String comment = "Custom Legendary Item Tooltip";

    @Comment("Lines of text rendered inside the custom tooltip")
    public List<String> text = new ArrayList<>(List.of("§6Legendary Blade", "§7Forged in dragon breath"));

    @Comment("Positioning mode of the tooltip relative to vanilla lines")
    public TooltipPosition position = TooltipPosition.BOTTOM;

    @Comment("Target item or tag identifier (e.g. minecraft:diamond_sword)")
    public Identifier target = new Identifier("minecraft", "diamond_sword");

    @Comment("Render tooltip header text in bold font")
    public boolean bold = true;

    @Comment("Render tooltip text in italic font")
    public boolean italic = false;

    @Comment("Background box opacity [0 - 255]")
    @RangeInt(min = 0, max = 255)
    public int backgroundOpacity = 240;

    @Comment("Unique persistent identifier for this tooltip entry")
    public UUID uuid = UUID.fromString("11111111-2222-3333-4444-555555555555");

    @Comment("Border accent color (AWT Color hex format)")
    public Color color = new Color(255, 215, 0); // Gold

    @Comment("List of Minecraft text colors for gradient/accent rendering")
    public List<TextColor> textColors = new ArrayList<>(List.of(
            TextColor.fromRgb(Colors.RED),
            TextColor.fromRgb(Colors.WHITE)
    ));

    public DummyTooltipEntry() {}

    public enum TooltipPosition {
        TOP, BOTTOM, REPLACE_NAME, APPEND, PREPEND, REPLACE_LINE
    }
}

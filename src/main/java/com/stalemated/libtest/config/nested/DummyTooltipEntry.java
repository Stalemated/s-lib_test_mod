package com.stalemated.libtest.config.nested;

import com.stalemated.lib.config.annotation.Comment;
import com.stalemated.lib.config.annotation.ItemDropdown;
import com.stalemated.lib.config.annotation.RangeInt;
import net.minecraft.network.chat.TextColor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DummyTooltipEntry {

    @Comment("Descriptive label or comment for this tooltip entry")
    public String comment = "Custom Legendary Item Tooltip";

    @Comment("Lines of text rendered inside the custom tooltip")
    public List<String> text = new ArrayList<>(List.of("Legendary Blade", "Forged in dragon breath"));

    @Comment("Positioning mode of the tooltip relative to vanilla lines")
    public TooltipPosition position = TooltipPosition.BOTTOM;

    @Comment("Target item, tag or matcher (e.g. minecraft:diamond_sword, #minecraft:swords or regex:minecraft:.*_sword)")
    @ItemDropdown
    public String target = "minecraft:diamond_sword";

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
    public Color color = new Color(255, 215, 0);

    @Comment("List of Minecraft text colors for gradient/accent rendering")
    public List<TextColor> textColors = new ArrayList<>(List.of(
            TextColor.fromRgb(0xFF5555),
            TextColor.fromRgb(0xFFFFFF)
    ));

    public DummyTooltipEntry() {
    }

    public DummyTooltipEntry(String comment, String target, List<String> text) {
        this.comment = comment;
        this.target = target;
        this.text = new ArrayList<>(text);
    }

    public enum TooltipPosition {
        TOP, BOTTOM
    }
}

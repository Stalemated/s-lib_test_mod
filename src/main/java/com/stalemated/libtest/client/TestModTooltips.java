package com.stalemated.libtest.client;

import com.stalemated.lib.predicate.target.TargetMatcher;
import com.stalemated.lib.predicate.target.TargetMatcherFactory;
import com.stalemated.libtest.SLibTestMod;
import com.stalemated.libtest.config.model.TestModConfig;
import com.stalemated.libtest.config.nested.DummyTooltipEntry;
import com.stalemated.libtest.config.nested.DummyTooltipEntry.TooltipPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;

public final class TestModTooltips {
    private static final Map<TooltipPosition, BiConsumer<List<Component>, List<Component>>> INSERTIONS = Map.of(
            TooltipPosition.TOP, (lines, added) -> lines.addAll(Math.min(1, lines.size()), added),
            TooltipPosition.BOTTOM, List::addAll
    );

    private static volatile List<TooltipRule> rules = List.of();

    private TestModTooltips() {
    }

    public static void reload(TestModConfig config) {
        List<TooltipRule> loaded = new ArrayList<>();
        for (DummyTooltipEntry entry : config.tooltipEntries) {
            Optional<TargetMatcher> matcher = TargetMatcherFactory.tryCreate(entry.target);
            if (matcher.isEmpty()) {
                SLibTestMod.LOGGER.warn("Skipping tooltip entry '{}': '{}' is not a valid target", entry.comment, entry.target);
                continue;
            }
            loaded.add(new TooltipRule(matcher.get(), INSERTIONS.get(entry.position), styledLines(entry)));
        }
        rules = List.copyOf(loaded);
    }

    public static void append(ItemStack stack, List<Component> lines) {
        for (TooltipRule rule : rules) {
            if (rule.matcher().matches(stack)) {
                rule.insertion().accept(lines, rule.lines());
            }
        }
    }

    private static List<Component> styledLines(DummyTooltipEntry entry) {
        TextColor color = TextColor.fromRgb(entry.color.getRGB() & 0xFFFFFF);
        List<Component> styled = new ArrayList<>();
        for (String text : entry.text) {
            boolean header = styled.isEmpty();
            styled.add(Component.literal(text).withStyle(style -> style.withColor(color).withBold(header && entry.bold).withItalic(entry.italic)));
        }
        return List.copyOf(styled);
    }

    private record TooltipRule(TargetMatcher matcher, BiConsumer<List<Component>, List<Component>> insertion, List<Component> lines) {
    }
}

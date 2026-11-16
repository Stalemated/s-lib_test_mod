package com.stalemated.libtest.config;

import com.stalemated.lib.config.LocalConfigManager;
import com.stalemated.lib.config.SLibConfig;
import com.stalemated.lib.config.io.ConfigLoadFailure;
import com.stalemated.lib.config.io.InvalidEntry;
import com.stalemated.libtest.SLibTestMod;
import com.stalemated.libtest.config.model.TestModConfig;
import net.minecraft.resources.ResourceLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestModConfigLoadTest {
    @TempDir
    Path tempDir;

    @Test
    void badElementsInNestedListsDropOnlyThemselves() throws IOException {
        Path file = tempDir.resolve("config.json5");
        Files.writeString(file, """
                {
                  "enchantmentRules": {
                    "minecraft:mending": { "allowed": ["minecraft:infinity", "Not An Id"] }
                  },
                  "tooltipEntries": [
                    { "comment": "kept", "textColors": ["#FF0000", "not a color"] }
                  ]
                }
                """);

        List<ConfigLoadFailure> failures = new ArrayList<>();
        LocalConfigManager<TestModConfig> manager = SLibConfig.local(SLibTestMod.MOD_ID, TestModConfig.class)
                .configPath(file)
                .onLoadFailed(failures::add)
                .register();

        TestModConfig config = manager.get();
        assertEquals(List.of("minecraft:infinity"), config.enchantmentRules.get("minecraft:mending").allowed.stream().map(ResourceLocation::toString).toList());
        assertEquals("kept", config.tooltipEntries.get(0).comment);
        assertEquals(1, config.tooltipEntries.get(0).textColors.size());
        assertEquals(List.of("enchantmentRules.minecraft:mending.allowed[1]", "tooltipEntries[0].textColors[1]"),
                failures.stream().flatMap(failure -> failure.rejectedValues().stream()).map(InvalidEntry::path).toList());
    }
}

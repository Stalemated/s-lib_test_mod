package com.stalemated.libtest.gui.screen;

import com.stalemated.lib.compat.yacl.SLibTesting;
import com.stalemated.lib.config.InMemoryConfigManager;
import com.stalemated.libtest.config.model.TestModConfig;
import com.stalemated.libtest.config.model.TestModConfigLocal;
import com.stalemated.libtest.config.nested.DummyTooltipEntry;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

class TestModConfigScreenTest {
    @Test
    void theConfigScreenBuilds() throws Exception {
        SLibTesting.assertScreenBuilds(
                TestModConfigScreen.builder(null,
                        new InMemoryConfigManager<>(new TestModConfig(), TestModConfig::new, null),
                        new InMemoryConfigManager<>(new TestModConfigLocal(), TestModConfigLocal::new, null),
                        () -> List.of("minecraft:mending", "minecraft:unbreaking")),
                Path.of(TestModConfigScreenTest.class.getResource("/assets/s_lib_test_mod/lang/en_us.json").toURI())
        );
    }

    @Test
    void theTooltipEntryScreenBuilds() throws Exception {
        SLibTesting.assertScreenBuilds(
                TooltipEntryScreen.builder(null,
                        new InMemoryConfigManager<>(new DummyTooltipEntry(), DummyTooltipEntry::new, null),
                        new DummyTooltipEntry()),
                Path.of(TestModConfigScreenTest.class.getResource("/assets/s_lib_test_mod/lang/en_us.json").toURI())
        );
    }
}

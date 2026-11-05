package com.stalemated.libtest.client;

import com.stalemated.libtest.config.model.TestModConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class TestModTooltipsTest {
    @Test
    void anEntryWithoutAPositionDoesNotBreakTheReload() {
        TestModConfig config = new TestModConfig();
        config.tooltipEntries.get(0).position = null;

        assertDoesNotThrow(() -> TestModTooltips.reload(config));
    }
}

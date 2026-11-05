package com.stalemated.libtest.config;

import com.stalemated.lib.compat.yacl.SLibTesting;
import com.stalemated.lib.config.InMemoryConfigManager;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

class SLibTestModConfigScreenTest {
    @Test
    void theConfigScreenBuilds() throws Exception {
        SLibTesting.assertScreenBuilds(
                SLibTestModConfigScreen.builder(null, new InMemoryConfigManager<>(new SLibTestModConfig(), SLibTestModConfig::new, null)),
                Path.of(SLibTestModConfigScreenTest.class.getResource("/assets/s_lib_test_mod/lang/en_us.json").toURI())
        );
    }
}

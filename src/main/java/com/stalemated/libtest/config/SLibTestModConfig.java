package com.stalemated.libtest.config;

import com.stalemated.lib.config.annotation.Comment;
import com.stalemated.lib.config.annotation.EnabledIf;
import com.stalemated.lib.config.annotation.RangeInt;
import com.stalemated.lib.config.annotation.Sync;
import com.stalemated.lib.config.network.SyncMode;

@Sync(SyncMode.OVERRIDE_CLIENT)
public class SLibTestModConfig {
    @Comment("Turns the mod on or off")
    public boolean enabled = true;

    @Comment("How many times the effect repeats")
    @EnabledIf("enabled")
    @RangeInt(min = 1, max = 64)
    public int repeats = 4;
}

package com.stalemated.libtest.config.nested;

import com.stalemated.lib.config.annotation.Comment;
import com.stalemated.lib.config.annotation.RangeInt;
import com.stalemated.lib.util.ResourceIds;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class DummyEnchantmentRules {

    @Comment("List of enchantment IDs allowed to bypass normal restrictions")
    public List<ResourceLocation> allowed = new ArrayList<>(List.of(ResourceIds.of("minecraft", "infinity"), ResourceIds.of("minecraft", "mending")));

    @Comment("List of enchantment IDs explicitly restricted or forced incompatible")
    public List<ResourceLocation> restricted = new ArrayList<>(List.of(ResourceIds.of("minecraft", "unbreaking")));

    @Comment("Multiplier applied to the maximum allowable enchantment level [1 - 255]")
    @RangeInt(min = 1, max = 255)
    public int maxLevelMultiplier = 1;

    @Comment("Whether to completely bypass vanilla enchantment incompatibility checks")
    public boolean bypassVanillaIncompatibilities = true;

    public DummyEnchantmentRules() {
    }

    public DummyEnchantmentRules(List<ResourceLocation> allowed, List<ResourceLocation> restricted, int maxLevelMultiplier, boolean bypassIncompatibilities) {
        this.allowed = new ArrayList<>(allowed);
        this.restricted = new ArrayList<>(restricted);
        this.maxLevelMultiplier = maxLevelMultiplier;
        this.bypassVanillaIncompatibilities = bypassIncompatibilities;
    }
}

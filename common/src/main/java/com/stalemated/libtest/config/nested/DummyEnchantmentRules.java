package com.stalemated.libtest.config.nested;

import com.stalemated.lib.config.annotation.Comment;
import com.stalemated.lib.config.annotation.RangeInt;

import java.util.ArrayList;
import java.util.List;

/**
 * Data model simulating an enchantment rule set (inspired by Unrestricted Enchanting).
 * Demonstrates a complex object containing collections, primitive flags, and bounded integers.
 */
public class DummyEnchantmentRules {

    @Comment("List of enchantment IDs allowed to bypass normal restrictions")
    public List<String> allowed = new ArrayList<>(List.of("minecraft:infinity", "minecraft:mending"));

    @Comment("List of enchantment IDs explicitly restricted or forced incompatible")
    public List<String> restricted = new ArrayList<>(List.of("minecraft:unbreaking"));

    @Comment("Multiplier applied to the maximum allowable enchantment level [1 - 255]")
    @RangeInt(min = 1, max = 255)
    public int maxLevelMultiplier = 1;

    @Comment("Whether to completely bypass vanilla enchantment incompatibility checks")
    public boolean bypassVanillaIncompatibilities = true;

    public DummyEnchantmentRules() {}

    public DummyEnchantmentRules(List<String> allowed, List<String> restricted, int maxLevelMultiplier, boolean bypassIncompatibilities) {
        this.allowed = new ArrayList<>(allowed);
        this.restricted = new ArrayList<>(restricted);
        this.maxLevelMultiplier = maxLevelMultiplier;
        this.bypassVanillaIncompatibilities = bypassIncompatibilities;
    }
}

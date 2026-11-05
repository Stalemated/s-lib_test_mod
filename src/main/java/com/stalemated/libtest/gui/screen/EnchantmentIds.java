package com.stalemated.libtest.gui.screen;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
//? if <1.21 {
import net.minecraft.core.registries.BuiltInRegistries;
//?} else {
/*import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.registries.Registries;
import java.util.List;
*///?}

import java.util.Collection;

final class EnchantmentIds {
    private EnchantmentIds() {
    }

    static Collection<String> current() {
        //? if <1.21 {
        return idsOf(BuiltInRegistries.ENCHANTMENT);
        //?} elif <1.21.2 {
        /*ClientLevel level = Minecraft.getInstance().level;
        return level == null ? List.of() : idsOf(level.registryAccess().registryOrThrow(Registries.ENCHANTMENT));
        *///?} else {
        /*ClientLevel level = Minecraft.getInstance().level;
        return level == null ? List.of() : idsOf(level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT));
        *///?}
    }

    private static Collection<String> idsOf(Registry<Enchantment> registry) {
        return registry.keySet().stream().map(ResourceLocation::toString).toList();
    }
}

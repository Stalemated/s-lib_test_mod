package com.stalemated.libtest.client;

import com.stalemated.lib.helper.attribute.AttributeGetter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class WeaponStatsTooltip {
    private static final String NO_DAMAGE = "0";

    private WeaponStatsTooltip() {
    }

    public static void append(ItemStack stack, List<Component> lines) {
        String damage = AttributeGetter.calculateWeaponDamage(stack);
        if (damage.equals(NO_DAMAGE)) return;
        String speed = AttributeGetter.calculateWeaponSpeed(stack);
        lines.add(Component.literal("S-Lib: " + damage + " attack damage, " + speed + " attack speed").withStyle(ChatFormatting.GRAY));
    }
}

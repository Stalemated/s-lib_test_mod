package com.stalemated.libtest.gui.screen;

import com.stalemated.lib.compat.yacl.controller.builder.ItemOrTagControllerBuilder;
import com.stalemated.libtest.SLibTestMod;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.controller.ControllerBuilder;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class HandMadeCategory {
    private static final Map<String, String> DEFAULT_VALUES = Map.of(
            "itemOrTag", "minecraft:diamond",
            "itemOnly", "minecraft:stick"
    );
    private static final Map<String, String> SAVED_VALUES = new HashMap<>(DEFAULT_VALUES);

    private HandMadeCategory() {
    }

    public static ConfigCategory create() {
        return ConfigCategory.createBuilder()
                .name(Component.translatable("s_lib_test_mod.category.hand_made"))
                .tooltip(Component.translatable("s_lib_test_mod.category.hand_made.desc"))
                .option(itemOption("itemOrTag", "hand_made_item_or_tag", ItemOrTagControllerBuilder::create))
                .option(itemOption("itemOnly", "hand_made_item_only", option -> ItemOrTagControllerBuilder.create(option).allowTags(false)))
                .build();
    }

    private static Option<String> itemOption(String field, String key, Function<Option<String>, ControllerBuilder<String>> controller) {
        return Option.<String>createBuilder()
                .name(Component.translatable("s_lib_test_mod.option." + key))
                .description(OptionDescription.of(Component.translatable("s_lib_test_mod.option." + key + ".desc")))
                .binding(DEFAULT_VALUES.get(field), () -> SAVED_VALUES.get(field), value -> save(field, value))
                .controller(controller)
                .build();
    }

    private static void save(String field, String value) {
        SAVED_VALUES.put(field, value);
        SLibTestMod.LOGGER.info("Hand-made category saved {} '{}'", field, value);
    }
}

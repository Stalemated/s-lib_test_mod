package com.stalemated.libtest.forge.client;

import com.stalemated.libtest.gui.screen.TestModConfigScreen;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;

@SuppressWarnings("removal")
public final class SLibTestModForgeClient {

    private SLibTestModForgeClient() {}

    public static void init() {
        ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((client, parent) -> TestModConfigScreen.create(parent))
        );
    }
}

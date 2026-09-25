package com.stalemated.libtest.neoforge.client;

import com.stalemated.libtest.gui.screen.TestModConfigScreen;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

public final class SLibTestModNeoForgeClient {

    private SLibTestModNeoForgeClient() {}

    public static void init(ModContainer container) {
        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (client, parent) -> TestModConfigScreen.create(parent)
        );
    }
}

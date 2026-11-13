package com.stalemated.libtest;

import com.stalemated.libtest.config.TestModConfigRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? if fabric {
import net.fabricmc.api.ModInitializer;
//?} elif forge {
/*import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
*///?} elif neoforge {
/*import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
*///?}

//? if forge || neoforge {
/*@Mod(SLibTestMod.MOD_ID)
*///?}
public final class SLibTestMod //? if fabric
implements ModInitializer
{
    public static final String MOD_ID = "s_lib_test_mod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    //? if fabric {
    @Override
    public void onInitialize() {
        init();
    }
    //?} elif forge {
    /*public SLibTestMod() {
        init();
        if (FMLEnvironment.dist == Dist.CLIENT) SLibTestModClient.init();
    }
    *///?} elif neoforge {
    /*public SLibTestMod(Dist dist) {
        init();
        if (dist == Dist.CLIENT) SLibTestModClient.init();
    }
    *///?}

    private static void init() {
        TestModConfigRegistry.register();
    }
}

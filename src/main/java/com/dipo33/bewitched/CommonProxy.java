package com.dipo33.bewitched;

import com.dipo33.bewitched.block.BlockRegistry;
import com.dipo33.bewitched.items.ItemRegistry;
import com.dipo33.bewitched.items.SeedDrops;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    /**
     * Performs mod pre-initialization by registering blocks and items, then synchronizing configuration.
     *
     * @param event the pre-initialization event containing mod context and the suggested configuration file
     */
    public void preInit(FMLPreInitializationEvent event) {
        BlockRegistry.registerBlocks();
        ItemRegistry.registerItems();


        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
    }

    /**
     * Performs mod initialization tasks during the Forge initialization phase.
     *
     * Registers seed drops from grass blocks so those seeds can appear during world generation and gameplay.
     *
     * @param event the initialization event provided by Forge
     */
    public void init(FMLInitializationEvent event) {
        SeedDrops.dropSeedsFromGrass();
    }

    /**
     * Called during the mod post-initialization phase to handle interactions with other mods and perform any final setup.
     *
     * @param event the post-initialization event providing context for cross-mod integration and late-stage initialization
     */
    public void postInit(FMLPostInitializationEvent event) {
    }

    /**
     * Called during server startup to register server-side commands and perform server-specific initialization.
     *
     * @param event the server starting event providing access to command registration and server context
     */
    public void serverStarting(FMLServerStartingEvent event) {
    }
}

package com.dipo33.bewitched;

import com.dipo33.bewitched.init.BewitchedBlocks;
import com.dipo33.bewitched.config.Config;
import com.dipo33.bewitched.init.BewitchedMutations;
import com.dipo33.bewitched.init.BewitchedRecipes;
import com.dipo33.bewitched.init.BewitchedItems;
import com.dipo33.bewitched.items.SeedDrops;
import com.dipo33.bewitched.network.BwNetwork;
import com.dipo33.bewitched.network.message.EffectPlayMsg;
import com.dipo33.bewitched.network.message.UpdateFlowerPotMsg;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    /**
     * Perform mod pre-initialization: register blocks and items, set up networking and client message handlers,
     * and synchronize the mod configuration.
     *
     * @param event the Forge pre-initialization event providing the suggested configuration file and environment data
     */
    public void preInit(FMLPreInitializationEvent event) {
        BewitchedBlocks.registerBlocks();
        BewitchedItems.registerItems();
        BwNetwork.register();
        this.registerClientMessages();

        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
    }

    /**
     * Registers client-side network message handlers used by the mod.
     *
     * Specifically registers the client handlers for EffectPlayMsg (EffectPlayMsg.SafeHandler)
     * and UpdateFlowerPotMsg (UpdateFlowerPotMsg.SafeHandler).
     */
    protected void registerClientMessages() {
        BwNetwork.registerClientMessage(EffectPlayMsg.SafeHandler.class, EffectPlayMsg.class);
        BwNetwork.registerClientMessage(UpdateFlowerPotMsg.SafeHandler.class, UpdateFlowerPotMsg.class);
    }

    /**
     * Initialize core mod systems and perform inter-mod registration.
     *
     * Initializes recipes and mutation data, triggers grass seed drops, and sends a Waila registration message.
     *
     * @param event the Forge initialization event
     */
    public void init(FMLInitializationEvent event) {
        BewitchedRecipes.init();
        BewitchedMutations.init();
        SeedDrops.dropSeedsFromGrass();

        FMLInterModComms.sendMessage("Waila", "register", "com.dipo33.bewitched.integration.waila.HUDHandlerBewitched.register");
    }

    /**
     * Handle the mod's post-initialization lifecycle event.
     * <p>
     * Delegates post-initialization work (interaction with other mods) to the sided proxy.
     *
     * @param event
     *     the Forge post-initialization event containing mod configuration and environment data
     */
    public void postInit(FMLPostInitializationEvent event) {
    }

    /**
     * Handle the mod's server-starting lifecycle event.
     * <p>
     * Delegates server-side setup (such as command registration) to the sided proxy.
     *
     * @param event
     *     the Forge server starting event providing access to command registration and server context
     */
    public void serverStarting(FMLServerStartingEvent event) {
    }
}

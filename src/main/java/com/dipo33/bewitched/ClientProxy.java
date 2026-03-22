package com.dipo33.bewitched;

import com.dipo33.bewitched.client.effect.EffectRegistry;
import com.dipo33.bewitched.network.BwNetwork;
import com.dipo33.bewitched.network.handler.EffectPlayMsgHandler;
import com.dipo33.bewitched.network.handler.UpdateFlowerPotMsgHandler;
import com.dipo33.bewitched.network.message.EffectPlayMsg;
import com.dipo33.bewitched.network.message.UpdateFlowerPotMsg;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    /**
     * Perform client-side pre-initialization tasks.
     *
     * Registers client-only visual effects and performs the shared pre-initialization steps required
     * before the mod fully initializes.
     *
     * @param event the Forge pre-initialization event provided during mod loading
     */
    @Override
    public void preInit(final FMLPreInitializationEvent event) {
        super.preInit(event);
        EffectRegistry.registerEffects();
    }

    /**
     * Register client-side network message handlers used by the mod.
     *
     * Maps EffectPlayMsg to EffectPlayMsgHandler and UpdateFlowerPotMsg to UpdateFlowerPotMsgHandler via BwNetwork.
     */
    @Override
    protected void registerClientMessages() {
        BwNetwork.registerClientMessage(EffectPlayMsgHandler.class, EffectPlayMsg.class);
        BwNetwork.registerClientMessage(UpdateFlowerPotMsgHandler.class, UpdateFlowerPotMsg.class);
    }
}

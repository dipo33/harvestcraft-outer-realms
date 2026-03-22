package com.dipo33.bewitched;

import com.dipo33.bewitched.client.effect.EffectRegistry;
import com.dipo33.bewitched.network.BewitchedNetwork;
import com.dipo33.bewitched.network.handler.EffectPlayMsgHandler;
import com.dipo33.bewitched.network.handler.UpdateFlowerPotMsgHandler;
import com.dipo33.bewitched.network.message.EffectPlayMsg;
import com.dipo33.bewitched.network.message.UpdateFlowerPotMsg;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(final FMLPreInitializationEvent event) {
        super.preInit(event);
        EffectRegistry.registerEffects();
    }

    @Override
    protected void registerClientMessages() {
        BewitchedNetwork.registerClientMessage(EffectPlayMsgHandler.class, EffectPlayMsg.class);
        BewitchedNetwork.registerClientMessage(UpdateFlowerPotMsgHandler.class, UpdateFlowerPotMsg.class);
    }
}

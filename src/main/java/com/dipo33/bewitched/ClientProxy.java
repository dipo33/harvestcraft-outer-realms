package com.dipo33.bewitched;

import com.dipo33.bewitched.client.effect.EffectRegistry;
import com.dipo33.bewitched.network.BwNetwork;
import com.dipo33.bewitched.network.handler.UpdateFlowerPotMsgHandler;
import com.dipo33.bewitched.network.message.EffectPlayMsg;
import com.dipo33.bewitched.network.message.UpdateFlowerPotMsg;

import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.Minecraft;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(final FMLPreInitializationEvent event) {
        super.preInit(event);
        EffectRegistry.registerEffects();
    }

    @Override
    protected void registerClientMessages() {
        BwNetwork.registerClientMessage(new UpdateFlowerPotMsgHandler(), UpdateFlowerPotMsg.class);
    }

    @Override
    public void playFX(final EffectPlayMsg message) {
        Minecraft.getMinecraft().func_152344_a(() -> {
            EffectRegistry.play(message);
        });
    }
}

package com.dipo33.bewitched.network.handler;

import com.dipo33.bewitched.client.effect.EffectRegistry;
import com.dipo33.bewitched.network.message.EffectPlayMsg;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;

public class EffectPlayMsgHandler implements IMessageHandler<EffectPlayMsg, IMessage> {
    @Override
    public IMessage onMessage(final EffectPlayMsg message, final MessageContext ctx) {
        Minecraft.getMinecraft().func_152344_a(() -> {
            EffectRegistry.play(message);
        });
        return null;
    }
}

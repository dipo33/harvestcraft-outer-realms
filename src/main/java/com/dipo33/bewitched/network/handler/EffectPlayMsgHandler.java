package com.dipo33.bewitched.network.handler;

import com.dipo33.bewitched.client.effect.EffectRegistry;
import com.dipo33.bewitched.network.message.EffectPlayMsg;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;

public class EffectPlayMsgHandler implements IMessageHandler<EffectPlayMsg, IMessage> {
    /**
     * Schedules playback of the received effect on the Minecraft client thread.
     *
     * The effect described by {@code message} is executed asynchronously on the client; this handler does not send a reply.
     *
     * @param message the effect message describing what to play
     * @param ctx the network message context (unused)
     * @return {@code null} indicating no response message is sent
     */
    @Override
    public IMessage onMessage(final EffectPlayMsg message, final MessageContext ctx) {
        Minecraft.getMinecraft().func_152344_a(() -> {
            EffectRegistry.play(message);
        });
        return null;
    }
}

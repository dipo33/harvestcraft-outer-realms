package com.dipo33.bewitched.network.handler;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PlaceholderHandler<T extends IMessage> implements IMessageHandler<T, IMessage> {

    /**
     * Handle an incoming network message without producing a response.
     *
     * @param message the received message (ignored)
     * @param ctx the message context (ignored)
     * @return null indicating no response message is sent
     */
    @Override
    public IMessage onMessage(final T message, final MessageContext ctx) {
        return null;
    }
}

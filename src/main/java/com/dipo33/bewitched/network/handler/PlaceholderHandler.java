package com.dipo33.bewitched.network.handler;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PlaceholderHandler<T extends IMessage> implements IMessageHandler<T, IMessage> {

    @Override
    public IMessage onMessage(final T message, final MessageContext ctx) {
        return null;
    }
}

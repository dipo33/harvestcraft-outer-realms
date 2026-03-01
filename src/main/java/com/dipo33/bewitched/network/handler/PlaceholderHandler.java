package com.dipo33.bewitched.network.handler;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public class PlaceholderHandler implements IMessageHandler<IMessage, IMessage> {

    @Override
    public IMessage onMessage(final IMessage message, final MessageContext ctx) {
        return null;
    }
}

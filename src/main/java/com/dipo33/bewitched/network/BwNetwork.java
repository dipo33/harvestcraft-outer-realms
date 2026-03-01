package com.dipo33.bewitched.network;

import com.dipo33.bewitched.Bewitched;
import com.dipo33.bewitched.network.message.EffectPlayMsg;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class BwNetwork {
    public static SimpleNetworkWrapper NET;

    private static int MESSAGE_ID = 0;

    public static void register() {
        NET = NetworkRegistry.INSTANCE.newSimpleChannel(Bewitched.MODID);

        registerCommonMessages();
    }

    public static <REQ extends IMessage, REPLY extends IMessage> void registerClientMessage(
        IMessageHandler<? super REQ, ? extends REPLY> messageHandler, Class<REQ> message
    ) {
        NET.registerMessage(messageHandler, message, MESSAGE_ID++, Side.CLIENT);
    }

    private static void registerCommonMessages() {
        NET.registerMessage(EffectPlayMsg.Handler.class, EffectPlayMsg.class, MESSAGE_ID++, Side.CLIENT);
    }
}

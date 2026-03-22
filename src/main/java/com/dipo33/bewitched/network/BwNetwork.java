package com.dipo33.bewitched.network;

import com.dipo33.bewitched.Bewitched;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class BwNetwork {
    public static SimpleNetworkWrapper NET;

    private static int MESSAGE_ID = 0;

    /**
     * Initializes the mod network channel and registers common network messages.
     *
     * This sets BwNetwork.NET to a new SimpleNetworkWrapper for the mod's channel and
     * performs registration of messages that are common across sides.
     */
    public static void register() {
        NET = NetworkRegistry.INSTANCE.newSimpleChannel(Bewitched.MODID);

        registerCommonMessages();
    }

    /**
     * Registers a network message and its handler for processing on the client side.
     *
     * @param messageHandler the handler class that processes incoming `REQ` messages and optionally returns `REPLY` responses
     * @param message the message class to register for client-side handling
     */
    public static <REQ extends IMessage, REPLY extends IMessage> void registerClientMessage(
        Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> message
    ) {
        NET.registerMessage(messageHandler, message, MESSAGE_ID++, Side.CLIENT);
    }

    /**
     * Registers network messages that are common to both client and server.
     *
     * <p>Currently no common messages are registered; this method exists as a placeholder
     * for shared message registrations.</p>
     */
    private static void registerCommonMessages() {
    }
}

package com.dipo33.bewitched.network;

import com.dipo33.bewitched.Bewitched;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/**
 * Network setup and message registration utility.
 */
public class BewitchedNetwork {
    public static SimpleNetworkWrapper NET;

    private static int MESSAGE_ID = 0;

    /**
     * Initializes the network channel and registers messages.
     */
    public static void register() {
        NET = NetworkRegistry.INSTANCE.newSimpleChannel(Bewitched.MODID);

        registerCommonMessages();
    }

    /**
     * Registers a message whose real handler is client-only.
     *
     * <p>This exists for messages whose actual handler touches client-only code and therefore
     * cannot be registered normally on the server, since doing so would load the handler class
     * and crash dedicated servers.</p>
     *
     * <p>On the client, this is called with the real handler. On the server, it may be called
     * with a placeholder handler solely to satisfy message registration; that handler is never
     * expected to run there.</p>
     *
     * @param messageHandler
     *     the handler class to register for this side
     * @param message
     *     the message class
     */
    public static <REQ extends IMessage, REPLY extends IMessage> void registerClientMessage(
        Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, Class<REQ> message
    ) {
        NET.registerMessage(messageHandler, message, MESSAGE_ID++, Side.CLIENT);
    }

    /**
     * Registers messages whose handlers are safe to load on both sides.
     *
     * <p>Only messages with side-safe handler classes should be registered here.</p>
     */
    private static void registerCommonMessages() {
    }
}

package com.dipo33.bewitched.client.effect;

import com.dipo33.bewitched.network.BwNetwork;
import com.dipo33.bewitched.network.message.EffectPlayMsg;

import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.world.World;

public class EffectPlayer {

    /**
     * Broadcasts an effect playback message to all players near the given position in the specified world.
     *
     * @param effectId identifier of the effect to play
     * @param world target world where the effect occurs
     * @param x x-coordinate of the effect origin
     * @param y y-coordinate of the effect origin
     * @param z z-coordinate of the effect origin
     * @param range radius around (x, y, z) within which players will receive the message
     */
    public static void playFX(String effectId, World world, double x, double y, double z, double range) {
        NetworkRegistry.TargetPoint tp = new NetworkRegistry.TargetPoint(
            world.provider.dimensionId, x, y, z, range
        );
        BwNetwork.NET.sendToAllAround(new EffectPlayMsg(world, effectId, x, y, z), tp);
    }
}

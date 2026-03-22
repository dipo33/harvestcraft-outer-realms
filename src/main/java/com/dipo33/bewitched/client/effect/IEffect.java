package com.dipo33.bewitched.client.effect;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.World;

/**
 * Effect that can be played at a world position.
 */
@SideOnly(Side.CLIENT)
public interface IEffect {

    /**
     * Plays the effect at given coordinates.
     */
    void play(World world, double x, double y, double z);
}

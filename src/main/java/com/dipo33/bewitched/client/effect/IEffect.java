package com.dipo33.bewitched.client.effect;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public interface IEffect {
    /**
 * Play the effect at the specified location in the given world.
 *
 * @param world the world where the effect should be played
 * @param x     x-coordinate of the effect's origin
 * @param y     y-coordinate of the effect's origin
 * @param z     z-coordinate of the effect's origin
 */
void play(World world, double x, double y, double z);
}

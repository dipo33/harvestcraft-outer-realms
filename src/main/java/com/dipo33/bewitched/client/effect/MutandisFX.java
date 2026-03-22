package com.dipo33.bewitched.client.effect;

import com.dipo33.bewitched.client.effect.entity.EntityMutandisFX;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class MutandisFX implements IEffect {
    private static final double SPAWN_RADIUS = 0.8D;
    private static final int PARTICLE_COUNT = 32;
    private static final double HORIZONTAL_MOTION_SCALE = 0.05D;
    private static final double VERTICAL_MOTION_SCALE = 0.03D;

    private final EntityMutandisFX.Variant variant;

    /**
     * Create a MutandisFX effect configured to spawn particles of the given variant.
     *
     * @param variant the particle variant to use for all spawned EntityMutandisFX instances
     */
    public MutandisFX(EntityMutandisFX.Variant variant) {
        this.variant = variant;
    }

    /**
     * Spawns a burst of Mutandis particles around the specified position.
     *
     * Creates PARTICLE_COUNT EntityMutandisFX instances with randomized offsets and motion, and registers them with the client effect renderer.
     *
     * @param world the world in which to spawn the particles
     * @param x the base x coordinate to spawn around
     * @param y the base y coordinate to spawn around
     * @param z the base z coordinate to spawn around
     */
    @Override
    public void play(final World world, final double x, final double y, final double z) {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double offsetX = (world.rand.nextDouble() - 0.5) * 2 * SPAWN_RADIUS;
            double offsetY = (world.rand.nextDouble() - 0.5) * SPAWN_RADIUS;
            double offsetZ = (world.rand.nextDouble() - 0.5) * 2 * SPAWN_RADIUS;

            double motionX = (world.rand.nextDouble() - 0.5) * HORIZONTAL_MOTION_SCALE;
            double motionY = world.rand.nextDouble() * VERTICAL_MOTION_SCALE;
            double motionZ = (world.rand.nextDouble() - 0.5) * HORIZONTAL_MOTION_SCALE;

            EntityFX fx = new EntityMutandisFX(
                world, x + 0.5 + offsetX, y + 0.5 + offsetY, z + 0.5 + offsetZ, motionX, motionY, motionZ, this.variant
            );
            Minecraft.getMinecraft().effectRenderer.addEffect(fx);
        }
    }
}

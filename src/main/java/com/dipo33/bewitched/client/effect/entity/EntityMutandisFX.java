package com.dipo33.bewitched.client.effect.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityMutandisFX extends EntityFX {

    public enum Variant {
        MUTANDIS,
        MUTANDIS_EXTREMIS
    }

    private static final int PINK_RGB = 0xd74fc9;

    /**
     * Creates a Mutandis particle at the given world position with initial motion and the specified color variant.
     *
     * @param world   the world the particle is created in
     * @param x       the particle's X coordinate
     * @param y       the particle's Y coordinate
     * @param z       the particle's Z coordinate
     * @param mx      the particle's initial motion along X
     * @param my      the particle's initial motion along Y
     * @param mz      the particle's initial motion along Z
     * @param variant the color variant to apply to the particle
     */
    public EntityMutandisFX(World world, double x, double y, double z,
                            double mx, double my, double mz, Variant variant) {
        super(world, x, y, z, mx, my, mz);

        this.particleGravity = 0.0F;
        this.particleScale = 1f;
        this.particleMaxAge = 20 + this.rand.nextInt(10);

        applyColor(variant, world.rand);
    }

    /**
     * Set the particle's color based on the provided variant.
     *
     * For MUTANDIS the color is set to white; for MUTANDIS_EXTREMIS the color is interpolated
     * from white toward the predefined pink color using the provided random value.
     *
     * @param variant the color variant to apply
     * @param rng     source of randomness used when a variant requires randomized interpolation
     */
    private void applyColor(Variant variant, Random rng) {
        switch (variant) {
            case MUTANDIS:
                setRBGColorF(1F, 1F, 1F);
                break;
            case MUTANDIS_EXTREMIS:
                setLerpedColorFromWhite(PINK_RGB, rng.nextFloat());
                break;
        }
    }

    /**
     * Sets the particle color to a linear interpolation between white and the provided RGB color.
     *
     * @param rgb packed RGB color in 0xRRGGBB format
     * @param t   interpolation factor in the range [0, 1]; 0 yields white, 1 yields the provided color
     */
    private void setLerpedColorFromWhite(int rgb, float t) {
        float tr = ((rgb >> 16) & 0xFF) / 255F;
        float tg = ((rgb >> 8) & 0xFF) / 255F;
        float tb = (rgb & 0xFF) / 255F;

        // lerp(white, target, t)
        setRBGColorF(
            1F + (tr - 1F) * t,
            1F + (tg - 1F) * t,
            1F + (tb - 1F) * t
        );
    }

    /**
     * Updates the particle's state each tick.
     *
     * Increases the particle's upward motion slightly and adjusts its alpha to fade
     * linearly from 1.0 to 0.0 based on the particle's age relative to its maximum age.
     */
    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionY += 0.002;
        this.particleAlpha = 1.0F - (float) this.particleAge / (float) this.particleMaxAge;
    }
}

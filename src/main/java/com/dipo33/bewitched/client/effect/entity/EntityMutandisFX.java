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

    public EntityMutandisFX(World world, double x, double y, double z,
                            double mx, double my, double mz, Variant variant) {
        super(world, x, y, z, mx, my, mz);

        this.particleGravity = 0.0F;
        this.particleScale = 1f;
        this.particleMaxAge = 20 + this.rand.nextInt(10);

        applyColor(variant, world.rand);
    }

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

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionY += 0.002;
        this.particleAlpha = 1.0F - (float) this.particleAge / (float) this.particleMaxAge;
    }
}

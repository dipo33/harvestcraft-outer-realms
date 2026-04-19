package com.dipo33.bewitched.block;

import com.dipo33.bewitched.entity.EntityMandrake;
import com.dipo33.bewitched.init.BewitchedItems;

import java.util.ArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Fully grown mandrake harvest may spawn a {@link EntityMandrake}
 */
public class BlockMandrakeCrop extends BlockBewitchedCrops {

    /** Higher chance for a mandrake to spawn when the crop is harvested in daylight. */
    private static final double SPAWN_CHANCE_DAY = 1.0D;
    /** Lower chance at night than daytime. */
    private static final double SPAWN_CHANCE_NIGHT = 0.15D;

    public BlockMandrakeCrop() {
        super(BewitchedItems.MANDRAKE_SEEDS, BewitchedItems.MANDRAKE_ROOT);
        this.setStages(5);
    }

    @Override
    public ArrayList<ItemStack> getDrops(final World world, final int x, final int y, final int z, final int metadata, final int fortune) {
        ArrayList<ItemStack> drops = super.getDrops(world, x, y, z, metadata, fortune);
        if (metadata < 7 || world.isRemote) {
            return drops;
        }

        double chance = isDaytime(world) ? SPAWN_CHANCE_DAY : SPAWN_CHANCE_NIGHT;
        if (world.rand.nextDouble() >= chance) {
            return drops;
        }

        EntityMandrake mandrake = new EntityMandrake(world);
        mandrake.setPosition(x + 0.5D, y, z + 0.5D);
        if (!world.spawnEntityInWorld(mandrake)) {
            return drops;
        }

        drops.clear();
        return drops;
    }

    /**
     * @return {@code true} when world time is in the sky-bright half of the overworld day cycle
     */
    private static boolean isDaytime(World world) {
        return world.getWorldTime() % 24000L < 12000L;
    }
}

package com.dipo33.bewitched.block;

import net.minecraft.block.BlockVine;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IShearable;

public class BlockSpanishMoss extends BlockVine implements IShearable {

    /**
     * Constructs a new BlockSpanishMoss.
     */
    public BlockSpanishMoss() {
    }

    /**
     * Determine whether this block should act as a ladder for the specified entity at the given position.
     *
     * @param world  the world access used to query block state
     * @param x      block X coordinate
     * @param y      block Y coordinate
     * @param z      block Z coordinate
     * @param entity the entity attempting to use the block as a ladder
     * @return       `true` if the block acts as a ladder for the entity at the position, `false` otherwise
     */
    @Override
    public boolean isLadder(final IBlockAccess world, final int x, final int y, final int z, final EntityLivingBase entity) {
        return false;
    }
}

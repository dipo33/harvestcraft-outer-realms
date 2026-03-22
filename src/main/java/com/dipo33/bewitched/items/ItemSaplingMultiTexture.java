package com.dipo33.bewitched.items;

import com.dipo33.bewitched.block.BlockBwSapling;

import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

public class ItemSaplingMultiTexture extends ItemMultiTexture {
    /**
     * Creates an item that uses the sapling variant textures of the provided block.
     *
     * @param block the block whose sapling variant textures will determine this item's multiple textures
     */
    public ItemSaplingMultiTexture(final Block block) {
        super(block, block, BlockBwSapling.VARIANTS);
    }
}

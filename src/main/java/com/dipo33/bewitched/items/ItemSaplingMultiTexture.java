package com.dipo33.bewitched.items;

import com.dipo33.bewitched.block.BlockBewitchedSapling;

import net.minecraft.block.Block;
import net.minecraft.item.ItemMultiTexture;

public class ItemSaplingMultiTexture extends ItemMultiTexture {
    public ItemSaplingMultiTexture(final Block block) {
        super(block, block, BlockBewitchedSapling.VARIANTS);
    }
}

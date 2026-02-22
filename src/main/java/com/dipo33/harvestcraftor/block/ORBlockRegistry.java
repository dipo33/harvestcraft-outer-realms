package com.dipo33.harvestcraftor.block;

import com.pam.harvestcraft.BlockPamCrop;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;

public class ORBlockRegistry {
    public static Block hemleafCrop = new BlockPamCrop();

    public static void initBlocks() {
    }

    public static void registerBlocks() {
        registerBlock(hemleafCrop, "hemleaf");
    }

    public static void registerBlock(Block block, String name) {
        block.setBlockTextureName(name);
        block.setBlockName(name);
        GameRegistry.registerBlock(block, name);
    }
}

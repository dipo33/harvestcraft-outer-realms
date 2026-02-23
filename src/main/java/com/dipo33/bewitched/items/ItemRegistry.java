package com.dipo33.bewitched.items;

import com.dipo33.bewitched.Bewitched;
import com.dipo33.bewitched.block.BlockRegistry;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;

public class ItemRegistry {

    public static Item belladonnaSeed;

    public static void initItems() {
        belladonnaSeed = new ItemSeeds(BlockRegistry.belladonnaCrop, Blocks.farmland).setCreativeTab(CreativeTabs.tabMaterials);
    }

    public static void registerItems() {
        registerItem(belladonnaSeed, "belladonna_seed");
    }

    private static void registerItem(Item item, String name) {
        item.setUnlocalizedName(name);
        item.setTextureName(Bewitched.MODID + ":" + name);
        GameRegistry.registerItem(item, name);
    }
}

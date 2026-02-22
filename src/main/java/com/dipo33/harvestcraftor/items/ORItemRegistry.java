package com.dipo33.harvestcraftor.items;

import com.dipo33.harvestcraftor.HCOuterRealms;
import com.pam.harvestcraft.BlockRegistry;
import com.pam.harvestcraft.HarvestCraft;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSeeds;

public class ORItemRegistry {
    public static CreativeTabs tabCrops = HarvestCraft.tabHarvestCraft2;
    public static CreativeTabs tabFood = HarvestCraft.tabHarvestCraft3;

    public static Item hemleafSeed;

    public static void initItems() {
        // TODO: use hemleafCropBlock
        hemleafSeed = new ItemSeeds(BlockRegistry.pamstrawberryCrop, Blocks.farmland).setCreativeTab(tabCrops);
    }

    public static void registerItems() {
        registerItem(hemleafSeed, "hemleaf_seed");
    }

    private static void registerItem(Item item, String name) {
        item.setUnlocalizedName(name);
        item.setTextureName(HCOuterRealms.MODID + ":" + name);
        GameRegistry.registerItem(item, name);
    }
}

package com.dipo33.bewitched.items;

import com.dipo33.bewitched.init.BewitchedItems;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class SeedDrops {
    /**
     * Register mod seed items to be obtainable as grass drops.
     */
    public static void dropSeedsFromGrass() {
        MinecraftForge.addGrassSeed(new ItemStack(BewitchedItems.BELLADONNA_SEEDS.get()), 3);
        MinecraftForge.addGrassSeed(new ItemStack(BewitchedItems.WOLFSBANE_SEEDS.get()), 1);
        MinecraftForge.addGrassSeed(new ItemStack(BewitchedItems.WATER_ARTICHOKE_SEEDS.get()), 3);
        MinecraftForge.addGrassSeed(new ItemStack(BewitchedItems.MANDRAKE_SEEDS.get()), 5);
        MinecraftForge.addGrassSeed(new ItemStack(BewitchedItems.SNOW_WISP_SEEDS.get()), 3);
        MinecraftForge.addGrassSeed(new ItemStack(BewitchedItems.GARLIC.get()), 1);
    }
}

package com.dipo33.bewitched.items;

import com.dipo33.bewitched.init.BewitchedItems;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class SeedDrops {
    /**
     * Register mod seed items to be obtainable as grass drops.
     */
    public static void dropSeedsFromGrass() {
        MinecraftForge.addGrassSeed(new ItemStack(BewitchedItems.BELLADONNA_SEED.get()), 3);
        MinecraftForge.addGrassSeed(new ItemStack(BewitchedItems.WOLFSBANE_SEED.get()), 1);
        MinecraftForge.addGrassSeed(new ItemStack(BewitchedItems.WATER_ARTICHOKE_SEED.get()), 3);
        MinecraftForge.addGrassSeed(new ItemStack(BewitchedItems.MANDRAKE_SEED.get()), 5);
        MinecraftForge.addGrassSeed(new ItemStack(BewitchedItems.SNOW_WISP_SEED.get()), 3);
        MinecraftForge.addGrassSeed(new ItemStack(BewitchedItems.GARLIC.get()), 1);
    }
}

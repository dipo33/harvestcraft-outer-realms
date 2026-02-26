package com.dipo33.bewitched.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

public class SeedDrops {
    /**
     * Register mod seed items to be obtainable as grass drops.
     *
     * Registers the following seeds with their grass drop weights: Belladonna (3), Wolfsbane (1),
     * Water Artichoke (3), Mandrake (5), Snow Wisp (3), and Garlic (1).
     */
    public static void dropSeedsFromGrass() {
        MinecraftForge.addGrassSeed(new ItemStack(ItemRegistry.BELLADONNA_SEED.get()), 3);
        MinecraftForge.addGrassSeed(new ItemStack(ItemRegistry.WOLFSBANE_SEED.get()), 1);
        MinecraftForge.addGrassSeed(new ItemStack(ItemRegistry.WATER_ARTICHOKE_SEED.get()), 3);
        MinecraftForge.addGrassSeed(new ItemStack(ItemRegistry.MANDRAKE_SEED.get()), 5);
        MinecraftForge.addGrassSeed(new ItemStack(ItemRegistry.SNOW_WISP_SEED.get()), 3);
        MinecraftForge.addGrassSeed(new ItemStack(ItemRegistry.GARLIC.get()), 1);
    }
}

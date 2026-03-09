package com.dipo33.bewitched.init;

import com.dipo33.bewitched.items.ItemRegistry;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class BewitchedRecipes {

    public static void init() {
        registerCraftingRecipes();
    }

    private static void registerCraftingRecipes() {
        // TODO: Temporary recipe until cauldron recipes are implemented
        GameRegistry.addShapelessRecipe(
            new ItemStack(ItemRegistry.MUTANDIS.get(), 6),
            Items.egg,
            ItemRegistry.MANDRAKE_ROOT.get(),
            Items.potionitem
        );

        // TODO: Temporary recipe until cauldron recipes are implemented
        GameRegistry.addShapelessRecipe(
            new ItemStack(ItemRegistry.MUTANDIS_EXTREMIS.get(), 1),
            ItemRegistry.MUTANDIS.get(),
            Items.nether_wart
        );
    }
}

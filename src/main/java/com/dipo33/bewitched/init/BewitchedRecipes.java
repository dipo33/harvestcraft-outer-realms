package com.dipo33.bewitched.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class BewitchedRecipes {

    /**
     * Initializes the mod's crafting recipe registrations.
     *
     * This should be invoked during mod initialization to register all crafting recipes.
     */
    public static void init() {
        registerCraftingRecipes();
    }

    /**
     * Registers two temporary shapeless crafting recipes used until cauldron recipes are implemented.
     *
     * Adds a recipe that produces 6 MUTANDIS from an egg, a mandrake root, and a potion,
     * and a recipe that produces 1 MUTANDIS_EXTREMIS from MUTANDIS and nether wart.
     */
    private static void registerCraftingRecipes() {
        // TODO: Temporary recipe until cauldron recipes are implemented
        GameRegistry.addShapelessRecipe(
            new ItemStack(BewitchedItems.MUTANDIS.get(), 6),
            Items.egg,
            BewitchedItems.MANDRAKE_ROOT.get(),
            Items.potionitem
        );

        // TODO: Temporary recipe until cauldron recipes are implemented
        GameRegistry.addShapelessRecipe(
            new ItemStack(BewitchedItems.MUTANDIS_EXTREMIS.get(), 1),
            BewitchedItems.MUTANDIS.get(),
            Items.nether_wart
        );
    }
}

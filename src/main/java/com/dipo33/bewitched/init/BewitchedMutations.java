package com.dipo33.bewitched.init;

import com.dipo33.bewitched.api.mutation.Mutation;
import com.dipo33.bewitched.api.mutation.MutationPool;
import com.dipo33.bewitched.api.mutation.MutationPoolType;
import com.dipo33.bewitched.api.mutation.MutationRegistry;
import com.dipo33.bewitched.config.Config;

import java.util.Collections;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class BewitchedMutations {

    /**
     * Registers all Bewitched mutation pools and their members with the MutationRegistry.
     *
     * Initializes Mutandis, Mutandis Extremis, Mutandis Extremis Grass, and Flower Pot pools,
     * including their catalysts and configured or hard-coded mutation members.
     */
    public static void init() {
        registerMutandisMutations();
        registerMutandisExtremisMutations();
        registerMutandisExtremisGrassMutations();
        registerFlowerPotMutations();
    }

    /**
     * Registers the MUTANDIS mutation pool and populates it with catalysts and members.
     *
     * Creates a MutationPool for MutationPoolType.MUTANDIS, adds the MUTANDIS catalyst,
     * merges any additional mutations from configuration, and registers a set of fixed
     * mutations: primitive mutations for various plants (including tallgrass, mushrooms,
     * flowers, waterlily, and Ember Moss), a Spanish Moss mutation using the wall strategy,
     * plus sapling mutations for vanilla saplings (meta 0–5) and bewitched saplings (meta 0–2).
     */
    private static void registerMutandisMutations() {
        MutationPool pool = MutationRegistry.createPool(
            MutationPoolType.MUTANDIS,
            "nei.bewitched.mutation.mutandis"
        );
        pool.addCatalyst(new ItemStack(BewitchedItems.MUTANDIS.get()));

        for (Mutation mutation : Config.mutandisAdditionalMutations) {
            pool.addMember(mutation);
        }

        pool.addMember(Mutation.primitiveMutation(Blocks.tallgrass, 1));
        pool.addMember(Mutation.primitiveMutation(Blocks.brown_mushroom, 0));
        pool.addMember(Mutation.primitiveMutation(Blocks.red_mushroom, 0));
        pool.addMember(Mutation.primitiveMutation(Blocks.yellow_flower, 0));
        pool.addMember(Mutation.primitiveMutation(Blocks.red_flower, 0));
        pool.addMember(Mutation.primitiveMutation(Blocks.waterlily, 0));
        pool.addMember(Mutation.primitiveMutation(BewitchedBlocks.EMBER_MOSS.get(), 0));

        pool.addMember(new Mutation(
            new Mutation.Output(BewitchedBlocks.SPANISH_MOSS.get(), Mutation.wallStrategy()),
            Collections.singletonList(
                Mutation.Source.anyMeta(BewitchedBlocks.SPANISH_MOSS.get())
            )
        ));

        // Vanilla Saplings
        pool.addMember(Mutation.saplingMutation(Blocks.sapling, 0));
        pool.addMember(Mutation.saplingMutation(Blocks.sapling, 1));
        pool.addMember(Mutation.saplingMutation(Blocks.sapling, 2));
        pool.addMember(Mutation.saplingMutation(Blocks.sapling, 3));
        pool.addMember(Mutation.saplingMutation(Blocks.sapling, 4));
        pool.addMember(Mutation.saplingMutation(Blocks.sapling, 5));

        // Bewitched Saplings
        pool.addMember(Mutation.saplingMutation(BewitchedBlocks.SAPLING.get(), 0));
        pool.addMember(Mutation.saplingMutation(BewitchedBlocks.SAPLING.get(), 1));
        pool.addMember(Mutation.saplingMutation(BewitchedBlocks.SAPLING.get(), 2));
    }

    /**
     * Register and populate the MUTANDIS_EXTREMIS mutation pool.
     *
     * Creates the mutation pool keyed as "nei.bewitched.mutation.mutandis_extremis", adds
     * the MUTANDIS_EXTREMIS catalyst, copies all members from the MUTANDIS pool, appends
     * any configured additional mutations, and registers primitive (meta-agnostic) crop/plant
     * mutations for potatoes, carrots, nether wart, wheat, reeds, cactus, pumpkin stem,
     * melon stem, mandrake, belladonna, and water artichoke.
     */
    private static void registerMutandisExtremisMutations() {
        MutationPool pool = MutationRegistry.createPool(
            MutationPoolType.MUTANDIS_EXTREMIS,
            "nei.bewitched.mutation.mutandis_extremis"
        );
        pool.addCatalyst(new ItemStack(BewitchedItems.MUTANDIS_EXTREMIS.get()));

        for (Mutation mutation : MutationRegistry.getMembers(MutationPoolType.MUTANDIS)) {
            pool.addMember(mutation);
        }
        for (Mutation mutation : Config.mutandisExtremisAdditionalMutations) {
            pool.addMember(mutation);
        }

        pool.addMember(Mutation.primitiveAnyMetaMutation(Blocks.potatoes, 0));
        pool.addMember(Mutation.primitiveAnyMetaMutation(Blocks.carrots, 0));
        pool.addMember(Mutation.primitiveAnyMetaMutation(Blocks.nether_wart, 0));
        pool.addMember(Mutation.primitiveAnyMetaMutation(Blocks.wheat, 0));
        pool.addMember(Mutation.primitiveAnyMetaMutation(Blocks.reeds, 0));
        pool.addMember(Mutation.primitiveAnyMetaMutation(Blocks.cactus, 0));
        pool.addMember(Mutation.primitiveAnyMetaMutation(Blocks.pumpkin_stem, 0));
        pool.addMember(Mutation.primitiveAnyMetaMutation(Blocks.melon_stem, 0));
        pool.addMember(Mutation.primitiveAnyMetaMutation(BewitchedBlocks.MANDRAKE_CROP.get(), 0));
        pool.addMember(Mutation.primitiveAnyMetaMutation(BewitchedBlocks.BELLADONNA_CROP.get(), 0));
        pool.addMember(Mutation.primitiveAnyMetaMutation(BewitchedBlocks.WATER_ARTICHOKE_CROP.get(), 0));
    }

    /**
     * Registers and populates the MUTANDIS_EXTREMIS_GRASS mutation pool.
     *
     * Creates the mutation pool, adds the Mutandis Extremis catalyst, includes any
     * additional grass mutations from configuration, and adds primitive mutations
     * for grass and mycelium (meta 0).
     */
    private static void registerMutandisExtremisGrassMutations() {
        MutationPool pool = MutationRegistry.createPool(
            MutationPoolType.MUTANDIS_EXTREMIS_GRASS,
            "nei.bewitched.mutation.mutandis_extremis_grass"
        );
        pool.addCatalyst(new ItemStack(BewitchedItems.MUTANDIS_EXTREMIS.get()));

        for (Mutation mutation : Config.mutandisExtremisAdditionalGrassMutations) {
            pool.addMember(mutation);
        }

        pool.addMember(Mutation.primitiveMutation(Blocks.grass, 0));
        pool.addMember(Mutation.primitiveMutation(Blocks.mycelium, 0));
    }

    /**
     * Creates and populates the FLOWER_POT mutation pool used by the mod.
     *
     * Creates a MutationPool with the registry key "nei.bewitched.mutation.flower_pot",
     * registers catalysts for Mutandis and Mutandis Extremis, adds any configured
     * additional flower-pot mutations, and registers fixed primitive members for
     * common potted plants (flowers, mushrooms, cactus, deadbush, tallgrass) and
     * all vanilla sapling metas 0–5.
     */
    private static void registerFlowerPotMutations() {
        MutationPool pool = MutationRegistry.createPool(
            MutationPoolType.FLOWER_POT,
            "nei.bewitched.mutation.flower_pot"
        );
        pool.addCatalyst(new ItemStack(BewitchedItems.MUTANDIS.get()));
        pool.addCatalyst(new ItemStack(BewitchedItems.MUTANDIS_EXTREMIS.get()));

        for (Mutation mutation : Config.mutandisAdditionalFlowerPotMutations) {
            pool.addMember(mutation);
        }

        pool.addMember(Mutation.primitiveMutation(Blocks.yellow_flower, 0));
        pool.addMember(Mutation.primitiveMutation(Blocks.red_flower, 0));
        pool.addMember(Mutation.primitiveMutation(Blocks.cactus, 0));
        pool.addMember(Mutation.primitiveMutation(Blocks.brown_mushroom, 0));
        pool.addMember(Mutation.primitiveMutation(Blocks.red_mushroom, 0));
        pool.addMember(Mutation.primitiveMutation(Blocks.deadbush, 0));
        pool.addMember(Mutation.primitiveMutation(Blocks.tallgrass, 2));

        pool.addMember(Mutation.primitiveMutation(Blocks.sapling, 0));
        pool.addMember(Mutation.primitiveMutation(Blocks.sapling, 1));
        pool.addMember(Mutation.primitiveMutation(Blocks.sapling, 2));
        pool.addMember(Mutation.primitiveMutation(Blocks.sapling, 3));
        pool.addMember(Mutation.primitiveMutation(Blocks.sapling, 4));
        pool.addMember(Mutation.primitiveMutation(Blocks.sapling, 5));
    }
}

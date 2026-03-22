package com.dipo33.bewitched.api.mutation;

import com.github.bsideup.jabel.Desugar;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

@Desugar
public record Mutation(Output output, List<Source> sources) {
    /**
     * Checks whether any configured source matches the given block and metadata.
     *
     * @param block
     *     the block to test against the mutation's sources
     * @param meta
     *     the metadata value to test
     * @return {@code true} if any source matches the provided block and metadata, {@code false} otherwise
     */
    public boolean matchesSource(Block block, int meta) {
        for (Source source : sources) {
            if (source.matches(block, meta)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns a random mutation from the pool for {@code type}, excluding the one matching
     * {@code block} and {@code meta}.
     *
     * @param type
     *     the mutation pool type
     * @param block
     *     the block to mutate
     * @param meta
     *     the block metadata to mutate
     * @param rng
     *     the randomness source
     * @return a different mutation from the same pool, or {@code null} if the pool is missing,
     * has fewer than two entries, or no matching mutation exists
     */
    public static Mutation mutate(MutationPoolType type, Block block, int meta, Random rng) {
        var pool = MutationRegistry.getPool(type);
        if (pool == null) {
            return null;
        }

        var mutations = pool.getMembers();
        if (mutations.size() < 2) {
            return null;
        }

        var match = pool.getMutationFor(block, meta);
        if (match == null) {
            return null;
        }

        int resultMutationIdx = rng.nextInt(mutations.size() - 1);
        if (resultMutationIdx >= match.index()) {
            resultMutationIdx++;
        }

        return mutations.get(resultMutationIdx);
    }

    /**
     * Matches a block with either exact metadata or any metadata (wildcard).
     *
     * @param block
     *     the block to match
     * @param meta
     *     the metadata to match, or {@code null} to match any value
     */
    @Desugar
    public record Source(Block block, Integer meta) {
        /**
         * Returns whether the given block and metadata match this source.
         */
        public boolean matches(Block b, int m) {
            return b == block && (meta == null || meta == m);
        }

        /**
         * Creates a source matching any metadata for the given block.
         */
        public static Source anyMeta(Block b) {
            return new Source(b, null);
        }

        /**
         * Creates a source matching the exact metadata for the given block.
         */
        public static Source exact(Block b, int meta) {
            return new Source(b, meta);
        }
    }

    public interface PlacementStrategy {
        /**
         * Computes metadata for placement at the given position.
         *
         * @return the metadata value to use
         */
        int placementMeta(World world, int x, int y, int z);

        /**
         * Returns the fallback metadata when no contextual value is determined.
         *
         * @return the default metadata value
         */
        int defaultPlacementMeta();
    }

    /**
     * Output definition for a mutation
     */
    @Desugar
    public record Output(Block block, PlacementStrategy placement) {

        /**
         * Converts the output block into an {@link ItemStack} (size 1), primarily for display
         * purposes (e.g., NEI).
         *
         * @return an {@code ItemStack} representing this output
         * @throws IllegalStateException
         *     if no item can be resolved for {@code block}
         */
        public ItemStack asStack() {
            Item item = Item.getItemFromBlock(this.block);
            if (item == null || block instanceof BlockCrops) {
                // Render seeds for crop blocks (it looks better)
                item = this.block.getItem(null, 0, 0, 0);
            }
            if (item == null) {
                throw new IllegalStateException("No item form found for mutation block: " + this.block);
            }

            return new ItemStack(item, 1, this.placement.defaultPlacementMeta());
        }
    }

    /**
     * Creates a mutation that matches exactly {@code block/meta} and produces the same block
     * with fixed placement metadata.
     *
     * @param block
     *     the source and output block
     * @param meta
     *     the exact metadata to match and to use for output placement
     * @return a mutation with a single exact source and fixed-meta output
     */
    public static Mutation primitiveMutation(final Block block, int meta) {
        return new Mutation(
            new Output(block, basicStrategy(meta)),
            Collections.singletonList(
                Source.exact(block, meta)
            )
        );
    }

    /**
     * Creates a mutation that matches any metadata of {@code block} and produces it
     * with fixed placement metadata.
     *
     * @param block
     *     the source and output block
     * @param meta
     *     the metadata to use for output placement
     * @return a mutation with a wildcard source and fixed-meta output
     */
    public static Mutation primitiveAnyMetaMutation(final Block block, int meta) {
        return new Mutation(
            new Output(block, basicStrategy(meta)),
            Collections.singletonList(
                Source.anyMeta(block)
            )
        );
    }

    /**
     * Creates a mutation for a sapling that matches both {@code meta} and {@code meta + 8}
     * variants and produces the block with fixed placement metadata.
     *
     * @param block
     *     the sapling block (source and output)
     * @param meta
     *     the base metadata; {@code meta + 8} is also matched
     * @return a mutation matching both sapling variants with fixed-meta output
     */
    public static Mutation saplingMutation(final Block block, int meta) {
        return new Mutation(
            new Mutation.Output(block, Mutation.basicStrategy(meta)),
            Arrays.asList(
                Mutation.Source.exact(block, meta),
                Mutation.Source.exact(block, meta + 8)
            )
        );
    }

    /**
     * Creates a placement strategy that always returns {@code meta}.
     *
     * @param meta
     *     the fixed placement metadata
     * @return a fixed-meta placement strategy
     */
    public static PlacementStrategy basicStrategy(int meta) {
        return new PlacementStrategy() {
            @Override
            public int placementMeta(World world, int x, int y, int z) {
                return meta;
            }

            @Override
            public int defaultPlacementMeta() {
                return meta;
            }
        };
    }

    /**
     * Creates a placement strategy for wall-mounted blocks.
     *
     * <p>Selects metadata from adjacent solid faces.</p>
     *
     * @return a wall-placement strategy
     */
    public static PlacementStrategy wallStrategy() {
        return new PlacementStrategy() {
            @Override
            public int placementMeta(final World world, final int x, final int y, final int z) {
                // South (+Z) -> meta 1
                if (world.getBlock(x, y, z + 1).isSideSolid(world, x, y, z + 1, ForgeDirection.NORTH)) {
                    return 1;
                }

                // West (-X) -> meta 2
                if (world.getBlock(x - 1, y, z).isSideSolid(world, x - 1, y, z, ForgeDirection.EAST)) {
                    return 2;
                }

                // North (-Z) -> meta 4
                if (world.getBlock(x, y, z - 1).isSideSolid(world, x, y, z - 1, ForgeDirection.SOUTH)) {
                    return 4;
                }

                // East (+X) -> meta 8
                if (world.getBlock(x + 1, y, z).isSideSolid(world, x + 1, y, z, ForgeDirection.WEST)) {
                    return 8;
                }

                return 1;
            }

            @Override
            public int defaultPlacementMeta() {

                return 1;
            }
        };
    }
}

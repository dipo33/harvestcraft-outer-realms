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
     * @param block the block to test against the mutation's sources
     * @param meta  the metadata value to test (exact match or wildcard)
     * @return      `true` if any source matches the provided block and metadata, `false` otherwise
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
     * Selects a random mutation from the registry pool for the given type that is different from
     * the mutation matching the specified block and metadata.
     *
     * @param type the mutation pool type to query
     * @param block the input block used to find the matching mutation
     * @param meta the metadata of the input block used to find the matching mutation
     * @param rand source of randomness for selecting an alternative mutation
     * @return a Mutation different from the pool member that matches the provided block and meta,
     *         or `null` if the pool is missing, the pool has fewer than two members, or no matching
     *         mutation exists
     */
    public static Mutation mutate(MutationPoolType type, Block block, int meta, Random rand) {
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

        int resultMutationIdx = rand.nextInt(mutations.size() - 1);
        if (resultMutationIdx >= match.index()) {
            resultMutationIdx++;
        }

        return mutations.get(resultMutationIdx);
    }

    /**
     * A source matcher: matches a block and either an exact metadata or ANY metadata (wildcard).
     *
     * @param meta
     *     null = wildcard
     */
    @Desugar
    public record Source(Block block, Integer meta) {
        /**
         * Checks whether the given block and metadata match this Source.
         *
         * @param b the block to test
         * @param m the metadata to test
         * @return `true` if `b` is the same block as this Source and this Source's `meta` is `null` or equals `m`, `false` otherwise
         */
        public boolean matches(Block b, int m) {
            return b == block && (meta == null || meta == m);
        }

        /**
         * Creates a Source that matches the given block irrespective of metadata.
         *
         * @param b the block to match
         * @return a Source that matches the specified block with any metadata value
         */
        public static Source anyMeta(Block b) {
            return new Source(b, null);
        }

        /**
         * Creates a Source that matches the given block with an exact metadata value.
         *
         * @param b    the block to match
         * @param meta the exact metadata value to require for a match
         * @return     a Source that matches only the specified block with the specified metadata
         */
        public static Source exact(Block b, int meta) {
            return new Source(b, meta);
        }
    }

    public interface PlacementStrategy {
        /**
 * Determine the metadata value to use when placing the output block at the specified world coordinates.
 *
 * @param world the world where the block will be placed
 * @param x the x-coordinate of the placement position
 * @param y the y-coordinate of the placement position
 * @param z the z-coordinate of the placement position
 * @return the metadata value to use for placement at the specified position
 */
        int placementMeta(World world, int x, int y, int z);

        /**
 * Provides the fallback metadata value to use when placing the output block.
 *
 * @return the default metadata value to apply when no contextual placement decision is available
 */
int defaultPlacementMeta();
    }

    @Desugar
    public record Output(Block block, PlacementStrategy placement) {
        /**
         * Create an ItemStack for this mutation's output block using the placement strategy's default metadata.
         *
         * If the block has no direct item mapping or is a crop, attempts to obtain a seed-like item from the block.
         *
         * @return an ItemStack of the output block with quantity 1 and metadata from the placement strategy's default
         * @throws IllegalStateException if no item can be resolved for the output block
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
     * Creates a mutation that produces the specified block with a fixed placement meta and matches only the exact source block/meta.
     *
     * @param block the output block and the required source block to match
     * @param meta  the metadata value used for output placement and for the exact source match
     * @return a Mutation whose output is the given block with a basic placement strategy for `meta` and whose sources list contains a single exact match for `block`/`meta`
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
     * Creates a mutation that matches any metadata of the given block and produces the block with a fixed placement meta.
     *
     * @param block the block used both as the source matcher (wildcard metadata) and as the mutation output
     * @param meta  the placement metadata to use for the mutation's output
     * @return a Mutation whose sources contain a wildcard-matching Source for `block` and whose output uses a basic strategy with `meta`
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
     * Creates a mutation for a sapling block that matches both its base and alternate (meta + 8) metadata variants.
     *
     * @param block the sapling block produced by the mutation
     * @param meta  the base metadata value for the sapling; the mutation will match sources with metadata equal to `meta` and `meta + 8`
     * @return      a Mutation configured to output the given block and to match the two sapling metadata variants
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
     * Creates a placement strategy that always uses the given metadata value.
     *
     * @param meta the metadata value to use for placement and as the default
     * @return a PlacementStrategy that returns `meta` for placement and default placement
     */
    public static PlacementStrategy basicStrategy(int meta) {
        return new PlacementStrategy() {
            /**
             * Provide the fixed metadata value used for placement regardless of world or position.
             *
             * @return the fixed metadata value to use when placing the block
             */
            @Override
            public int placementMeta(World world, int x, int y, int z) {
                return meta;
            }

            /**
             * Provide the fallback metadata value used when placing the output block.
             *
             * @return the fixed metadata value configured for this placement strategy
             */
            @Override
            public int defaultPlacementMeta() {
                return meta;
            }
        };
    }

    /**
     * Creates a placement strategy that selects a wall-facing meta based on a solid neighbor.
     *
     * The strategy's {@code placementMeta} returns:
     * - 1 when the block to the south (+Z) has a solid north face,
     * - 2 when the block to the west (-X) has a solid east face,
     * - 4 when the block to the north (-Z) has a solid south face,
     * - 8 when the block to the east (+X) has a solid west face;
     * if none match it returns 1. {@code defaultPlacementMeta} returns 1.
     *
     * @return a {@link PlacementStrategy} that computes placement meta from adjacent solid faces
     */
    public static PlacementStrategy wallStrategy() {
        return new PlacementStrategy() {
            /**
             * Selects placement metadata for a wall-mounted block based on which adjacent face is solid.
             *
             * @return `1` for a solid neighbor to the south (+Z), `2` for west (-X), `4` for north (-Z), `8` for east (+X); defaults to `1` if none match.
             */
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

            /**
             * Provides the fallback metadata value to use for placement when no world context is available.
             *
             * @return the default placement metadata value, 1
             */
            @Override
            public int defaultPlacementMeta() {

                return 1;
            }
        };
    }
}

package com.dipo33.bewitched.api.mutation;

import com.github.bsideup.jabel.Desugar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class MutationPool {

    private final MutationPoolType type;
    private final String unlocalizedName;
    private final List<ItemStack> catalysts = new ArrayList<>();
    private final List<Mutation> members = new ArrayList<>();

    /**
     * Creates a MutationPool with the specified pool type and identifier.
     *
     * The new pool is initialized with empty catalyst and member lists.
     *
     * @param type the classification of this pool
     * @param unlocalizedName the pool's identifier used for localization/unambiguous lookup
     */
    public MutationPool(MutationPoolType type, String unlocalizedName) {
        this.type = type;
        this.unlocalizedName = unlocalizedName;
    }

    /**
     * Get the pool's mutation classification.
     *
     * @return the pool's {@link MutationPoolType} classification
     */
    public MutationPoolType getType() {
        return type;
    }

    /**
     * Gets the pool's unlocalized name.
     *
     * @return the unlocalized name of this mutation pool
     */
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    /**
     * Adds a catalyst item to the pool.
     *
     * Stores a defensive copy of the provided ItemStack in the pool's catalyst list.
     *
     * @param stack the ItemStack to add; a copy of this stack is stored internally
     */
    public void addCatalyst(ItemStack stack) {
        catalysts.add(stack.copy());
    }

    /**
     * Adds a mutation to this pool's members.
     *
     * @param mutation the mutation to add
     */
    public void addMember(Mutation mutation) {
        members.add(mutation);
    }

    /**
     * Returns an unmodifiable view of the catalyst item stacks for this pool.
     *
     * @return an unmodifiable List of the pool's catalyst {@code ItemStack} objects
     */
    public List<ItemStack> getCatalysts() {
        return Collections.unmodifiableList(catalysts);
    }

    /**
     * Access the mutation members contained in this pool.
     *
     * @return an unmodifiable list of the pool's Mutation members
     */
    public List<Mutation> getMembers() {
        return Collections.unmodifiableList(members);
    }

    /**
     * Finds the first mutation in this pool whose source matches the specified block and metadata.
     *
     * @param block the block to match against mutation sources
     * @param meta  the block metadata value to match
     * @return      a {@code MutationMatch} containing the matched mutation and its index in this pool, or {@code null} if no match is found
     */
    public MutationMatch getMutationFor(Block block, int meta) {
        for (int i = 0; i < this.members.size(); i++) {
            Mutation mutation = this.members.get(i);
            if (mutation.matchesSource(block, meta)) {
                return new MutationMatch(mutation, i);
            }
        }

        return null;
    }

    @Desugar
    public record MutationMatch(Mutation mutation, int index) {
    }
}

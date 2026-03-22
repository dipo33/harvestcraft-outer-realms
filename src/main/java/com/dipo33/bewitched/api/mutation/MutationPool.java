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
     * Creates an empty mutation pool.
     *
     * @param type
     *     the pool classification
     * @param unlocalizedName
     *     the unique identifier (unlocalized)
     */
    public MutationPool(MutationPoolType type, String unlocalizedName) {
        this.type = type;
        this.unlocalizedName = unlocalizedName;
    }

    /**
     * Returns the pool type.
     */
    public MutationPoolType getType() {
        return type;
    }

    /**
     * Returns the unlocalized name.
     */
    public String getUnlocalizedName() {
        return unlocalizedName;
    }

    /**
     * Adds an item as a catalyst to this pool.
     *
     * @param stack
     *     the catalyst item to add
     */
    public void addCatalyst(ItemStack stack) {
        catalysts.add(stack.copy());
    }

    /**
     * Adds a mutation to this pool.
     *
     * @param mutation
     *     the mutation to add
     */
    public void addMember(Mutation mutation) {
        members.add(mutation);
    }

    /**
     * Returns an unmodifiable view of the catalysts.
     */
    public List<ItemStack> getCatalysts() {
        return Collections.unmodifiableList(catalysts);
    }

    /**
     * Returns an unmodifiable view of the mutations.
     */
    public List<Mutation> getMembers() {
        return Collections.unmodifiableList(members);
    }

    /**
     * Returns the first mutation matching {@code block/meta}, or {@code null} if none match.
     *
     * @param block
     *     the source block
     * @param meta
     *     the source metadata
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

    /**
     * Result of a mutation lookup, including the matched mutation and its index.
     */
    @Desugar
    public record MutationMatch(Mutation mutation, int index) {
    }
}

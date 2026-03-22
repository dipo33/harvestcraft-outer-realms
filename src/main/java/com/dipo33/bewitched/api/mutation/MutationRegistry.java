package com.dipo33.bewitched.api.mutation;

import com.dipo33.bewitched.utils.ItemStackHelper;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.item.ItemStack;

public final class MutationRegistry {

    private static final Map<MutationPoolType, MutationPool> POOLS = new EnumMap<>(MutationPoolType.class);

    private MutationRegistry() {
    }

    /**
     * Creates and registers a pool for {@code type}.
     *
     * @param type
     *     the pool type
     * @param unlocalizedName
     *     the pool identifier
     * @return the registered pool
     * @throws IllegalStateException
     *     if a pool is already registered for {@code type}
     */
    public static MutationPool createPool(MutationPoolType type, String unlocalizedName) {
        if (POOLS.containsKey(type)) {
            throw new IllegalStateException("Mutation pool already registered for type: " + type);
        }
        MutationPool pool = new MutationPool(type, unlocalizedName);
        POOLS.put(type, pool);
        return pool;
    }

    /**
     * Returns the registered pool for {@code type}, or {@code null} if none exists.
     *
     * @param type
     *     the pool type
     */
    public static MutationPool getPool(MutationPoolType type) {
        return POOLS.get(type);
    }

    /**
     * Returns whether {@code stack} matches any mutation output in the pool for {@code type}.
     *
     * @param type
     *     the pool type
     * @param stack
     *     the item to check
     * @return {@code true} if the stack matches a pool member output
     */
    public static boolean isMember(MutationPoolType type, ItemStack stack) {
        MutationPool pool = getPool(type);
        if (pool == null || stack == null) {
            return false;
        }

        for (Mutation member : pool.getMembers()) {
            if (ItemStackHelper.areStacksSame(member.output().asStack(), stack)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns whether {@code stack} is a catalyst in the pool for {@code type}.
     *
     * @param type
     *     the pool type
     * @param stack
     *     the item to check
     * @return {@code true} if the item matches a catalyst
     */
    public static boolean isCatalyst(MutationPoolType type, ItemStack stack) {
        MutationPool pool = getPool(type);
        if (pool == null || stack == null) {
            return false;
        }

        for (ItemStack catalyst : pool.getCatalysts()) {
            if (ItemStackHelper.areStacksSame(catalyst, stack)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Returns a copy of the catalysts for {@code type}, or an empty list if no pool exists.
     *
     * @param type
     *     the pool type
     */
    public static List<ItemStack> getCatalysts(MutationPoolType type) {
        MutationPool pool = getPool(type);
        return pool == null ? new ArrayList<>() : new ArrayList<>(pool.getCatalysts());
    }

    /**
     * Returns a copy of the members for {@code type}, or an empty list if no pool exists.
     *
     * @param type
     *     the pool type
     */
    public static List<Mutation> getMembers(MutationPoolType type) {
        MutationPool pool = getPool(type);
        return pool == null ? new ArrayList<>() : new ArrayList<>(pool.getMembers());
    }
}

package com.dipo33.bewitched.api.mutation;

import com.dipo33.bewitched.utils.ItemStackHelper;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.item.ItemStack;

public final class MutationRegistry {

    private static final Map<MutationPoolType, MutationPool> POOLS = new EnumMap<>(MutationPoolType.class);

    /**
     * Prevents instantiation of this utility class.
     */
    private MutationRegistry() {
    }

    /**
     * Create and register a new MutationPool for the specified pool type.
     *
     * @param type the MutationPoolType key under which the new pool will be registered
     * @param unlocalizedName the unlocalized name used to construct the new pool
     * @return the newly created and registered MutationPool
     * @throws IllegalStateException if a pool is already registered for the given type
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
     * Retrieve the registered mutation pool for the specified pool type.
     *
     * @param type the mutation pool type to look up
     * @return the registered {@code MutationPool} for {@code type}, or {@code null} if no pool is registered
     */
    public static MutationPool getPool(MutationPoolType type) {
        return POOLS.get(type);
    }

    /**
     * Checks whether the provided ItemStack matches the output of any member in the pool for the given type.
     *
     * @param type  the mutation pool type to query
     * @param stack the item stack to check; if `null` the method returns `false`
     * @return `true` if `stack` is equal to the output stack of any member in the pool for `type`, `false` otherwise (also `false` when no pool is registered for `type`)
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
     * Checks whether the given ItemStack is registered as a catalyst in the pool for the specified type.
     *
     * @param type  the mutation pool type to query
     * @param stack the ItemStack to check (may be null)
     * @return `true` if the pool for `type` contains a catalyst equivalent to `stack`, `false` otherwise
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
     * Retrieve a defensive list of catalyst ItemStacks for the specified mutation pool type.
     *
     * @param type the mutation pool type whose catalysts to retrieve
     * @return a new List containing the pool's catalyst ItemStacks; an empty list if no pool is registered for the given type
     */
    public static List<ItemStack> getCatalysts(MutationPoolType type) {
        MutationPool pool = getPool(type);
        return pool == null ? new ArrayList<>() : new ArrayList<>(pool.getCatalysts());
    }

    /**
     * Retrieve the members registered for the given mutation pool type.
     *
     * @param type the mutation pool type to query
     * @return a new list containing the pool's members, or an empty list if no pool is registered for the given type
     */
    public static List<Mutation> getMembers(MutationPoolType type) {
        MutationPool pool = getPool(type);
        return pool == null ? new ArrayList<>() : new ArrayList<>(pool.getMembers());
    }
}

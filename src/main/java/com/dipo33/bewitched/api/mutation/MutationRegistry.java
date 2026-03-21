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

    public static MutationPool createPool(MutationPoolType type, String unlocalizedName) {
        if (POOLS.containsKey(type)) {
            throw new IllegalStateException("Mutation pool already registered for type: " + type);
        }
        MutationPool pool = new MutationPool(type, unlocalizedName);
        POOLS.put(type, pool);
        return pool;
    }

    public static MutationPool getPool(MutationPoolType type) {
        return POOLS.get(type);
    }

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

    public static List<ItemStack> getCatalysts(MutationPoolType type) {
        MutationPool pool = getPool(type);
        return pool == null ? new ArrayList<>() : new ArrayList<>(pool.getCatalysts());
    }

    public static List<Mutation> getMembers(MutationPoolType type) {
        MutationPool pool = getPool(type);
        return pool == null ? new ArrayList<>() : new ArrayList<>(pool.getMembers());
    }
}

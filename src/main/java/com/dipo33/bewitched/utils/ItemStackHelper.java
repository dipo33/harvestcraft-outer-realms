package com.dipo33.bewitched.utils;

import net.minecraft.item.ItemStack;

public class ItemStackHelper {

    /**
     * Determines whether two ItemStack instances represent the same item and damage value.
     *
     * @param a the first ItemStack to compare
     * @param b the second ItemStack to compare
     * @return `true` if both stacks are non-null, reference the same item, and have equal item damage; `false` otherwise
     */
    public static boolean areStacksSame(ItemStack a, ItemStack b) {
        if (a == null || b == null) {
            return false;
        }
        if (a.getItem() != b.getItem()) {
            return false;
        }

        return a.getItemDamage() == b.getItemDamage();
    }
}

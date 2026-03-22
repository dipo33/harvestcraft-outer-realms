package com.dipo33.bewitched.utils;

import net.minecraft.item.ItemStack;

/**
 * Utility methods for working with {@link ItemStack}.
 */
public class ItemStackHelper {

    /**
     * Returns whether both stacks have the same item and damage value.
     *
     * @param a
     *     the first stack
     * @param b
     *     the second stack
     * @return {@code true} if both are non-null and equal by item and damage
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

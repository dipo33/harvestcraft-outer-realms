package com.dipo33.bewitched.utils;

import net.minecraft.item.ItemStack;

public class ItemStackHelper {

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

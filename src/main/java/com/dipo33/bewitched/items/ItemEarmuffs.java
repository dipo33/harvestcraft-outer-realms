package com.dipo33.bewitched.items;

import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public class ItemEarmuffs extends ItemArmor {

    private static final ItemArmor.ArmorMaterial BEWITCHED_LEATHER =
        EnumHelper.addArmorMaterial(
            "BEWITCHED_LEATHER",
            5, // 5 (from here) * 11 (from vanilla factor) = 55
            new int[]{1, 3, 2, 1},
            15
        );

    static {
        BEWITCHED_LEATHER.customCraftingMaterial = Items.leather;
    }

    public ItemEarmuffs() {
        // material, render passes, armor slot (0 for helmet)
        super(BEWITCHED_LEATHER, 0, 0);
    }
}

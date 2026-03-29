package com.dipo33.bewitched.init;

import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

public class BewitchedItemArmorMaterials {

    public static final ItemArmor.ArmorMaterial BEWITCHED_LEATHER
        = EnumHelper.addArmorMaterial("BEWITCHED_LEATHER", 5, new int[]{1, 3, 2, 1}, 15);

    /**
     * Associates the BEWITCHED_LEATHER armor material with the vanilla leather crafting ingredient.
     *
     * Sets the armor material's crafting ingredient so items using BEWITCHED_LEATHER can be repaired or
     * crafted with `Items.leather`.
     */
    public static void addArmorMaterials() {
        BEWITCHED_LEATHER.customCraftingMaterial = Items.leather;
    }
}

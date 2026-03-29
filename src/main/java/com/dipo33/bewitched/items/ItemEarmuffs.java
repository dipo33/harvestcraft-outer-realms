package com.dipo33.bewitched.items;

import com.dipo33.bewitched.init.BewitchedItemArmorMaterials;

import net.minecraft.item.ItemArmor;

public class ItemEarmuffs extends ItemArmor {

    public ItemEarmuffs() {
        // material, render passes, armor slot (0 for helmet)
        super(BewitchedItemArmorMaterials.BEWITCHED_LEATHER, 0, 0);
    }
}

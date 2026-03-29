package com.dipo33.bewitched.items;

import com.dipo33.bewitched.init.BewitchedItemArmorMaterials;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemEarmuffs extends ItemArmor {

    public ItemEarmuffs() {
        // material, render passes, armor slot (0 for helmet)
        super(BewitchedItemArmorMaterials.BEWITCHED_LEATHER, 0, 0);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltips, boolean advancedTooltips) {
        tooltips.add(StatCollector.translateToLocal(this.getUnlocalizedName(stack) + ".desc"));
    }
}

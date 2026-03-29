package com.dipo33.bewitched.items;

import com.dipo33.bewitched.init.BewitchedItemArmorMaterials;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemEarmuffs extends ItemArmor {

    /**
     * Creates a new Earmuffs armor item configured as a helmet using bewitched leather.
     *
     * Configures this item with BewitchedItemArmorMaterials.BEWITCHED_LEATHER and the helmet armor slot.
     */
    public ItemEarmuffs() {
        // material, render passes, armor slot (0 for helmet)
        super(BewitchedItemArmorMaterials.BEWITCHED_LEATHER, 0, 0);
    }

    /**
     * Appends the localized description for this item to the provided tooltip list.
     *
     * The localized text is looked up using the item's unlocalized name with ".desc" appended.
     *
     * @param stack the ItemStack instance of this item
     * @param player the player viewing the tooltip
     * @param tooltips the list to which the description line will be added
     * @param advancedTooltips true if advanced tooltips are enabled
     */
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltips, boolean advancedTooltips) {
        tooltips.add(StatCollector.translateToLocal(this.getUnlocalizedName(stack) + ".desc"));
    }
}

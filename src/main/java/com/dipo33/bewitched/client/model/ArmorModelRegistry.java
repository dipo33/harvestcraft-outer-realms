package com.dipo33.bewitched.client.model;

import com.dipo33.bewitched.client.model.model.BewitchedModelEarmuffs;
import com.dipo33.bewitched.init.BewitchedItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.item.Item;

@SideOnly(Side.CLIENT)
public class ArmorModelRegistry {

    public static ModelBiped helmetModel;

    public static void init() {
        helmetModel = new BewitchedModelEarmuffs();
    }

    public static ModelBiped getModel(Item item) {
        if (item == BewitchedItems.EARMUFFS.get()) return helmetModel;

        return null;
    }
}

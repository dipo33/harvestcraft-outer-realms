package com.dipo33.bewitched.integration;

import net.minecraft.item.ItemStack;

import com.dipo33.bewitched.Bewitched;
import com.dipo33.bewitched.Tags;
import com.dipo33.bewitched.block.BlockRegistry;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.Optional;

@Optional.Interface(iface = "codechicken.nei.api.IConfigureNEI", modid = "NotEnoughItems")
public class NEIBewitchedConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {
        API.hideItem(new ItemStack(BlockRegistry.BELLADONNA_CROP.get()));
        API.hideItem(new ItemStack(BlockRegistry.WOLFSBANE_CROP.get()));
        API.hideItem(new ItemStack(BlockRegistry.WATER_ARTICHOKE_CROP.get()));
        API.hideItem(new ItemStack(BlockRegistry.MANDRAKE_CROP.get()));
        API.hideItem(new ItemStack(BlockRegistry.SNOW_WISP_CROP.get()));
        API.hideItem(new ItemStack(BlockRegistry.GARLIC_CROP.get()));
    }

    @Override
    public String getName() {
        return Bewitched.MOD_NAME;
    }

    @Override
    public String getVersion() {
        return Tags.VERSION;
    }
}

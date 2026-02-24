package com.dipo33.bewitched.block;

import com.dipo33.bewitched.data.ObjectHolder;
import com.dipo33.bewitched.data.Pair;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class BwBlockCrops extends BlockCrops {

    private final ObjectHolder<Item> crop;
    private final ObjectHolder<Item> seed;

    private int stages = 4;
    private final List<Pair<Item, Double>> additionalDrops = new ArrayList<>();

    // prefer earlier stages to grow faster
    private static final int[][] META_STAGE_TO_ICON = new int[][]{
        new int[]{0, 0, 0, 0, 0, 0, 0, 0}, // 1 stage
        new int[]{0, 0, 0, 0, 0, 0, 0, 1}, // 2 stages
        new int[]{0, 0, 0, 1, 1, 1, 1, 2}, // 3 stages
        new int[]{0, 0, 1, 1, 2, 2, 2, 3}, // 4 stages
        new int[]{0, 1, 1, 2, 2, 3, 3, 4}, // 5 stages
        new int[]{0, 1, 2, 3, 3, 4, 4, 5}, // 6 stages
        new int[]{0, 1, 2, 3, 4, 5, 5, 6}, // 7 stages
        new int[]{0, 1, 2, 3, 4, 5, 6, 7}, // 8 stages
    };

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public BwBlockCrops(ObjectHolder<Item> seed, ObjectHolder<Item> crop) {
        super();
        this.seed = seed;
        this.crop = crop;
    }

    public BwBlockCrops setStages(int stages) {
        this.stages = stages;
        return this;
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        return this.icons[META_STAGE_TO_ICON[this.stages - 1][meta]];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        this.icons = new IIcon[this.stages];

        for (int i = 0; i < this.icons.length; ++i) {
            this.icons[i] = reg.registerIcon(this.getTextureName() + "_stage_" + i);
        }
    }

    @Override
    // getSeed
    protected Item func_149866_i() {
        return this.seed.get();
    }

    @Override
    // getCrop
    protected Item func_149865_P() {
        return this.crop.get();
    }
}

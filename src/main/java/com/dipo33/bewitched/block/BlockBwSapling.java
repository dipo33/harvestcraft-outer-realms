package com.dipo33.bewitched.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenForest;
import net.minecraft.world.gen.feature.WorldGenMegaJungle;
import net.minecraft.world.gen.feature.WorldGenMegaPineTree;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

public class BlockBwSapling extends BlockSapling {
    public static final String[] VARIANTS = {"rowan", "alder", "hawthorn"};

    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    /**
     * Register an icon for each sapling variant using the block's texture name.
     *
     * @param reg the icon register used to load and store variant icons
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(final IIconRegister reg) {
        icons = new IIcon[VARIANTS.length];
        for (int i = 0; i < VARIANTS.length; i++) {
            this.icons[i] = reg.registerIcon(this.getTextureName() + "_" + VARIANTS[i]);
        }
    }

    /**
     * Selects the texture icon for this sapling according to its variant metadata.
     *
     * @param side the block face being rendered
     * @param meta the block metadata (variant id stored in the low 3 bits)
     * @return the icon corresponding to the sapling variant for the given face and metadata
     */
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(final int side, final int meta) {
        return this.icons[MathHelper.clamp_int(meta & 7, 0, VARIANTS.length - 1)];
    }

    /**
     * Map the block's metadata to the variant id used for the dropped item.
     *
     * @param meta the block metadata value
     * @return the variant id between 0 and VARIANTS.length - 1 to use for the dropped item
     */
    @Override
    public int damageDropped(final int meta) {
        return MathHelper.clamp_int(meta & 7, 0, VARIANTS.length - 1);
    }

    /**
     * Adds one ItemStack for each sapling variant to the provided creative-tab list.
     *
     * @param item the Item instance for this block
     * @param tab  the CreativeTabs being populated
     * @param list the list to receive one ItemStack per variant (damage value = variant index)
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(final Item item, final CreativeTabs tab, final List<ItemStack> list) {
        for (int i = 0; i < VARIANTS.length; i++) {
            list.add(new ItemStack(item, 1, i));
        }
    }

    /**
     * Attempts to grow this sapling into its corresponding tree at the given coordinates.
     *
     * The method first consults TerrainGen.saplingGrowTree to determine whether growth is allowed.
     * It reads the sapling variant from the block metadata (meta & 7) and chooses a WorldGenerator:
     * meta 0 → a forest tree, meta 1 → a mega pine tree, meta 2 → a mega jungle tree.
     * The sapling block is replaced with air before generation; if generation fails the original sapling
     * is restored with its original metadata.
     *
     * @param world the world containing the sapling
     * @param x     the x coordinate of the sapling
     * @param y     the y coordinate of the sapling
     * @param z     the z coordinate of the sapling
     * @param rng   a random number generator for tree generation
     */
    @Override
    public void func_149878_d(final World world, final int x, final int y, final int z, final Random rng) {
        if (!TerrainGen.saplingGrowTree(world, rng, x, y, z)) {
            return;
        }

        int meta = world.getBlockMetadata(x, y, z) & 7;
        WorldGenerator gen;

        switch (meta) {
            // TODO: Grow actual correct trees
            case 0:
                gen = new WorldGenForest(true, false);
                break;
            case 1:
                gen = new WorldGenMegaPineTree(false, true);
                break;
            case 2:
                gen = new WorldGenMegaJungle(true, 10, 20, 3, 3);
                break;
            default:
                return;
        }

        world.setBlockToAir(x, y, z);

        if (!gen.generate(world, rng, x, y, z)) {
            world.setBlock(x, y, z, this, meta, 4);
        }
    }
}

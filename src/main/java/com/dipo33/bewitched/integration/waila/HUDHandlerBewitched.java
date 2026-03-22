package com.dipo33.bewitched.integration.waila;

import com.dipo33.bewitched.block.BwBlockCrops;
import com.dipo33.bewitched.init.BewitchedBlocks;
import com.dipo33.bewitched.init.BewitchedItems;

import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.overlay.DisplayUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class HUDHandlerBewitched implements IWailaDataProvider {
    /**
     * Selects the ItemStack that should be shown in the Waila HUD for the block currently targeted.
     *
     * @return the ItemStack to display for the targeted block: an `ICY_NEEDLE` for Snow Wisp Crop, the crop's item for instances of `BwBlockCrops`, or `null` if no display stack is provided.
     */
    @Override
    public ItemStack getWailaStack(final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
        Block block = accessor.getBlock();
        if (block == BewitchedBlocks.SNOW_WISP_CROP.get()) {
            return new ItemStack(BewitchedItems.ICY_NEEDLE.get());
        } else if (block instanceof BwBlockCrops blockCrops) {
            return new ItemStack(blockCrops.func_149865_P());
        }

        return null;
    }

    /**
     * Replaces or inserts the first HUD tooltip line with a short display name for Bewitched crop blocks.
     *
     * If the targeted block is a BwBlockCrops and a short name is available, sets it as element 0 of {@code currentTip}
     * (inserting if the list is empty). Otherwise leaves {@code currentTip} unchanged.
     *
     * @param itemStack the item stack under the cursor (may be unused)
     * @param currentTip the existing tooltip lines; may be modified in place
     * @param accessor provides context about the targeted block and world
     * @param config WAILA configuration (unused)
     * @return the same {@code currentTip} list instance, possibly modified with an updated first line
     */
    @Override
    public List<String> getWailaHead(final ItemStack itemStack, final List<String> currentTip, final IWailaDataAccessor accessor,
                                     final IWailaConfigHandler config) {
        Block block = accessor.getBlock();
        if (block instanceof BwBlockCrops) {
            String name = DisplayUtil.itemDisplayNameShort(new ItemStack(block));
            if (name != null) {
                if (currentTip.isEmpty()) {
                    currentTip.add(name);
                } else {
                    currentTip.set(0, name);
                }
            }
        }

        return currentTip;
    }

    /**
     * Provide the HUD body (middle) tooltip lines for the targeted block; this implementation leaves the list unchanged.
     *
     * @param currentTip the current list of tooltip lines to display; may be modified by providers and is returned as-is here
     * @param accessor   contextual accessor (world, position, block state, etc.) for the targeted object
     * @return the tooltip lines to display, identical to the provided `currentTip`
     */
    @Override
    public List<String> getWailaBody(final ItemStack itemStack, final List<String> currentTip, final IWailaDataAccessor accessor,
                                     final IWailaConfigHandler config) {
        return currentTip;
    }

    /**
     * Leaves the HUD tail section unmodified.
     *
     * @return the unmodified list of tooltip lines passed in `currentTip`
     */
    @Override
    public List<String> getWailaTail(final ItemStack itemStack, final List<String> currentTip, final IWailaDataAccessor accessor,
                                     final IWailaConfigHandler config) {
        return currentTip;
    }

    /**
     * Passes through the given NBT tag without modification.
     *
     * @param tag the NBT tag provided by Waila; returned unchanged
     * @return the same {@link NBTTagCompound} instance passed in {@code tag}
     */
    @Override
    public NBTTagCompound getNBTData(final EntityPlayerMP player, final TileEntity te, final NBTTagCompound tag, final World world, final int x,
                                     final int y, final int z) {
        return tag;
    }

    /**
     * Registers the Bewitched HUD data provider with the given Waila registrar for crop blocks.
     *
     * @param registrar the Waila registrar used to register stack and head providers
     */
    public static void register(IWailaRegistrar registrar) {
        IWailaDataProvider provider = new HUDHandlerBewitched();
        registrar.registerStackProvider(provider, BwBlockCrops.class);
        registrar.registerHeadProvider(provider, BwBlockCrops.class);
    }
}

package com.dipo33.bewitched.items;

import com.dipo33.bewitched.api.mutation.MutationPoolType;
import com.dipo33.bewitched.api.mutation.MutationRegistry;
import com.dipo33.bewitched.client.effect.EffectPlayer;
import com.dipo33.bewitched.client.effect.Effects;
import com.dipo33.bewitched.api.mutation.Mutation;
import com.dipo33.bewitched.init.BewitchedItems;
import com.dipo33.bewitched.network.BwNetwork;
import com.dipo33.bewitched.network.message.UpdateFlowerPotMsg;
import com.dipo33.bewitched.sound.Sounds;

import cpw.mods.fml.common.network.NetworkRegistry;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.world.World;

public class ItemMutandis extends Item {

    private static final MutationPoolType[] WORLD_POOLS = {
        MutationPoolType.MUTANDIS,
        MutationPoolType.MUTANDIS_EXTREMIS_GRASS,
        MutationPoolType.MUTANDIS_EXTREMIS
    };

    /**
     * Handles using this item on a world position: verifies edit permission, attempts a Mutandis mutation at the target,
     * and triggers the appropriate server-side effects when a mutation is applied.
     *
     * <p>When a mutation is successfully applied, server-side visual FX and the Mutandis sound are played at the target
     * position.</p>
     *
     * @param stack the item stack being used
     * @param player the player using the item
     * @param world the world in which the interaction occurs
     * @param x target block X coordinate
     * @param y target block Y coordinate
     * @param z target block Z coordinate
     * @param side the side of the block that was clicked
     * @param hitX hit X offset on the block
     * @param hitY hit Y offset on the block
     * @param hitZ hit Z offset on the block
     * @return `true` if a mutation was successfully applied (and effects were triggered), `false` otherwise
     */
    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!player.canPlayerEdit(x, y, z, side, stack)) {
            return false;
        }

        if (this.applyMutandis(stack, world, x, y, z)) {
            if (!world.isRemote) {
                if (this == BewitchedItems.MUTANDIS_EXTREMIS.get()) {
                    EffectPlayer.playFX(Effects.MUTANDIS_EXTREMIS_FX, world, x, y, z, 32);
                } else {
                    EffectPlayer.playFX(Effects.MUTANDIS_FX, world, x, y, z, 32);
                }

                world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, Sounds.MUTANDIS, 1.0F, 0.60F);
            }

            return true;
        }

        return false;
    }

    /**
     * Apply the given mutation by replacing the block at the specified coordinates and decrement the catalyst stack if placement succeeds.
     *
     * This operation is only performed on the server; it is a no-op when the world is remote (client).
     *
     * @param catalyst       the catalyst ItemStack to consume; its stack size is decremented by one if the block placement succeeds
     * @param world          the world to modify
     * @param x              target X coordinate
     * @param y              target Y coordinate
     * @param z              target Z coordinate
     * @param mutationOutput the mutation output describing the block to place and its placement metadata
     * @return               `true` (always)
     */
    private static boolean applyMutationToWorldBlock(
        ItemStack catalyst, World world, int x, int y, int z, Mutation.Output mutationOutput
    ) {
        if (!world.isRemote) {
            int meta = mutationOutput.placement().placementMeta(world, x, y, z);
            if (world.setBlock(x, y, z, mutationOutput.block(), meta, 2)) {
                catalyst.stackSize--;
            }
        }
        return true;
    }

    /**
     * Apply the given mutation output to a flower pot, update world and clients, and consume the catalyst on the server.
     *
     * On the server, this replaces the pot's contents and metadata with values derived from `mutationOutput`,
     * marks the tile entity dirty, notifies the world/clients (metadata update or block update plus an explicit
     * flower-pot packet to nearby players), and decrements `catalyst.stackSize`.
     *
     * @param catalyst the catalyst ItemStack to consume when the mutation is applied
     * @param world the world containing the flower pot
     * @param pot the TileEntityFlowerPot to mutate
     * @param mutationOutput the mutation result whose block and placement metadata will be applied to the pot
     * @return `true` always
     */
    private static boolean applyMutationToFlowerPot(
        ItemStack catalyst, World world, TileEntityFlowerPot pot, Mutation.Output mutationOutput
    ) {
        if (!world.isRemote) {
            int meta = mutationOutput.placement().defaultPlacementMeta();
            pot.func_145964_a(Item.getItemFromBlock(mutationOutput.block()), meta);
            pot.markDirty();

            if (!world.setBlockMetadataWithNotify(pot.xCoord, pot.yCoord, pot.zCoord, meta, 2)) {
                world.markBlockForUpdate(pot.xCoord, pot.yCoord, pot.zCoord);
            }

            BwNetwork.NET.sendToAllAround(
                new UpdateFlowerPotMsg(pot.xCoord, pot.yCoord, pot.zCoord, pot.getFlowerPotItem(), pot.getFlowerPotData()),
                new NetworkRegistry.TargetPoint(world.provider.dimensionId, pot.xCoord + 0.5, pot.yCoord + 0.5, pot.zCoord + 0.5, 128)
            );

            catalyst.stackSize--;
        }
        return true;
    }

    /**
     * Attempts to apply a Mutandis mutation at the given world position using the provided catalyst.
     *
     * If the target is a flower pot, the catalyst must be valid for flower-pot mutations; otherwise
     * a world-block mutation is selected from configured mutation pools. On success the world is
     * changed (block or flower-pot contents) and the catalyst is consumed.
     *
     * @param catalyst the catalyst ItemStack used to select and apply a mutation
     * @param world    the world containing the target position
     * @param x        target block X coordinate
     * @param y        target block Y coordinate
     * @param z        target block Z coordinate
     * @return         `true` if a mutation was produced and applied, `false` otherwise
     */
    private boolean applyMutandis(ItemStack catalyst, World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        if (block == Blocks.flower_pot) {
            if (MutationRegistry.isCatalyst(MutationPoolType.FLOWER_POT, catalyst)) {
                return applyMutandisOnFlowerPot(catalyst, world, x, y, z);
            }
            return false;
        }

        var mutation = pickMutation(catalyst, block, meta, world.rand);
        return mutation != null && applyMutationToWorldBlock(catalyst, world, x, y, z, mutation.output());
    }

    /**
     * Attempts to mutate the contents of the flower pot at the specified world coordinates using the given catalyst.
     *
     * @param catalyst the catalyst ItemStack used to select a mutation
     * @param world the world containing the flower pot
     * @param x block X coordinate of the flower pot
     * @param y block Y coordinate of the flower pot
     * @param z block Z coordinate of the flower pot
     * @return `true` if a mutation was selected and applied to the flower pot, `false` otherwise
     */
    private static boolean applyMutandisOnFlowerPot(ItemStack catalyst, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (!(tileEntity instanceof TileEntityFlowerPot pot)) {
            return false;
        }

        Item potItem = pot.getFlowerPotItem();
        if (!(potItem instanceof ItemBlock itemBlock)) {
            return false;
        }

        Block contained = Block.getBlockFromItem(itemBlock);
        int meta = pot.getFlowerPotData();
        var mutation = Mutation.mutate(MutationPoolType.FLOWER_POT, contained, meta, world.rand);
        return mutation != null && applyMutationToFlowerPot(catalyst, world, pot, mutation.output());
    }

    /**
     * Selects the first applicable mutation for the given block by iterating the configured mutation pools in order.
     *
     * @param catalyst the catalyst ItemStack used to determine which pools are applicable
     * @param block    the target block to attempt mutation on
     * @param meta     the target block's metadata value
     * @param rng      the source of randomness used by mutation selection
     * @return         the first matching Mutation found, or `null` if no mutation is available
     */
    private Mutation pickMutation(ItemStack catalyst, Block block, int meta, Random rng) {
        for (var pool : WORLD_POOLS) {
            if (MutationRegistry.isCatalyst(pool, catalyst)) {
                Mutation mutation = Mutation.mutate(pool, block, meta, rng);
                if (mutation != null) {
                    return mutation;
                }
            }
        }
        return null;
    }
}

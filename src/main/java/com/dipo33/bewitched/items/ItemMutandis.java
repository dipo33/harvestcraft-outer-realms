package com.dipo33.bewitched.items;

import com.dipo33.bewitched.client.effect.EffectPlayer;
import com.dipo33.bewitched.client.effect.Effects;
import com.dipo33.bewitched.items.mutandis.MutandisMutation;
import com.dipo33.bewitched.items.mutandis.MutandisMutationRegistry;
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

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!player.canPlayerEdit(x, y, z, side, stack)) {
            return false;
        }

        if (this.applyMutandis(stack, world, x, y, z)) {
            if (!world.isRemote) {
                if (this == ItemRegistry.MUTANDIS_EXTREMIS.get()) {
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

    private static boolean applyMutationToWorldBlock(
        ItemStack stack, World world, int x, int y, int z, MutandisMutation.Output mutationOutput
    ) {
        if (!world.isRemote) {
            int meta = mutationOutput.placement().placementMeta(world, x, y, z);
            world.setBlock(x, y, z, mutationOutput.block(), meta, 2);
            stack.stackSize--;
        }
        return true;
    }

    private static boolean applyMutationToFlowerPot(
        ItemStack stack, World world, TileEntityFlowerPot pot, MutandisMutation.Output mutationOutput
    ) {
        if (!world.isRemote) {
            int meta = mutationOutput.placement().placementMeta(world, pot.xCoord, pot.yCoord, pot.zCoord);
            pot.func_145964_a(Item.getItemFromBlock(mutationOutput.block()), meta);
            pot.markDirty();

            if (!world.setBlockMetadataWithNotify(pot.xCoord, pot.yCoord, pot.zCoord, meta, 2)) {
                world.markBlockForUpdate(pot.xCoord, pot.yCoord, pot.zCoord);
            }

            BwNetwork.NET.sendToAllAround(
                new UpdateFlowerPotMsg(pot.xCoord, pot.yCoord, pot.zCoord, pot.getFlowerPotItem(), pot.getFlowerPotData()),
                new NetworkRegistry.TargetPoint(world.provider.dimensionId, pot.xCoord + 0.5, pot.yCoord + 0.5, pot.zCoord + 0.5, 128)
            );

            stack.stackSize--;
        }
        return true;
    }

    private boolean applyMutandis(ItemStack stack, World world, int x, int y, int z) {
        Block block = world.getBlock(x, y, z);
        int meta = world.getBlockMetadata(x, y, z);
        if (block == Blocks.flower_pot) {
            return applyMutandisOnFlowerPot(stack, world, x, y, z);
        }

        var mutation = pickMutation(block, meta, world.rand);
        return mutation != null && applyMutationToWorldBlock(stack, world, x, y, z, mutation.output());
    }

    private static boolean applyMutandisOnFlowerPot(ItemStack stack, World world, int x, int y, int z) {
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
        var mutation = MutandisMutation.mutate(MutandisMutationRegistry.FLOWER_POT_MUTATIONS, contained, meta, world.rand);
        return mutation != null && applyMutationToFlowerPot(stack, world, pot, mutation.output());
    }

    private MutandisMutation pickMutation(Block block, int meta, Random rng) {
        if (this == ItemRegistry.MUTANDIS_EXTREMIS.get()) {
            MutandisMutation m = MutandisMutation.mutate(MutandisMutationRegistry.ADVANCED_WORLD_MUTATIONS, block, meta, rng);
            return m != null ? m : MutandisMutation.mutate(MutandisMutationRegistry.SPECIAL_WORLD_MUTATIONS, block, meta, rng);
        } else {
            return MutandisMutation.mutate(MutandisMutationRegistry.WORLD_MUTATIONS, block, meta, rng);
        }
    }
}

package com.dipo33.bewitched.network.handler;

import com.dipo33.bewitched.network.message.UpdateFlowerPotMsg;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFlowerPot;
import net.minecraft.world.World;

public class UpdateFlowerPotMsgHandler implements IMessageHandler<UpdateFlowerPotMsg, IMessage> {
    /**
     * Update the client-side flower pot tile entity at the coordinates carried by the message.
     *
     * @param msg the message containing the target coordinates (`x`,`y`,`z`), the `itemId` and `meta` to place into the flower pot
     * @param ctx the network message context
     * @return     `null` to indicate no response message
     */
    @Override
    public IMessage onMessage(UpdateFlowerPotMsg msg, MessageContext ctx) {
        Minecraft.getMinecraft().func_152344_a(() -> {
            World world = Minecraft.getMinecraft().theWorld;
            if (world == null) {
                return;
            }

            TileEntity te = world.getTileEntity(msg.x, msg.y, msg.z);
            if (!(te instanceof TileEntityFlowerPot pot)) {
                return;
            }

            Item item = Item.getItemById(msg.itemId);
            pot.func_145964_a(item, msg.meta);
            pot.markDirty();

            world.markBlockForUpdate(msg.x, msg.y, msg.z);
            world.markBlockRangeForRenderUpdate(msg.x, msg.y, msg.z, msg.x, msg.y, msg.z);
        });
        return null;
    }
}

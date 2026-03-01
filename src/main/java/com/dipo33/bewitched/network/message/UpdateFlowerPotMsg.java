package com.dipo33.bewitched.network.message;

import com.dipo33.bewitched.network.handler.PlaceholderHandler;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;

public class UpdateFlowerPotMsg implements IMessage {
    public int x, y, z;
    public int itemId;
    public int meta;

    public UpdateFlowerPotMsg() {
    }

    public UpdateFlowerPotMsg(int x, int y, int z, Item item, int meta) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.itemId = Item.getIdFromItem(item);
        this.meta = meta;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        itemId = buf.readInt();
        meta = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(itemId);
        buf.writeInt(meta);
    }

    public static class SafeHandler extends PlaceholderHandler<UpdateFlowerPotMsg> {
    }
}

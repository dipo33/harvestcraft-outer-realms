package com.dipo33.bewitched.network.message;

import com.dipo33.bewitched.network.handler.PlaceholderHandler;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;

public class UpdateFlowerPotMsg implements IMessage {
    public int x, y, z;
    public int itemId;
    public int meta;

    /**
     * Constructs an empty UpdateFlowerPotMsg.
     *
     * This no-argument constructor is provided for network deserialization.
     */
    public UpdateFlowerPotMsg() {
    }

    /**
     * Creates a message containing a block position and the item's ID and metadata.
     *
     * @param x    the X coordinate of the target block
     * @param y    the Y coordinate of the target block
     * @param z    the Z coordinate of the target block
     * @param item the item to encode into the message (its ID is stored)
     * @param meta the item's metadata value
     */
    public UpdateFlowerPotMsg(int x, int y, int z, Item item, int meta) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.itemId = Item.getIdFromItem(item);
        this.meta = meta;
    }

    /**
     * Deserialize the message's fields from the provided buffer.
     *
     * @param buf the source ByteBuf containing five integers in order: x, y, z, itemId, meta
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        itemId = buf.readInt();
        meta = buf.readInt();
    }

    /**
     * Serializes the message fields into the provided byte buffer.
     *
     * Writes the x, y, z coordinates followed by the item ID and item metadata in that order.
     *
     * @param buf the byte buffer to write the serialized message data into
     */
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

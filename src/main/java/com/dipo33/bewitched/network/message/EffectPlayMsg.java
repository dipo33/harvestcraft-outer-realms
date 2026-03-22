package com.dipo33.bewitched.network.message;

import com.dipo33.bewitched.network.handler.PlaceholderHandler;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;

public class EffectPlayMsg implements IMessage {
    public String effectId;
    public int dimensionId;
    public double x, y, z;

    /**
     * Creates an empty EffectPlayMsg instance for use during network deserialization.
     *
     * Fields are left uninitialized and will be populated by fromBytes(ByteBuf).
     */
    public EffectPlayMsg() {
    }

    /**
     * Constructs a message that requests playing the specified effect at the given coordinates in the provided world's dimension.
     *
     * @param world    the world whose dimension will be used for the message
     * @param effectId the identifier of the effect to play
     * @param x        x-coordinate for the effect's position
     * @param y        y-coordinate for the effect's position
     * @param z        z-coordinate for the effect's position
     */
    public EffectPlayMsg(World world, String effectId, double x, double y, double z) {
        this.dimensionId = world.provider.dimensionId;
        this.effectId = effectId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Deserializes this message's fields from the given byte buffer.
     *
     * Reads the UTF-8 effect identifier, the dimension ID, and the X, Y, Z coordinates from
     * the provided buffer in that order and assigns them to this instance.
     *
     * @param buf the source ByteBuf containing the serialized message data
     */
    @Override
    public void fromBytes(final ByteBuf buf) {
        this.effectId = ByteBufUtils.readUTF8String(buf);
        this.dimensionId = buf.readInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
    }

    /**
     * Serializes the message fields into the provided Netty buffer.
     *
     * Writes the `effectId` as a UTF-8 string, then `dimensionId` as an int, followed by
     * `x`, `y`, and `z` as doubles in that order.
     *
     * @param buf the ByteBuf to write the message data into
     */
    @Override
    public void toBytes(final ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.effectId);
        buf.writeInt(this.dimensionId);
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
    }

    public static class SafeHandler extends PlaceholderHandler<EffectPlayMsg> {
    }
}

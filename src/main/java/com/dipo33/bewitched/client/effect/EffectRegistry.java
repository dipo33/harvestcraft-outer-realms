package com.dipo33.bewitched.client.effect;

import com.dipo33.bewitched.client.effect.entity.EntityMutandisFX;
import com.dipo33.bewitched.network.message.EffectPlayMsg;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EffectRegistry {
    private static final Map<String, IEffect> EFFECTS = new HashMap<>();

    /**
     * Prevents instantiation of this utility class.
     */
    private EffectRegistry() {
    }

    /**
     * Registers an effect implementation under the given identifier, replacing any existing mapping.
     *
     * @param id     the unique string identifier for the effect
     * @param effect the effect implementation to store in the registry
     */
    public static void register(String id, IEffect effect) {
        EFFECTS.put(id, effect);
    }

    /**
     * Plays the client-side visual effect identified by the message at the message's coordinates
     * when the client world exists and its dimension matches the message's dimension.
     *
     * The method looks up the effect by `msg.effectId` and invokes its play routine at `msg.x`, `msg.y`, `msg.z`.
     * If no client world is present, the world's dimension differs, or the effect is not registered, the method does nothing.
     *
     * @param msg message containing the effect id, target dimension, and position (`effectId`, `dimensionId`, `x`, `y`, `z`)
     */
    public static void play(EffectPlayMsg msg) {
        World w = Minecraft.getMinecraft().theWorld;
        if (w == null) {
            return;
        }
        if (w.provider.dimensionId != msg.dimensionId) {
            return;
        }

        IEffect fx = EFFECTS.get(msg.effectId);
        if (fx != null) {
            fx.play(w, msg.x, msg.y, msg.z);
        }
    }

    /**
     * Registers the built-in client effect implementations in the registry.
     *
     * <p>Adds:
     * <ul>
     *   <li>`Effects.MUTANDIS_FX` → a `MutandisFX` using `EntityMutandisFX.Variant.MUTANDIS`</li>
     *   <li>`Effects.MUTANDIS_EXTREMIS_FX` → a `MutandisFX` using `EntityMutandisFX.Variant.MUTANDIS_EXTREMIS`</li>
     * </ul>
     */
    public static void registerEffects() {
        register(Effects.MUTANDIS_FX, new MutandisFX(EntityMutandisFX.Variant.MUTANDIS));
        register(Effects.MUTANDIS_EXTREMIS_FX, new MutandisFX(EntityMutandisFX.Variant.MUTANDIS_EXTREMIS));
    }
}

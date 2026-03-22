package com.dipo33.bewitched.client.effect;

import com.dipo33.bewitched.client.effect.entity.EntityMutandisFX;
import com.dipo33.bewitched.network.message.EffectPlayMsg;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

/**
 * Client-side registry for visual effects.
 */
@SideOnly(Side.CLIENT)
public class EffectRegistry {
    private static final Map<String, IEffect> EFFECTS = new HashMap<>();

    private EffectRegistry() {
    }

    /**
     * Registers an effect by id.
     *
     * @throws IllegalStateException
     *     if effect with {@code id} is already registered
     */
    public static void register(String id, IEffect effect) {
        if (EFFECTS.containsKey(id)) {
            throw new IllegalStateException("Effect with ID '" + id + "' is already registered.");
        }
        EFFECTS.put(id, effect);
    }

    /**
     * Plays the effect determined from {@code msg}.
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
     * Registers built-in effects.
     */
    public static void registerEffects() {
        register(Effects.MUTANDIS_FX, new MutandisFX(EntityMutandisFX.Variant.MUTANDIS));
        register(Effects.MUTANDIS_EXTREMIS_FX, new MutandisFX(EntityMutandisFX.Variant.MUTANDIS_EXTREMIS));
    }
}

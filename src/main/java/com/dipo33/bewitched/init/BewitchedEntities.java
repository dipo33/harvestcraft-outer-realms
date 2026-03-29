package com.dipo33.bewitched.init;

import com.dipo33.bewitched.Bewitched;
import com.dipo33.bewitched.entity.EntityMandrake;

import cpw.mods.fml.common.registry.EntityRegistry;

public class BewitchedEntities {

    public static final String MANDRAKE_ID = "mandragora";

    /**
     * Registers every entity declared in this class with the game's entity registry.
     */
    public static void registerEntities() {
        registerMandrakeEntity();
    }

    /**
     * Register the Mandrake entity with Forge's global and mod registries.
     *
     * Allocates a unique global entity ID, registers EntityMandrake with that ID
     * (including its spawn egg colors), and registers the mod-scoped entity entry
     * with tracking, update frequency, and velocity update settings.
     */
    private static void registerMandrakeEntity() {
        int globalId = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(EntityMandrake.class, MANDRAKE_ID, globalId, 0x5E7C3A, 0xD8C46A);
        EntityRegistry.registerModEntity(EntityMandrake.class, MANDRAKE_ID, 1, Bewitched.instance, 64, 1, true);
    }
}

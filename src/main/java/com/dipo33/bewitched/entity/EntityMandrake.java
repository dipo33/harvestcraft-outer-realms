package com.dipo33.bewitched.entity;

import com.dipo33.bewitched.init.BewitchedItems;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityMandrake extends EntityMob {
    /**
     * Creates a Mandrake entity in the specified world and configures its collision box.
     *
     * @param world the world the entity will exist in
     */
    public EntityMandrake(final World world) {
        super(world);
        this.setSize(0.6F, 1.2F);
    }

    /**
     * Initialize the entity's base attributes.
     *
     * Sets the movement speed attribute's base value to 0.65.
     */
    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.65D);
    }

    /**
     * Sound event name played while the entity is alive.
     *
     * @return the sound event identifier "mob.ghast.scream"
     */
    @Override
    protected String getLivingSound() {
        return "mob.ghast.scream";
    }

    /**
     * Provides the sound event identifier played when the mandrake is hurt.
     *
     * @return the sound event identifier used when this entity is hurt
     */
    @Override
    protected String getHurtSound() {
        return "mob.ghast.scream";
    }

    /**
     * Provides the identifier for the entity's death sound.
     *
     * @return the sound event identifier used when the entity dies, e.g. "mob.ghast.death"
     */
    @Override
    protected String getDeathSound() {
        return "mob.ghast.death";
    }

    /**
     * Determines the primary item dropped by this mandrake entity when it is killed.
     *
     * @return the Mandrake Seeds item to drop
     */
    @Override
    protected Item getDropItem() {
        return BewitchedItems.MANDRAKE_SEEDS.get();
    }

    /**
     * Drops a single mandrake root in addition to the standard drops.
     *
     * @param recentlyHit whether the entity was recently hit by a player (affects loot)
     * @param looting     the looting level applied to the drop calculation
     */
    @Override
    protected void dropFewItems(final boolean recentlyHit, final int looting) {
        super.dropFewItems(recentlyHit, looting);
        this.dropItem(BewitchedItems.MANDRAKE_ROOT.get(), 1);
    }
}

package com.dipo33.bewitched.entity;

import com.dipo33.bewitched.init.BewitchedItems;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class EntityMandrake extends EntityMob {
    public EntityMandrake(final World world) {
        super(world);
        this.setSize(0.6F, 1.2F);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.65D);
    }

    @Override
    protected String getLivingSound() {
        return "mob.ghast.scream";
    }

    @Override
    protected String getHurtSound() {
        return "mob.ghast.scream";
    }

    @Override
    protected String getDeathSound() {
        return "mob.ghast.death";
    }

    @Override
    protected Item getDropItem() {
        return BewitchedItems.MANDRAKE_SEEDS.get();
    }

    @Override
    protected void dropFewItems(final boolean recentlyHit, final int looting) {
        super.dropFewItems(recentlyHit, looting);
        this.dropItem(BewitchedItems.MANDRAKE_ROOT.get(), 1);
    }
}

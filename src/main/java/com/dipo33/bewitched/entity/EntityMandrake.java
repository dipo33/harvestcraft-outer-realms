package com.dipo33.bewitched.entity;

import com.dipo33.bewitched.init.BewitchedItems;

import java.util.List;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

public class EntityMandrake extends EntityMob {

    private static final double SCREAM_RANGE = 8.0D;
    private static final double SCREAM_RANGE_SQ = SCREAM_RANGE * SCREAM_RANGE;
    /** Nausea II for 15 seconds */
    private static final int NAUSEA_DURATION_TICKS = 15 * 20;
    private static final int NAUSEA_AMPLIFIER = 1;

    public EntityMandrake(final World world) {
        super(world);
        this.setSize(0.6F, 1.2F);
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
        if (this.worldObj.isRemote || !this.isEntityAlive()) {
            return;
        }

        AxisAlignedBB box = this.boundingBox.expand(SCREAM_RANGE, SCREAM_RANGE, SCREAM_RANGE);
        List<EntityPlayer> players = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, box);

        for (EntityPlayer player : players) {
            if (!player.isEntityAlive()) {
                continue;
            }
            if (player.capabilities.isCreativeMode) {
                continue;
            }
            if (player.getDistanceSqToEntity(this) > SCREAM_RANGE_SQ) {
                continue;
            }
            ItemStack helmet = player.inventory.armorInventory[3];
            if (helmet != null && helmet.getItem() == BewitchedItems.EARMUFFS.get()) {
                continue;
            }

            PotionEffect nausea = player.getActivePotionEffect(Potion.confusion);
            if (nausea != null && nausea.getAmplifier() >= NAUSEA_AMPLIFIER && nausea.getDuration() > 0) {
                continue;
            }

            player.addPotionEffect(new PotionEffect(Potion.confusion.id, NAUSEA_DURATION_TICKS, NAUSEA_AMPLIFIER));
        }
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

package com.dipo33.bewitched.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public abstract class BewitchedModel extends ModelBiped {

    protected boolean hideBody = true;
    protected boolean hideArms = true;
    protected boolean hideLegs = true;
    protected boolean hideHeadwear = true;

    public BewitchedModel(int textureWidth, int textureHeight) {
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;

        bipedHead.cubeList.clear();
    }

    /**
     * Call this in constructor AFTER model parts are created
     */
    protected void setArmorPartVisibility() {
        bipedHead.showModel = !hideHeadwear;
        bipedHeadwear.showModel = false;
        bipedBody.showModel = !hideBody;
        bipedRightArm.showModel = !hideArms;
        bipedLeftArm.showModel = !hideArms;
        bipedRightLeg.showModel = !hideLegs;
        bipedLeftLeg.showModel = !hideLegs;
    }

    /**
     * Attach helper
     */
    protected void attachToHead(ModelRenderer part) {
        this.bipedHead.addChild(part);
    }

    @Override
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {

        if (par1Entity != null) {
            this.isSneak = par1Entity.isSneaking();
        }

        if (par1Entity instanceof EntityLivingBase) {
            this.heldItemRight = ((EntityLivingBase) par1Entity).getHeldItem() != null ? 1 : 0;
        }

        super.render(par1Entity, par2, par3, par4, par5, par6, par7);
    }
}

package com.seumod.sevtechhelpers.entity;

import com.seumod.sevtechhelpers.entity.ai.EntityAIHarvestCrop;
import net.minecraft.world.World;

public class EntityFarmer extends EntityHelper {
    public EntityFarmer(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAIHarvestCrop(this));
    }
}
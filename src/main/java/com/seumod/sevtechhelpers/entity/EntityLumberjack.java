package com.seumod.sevtechhelpers.entity;

import com.seumod.sevtechhelpers.entity.ai.EntityAIChopTree;
import net.minecraft.world.World;

public class EntityLumberjack extends EntityHelper {
    public EntityLumberjack(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAIChopTree(this));
    }
}
package com.seumod.sevtechhelpers.entity;

import com.seumod.sevtechhelpers.entity.ai.EntityAIMineOre;
import net.minecraft.world.World;

public class EntityMiner extends EntityHelper {
    public EntityMiner(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new EntityAIMineOre(this));
    }
}
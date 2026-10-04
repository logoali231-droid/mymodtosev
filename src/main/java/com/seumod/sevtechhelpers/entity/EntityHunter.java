package com.seumod.sevtechhelpers.entity;

import com.seumod.sevtechhelpers.entity.ai.EntityAIHunt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.world.World;

public class EntityHunter extends EntityHelper {
    public EntityHunter(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        // Caça mobs hostis e animais passivos
        this.targetTasks.addTask(1, new EntityAIHunt(this, EntityMob.class, true));
        this.targetTasks.addTask(2, new EntityAIHunt(this, EntityAnimal.class, true));
    }
}
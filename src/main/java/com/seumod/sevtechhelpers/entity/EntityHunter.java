package com.seumod.sevtechhelpers.entity;

import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
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
        // Ataca corpo a corpo
        this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, true));
        // Retalia quem bate nele
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false));
        // Caça mobs hostis primeiro
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityMob.class, true));
        // Depois animais passivos
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityAnimal.class, true));
    }
}
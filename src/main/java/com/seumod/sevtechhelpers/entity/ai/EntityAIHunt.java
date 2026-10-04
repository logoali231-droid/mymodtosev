package com.seumod.sevtechhelpers.entity.ai;

import com.seumod.sevtechhelpers.entity.EntityHunter;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import java.util.List;

public class EntityAIHunt extends EntityAINearestAttackableTarget {
    private final EntityHunter hunter;

    public EntityAIHunt(EntityHunter hunter, Class<? extends EntityLivingBase> targetClass, boolean shouldCheckSight) {
        super(hunter, targetClass, shouldCheckSight);
        this.hunter = hunter;
    }

    @Override
    public boolean shouldExecute() {
        if (!hunter.isActive()) return false;
        return super.shouldExecute();
    }
}
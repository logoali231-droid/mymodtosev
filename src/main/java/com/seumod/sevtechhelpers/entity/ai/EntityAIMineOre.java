package com.seumod.sevtechhelpers.entity.ai;

import com.seumod.sevtechhelpers.entity.EntityMiner;
import com.seumod.sevtechhelpers.geolosys.GeolosysIntegration;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIMineOre extends EntityAIBase {
    private final EntityMiner entity;
    private BlockPos targetOre;

    public EntityAIMineOre(EntityMiner entity) {
        this.entity = entity;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!entity.isActive()) return false;
        // Usa a API do Geolosys para encontrar o depósito mais próximo
        targetOre = GeolosysIntegration.findNearestOre(entity.world, entity.getPosition(), 32);
        return targetOre != null;
    }

    @Override
    public void startExecuting() {
        entity.getNavigator().tryMoveToXYZ(targetOre.getX(), targetOre.getY(), targetOre.getZ(), 1.0D);
    }

    @Override
    public void updateTask() {
        if (targetOre == null) return;
        if (entity.getDistanceSqToCenter(targetOre) < 4.0D) {
            World world = entity.world;
            IBlockState state = world.getBlockState(targetOre);
            if (GeolosysIntegration.isOre(state)) {
                world.destroyBlock(targetOre, true);
                entity.getInventory().addItem(new ItemStack(state.getBlock(), 1));
            }
            targetOre = null;
        } else if (entity.getNavigator().noPath()) {
            entity.getNavigator().tryMoveToXYZ(targetOre.getX(), targetOre.getY(), targetOre.getZ(), 1.0D);
        }
    }
}
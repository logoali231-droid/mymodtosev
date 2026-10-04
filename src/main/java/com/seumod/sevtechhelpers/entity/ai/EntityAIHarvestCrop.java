package com.seumod.sevtechhelpers.entity.ai;

import com.seumod.sevtechhelpers.entity.EntityFarmer;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityAIHarvestCrop extends EntityAIBase {
    private final EntityFarmer entity;
    private BlockPos targetCrop;
    private int harvestTimer = 0;
    private static final int MAX_HARVEST_TIME = 15;

    public EntityAIHarvestCrop(EntityFarmer entity) {
        this.entity = entity;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!entity.isActive()) return false;
        targetCrop = findMatureCrop(entity.world, entity.getPosition(), 16);
        return targetCrop != null;
    }

    @Override
    public void startExecuting() {
        entity.getNavigator().tryMoveToXYZ(targetCrop.getX(), targetCrop.getY(), targetCrop.getZ(), 1.0D);
    }

    @Override
    public void updateTask() {
        if (targetCrop == null) return;
        double dist = entity.getDistanceSqToCenter(targetCrop);
        if (dist < 4.0D) {
            World world = entity.world;
            IBlockState state = world.getBlockState(targetCrop);
            if (state.getBlock() instanceof BlockCrops) {
                BlockCrops crop = (BlockCrops) state.getBlock();
                if (crop.isMaxAge(state)) {
                    if (harvestTimer < MAX_HARVEST_TIME) {
                        harvestTimer++;
                        return;
                    }
                    harvestTimer = 0;
                    // Colhe e coleta drops
                    world.destroyBlock(targetCrop, true);
                    // Replanta
                    world.setBlockState(targetCrop, crop.getDefaultState());
                    // Guarda sementes se tiver
                    ItemStack seed = findSeed(crop);
                    if (!seed.isEmpty()) {
                        entity.getInventory().addItem(seed);
                    }
                }
            }
            targetCrop = null;
        } else if (entity.getNavigator().noPath()) {
            entity.getNavigator().tryMoveToXYZ(targetCrop.getX(), targetCrop.getY(), targetCrop.getZ(), 1.0D);
        }
    }

    private ItemStack findSeed(BlockCrops crop) {
        if (crop == net.minecraft.init.Blocks.WHEAT) return new ItemStack(Items.WHEAT_SEEDS);
        if (crop == net.minecraft.init.Blocks.CARROTS) return new ItemStack(Items.CARROT);
        if (crop == net.minecraft.init.Blocks.POTATOES) return new ItemStack(Items.POTATO);
        if (crop == net.minecraft.init.Blocks.BEETROOTS) return new ItemStack(Items.BEETROOT_SEEDS);
        return ItemStack.EMPTY;
    }

    private BlockPos findMatureCrop(World world, BlockPos center, int radius) {
        for (BlockPos pos : BlockPos.getAllInBoxMutable(center.add(-radius, -radius, -radius), center.add(radius, radius, radius))) {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof BlockCrops) {
                BlockCrops crop = (BlockCrops) state.getBlock();
                if (crop.isMaxAge(state)) {
                    return pos;
                }
            }
        }
        return null;
    }
}
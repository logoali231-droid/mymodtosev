package com.seumod.sevtechhelpers.entity.ai;

import com.seumod.sevtechhelpers.entity.EntityLumberjack;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.item.Item;   // <- se ainda não tiver

public class EntityAIChopTree extends EntityAIBase {
    private final EntityLumberjack entity;
    private BlockPos targetTree;
    private int chopTimer = 0;
    private static final int MAX_CHOP_TIME = 20; // 1 segundo por bloco

    public EntityAIChopTree(EntityLumberjack entity) {
        this.entity = entity;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (entity.isFull()) return false;
        if (!entity.isActive()) return false;
        targetTree = findTree(entity.world, entity.getPosition(), 16);
        return targetTree != null;
    }

    @Override
    public void startExecuting() {
        entity.getNavigator().tryMoveToXYZ(targetTree.getX(), targetTree.getY(), targetTree.getZ(), 1.0D);
    }

    @Override
    public void updateTask() {

        if (entity.isFull()) {
            targetTree = null; // ou targetOre / targetCrop
            return;
        }
        if (targetTree == null) return;
        double dist = entity.getDistanceSqToCenter(targetTree);
        if (dist < 4.0D) {
            World world = entity.world;
            // Corta a árvore de baixo para cima
            for (int y = 0; y < 30; y++) {
                BlockPos pos = targetTree.up(y);
                IBlockState state = world.getBlockState(pos);
                if (state.getBlock().isWood(world, pos)) {
                    // Simula tempo de corte
                    if (chopTimer < MAX_CHOP_TIME) {
                        chopTimer++;
                        return;
                    }
                    chopTimer = 0;
                    world.destroyBlock(pos, true);
                    // Adiciona ao inventário
                    ItemStack logStack = new ItemStack(state.getBlock(), 1, state.getBlock().getMetaFromState(state));
                    if (!entity.getInventory().addItem(logStack).isEmpty()) {
                        // Inventário cheio: para de cortar
                        targetTree = null;
                        return;
                    }
                } else {
                    break;
                }
            }
            // Replanta uma muda se tiver
            ItemStack sapling = findSapling();
            if (!sapling.isEmpty()) {
                world.setBlockState(targetTree, Blocks.SAPLING.getDefaultState());
                sapling.shrink(1);
            }
            targetTree = null;
        } else if (entity.getNavigator().noPath()) {
            entity.getNavigator().tryMoveToXYZ(targetTree.getX(), targetTree.getY(), targetTree.getZ(), 1.0D);
        }
    }

    private ItemStack findSapling() {
        for (int i = 0; i < entity.getInventory().getSizeInventory(); i++) {
            ItemStack stack = entity.getInventory().getStackInSlot(i);
            String id = stack.getItem().getRegistryName().toString().toLowerCase();
            if (id.contains("sapling")) return stack;
        }
        return ItemStack.EMPTY;
    }
    private BlockPos findTree(World world, BlockPos center, int radius) {
        for (BlockPos pos : BlockPos.getAllInBoxMutable(center.add(-radius, -radius, -radius), center.add(radius, radius, radius))) {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock().isWood(world, pos)) {
                return pos;
            }
        }
        return null;
    }
}
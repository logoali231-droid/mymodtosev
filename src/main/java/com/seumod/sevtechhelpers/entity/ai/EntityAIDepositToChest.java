package com.seumod.sevtechhelpers.entity.ai;

import com.seumod.sevtechhelpers.entity.EntityHelper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class EntityAIDepositToChest extends EntityAIBase {

    private final EntityHelper entity;
    private BlockPos targetChest;
    private int depositTimer = 0;

    public EntityAIDepositToChest(EntityHelper entity) {
        this.entity = entity;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!entity.isActive()) return false;
        if (entity.getMode() != EntityHelper.Mode.DEPOSIT_TO_CHEST) return false;
        if (!entity.isFull()) return false;
        targetChest = findNearbyContainer(entity.world, entity.getPosition(), 24);
        return targetChest != null;
    }

    @Override
    public void startExecuting() {
        entity.getNavigator().tryMoveToXYZ(
                targetChest.getX() + 0.5, targetChest.getY(), targetChest.getZ() + 0.5, 1.0D);
        depositTimer = 0;
    }

    @Override
    public void updateTask() {
        if (targetChest == null) return;
        double dist = entity.getDistanceSq(
                targetChest.getX() + 0.5, targetChest.getY() + 0.5, targetChest.getZ() + 0.5);

        if (dist < 4.0D) {
            if (++depositTimer < 10) return;
            depositTimer = 0;
            if (depositInto(targetChest)) {
                targetChest = null;
            } else {
                // Não conseguiu (baú cheio? sem capability?) -> desiste e procura outro
                targetChest = null;
            }
        } else if (entity.getNavigator().noPath()) {
            entity.getNavigator().tryMoveToXYZ(
                    targetChest.getX() + 0.5, targetChest.getY(), targetChest.getZ() + 0.5, 1.0D);
        }
    }

    private boolean depositInto(BlockPos pos) {
        World world = entity.world;
        TileEntity te = world.getTileEntity(pos);
        if (te == null) return false;
        if (!te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) return false;

        IItemHandler handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        if (handler == null) return false;

        boolean moved = false;
        for (int i = 0; i < entity.getInventory().getSizeInventory(); i++) {
            ItemStack stack = entity.getInventory().getStackInSlot(i);
            if (stack.isEmpty()) continue;

            ItemStack remaining = stack;
            for (int slot = 0; slot < handler.getSlots() && !remaining.isEmpty(); slot++) {
                remaining = handler.insertItem(slot, remaining, false);
            }

            if (remaining.getCount() != stack.getCount()) moved = true;
            entity.getInventory().setInventorySlotContents(i, remaining);
        }
        return moved;
    }

    /** Procura o container mais próximo num cubo ao redor do helper. */
    public static BlockPos findNearbyContainer(World world, BlockPos center, int radius) {
        BlockPos best = null;
        double bestDist = Double.MAX_VALUE;

        for (BlockPos pos : BlockPos.getAllInBoxMutable(
                center.add(-radius, -radius / 2, -radius),
                center.add( radius,  radius / 2,  radius))) {
            TileEntity te = world.getTileEntity(pos);
            if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                double d = center.distanceSq(pos);
                if (d < bestDist) {
                    bestDist = d;
                    best = pos.toImmutable();
                }
            }
        }
        return best;
    }
}
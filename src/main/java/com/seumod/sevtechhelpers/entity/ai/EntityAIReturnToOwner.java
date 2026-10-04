package com.seumod.sevtechhelpers.entity.ai;

import com.seumod.sevtechhelpers.entity.EntityHelper;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class EntityAIReturnToOwner extends EntityAIBase {

    private final EntityHelper entity;
    private int dropTimer = 0;

    public EntityAIReturnToOwner(EntityHelper entity) {
        this.entity = entity;
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        if (!entity.isActive()) return false;
        // Só roda no modo RETURN, ou quando o jogador forçou retorno
        if (entity.getMode() != EntityHelper.Mode.RETURN_TO_OWNER && !entity.isReturning()) return false;
        // Só se estiver cheio, ou se retorno foi forçado
        if (!entity.isFull() && !entity.isReturning()) return false;
        EntityPlayer owner = entity.getOwner();
        return owner != null;
    }

    @Override
    public void startExecuting() {
        EntityPlayer owner = entity.getOwner();
        if (owner != null) {
            entity.getNavigator().tryMoveToEntityLiving(owner, 1.1D);
        }
        dropTimer = 0;
    }

    @Override
    public void updateTask() {
        EntityPlayer owner = entity.getOwner();
        if (owner == null) return;

        double dist = entity.getDistanceSq(owner);
        if (dist < 9.0D) { // até ~3 blocos
            if (++dropTimer < 10) return;
            dropTimer = 0;
            dropAllAt(owner);
            entity.setReturning(false);
        } else if (entity.getNavigator().noPath() ||
                entity.ticksExisted % 20 == 0) {
            entity.getNavigator().tryMoveToEntityLiving(owner, 1.1D);
        }
    }

    private void dropAllAt(EntityPlayer owner) {
        for (int i = 0; i < entity.getInventory().getSizeInventory(); i++) {
            ItemStack stack = entity.getInventory().getStackInSlot(i);
            if (stack.isEmpty()) continue;

            // Tenta dar direto pro inventário do jogador
            boolean added = owner.inventory.addItemStackToInventory(stack.copy());
            if (added) {
                entity.getInventory().setInventorySlotContents(i, ItemStack.EMPTY);
            } else {
                // Joga no chão
                entity.entityDropItem(stack.copy(), 0.0F);
                entity.getInventory().setInventorySlotContents(i, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void resetTask() {
        dropTimer = 0;
    }
}
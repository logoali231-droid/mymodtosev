package com.seumod.sevtechhelpers.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class EntityHelper extends EntityCreature {

    public enum Mode { DEPOSIT_TO_CHEST, RETURN_TO_OWNER }

    private final InventoryBasic inventory = new InventoryBasic("helper", false, 9);
    private boolean active = true;
    private UUID ownerUUID;
    private Mode mode = Mode.DEPOSIT_TO_CHEST;
    private boolean returning = false;   // flag para forçar retorno

    public EntityHelper(World worldIn) {
        super(worldIn);
        this.setSize(0.7F, 0.7F);
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(8, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(10, new EntityAILookIdle(this));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
    }

    // ---------- Inventário ----------
    public InventoryBasic getInventory() { return inventory; }

    /** Retorna true se TODOS os slots estiverem ocupados (sem espaço). */
    public boolean isFull() {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack s = inventory.getStackInSlot(i);
            if (s.isEmpty()) return false;
            if (s.getCount() < s.getMaxStackSize()) return false;
        }
        return true;
    }

    public void clearInventory() {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            inventory.setInventorySlotContents(i, ItemStack.EMPTY);
        }
    }

    // ---------- Estado / Modo ----------
    public boolean isActive() { return active; }
    public void setActive(boolean a) { this.active = a; }

    public Mode getMode() { return mode; }
    public void setMode(Mode m) { this.mode = m; }

    public boolean isReturning() { return returning; }
    public void setReturning(boolean r) { this.returning = r; }

    public void toggleMode() {
        this.mode = (mode == Mode.DEPOSIT_TO_CHEST) ? Mode.RETURN_TO_OWNER : Mode.DEPOSIT_TO_CHEST;
    }

    // ---------- Dono ----------
    public void setOwner(EntityPlayer p) {
        if (p != null) this.ownerUUID = p.getUniqueID();
    }

    @Nullable
    public UUID getOwnerUUID() { return ownerUUID; }

    @Nullable
    public EntityPlayer getOwner() {
        if (ownerUUID == null) return null;
        return world.getPlayerEntityByUUID(ownerUUID);
    }

    // ---------- Interação direita no helper ----------
    @Override
    public boolean processInteract(EntityPlayer player, EnumHand hand) {
        if (!world.isRemote && hand == EnumHand.MAIN_HAND) {
            if (player.isSneaking()) {
                // Shift + clique: força retorno pro dono
                this.returning = true;
                player.sendMessage(new TextComponentString(
                        "\u00a7e" + getName() + " vai voltar pra voc\u00ea."));
            } else {
                this.toggleMode();
                String m = (mode == Mode.DEPOSIT_TO_CHEST)
                        ? "\u00a7aGuarda em ba\u00fais"
                        : "\u00a7bVolta pro dono quando cheio";
                player.sendMessage(new TextComponentString(
                        "\u00a7e" + getName() + ": " + m));
            }
            return true;
        }
        return super.processInteract(player, hand);
    }

    // ---------- NBT ----------
    @Override
    public void writeEntityToNBT(NBTTagCompound c) {
        super.writeEntityToNBT(c);
        c.setBoolean("Active", active);
        c.setBoolean("Returning", returning);
        c.setInteger("Mode", mode.ordinal());
        if (ownerUUID != null) c.setString("Owner", ownerUUID.toString());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound c) {
        super.readEntityFromNBT(c);
        active = c.getBoolean("Active");
        returning = c.getBoolean("Returning");
        int ord = c.getInteger("Mode");
        if (ord >= 0 && ord < Mode.values().length) mode = Mode.values()[ord];
        if (c.hasKey("Owner")) {
            try { ownerUUID = UUID.fromString(c.getString("Owner")); }
            catch (IllegalArgumentException ignored) {}
        }
    }
}
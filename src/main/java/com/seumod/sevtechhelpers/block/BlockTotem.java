package com.seumod.sevtechhelpers.block;

import com.seumod.sevtechhelpers.entity.*;
import com.seumod.sevtechhelpers.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BlockTotem extends Block {
    public BlockTotem() {
        super(Material.WOOD);
        setHardness(2.0F);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state,
                                    EntityPlayer playerIn, EnumHand hand, EnumFacing facing,
                                    float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            ItemStack held = playerIn.getHeldItem(hand);
            EntityHelper helper;

            if (held.getItem() == ModItems.helperWhistle) {
                // Apito na mão: cicla entre os 4 tipos
                if (playerIn.isSneaking()) {
                    // Shift: caçador
                    helper = new EntityHunter(worldIn);
                } else {
                    // Normal: minerador (você pode trocar a ordem)
                    helper = new EntityMiner(worldIn);
                }
            } else if (playerIn.isSneaking()) {
                helper = new EntityFarmer(worldIn);
            } else {
                helper = new EntityLumberjack(worldIn);
            }

            helper.setPosition(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
            helper.setOwner(playerIn);
            worldIn.spawnEntity(helper);

            playerIn.sendMessage(new TextComponentString(
                    "\u00a7aHelper invocado: \u00a7f" + helper.getName()));
        }
        return true;
    }
}
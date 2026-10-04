package com.seumod.sevtechhelpers.block;

import com.seumod.sevtechhelpers.entity.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTotem extends Block {
    public BlockTotem() {
        super(Material.WOOD);
        setHardness(2.0F);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            // Invoca um ajudante com base no item que o jogador está segurando
            EntityHelper helper = null;
            if (playerIn.getHeldItem(hand).getItem() == com.seumod.sevtechhelpers.init.ModItems.helperWhistle) {
                helper = new EntityLumberjack(worldIn);
            } else {
                helper = new EntityMiner(worldIn); // Padrão: minerador
            }
            if (helper != null) {
                helper.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
                worldIn.spawnEntity(helper);
            }
        }
        return true;
    }
}
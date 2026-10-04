package com.seumod.sevtechhelpers.item;

import com.seumod.sevtechhelpers.entity.EntityHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import java.util.List;

public class ItemHelperWhistle extends Item {
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            List<EntityHelper> helpers = worldIn.getEntitiesWithinAABB(EntityHelper.class, playerIn.getEntityBoundingBox().grow(32));
            for (EntityHelper helper : helpers) {
                helper.setActive(!helper.isActive());
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
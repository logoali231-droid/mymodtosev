package com.seumod.sevtechhelpers.item;

import com.seumod.sevtechhelpers.entity.EntityHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import java.util.List;

public class ItemHelperWhistle extends Item {

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (!worldIn.isRemote) {
            List<EntityHelper> helpers = worldIn.getEntitiesWithinAABB(
                    EntityHelper.class, playerIn.getEntityBoundingBox().grow(48));

            if (playerIn.isSneaking()) {
                // Shift + clique: alterna modo de todos
                for (EntityHelper h : helpers) {
                    h.toggleMode();
                }
                playerIn.sendMessage(new TextComponentString(
                        "\u00a7e" + helpers.size() + " helper(s) tiveram o modo alternado."));
            } else {
                // Clique normal: chama todos de volta
                for (EntityHelper h : helpers) {
                    h.setReturning(true);
                }
                playerIn.sendMessage(new TextComponentString(
                        "\u00a7e" + helpers.size() + " helper(s) voltando pra voc\u00ea."));
            }
        }
        return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
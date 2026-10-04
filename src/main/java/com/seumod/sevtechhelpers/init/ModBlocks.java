package com.seumod.sevtechhelpers.init;

import com.seumod.sevtechhelpers.SevTechHelpers;
import com.seumod.sevtechhelpers.block.BlockTotem;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {
    public static Block totem;

    public static void register() {
        totem = new BlockTotem();
        totem.setRegistryName(SevTechHelpers.MODID, "totem");
        totem.setUnlocalizedName(SevTechHelpers.MODID + ".totem");
        GameRegistry.findRegistry(Block.class).register(totem);
        GameRegistry.findRegistry(ItemBlock.class).register(new ItemBlock(totem).setRegistryName(totem.getRegistryName()));
    }
}
package com.seumod.sevtechhelpers.init;

import com.seumod.sevtechhelpers.SevTechHelpers;
import com.seumod.sevtechhelpers.block.BlockTotem;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = SevTechHelpers.MODID)
public class ModBlocks {
    public static Block totem;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        totem = new BlockTotem();
        totem.setRegistryName(SevTechHelpers.MODID, "totem");
        totem.setTranslationKey(SevTechHelpers.MODID + ".totem");  // <- mudou
        event.getRegistry().register(totem);
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        ItemBlock ib = new ItemBlock(totem);
        ib.setRegistryName(totem.getRegistryName());
        ib.setTranslationKey(totem.getTranslationKey());          // <- mudou
        event.getRegistry().register(ib);
    }
}
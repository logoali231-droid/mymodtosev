package com.seumod.sevtechhelpers.init;

import com.seumod.sevtechhelpers.SevTechHelpers;
import com.seumod.sevtechhelpers.item.ItemHelperWhistle;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = SevTechHelpers.MODID)
public class ModItems {
    public static Item helperWhistle;

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        helperWhistle = new ItemHelperWhistle();
        helperWhistle.setRegistryName(SevTechHelpers.MODID, "helper_whistle");
        helperWhistle.setUnlocalizedName(SevTechHelpers.MODID + ".helper_whistle");
        event.getRegistry().register(helperWhistle);
    }
}
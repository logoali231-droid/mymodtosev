package com.seumod.sevtechhelpers.init;

import com.seumod.sevtechhelpers.SevTechHelpers;
import com.seumod.sevtechhelpers.item.ItemHelperWhistle;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {
    public static Item helperWhistle;

    public static void register() {
        helperWhistle = new ItemHelperWhistle();
        helperWhistle.setRegistryName(SevTechHelpers.MODID, "helper_whistle");
        helperWhistle.setUnlocalizedName(SevTechHelpers.MODID + ".helper_whistle");
        GameRegistry.findRegistry(Item.class).register(helperWhistle);
    }
}
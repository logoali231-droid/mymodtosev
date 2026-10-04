package com.seumod.sevtechhelpers;

import com.seumod.sevtechhelpers.init.ModBlocks;
import com.seumod.sevtechhelpers.init.ModEntities;
import com.seumod.sevtechhelpers.init.ModItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = SevTechHelpers.MODID, name = SevTechHelpers.NAME, version = SevTechHelpers.VERSION)
public class SevTechHelpers {
    public static final String MODID = "sevtechhelpers";
    public static final String NAME = "SevTech Helpers";
    public static final String VERSION = "3.0.0";

    @Mod.Instance(MODID)
    public static SevTechHelpers instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModEntities.registerEntities();
        ModBlocks.register();
        ModItems.register();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Registro de receitas pode ser feito aqui ou via JSON
    }
}
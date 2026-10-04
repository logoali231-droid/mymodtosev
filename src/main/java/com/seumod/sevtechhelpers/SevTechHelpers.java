package com.seumod.sevtechhelpers;

import com.seumod.sevtechhelpers.init.ModEntities;
import com.seumod.sevtechhelpers.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = SevTechHelpers.MODID, name = SevTechHelpers.NAME, version = SevTechHelpers.VERSION)
public class SevTechHelpers {

    public static final String MODID   = "sevtechhelpers";
    public static final String NAME    = "SevTech Helpers";
    public static final String VERSION = "3.0.0";

    @Mod.Instance(MODID)
    public static SevTechHelpers instance;

    @SidedProxy(
            clientSide = "com.seumod.sevtechhelpers.proxy.ClientProxy",
            serverSide = "com.seumod.sevtechhelpers.proxy.CommonProxy"
    )
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ModEntities.registerEntities();
        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }
}
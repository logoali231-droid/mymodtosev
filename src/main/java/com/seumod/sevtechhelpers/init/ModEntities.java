package com.seumod.sevtechhelpers.init;

import com.seumod.sevtechhelpers.SevTechHelpers;
import com.seumod.sevtechhelpers.entity.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities {
    public static void registerEntities() {
        int id = 0;
        EntityRegistry.registerModEntity(new ResourceLocation(SevTechHelpers.MODID, "lumberjack"), EntityLumberjack.class, "lumberjack", id++, SevTechHelpers.instance, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(SevTechHelpers.MODID, "miner"), EntityMiner.class, "miner", id++, SevTechHelpers.instance, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(SevTechHelpers.MODID, "farmer"), EntityFarmer.class, "farmer", id++, SevTechHelpers.instance, 64, 1, true);
        EntityRegistry.registerModEntity(new ResourceLocation(SevTechHelpers.MODID, "hunter"), EntityHunter.class, "hunter", id++, SevTechHelpers.instance, 64, 1, true);
    }
}
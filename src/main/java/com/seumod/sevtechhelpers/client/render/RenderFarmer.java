package com.seumod.sevtechhelpers.client.render;

import com.seumod.sevtechhelpers.SevTechHelpers;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderFarmer extends RenderHelper {
    private static final ResourceLocation TEX =
            new ResourceLocation(SevTechHelpers.MODID, "textures/entity/farmer.png");

    public RenderFarmer(RenderManager rm) {
        super(rm, TEX);
    }
}
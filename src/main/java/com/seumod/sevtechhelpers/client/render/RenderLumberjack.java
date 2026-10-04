package com.seumod.sevtechhelpers.client.render;

import com.seumod.sevtechhelpers.SevTechHelpers;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderLumberjack extends RenderHelper {
    private static final ResourceLocation TEX =
            new ResourceLocation(SevTechHelpers.MODID, "textures/entity/lumberjack.png");

    public RenderLumberjack(RenderManager rm) {
        super(rm, TEX);
    }
}
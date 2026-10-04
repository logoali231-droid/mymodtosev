package com.seumod.sevtechhelpers.client.render;

import com.seumod.sevtechhelpers.entity.EntityHelper;
import net.minecraft.client.model.ModelSlime;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

public class RenderHelper extends RenderLiving<EntityHelper> {

    private final ResourceLocation texture;

    public RenderHelper(RenderManager rm, ResourceLocation texture) {
        super(rm, new ModelSlime(16), 0.25F);
        this.texture = texture;
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityHelper entity) {
        return texture;
    }

    @Override
    protected void preRenderCallback(EntityHelper entity, float partialTicks) {
        // ModelSlime renderiza 8x8x8 px = ~0.5 bloco. Escala pra ~1 bloco.
        float scale = 1.6F;
        GlStateManager.scale(scale, scale, scale);
        GlStateManager.translate(0.0F, -0.1F, 0.0F);
    }

    @Override
    public void doRender(EntityHelper entity, double x, double y, double z,
                         float entityYaw, float partialTicks) {
        // Compensa a hitbox baixinha (0.7F) do helper
        super.doRender(entity, x, y - 0.15D, z, entityYaw, partialTicks);
    }
}
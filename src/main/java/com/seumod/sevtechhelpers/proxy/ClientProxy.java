package com.seumod.sevtechhelpers.proxy;

import com.seumod.sevtechhelpers.client.render.*;
import com.seumod.sevtechhelpers.entity.*;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {
        RenderingRegistry.registerEntityRenderingHandler(
                EntityLumberjack.class, RenderLumberjack::new);
        RenderingRegistry.registerEntityRenderingHandler(
                EntityMiner.class, RenderMiner::new);
        RenderingRegistry.registerEntityRenderingHandler(
                EntityFarmer.class, RenderFarmer::new);
        RenderingRegistry.registerEntityRenderingHandler(
                EntityHunter.class, RenderHunter::new);
    }
}
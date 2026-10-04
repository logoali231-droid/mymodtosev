package com.seumod.sevtechhelpers.geolosys;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GeolosysIntegration {

    /**
     * Verifica se um bloco é um minério, usando nome de registro como fallback
     * (compatível com Geolosys e outros mods de minério do SevTech).
     */
    public static boolean isOre(IBlockState state) {
        Block block = state.getBlock();
        ResourceLocation registryName = block.getRegistryName();
        if (registryName == null) return false;
        String name = registryName.toString().toLowerCase();
        // Padrões de minério do SevTech (Geolosys, Primal Core, etc.)
        return name.contains("ore") || name.contains("geolosys") || name.contains("cluster")
                || name.contains("sample") || name.contains("deposit");
    }

    /**
     * Encontra o minério mais próximo num raio, usando uma varredura local.
     */
    public static BlockPos findNearestOre(World world, BlockPos center, int radius) {
        for (BlockPos pos : BlockPos.getAllInBoxMutable(center.add(-radius, -radius, -radius), center.add(radius, radius, radius))) {
            IBlockState state = world.getBlockState(pos);
            if (isOre(state)) {
                return pos;
            }
        }
        return null;
    }
}
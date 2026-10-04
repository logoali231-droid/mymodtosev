package com.seumod.sevtechhelpers.geolosys;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GeolosysIntegration {

    /** Detecta minério por nome de registro (compatível com Geolosys, Primal Core, IE, etc). */
    public static boolean isOre(IBlockState state) {
        Block block = state.getBlock();
        ResourceLocation rn = block.getRegistryName();
        if (rn == null) return false;
        String name = rn.toString().toLowerCase();
        return name.contains("ore")
                || name.contains("geolosys")
                || name.contains("cluster")
                || name.contains("sample")
                || name.contains("deposit")
                || name.contains("poor_")
                || name.contains("dense_");
    }

    /** Varredura local em raio. Retorna BlockPos imutável (cópia segura). */
    public static BlockPos findNearestOre(World world, BlockPos center, int radius) {
        for (BlockPos pos : BlockPos.getAllInBoxMutable(
                center.add(-radius, -radius, -radius),
                center.add( radius,  radius,  radius))) {

            IBlockState state = world.getBlockState(pos);
            if (isOre(state)) {
                return pos.toImmutable(); // <-- CORREÇÃO CRÍTICA
            }
        }
        return null;
    }
}
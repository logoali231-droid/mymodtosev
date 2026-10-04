package com.seumod.sevtechhelpers.geolosys;

import com.oitsjustjose.geolosys.common.api.GeolosysAPI;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GeolosysIntegration {
    /**
     * Verifica se um bloco é um minério do Geolosys.
     */
    public static boolean isOre(IBlockState state) {
        return GeolosysAPI.isOre(state.getBlock());
    }

    /**
     * Encontra o minério mais próximo.
     * A API do Geolosys pode ter um método para obter depósitos.
     * Aqui usamos uma varredura com o filtro do Geolosys.
     */
    public static BlockPos findNearestOre(World world, BlockPos center, int radius) {
        // Tenta usar a API para obter depósitos (se disponível)
        // Se não, faz uma varredura filtrada
        for (BlockPos pos : BlockPos.getAllInBoxMutable(center.add(-radius, -radius, -radius), center.add(radius, radius, radius))) {
            IBlockState state = world.getBlockState(pos);
            if (isOre(state)) {
                return pos;
            }
        }
        return null;
    }
}
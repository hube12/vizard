package com.seedfinding.neil.mixin.structures.endcities;

import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.EndCityFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EndCityFeature.class)
public interface EndCityAccessor { // needs to be an interface
    @Invoker("getGenerationHeight") // read the docs ;) apparently you can pass as argument the name of the target method to name it other than invoke...
    static int getGenerationHeight(int chunkX, int chunkZ, ChunkGenerator chunkGenerator) {
        throw new AbstractMethodError("Invoker");
    }
}

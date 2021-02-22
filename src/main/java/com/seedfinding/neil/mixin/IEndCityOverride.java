package com.seedfinding.neil.mixin;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.EndCityFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(EndCityFeature.class)
public interface IEndCityOverride { // needs to be an interface
    @Invoker("getGenerationHeight") // read the docs ;) apparently you can pass as argument the name of the target method to name it other than invoke...
    static int getGenerationHeight(int chunkX, int chunkZ, ChunkGenerator chunkGenerator) {
        throw new AbstractMethodError("Invoker");
    }
}

package com.seedfinding.neil.mixin.structures.endcities;

import net.minecraft.structure.EndCityGenerator;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.EndCityFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(EndCityFeature.Start.class)
public class MixinEndCityStart extends MixinStructureStart {
    @Inject(at=@At("HEAD"),method = "init", cancellable = true)
    public void init(DynamicRegistryManager dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager structureManager, int i, int j, Biome biome, DefaultFeatureConfig defaultFeatureConfig, CallbackInfo ci) {
        ci.cancel();
        BlockRotation blockRotation = BlockRotation.random(this.random);
        int k = EndCityAccessor.getGenerationHeight(i, j, chunkGenerator);
        if (k >= 0) {
            BlockPos blockPos = new BlockPos(i * 16 + 8, k, j * 16 + 8);
            EndCityGenerator.addPieces(structureManager, blockPos, blockRotation,this.children , this.random);
            this.setBoundingBoxFromChildren();
        }
    }

}

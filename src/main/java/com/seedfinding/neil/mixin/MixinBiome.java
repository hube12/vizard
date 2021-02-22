package com.seedfinding.neil.mixin;

import net.minecraft.structure.StructureStart;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(Biome.class)
public class MixinBiome {
    @Final
    @Shadow
    private GenerationSettings generationSettings;

    @Final
    @Shadow
    private Map<Integer, List<StructureFeature<?>>> structures;

    @Inject(at=@At("HEAD"),method = "generateFeatureStep(Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Lnet/minecraft/world/ChunkRegion;JLnet/minecraft/world/gen/ChunkRandom;Lnet/minecraft/util/math/BlockPos;)V", cancellable = true)
    public void generateFeatureStep(StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, ChunkRegion region, long populationSeed, ChunkRandom random, BlockPos pos, CallbackInfo ci) {
        ci.cancel();
        List<List<Supplier<ConfiguredFeature<?, ?>>>> list = this.generationSettings.getFeatures();
        int i = GenerationStep.Feature.values().length;

        for(int j = 0; j < i; ++j) {
            int k = 0;
            if (structureAccessor.shouldGenerateStructures()) {
                List<StructureFeature<?>> list2 = (List)this.structures.getOrDefault(j, Collections.emptyList());

                for(Iterator var13 = list2.iterator(); var13.hasNext(); ++k) {
                    StructureFeature<?> structureFeature = (StructureFeature)var13.next();
                    random.setDecoratorSeed(populationSeed, k, j);
                    int l = pos.getX() >> 4;
                    int m = pos.getZ() >> 4;
                    int n = l << 4;
                    int o = m << 4;

                    try {
                       Stream<? extends StructureStart<?>> structureStarts= structureAccessor.getStructuresWithChildren(ChunkSectionPos.from(pos), structureFeature);
                        for (StructureStart<?> structureStart:structureStarts.collect(Collectors.toList())){
                            System.out.println(structureStart.getFeature().getName());
                            structureStart.generateStructure(region, structureAccessor, chunkGenerator, random, new BlockBox(n, o, n + 15, o + 15), new ChunkPos(l, m));
                        }


                    } catch (Exception var21) {
                        CrashReport crashReport = CrashReport.create(var21, "Feature placement");
                        crashReport.addElement("Feature").add("Id", Registry.STRUCTURE_FEATURE.getId(structureFeature)).add("Description", () -> {
                            return structureFeature.toString();
                        });
                        throw new CrashException(crashReport);
                    }
                }
            }

            if (list.size() > j) {
                for(Iterator var23 = ((List)list.get(j)).iterator(); var23.hasNext(); ++k) {
                    Supplier<ConfiguredFeature<?, ?>> supplier = (Supplier)var23.next();
                    ConfiguredFeature<?, ?> configuredFeature = (ConfiguredFeature)supplier.get();
                    random.setDecoratorSeed(populationSeed, k, j);

                    try {
                        configuredFeature.generate(region, chunkGenerator, random, pos);
                    } catch (Exception var22) {
                        CrashReport crashReport2 = CrashReport.create(var22, "Feature placement");
                        crashReport2.addElement("Feature").add("Id", Registry.FEATURE.getId(configuredFeature.feature)).add("Config", configuredFeature.config).add("Description", () -> {
                            return configuredFeature.feature.toString();
                        });
                        throw new CrashException(crashReport2);
                    }
                }
            }
        }

    }
}

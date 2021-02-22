package com.seedfinding.neil.mixin;

import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.StructureHolder;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Mixin(StructureAccessor.class)
public class MixinStructureAccessor {
    @Final
    @Shadow
    private WorldAccess world;

    @Inject(at = @At("HEAD"), method = "getStructuresWithChildren(Lnet/minecraft/util/math/ChunkSectionPos;Lnet/minecraft/world/gen/feature/StructureFeature;)Ljava/util/stream/Stream;", cancellable = true)
    public void getStructuresWithChildren(ChunkSectionPos pos, StructureFeature<?> feature, CallbackInfoReturnable<Stream<? extends StructureStart<?>>> cir) {

        cir.cancel();
//        Stream<ChunkSectionPos> temp = this.world
//                .getChunk(pos.getSectionX(), pos.getSectionZ(), ChunkStatus.STRUCTURE_REFERENCES)
//                .getStructureReferences(feature).stream().map((posx) ->
//                        ChunkSectionPos.from(new ChunkPos(posx), 0));
//        Object[] ree = temp.toArray();
//        if (ree.length > 0) {
//            System.out.println(Arrays.toString(ree));
//        }
        Stream<? extends StructureStart<?>> res = this.world.getChunk(pos.getSectionX(), pos.getSectionZ(), ChunkStatus.STRUCTURE_REFERENCES).
                getStructureReferences(feature).stream().map((posx) -> ChunkSectionPos.from(new ChunkPos(posx), 0)).
                map((posx) ->
                        this.getStructureStart(posx, feature,
                                this.world.getChunk(posx.getSectionX(), posx.getSectionZ(), ChunkStatus.STRUCTURE_STARTS)));
        Object[] reee = res.toArray();
        if (reee.length > 0) {
            System.out.println(Arrays.toString(reee));
        }
        Stream<? extends StructureStart<?>> structureStartStream=this.world.getChunk(pos.getSectionX(), pos.getSectionZ(), ChunkStatus.STRUCTURE_REFERENCES).getStructureReferences(feature).stream().map((posx) -> ChunkSectionPos.from(new ChunkPos(posx), 0)).map((posx) -> this.getStructureStart(posx, feature, this.world.getChunk(posx.getSectionX(), posx.getSectionZ(), ChunkStatus.STRUCTURE_STARTS)));

        cir.setReturnValue(structureStartStream);

    }

    @Shadow
    public StructureStart<?> getStructureStart(ChunkSectionPos pos, StructureFeature<?> feature, StructureHolder holder) {
        throw new AbstractMethodError("SHADOW");
    }
}

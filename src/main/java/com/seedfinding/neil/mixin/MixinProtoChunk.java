package com.seedfinding.neil.mixin;

import net.minecraft.structure.StructureStart;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

@Mixin(ProtoChunk.class)
public class MixinProtoChunk {
    @Shadow @Final
    private Map<StructureFeature<?>, StructureStart<?>> structureStarts;

    @Inject(at = @At("HEAD"),method = "getStructureStarts()Ljava/util/Map;", cancellable = true)
    public void getStructureStarts(CallbackInfoReturnable<Map<StructureFeature<?>, StructureStart<?>>> cir) {
        cir.cancel();
        //System.out.println(Arrays.toString(this.structureStarts.values().toArray()));
        cir.setReturnValue(Collections.unmodifiableMap(this.structureStarts));
    }
}

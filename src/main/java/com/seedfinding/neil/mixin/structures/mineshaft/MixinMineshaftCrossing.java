package com.seedfinding.neil.mixin.structures.mineshaft;

import com.seedfinding.neil.mixin.structures.StructurePieceAccessor;
import net.minecraft.block.Blocks;
import net.minecraft.structure.MineshaftGenerator;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.StructureWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MineshaftGenerator.MineshaftCrossing.class)
public class MixinMineshaftCrossing extends MixinAbstractMineshaftPart {
    @Inject(at = @At("HEAD"), method = "generateCrossingPilliar(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/util/math/BlockBox;IIII)V", cancellable = true)
    private void generateCrossingPillar(StructureWorldAccess structureWorldAccess, BlockBox boundingBox, int x, int minY, int z, int maxY, CallbackInfo ci) {
        ci.cancel();
        ((StructurePieceAccessor) this).invokeFillWithOutline(structureWorldAccess, boundingBox, x, minY, z, x, maxY, z, this.getPlanksType(), Blocks.CAVE_AIR.getDefaultState(), false);
    }
}

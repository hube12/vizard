package com.seedfinding.neil.mixin.structures;

import net.minecraft.block.BlockState;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.StructureWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;


@Mixin(StructurePiece.class)
public class MixinStructurePiece {
    @ModifyVariable(method = "fillWithOutline(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/util/math/BlockBox;IIIIIILnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Z)V", ordinal = 0, at = @At(value = "HEAD"), argsOnly = true)
    private boolean adjustOutline(boolean cantReplaceAir) {
        return false;
    }

    @ModifyVariable(method = "fillWithOutline(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/util/math/BlockBox;IIIIIIZLjava/util/Random;Lnet/minecraft/structure/StructurePiece$BlockRandomizer;)V", ordinal = 0, at = @At(value = "HEAD"), argsOnly = true)
    private boolean adjustOutlineRandom(boolean cantReplaceAir) {
        return false;
    }

    @ModifyVariable(method = "fillWithOutlineUnderSeaLevel", ordinal = 0, at = @At(value = "HEAD"), argsOnly = true)
    private boolean adjustOutlineSea1(boolean cantReplaceAir) {
        return false;
    }

    @ModifyVariable(method = "fillWithOutlineUnderSeaLevel", ordinal = 1, at = @At(value = "HEAD"), argsOnly = true)
    private boolean adjustOutlineSea2(boolean stayBelowSeaLevel) {
        return false;
    }

    @ModifyVariable(method = "fillWithOutlineUnderSeaLevel", ordinal = 0, at = @At(value = "HEAD"), argsOnly = true)
    private float adjustOutlineSeaChance(float blockChance) {
        return 1.0F;
    }

    @Inject(at = @At("HEAD"), method = "fillWithOutline(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/util/math/BlockBox;IIIIIIZLjava/util/Random;Lnet/minecraft/structure/StructurePiece$BlockRandomizer;)V", cancellable = true)
    protected void fillWithOutline(StructureWorldAccess structureWorldAccess, BlockBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, boolean cantReplaceAir, Random random, StructurePiece.BlockRandomizer blockRandomizer, CallbackInfo ci) {
        // remove the old method ;)
        ci.cancel();
        for (int i = minY; i <= maxY; ++i) {
            for (int j = minX; j <= maxX; ++j) {
                for (int k = minZ; k <= maxZ; ++k) {
                    blockRandomizer.setBlock(random, j, i, k, i == minY || j == minX || j == maxX || k == minZ || k == maxZ); // i == maxY || REMOVE TOP
                    ((StructurePieceAccessor) this).invokeAddBlock(structureWorldAccess, blockRandomizer.getBlock(), j, i, k, blockBox);
                }
            }
        }

    }

    @Inject(at = @At("HEAD"), method = "fillWithOutline(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/util/math/BlockBox;IIIIIILnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Z)V", cancellable = true)
    protected void fillWithOutline(StructureWorldAccess structureWorldAccess, BlockBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState outline, BlockState inside, boolean cantReplaceAir, CallbackInfo ci) {
        // remove the old method ;)
        ci.cancel();
        for (int i = minY; i <= maxY; ++i) {
            for (int j = minX; j <= maxX; ++j) {
                for (int k = minZ; k <= maxZ; ++k) {
                    if (i != minY && i != maxY && j != minX && j != maxX && k != minZ && k != maxZ) { // see what remove top
                        ((StructurePieceAccessor) this).invokeAddBlock(structureWorldAccess, inside, j, i, k, blockBox);
                    } else {
                        ((StructurePieceAccessor) this).invokeAddBlock(structureWorldAccess, outline, j, i, k, blockBox);
                    }
                }
            }
        }

    }

    @Inject(method = "isUnderSeaLevel", at = @At("RETURN"), cancellable = true)
    private void isUnderSeaLevel(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }   
}

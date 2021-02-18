package com.seedfinding.neil.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.StructureWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;


@Mixin(StructurePiece.class)
public class MakeStructureAirLess {
    @ModifyVariable(method = "fillWithOutline(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/util/math/BlockBox;IIIIIILnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;Z)V", ordinal = 0, at = @At(value = "HEAD"),argsOnly = true)
    private boolean adjustOutline(boolean cantReplaceAir) {
        return false;
    }

    @ModifyVariable(method = "fillWithOutline(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/util/math/BlockBox;IIIIIIZLjava/util/Random;Lnet/minecraft/structure/StructurePiece$BlockRandomizer;)V", ordinal = 0, at = @At(value = "HEAD"),argsOnly = true)
    private boolean adjustOutlineRandom(boolean cantReplaceAir) {
        return false;
    }

    @ModifyVariable(method = "fillWithOutlineUnderSeaLevel", ordinal = 0, at = @At(value = "HEAD"),argsOnly = true)
    private boolean adjustOutlineSea1(boolean cantReplaceAir) {
        return false;
    }

    @ModifyVariable(method = "fillWithOutlineUnderSeaLevel", ordinal = 1, at = @At(value = "HEAD"),argsOnly = true)
    private boolean adjustOutlineSea2(boolean stayBelowSeaLevel) {
        return false;
    }

    @ModifyVariable(method = "fillWithOutlineUnderSeaLevel", ordinal = 0, at = @At(value = "HEAD"),argsOnly = true)
    private float adjustOutlineSeaChance(float blockChance) {
        return 1.0F;
    }
}

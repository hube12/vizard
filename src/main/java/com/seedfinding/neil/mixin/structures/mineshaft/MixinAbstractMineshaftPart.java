package com.seedfinding.neil.mixin.structures.mineshaft;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = {"net.minecraft.structure.MineshaftGenerator$MineshaftPart"})
public abstract class MixinAbstractMineshaftPart {
    @Shadow
    protected BlockState getPlanksType() {
        throw new AbstractMethodError("SHADOW");
    }

    @Inject(at=@At("RETURN"),method = "isSolidCeiling(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockBox;IIII)Z", cancellable = true)
    protected void isSolidCeiling(BlockView blockView, BlockBox boundingBox, int minX, int maxX, int y, int z, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}

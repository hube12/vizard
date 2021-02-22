package com.seedfinding.neil.mixin.structures.mineshaft;

import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = {"net.minecraft.structure.MineshaftGenerator$MineshaftPart"})
public abstract class MixinAbstractMineshaftPart {
    @Shadow
    protected BlockState getPlanksType() {
        throw new AbstractMethodError("SHADOW");
    }
}

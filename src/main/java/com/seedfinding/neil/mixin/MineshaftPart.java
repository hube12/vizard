package com.seedfinding.neil.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.structure.MineshaftGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = {"net.minecraft.structure.MineshaftGenerator$MineshaftPart"})
public abstract class MineshaftPart {
    @Shadow
    protected BlockState getPlanksType() {
        throw new AbstractMethodError("SHADOW");
    }
}

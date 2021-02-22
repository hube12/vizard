package com.seedfinding.neil.mixin;

import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.world.gen.ChunkRandom;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(StructureStart.class)
public abstract class StructureOverride {
    @Final
    @Shadow
    protected List<StructurePiece> children;

    @Final
    @Shadow
    protected ChunkRandom random;

    @Shadow
    protected void setBoundingBoxFromChildren(){}
}

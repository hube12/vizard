package com.seedfinding.neil.mixin.structures;

import net.minecraft.block.BlockState;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.BlockView;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StructurePiece.class)
public interface StructurePieceAccessor {
    @Invoker("getBlockAt")
    BlockState getBlockAt(BlockView blockView, int x, int y, int z, BlockBox blockBox);

    @Invoker("fillWithOutline")
    void fillWithOutline(StructureWorldAccess structureWorldAccess, BlockBox blockBox, int minX, int minY, int minZ, int maxX, int maxY, int maxZ, BlockState outline, BlockState inside, boolean cantReplaceAir);

    @Invoker("addBlock")
    void addBlock(StructureWorldAccess structureWorldAccess, BlockState block, int x, int y, int z, BlockBox blockBox);

}
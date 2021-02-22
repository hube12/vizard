package com.seedfinding.neil.mixin.structures.stronghold;

import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChunkGenerator.class)
public class MixinChunkGenerator {
	@Shadow
	@Mutable
	@Final
	private List<ChunkPos> strongholds;
	@Inject(at=@At("RETURN"),method = "generateStrongholdPositions()V")
	private void generateStrongholdPositions(CallbackInfo ci){
		if (this.strongholds.stream().noneMatch(c->c.x==0 && c.z==0)) {
			this.strongholds.add(new ChunkPos(0,0));
			System.out.printf("We have now %d strongholds%n", this.strongholds.size());
		}
	}
}

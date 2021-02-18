package com.seedfinding.neil.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class SetPlayPos00 {
	@Inject(at=@At("HEAD"),method = "setupSpawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/level/ServerWorldProperties;ZZZ)V", cancellable = true)
	private static void setupSpawn(ServerWorld world, ServerWorldProperties serverWorldProperties, boolean bonusChest, boolean debugWorld, boolean bl,CallbackInfo ci){
		ChunkGenerator chunkGenerator = world.getChunkManager().getChunkGenerator();
		serverWorldProperties.setSpawnPos(BlockPos.ORIGIN.up(chunkGenerator.getSpawnHeight()), 0.0F);
		System.out.printf("Player pos was set at %d %d %d%n",serverWorldProperties.getSpawnX(),serverWorldProperties.getSpawnY(),serverWorldProperties.getSpawnZ());
		ci.cancel();
	}
}

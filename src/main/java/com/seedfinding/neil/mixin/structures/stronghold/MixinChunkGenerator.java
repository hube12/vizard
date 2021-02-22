package com.seedfinding.neil.mixin.structures.stronghold;

import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StrongholdFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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


	@Inject(at=@At("HEAD"),method = "addStructureReferences(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/chunk/Chunk;)V", cancellable = true)
	public void addStructureReferences(StructureWorldAccess world, StructureAccessor accessor, Chunk chunk, CallbackInfo ci) {
		ci.cancel();
		boolean i = true;
		int chunkX = chunk.getPos().x;
		int chunkZ = chunk.getPos().z;
		int posX = chunkX << 4;
		int posZ = chunkZ << 4;
		ChunkSectionPos chunkSectionPos = ChunkSectionPos.from(chunk.getPos(), 0);

		for(int cx = chunkX - 8; cx <= chunkX + 8; ++cx) {
			for(int cz = chunkZ - 8; cz <= chunkZ + 8; ++cz) {
				boolean printy=false;
				long p = ChunkPos.toLong(cx, cz);
				Chunk chunk1= world.getChunk(cx, cz);
				Map<StructureFeature<?>, StructureStart<?>> structureStartMap= chunk1.getStructureStarts();
				Object[] map=structureStartMap.values().toArray();
				if (map.length>0 && Arrays.stream(map).anyMatch(e->e instanceof StrongholdFeature.Start)){
					printy=true;
				}

				Iterator var14 =structureStartMap.values().iterator();

				while(var14.hasNext()) {
					StructureStart structureStart = (StructureStart)var14.next();

					try {
//						if (printy){
//							System.out.println((structureStart != StructureStart.DEFAULT)+" "+structureStart.getBoundingBox().intersectsXZ(posX, posZ, posX + 15, posZ + 15));
//							System.out.println(cx+" "+cz+" "+chunkX+" "+chunkZ);
//							System.out.println(Arrays.toString(map));
//							System.out.println(chunk1.getClass().toString());
//							System.out.println(chunk1.getClass().toString());
//						}
						if (structureStart != StructureStart.DEFAULT && structureStart.getBoundingBox().intersectsXZ(posX, posZ, posX + 15, posZ + 15)) {
							accessor.addStructureReference(chunkSectionPos, structureStart.getFeature(), p, chunk);
							DebugInfoSender.sendStructureStart(world, structureStart);
						}
					} catch (Exception var19) {
						CrashReport crashReport = CrashReport.create(var19, "Generating structure reference");
						CrashReportSection crashReportSection = crashReport.addElement("Structure");
						crashReportSection.add("Id", () -> {
							return Registry.STRUCTURE_FEATURE.getId(structureStart.getFeature()).toString();
						});
						crashReportSection.add("Name", () -> {
							return structureStart.getFeature().getName();
						});
						crashReportSection.add("Class", () -> {
							return structureStart.getFeature().getClass().getCanonicalName();
						});
						throw new CrashException(crashReport);
					}
				}
			}
		}

	}
}

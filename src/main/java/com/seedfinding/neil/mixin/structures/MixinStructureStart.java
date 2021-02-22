package com.seedfinding.neil.mixin.structures;


import com.seedfinding.neil.Instance;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

@Mixin(StructureStart.class)
public class MixinStructureStart {
    private static int counter = 0;
    @Final
    @Shadow
    protected List<StructurePiece> children;

    @Inject(at = @At("HEAD"), method = "generateStructure", cancellable = true)
    public void threadIt(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox box, ChunkPos chunkPos, CallbackInfo ci) {
        // cancel everything in that method (giving us complete control over it)
        ci.cancel();
        if (!this.children.isEmpty()) {
            TransferQueue<String> transferQueue = new LinkedTransferQueue<>();
            Thread thread = new Thread(() -> {
                BlockBox blockBox = this.children.get(0).getBoundingBox();
                Vec3i vec3i = blockBox.getCenter();
                BlockPos blockPos = new BlockPos(vec3i.getX(), blockBox.minY, vec3i.getZ());
                Iterator<StructurePiece> iterator = this.children.iterator();
                MinecraftServer server = world.toServerWorld().getServer();
                skip:
                while (iterator.hasNext()) {
                    try {
                        String msg = transferQueue.take();
                        switch (msg) {
                            case "go":
                                // just keep going
                                break;
                            case "skip":
                                iterator.remove();
                                continue skip;
                            case "kill":
                                iterator.forEachRemaining(e -> {});
                                continue skip;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    StructurePiece structurePiece = iterator.next();
                    CompletableFuture<Void> future = server.submit(
                            () -> {
                                if (structurePiece.getBoundingBox().intersects(box) && !structurePiece.generate(world, structureAccessor, chunkGenerator, random, box, chunkPos, blockPos)) {
                                    iterator.remove();
                                }
                                ProtoChunk chunk = (ProtoChunk) world.getChunk(chunkPos.x, chunkPos.z);
                                chunk.setShouldSave(true);
                                ((ServerChunkManager) world.getChunkManager()).threadedAnvilChunkStorage.
                                        getPlayersWatchingChunk(chunkPos, false).
                                        forEach(s -> s.networkHandler.sendPacket(new ChunkDataS2CPacket(new WorldChunk(world.toServerWorld(), chunk), 65535)));
                            }
                    );
                    future.join();
                    while (!future.isDone()) {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                this.setBoundingBoxFromChildren();
            }, "STRUCTURE THREAD + " + counter++);


            Instance.genController.register(thread, transferQueue);
            thread.start();


        }
    }

    @Shadow
    protected void setBoundingBoxFromChildren() {}

    @Final
    @Shadow
    protected ChunkRandom random;

}

package com.seedfinding.neil.mixin;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.seedfinding.neil.command.ClientCommand;
import com.seedfinding.neil.init.ClientCommands;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.command.CommandSource;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {

    @Shadow private ClientWorld world;
    @Shadow private CommandDispatcher<CommandSource> commandDispatcher;


    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(MinecraftClient mc, Screen screen, ClientConnection connection, GameProfile profile, CallbackInfo ci) {
        ClientCommands.registerCommands((CommandDispatcher<ServerCommandSource>)(Object)this.commandDispatcher);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "onCommandTree", at = @At("TAIL"))
    public void onOnCommandTree(CommandTreeS2CPacket packet, CallbackInfo ci) {
        ClientCommands.registerCommands((CommandDispatcher<ServerCommandSource>)(Object)this.commandDispatcher);
    }

    @Inject(method = "onGameJoin", at = @At(value = "TAIL"))
    public void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
        ClientCommand.sendFeedback("Vizard has been loaded", Formatting.AQUA,false);
    }
}

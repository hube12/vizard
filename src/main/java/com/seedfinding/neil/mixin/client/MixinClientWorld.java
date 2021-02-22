package com.seedfinding.neil.mixin.client;

import com.seedfinding.neil.GenController;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class MixinClientWorld {
    @Inject(method = "disconnect", at = @At("HEAD"))
    private void disconnect(CallbackInfo ci) {
        GenController.reset(); // TODO safe handling of not compliant
    }
}

package com.seedfinding.neil.mixin;

import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(targets = {"net.minecraft.client.gui.screen.PresetsScreen$SuperflatPreset" })
public class  Test {
    @Final
    @Shadow
    public final Text name = null;

}

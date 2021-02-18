package com.seedfinding.neil.mixin;

import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = {"net.minecraft.client.gui.screen.PresetsScreen$SuperflatPreset"})
public interface AccessFlatPreset {
        @Accessor
        Text getName();
}

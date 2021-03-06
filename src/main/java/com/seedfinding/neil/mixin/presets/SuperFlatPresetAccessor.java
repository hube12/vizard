package com.seedfinding.neil.mixin.presets;

import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = {"net.minecraft.client.gui.screen.PresetsScreen$SuperflatPreset"})
public interface SuperFlatPresetAccessor {
    @Accessor
    Text getName();
}

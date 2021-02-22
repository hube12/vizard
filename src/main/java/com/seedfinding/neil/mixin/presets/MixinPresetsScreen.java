package com.seedfinding.neil.mixin.presets;
import net.minecraft.text.LiteralText;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.PresetsScreen;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorLayer;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Mixin(PresetsScreen.class)
public class MixinPresetsScreen {
    @Final
    @Shadow
    @Mutable
    private static List<SuperFlatPresetAccessor> PRESETS; // we use an accessor (don't put it in the inner class)

    @Inject(method = "<init>", at = @At("RETURN"))
    public void sortList(CallbackInfo ci) {
        PRESETS.sort(Comparator.comparing(a -> a.getName().getString()));
    }


    @Shadow
    private static void addPreset(Text text, ItemConvertible icon, RegistryKey<Biome> registryKey, List<StructureFeature<?>> structures, boolean bl, boolean bl2, boolean bl3, FlatChunkGeneratorLayer... flatChunkGeneratorLayers) {

    }

    static {
        addPreset(new LiteralText("1) Stronghold Madness") , Items.ENDER_EYE, BiomeKeys.PLAINS, Collections.singletonList(StructureFeature.STRONGHOLD), true, false, false, new FlatChunkGeneratorLayer(1, Blocks.GRASS_BLOCK), new FlatChunkGeneratorLayer(2, Blocks.DIRT), new FlatChunkGeneratorLayer(1, Blocks.BEDROCK));
        addPreset(new LiteralText("2) Mineshaft Madness"), Items.COBWEB, BiomeKeys.PLAINS, Collections.singletonList(StructureFeature.MINESHAFT), false, false, false, new FlatChunkGeneratorLayer(1, Blocks.GRASS_BLOCK), new FlatChunkGeneratorLayer(2, Blocks.DIRT), new FlatChunkGeneratorLayer(1, Blocks.BEDROCK));
        addPreset(new LiteralText("3) Fortress Madness"), Items.BLAZE_ROD, BiomeKeys.NETHER_WASTES, Collections.singletonList(StructureFeature.FORTRESS), false, false, false, new FlatChunkGeneratorLayer(1, Blocks.GRASS_BLOCK), new FlatChunkGeneratorLayer(2, Blocks.DIRT), new FlatChunkGeneratorLayer(1, Blocks.BEDROCK));
        addPreset(new LiteralText("4) End Madness"), Items.SHULKER_BOX, BiomeKeys.END_HIGHLANDS, Collections.singletonList(StructureFeature.END_CITY), false, false, false, new FlatChunkGeneratorLayer(1, Blocks.GRASS_BLOCK), new FlatChunkGeneratorLayer(2, Blocks.DIRT), new FlatChunkGeneratorLayer(1, Blocks.BEDROCK));
        addPreset(new LiteralText("5) Bastion Madness"), Items.PIGLIN_BANNER_PATTERN, BiomeKeys.NETHER_WASTES, Collections.singletonList(StructureFeature.BASTION_REMNANT), false, false, false, new FlatChunkGeneratorLayer(1, Blocks.GRASS_BLOCK), new FlatChunkGeneratorLayer(2, Blocks.DIRT), new FlatChunkGeneratorLayer(1, Blocks.BEDROCK));
    }
}

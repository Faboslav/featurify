package com.faboslav.featurify.forge.registry;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.forge.worldgen.PlacedFeaturesBiomeModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.function.Supplier;

public final class FeaturifyBiomeModifiers
{
	public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Featurify.MOD_ID);

	public static final Supplier<Codec<? extends BiomeModifier>> BIOME_MODIFIER = BIOME_MODIFIERS.register("placed_features", () -> PlacedFeaturesBiomeModifier.CODEC);
}
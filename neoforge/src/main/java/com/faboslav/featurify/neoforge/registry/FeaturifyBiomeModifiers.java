package com.faboslav.featurify.neoforge.registry;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.neoforge.worldgen.PlacedFeaturesBiomeModifier;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class FeaturifyBiomeModifiers
{
	public static final DeferredRegister<MapCodec<? extends BiomeModifier>> BIOME_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Featurify.MOD_ID);

	public static final Supplier<MapCodec<? extends BiomeModifier>> BIOME_MODIFIER = BIOME_MODIFIERS.register("placed_features", () -> PlacedFeaturesBiomeModifier.CODEC);
}
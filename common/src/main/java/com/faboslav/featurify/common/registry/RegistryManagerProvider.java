package com.faboslav.featurify.common.registry;

import com.faboslav.featurify.common.Featurify;
import com.mojang.serialization.Lifecycle;
import net.minecraft.commands.Commands;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.WorldStem;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import org.jetbrains.annotations.Nullable;
import java.util.*;

//? if >= 1.21.11 {
/*import net.minecraft.server.permissions.PermissionSet;
*///?}

public final class RegistryManagerProvider
{
	@Nullable
	private static HolderLookup.Provider registryManager = null;
	private static boolean isLoading = false;

	@Nullable
	public static HolderLookup.Provider getRegistryManager() {
		if (registryManager == null) {
			loadRegistryManager();
		}

		return registryManager;
	}

	@Nullable
	public static HolderLookup.RegistryLookup<Biome> getBiomeRegistry() {
		var registryManager = RegistryManagerProvider.getRegistryManager();

		if (registryManager == null) {
			return null;
		}

		return registryManager.lookup(Registries.BIOME).orElse(null);
	}

	@Nullable

	public static HolderLookup.RegistryLookup<PlacedFeature> getPlacedFeatureRegistry()
	{
		var registryManager = RegistryManagerProvider.getRegistryManager();

		if (registryManager == null) {
			return null;
		}

		return registryManager.lookup(Registries.PLACED_FEATURE).orElse(null);
	}

	public static void setRegistryManager(HolderLookup.Provider registryAccess) {
		registryManager = registryAccess;
	}

	public static void loadRegistryManager() {
		if (isLoading) {
			return;
		}

		isLoading = true;
		try {
			Featurify.getLogger().info("Loading registry manager...");
			var resourcePackManager = ResourcePackProvider.getResourcePackRepository();

			var dataPacks = new WorldLoader.PackConfig(resourcePackManager, WorldDataConfiguration.DEFAULT, false, false);
			var serverConfig = new WorldLoader.InitConfig(dataPacks, Commands.CommandSelection.INTEGRATED, /*? if >= 1.21.11 {*//*PermissionSet.ALL_PERMISSIONS*//*?} else {*/2/*?}*/);

			var saveLoader = Util.blockUntilDone(executor ->
				WorldLoader.load(serverConfig, loadContextSupplierContext -> {
					var registry = new MappedRegistry<>(Registries.LEVEL_STEM, Lifecycle.stable()).freeze();

					//? if >=1.21.3 {
					/*var dimensionsConfig = loadContextSupplierContext
						.datapackWorldgen()
						.lookupOrThrow(Registries.WORLD_PRESET)
						.getOrThrow(WorldPresets.FLAT)
						.value()
						.createWorldDimensions()
						.bake(registry);
					*///?} else {
					var dimensionsConfig = loadContextSupplierContext
						.datapackWorldgen()
						.registryOrThrow(Registries.WORLD_PRESET)
						.getHolderOrThrow(WorldPresets.FLAT)
						.value()
						.createWorldDimensions()
						.bake(registry);
					//?}

					return new WorldLoader.DataLoadOutput<>(null, dimensionsConfig.dimensionsRegistryAccess());
				}, WorldStem::new, Util.backgroundExecutor(), executor)
			).get();

			if (saveLoader == null || saveLoader.registries() == null) {
				Featurify.getLogger().error("SaveLoader or CombinedDynamicRegistries is null.");
				return;
			}

			setRegistryManager(saveLoader.registries().compositeAccess());
			Featurify.getLogger().info("Finished loading registry manager");
		} catch (Exception exception) {
			Featurify.getLogger().error("Failed to load registry manager.", exception);
		} finally {
			isLoading = false;
		}
	}
}

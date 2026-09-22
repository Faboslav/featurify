package com.faboslav.featurify.fabric;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;

//? if >= 1.21.11 {
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
//?} else {
/*import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
*///?}

public final class FeaturifyFabricServer implements DedicatedServerModInitializer
{
	@Override
	public void onInitializeServer()
	{
		var loadConfigKey = Featurify.makeNamespacedId("load_config");

		//? if >= 1.21.11 {
		//? if >= 26.1.2 {
		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(loadConfigKey, (sharedState, prepareExecutor, barrier, applyExecutor) ->
			barrier.<Void>wait(null).thenRunAsync(() -> {
				LoadConfigEvent.EVENT.invoke(new LoadConfigEvent(true));
			}, applyExecutor)
		);

		ResourceLoader.get(PackType.SERVER_DATA).addListenerOrdering(ResourceReloaderKeys.AFTER_VANILLA, loadConfigKey);
		//?} else {
		/*ResourceLoader.get(PackType.SERVER_DATA).registerReloader(loadConfigKey, (sharedState, prepareExecutor, barrier, applyExecutor) ->
			barrier.<Void>wait(null).thenRunAsync(() -> {
				LoadConfigEvent.EVENT.invoke(new LoadConfigEvent(true));
			}, applyExecutor)
		);

		ResourceLoader.get(PackType.SERVER_DATA).addReloaderOrdering(ResourceReloaderKeys.AFTER_VANILLA, loadConfigKey);
		*///?}
		//?} else {
		/*//? if >= 1.21.3 {
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(
			loadConfigKey,
			registryLookup -> new SimpleResourceReloadListener<Void>() {
				@Override
				public Identifier getFabricId() {
					return loadConfigKey;
				}

				@Override
				public Collection<Identifier> getFabricDependencies() {
					return List.of(ResourceReloadListenerKeys.RECIPES, ResourceReloadListenerKeys.ADVANCEMENTS, ResourceReloadListenerKeys.FUNCTIONS);
				}

				@Override
				public CompletableFuture<Void> load(ResourceManager manager, Executor executor) {
					return CompletableFuture.completedFuture(null);
				}

				@Override
				public CompletableFuture<Void> apply(Void data, ResourceManager manager, Executor executor) {
					return CompletableFuture.runAsync(() -> {
						LoadConfigEvent.EVENT.invoke(new LoadConfigEvent(true));
					}, executor);
				}
			}
		);
		//?} else {
		/^//? if >= 1.21.1 {
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(
			loadConfigKey,
			registryLookup -> new SimpleResourceReloadListener<Void>() {
				@Override
				public Identifier getFabricId() {
					return loadConfigKey;
				}

				@Override
				public Collection<Identifier> getFabricDependencies() {
					return List.of(ResourceReloadListenerKeys.TAGS, ResourceReloadListenerKeys.RECIPES, ResourceReloadListenerKeys.ADVANCEMENTS, ResourceReloadListenerKeys.FUNCTIONS);
				}

				@Override
				public CompletableFuture<Void> load(ResourceManager manager, ProfilerFiller profiler, Executor executor) {
					return CompletableFuture.completedFuture(null);
				}

				@Override
				public CompletableFuture<Void> apply(Void data, ResourceManager manager, ProfilerFiller profiler, Executor executor) {
					return CompletableFuture.runAsync(() -> {
						LoadConfigEvent.EVENT.invoke(new LoadConfigEvent(true));
					}, executor);
				}
			}
		);
		//?} else {
		/^ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleResourceReloadListener<Void>() {
			@Override
			public Identifier getFabricId() {
				return loadConfigKey;
			}

			@Override
			public Collection<Identifier> getFabricDependencies() {
				return List.of(ResourceReloadListenerKeys.TAGS, ResourceReloadListenerKeys.RECIPES, ResourceReloadListenerKeys.ADVANCEMENTS, ResourceReloadListenerKeys.FUNCTIONS);
			}

			@Override
			public CompletableFuture<Void> load(ResourceManager manager, ProfilerFiller profiler, Executor executor) {
				return CompletableFuture.completedFuture(null);
			}

			@Override
			public CompletableFuture<Void> apply(Void data, ResourceManager manager, ProfilerFiller profiler, Executor executor) {
				return CompletableFuture.runAsync(() -> {
					LoadConfigEvent.EVENT.invoke(new LoadConfigEvent(true));
				}, executor);
			}
		});
		^///?}
		^///?}
		*///?}
	}
}

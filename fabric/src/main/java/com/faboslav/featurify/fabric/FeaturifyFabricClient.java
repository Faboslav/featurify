package com.faboslav.featurify.fabric;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.FeaturifyClient;
import com.faboslav.featurify.common.events.common.LoadConfigEvent;
import com.faboslav.featurify.common.events.common.UpdateWorldgenDataEvent;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

//? if >= 1.21.11 {
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.ResourceReloaderKeys;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
//?} else {
/*import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourceReloadListenerKeys;
import net.fabricmc.fabric.api.resource.SimpleResourceReloadListener;
*///?}

public final class FeaturifyFabricClient implements ClientModInitializer
{
	@Override
	@Environment(EnvType.CLIENT)
	public void onInitializeClient() {
		FeaturifyClient.init();

		//? if >= 1.21.11 {
		var key = Featurify.makeNamespacedId("load_config");

		//? if >= 26.1.2 {
		ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloadListener(key, new SimplePreparableReloadListener<Void>() {
			@Override
			protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
				return null;
			}

			@Override
			protected void apply(Void result, ResourceManager resourceManager, ProfilerFiller profiler) {
				LoadConfigEvent.EVENT.invoke(new LoadConfigEvent());
				UpdateWorldgenDataEvent.EVENT.invoke(new UpdateWorldgenDataEvent(RegistryManagerProvider.getRegistryManager()));
			}
		});

		ResourceLoader.get(PackType.CLIENT_RESOURCES).addListenerOrdering(ResourceReloaderKeys.AFTER_VANILLA, key);
		//?} else {
		/*ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(key, new SimplePreparableReloadListener<Void>() {
			@Override
			protected Void prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
				return null;
			}

			@Override
			protected void apply(Void result, ResourceManager resourceManager, ProfilerFiller profiler) {
				LoadConfigEvent.EVENT.invoke(new LoadConfigEvent());
				UpdateWorldgenDataEvent.EVENT.invoke(new UpdateWorldgenDataEvent(RegistryManagerProvider.getRegistryManager()));
			}
		});

		ResourceLoader.get(PackType.CLIENT_RESOURCES).addReloaderOrdering(ResourceReloaderKeys.AFTER_VANILLA, key);
		*///?}
		//?} else {
		/*//? if >= 1.21.3 {
		ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleResourceReloadListener<Void>() {
			@Override
			public Identifier getFabricId() {
				return Featurify.makeNamespacedId("load_config");
			}

			@Override
			public Collection<Identifier> getFabricDependencies() {
				return List.of(ResourceReloadListenerKeys.MODELS);
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
		});
		//?} else {
		/^ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleResourceReloadListener<Void>() {
			@Override
			public Identifier getFabricId() {
				return Featurify.makeNamespacedId("load_config");
			}

			@Override
			public Collection<Identifier> getFabricDependencies() {
				return List.of(ResourceReloadListenerKeys.MODELS);
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
		*///?}
	}
}

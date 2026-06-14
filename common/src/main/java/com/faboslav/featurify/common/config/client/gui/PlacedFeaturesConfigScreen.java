package com.faboslav.featurify.common.config.client.gui;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.FeaturifyClient;
import com.faboslav.featurify.common.config.FeaturifyConfig;
import com.faboslav.featurify.common.config.client.api.controller.builder.StructureButtonControllerBuilder;
import com.faboslav.featurify.common.config.client.api.option.InvisibleOptionGroup;
import com.faboslav.featurify.common.config.data.PlacedFeatureData;
import com.faboslav.featurify.common.config.data.WorldgenDataProvider;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import com.faboslav.featurify.common.util.Comparators;
import com.faboslav.featurify.common.util.LanguageUtil;
import com.faboslav.featurify.common.util.YACLUtil;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.util.*;

@SuppressWarnings({"unchecked"})
public final class PlacedFeaturesConfigScreen
{
	private final static List<Option<Boolean>> placedFeaturesOptions = new ArrayList<>();

	public static void createPlacedFeaturesTab(YetAnotherConfigLib.Builder yacl, FeaturifyConfig config) {
		var placedFeaturesCategoryBuilder = ConfigCategory.createBuilder()
			.name(Component.translatable("gui.featurify.placed_features_category.title"))
			.tooltip(Component.translatable("gui.featurify.placed_features_category.description"));

		addPlacedFeatures(placedFeaturesCategoryBuilder, config);

		yacl.category(placedFeaturesCategoryBuilder.build());
	}

	private static void addPlacedFeatures(ConfigCategory.Builder placedFeaturesCategoryBuilder, FeaturifyConfig config) {
		var placedFeatures = WorldgenDataProvider.getPlacedFeatures();
		var placedFeatureGroups = new TreeMap<String, TreeMap<ResourceLocation, PlacedFeatureData>>(Comparators.ALPHABETICALL_NAMESPACE_COMPARATOR);
		var biomeRegistry = RegistryManagerProvider.getBiomeRegistry();

		for (Map.Entry<String, PlacedFeatureData> entry : placedFeatures.entrySet()) {
			String placedFeatureStringId = entry.getKey();
			ResourceLocation placedFeatureId = Featurify.makeNamespacedId(placedFeatureStringId);
			String placedFeatureNamespace = placedFeatureId.getNamespace();
			PlacedFeatureData placedFeatureData = entry.getValue();
			placedFeatureGroups
				.computeIfAbsent(placedFeatureNamespace, namespace -> new TreeMap<>(Comparator.comparing(ResourceLocation::getPath)))
				.put(placedFeatureId, placedFeatureData);
		}

		for (var placedFeatureGroup : placedFeatureGroups.entrySet()) {
			String structureNamespace = placedFeatureGroup.getKey();
			var namespacePlacedFeatures = placedFeatureGroup.getValue();

			var invisibleGroup = new InvisibleOptionGroup.Builder().name(Component.literal(""));
			invisibleGroup.option(YACLUtil.createEmptyLabelOption());
			placedFeaturesCategoryBuilder.group(invisibleGroup.build());

			OptionGroup.Builder namespaceGroupBuilder = OptionGroup.createBuilder()
				.name(Component.translatable("gui.featurify.placed_features.placed_feature_group.title", LanguageUtil.translateId(null, structureNamespace).getString()).withStyle(style -> style.withUnderlined(true)))
				.description(OptionDescription.of(Component.translatable("gui.featurify.placed_features.placed_feature_group.description", structureNamespace)));

			for (var namespacePlacedFeature : namespacePlacedFeatures.entrySet()) {
				var placedFeatureData = namespacePlacedFeature.getValue();
				var placedFeatureStringId = namespacePlacedFeature.getKey().toString();
				var placedFeatureOption = addPlacedFeature(placedFeatureData, placedFeatureStringId, biomeRegistry);
				namespaceGroupBuilder.option(placedFeatureOption);
				placedFeaturesOptions.add(placedFeatureOption);
			}

			placedFeaturesCategoryBuilder.group(namespaceGroupBuilder.build());
		}

		var invisibleGroup = new InvisibleOptionGroup.Builder().name(Component.literal(""));
		invisibleGroup.option(YACLUtil.createEmptyLabelOption());
		placedFeaturesCategoryBuilder.group(invisibleGroup.build());
	}

	private static Option<Boolean> addPlacedFeature(
		PlacedFeatureData placedFeatureData,
		String placedFeatureId,
		HolderLookup.RegistryLookup<Biome> biomeRegistry
	) {
		var placedFeatureName = LanguageUtil.translatePlacedFeatureId(placedFeatureId);

		var structureOptionBuilder = Option.<Boolean>createBuilder()
			.name(placedFeatureName)
			.binding(
				true,
				() -> !placedFeatureData.isDisabled(),
				isEnabled -> placedFeatureData.setDisabled(!isEnabled)
			)
			.controller(opt -> StructureButtonControllerBuilder.create(opt, placedFeatureId)
				.formatValue(val -> val ? Component.translatable("gui.featurify.label.enabled"):Component.translatable("gui.featurify.label.disabled"))
				.coloured(true)
				.openConfigCallback((screen, id) -> {
					var configScreen = FeaturifyClient.getConfigScreen();

					if (configScreen == null) {
						return;
					}

					screen.finishOrSave();

					YACLScreen placedFeatureScreen = PlacedFeatureConfigScreen.create(Featurify.getConfig(), id, screen);

					configScreen.saveScreenState(screen);
					configScreen.switchScreen(screen, placedFeatureScreen);
					configScreen.loadScreenState(placedFeatureScreen);
				}).buttonTooltip("gui.featurify.placed_features.placed_feature.detail_button.tooltip")
			);

		structureOptionBuilder.description(v -> {
			var descriptionBuilder = OptionDescription.createBuilder();

			if(!placedFeatureData.getBiomes().isEmpty()) {
				descriptionBuilder.text(Component.translatable("gui.featurify.placed_features.biomes_description").append(Component.literal("\n")));

				for (String biome : placedFeatureData.getBiomes()) {
					if (biome.contains("#")) {
						if (biomeRegistry == null) {
							continue;
						}

						var biomeTagKey = TagKey.create(Registries.BIOME, Featurify.makeNamespacedId(biome.replace("#", "")));
						var biomeTagHolder = biomeRegistry.get(biomeTagKey).orElse(null);

						if (biomeTagHolder == null) {
							continue;
						}

						for (var biomeHolder : biomeTagHolder.stream().toList()) {
							descriptionBuilder.text(Component.literal(" - ").append(LanguageUtil.translateId("biome", biomeHolder.unwrap().left().get()/*? if >= 1.21.11 {*//*.identifier()*//*?} else {*/.location()/*?}*/.toLanguageKey())));
						}
					} else {
						descriptionBuilder.text(Component.literal(" - ").append(LanguageUtil.translateId("biome", biome)));
					}
				}
			}

			return descriptionBuilder.build();
		});

		return structureOptionBuilder.build();
	}
}
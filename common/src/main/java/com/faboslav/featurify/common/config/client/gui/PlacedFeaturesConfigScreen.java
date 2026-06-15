package com.faboslav.featurify.common.config.client.gui;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.FeaturifyClient;
import com.faboslav.featurify.common.config.FeaturifyConfig;
import com.faboslav.featurify.common.config.client.api.controller.builder.ButtonControllerBuilder;
import com.faboslav.featurify.common.config.client.api.option.InvisibleOptionGroup;
import com.faboslav.featurify.common.config.data.PlacedFeatureData;
import com.faboslav.featurify.common.config.data.WorldgenDataProvider;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import com.faboslav.featurify.common.util.Comparators;
import com.faboslav.featurify.common.util.LanguageUtil;
import com.faboslav.featurify.common.util.YACLUtil;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
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

		addGlobalSettings(placedFeaturesCategoryBuilder, config);
		addPlacedFeatures(placedFeaturesCategoryBuilder, config);

		yacl.category(placedFeaturesCategoryBuilder.build());
	}

	private static void addGlobalSettings(ConfigCategory.Builder placedFeaturesCategoryBuilder, FeaturifyConfig config) {
		var globalPlacedFeaturesGroupBuilder = OptionGroup.createBuilder()
			.name(Component.translatable("gui.featurify.placed_features.global.title").withStyle(style -> style.withUnderlined(true)))
			.description(OptionDescription.of(Component.translatable("gui.featurify.placed_features.global.description")));

		var disableAllPlacedFeaturesOption = Option.<Boolean>createBuilder()
			.name(Component.translatable("gui.featurify.placed_features.disable_all_placed_features.title"))
			.description(OptionDescription.of(Component.translatable("gui.featurify.placed_features.disable_all_placed_features.description")))
			.binding(
				false,
				() -> config.disableAllPlacedFeatures,
				disableAllPlacedFeatures -> config.disableAllPlacedFeatures = disableAllPlacedFeatures
			)
			.controller(opt -> BooleanControllerBuilder.create(opt).formatValue(val -> val ? Component.translatable("gui.featurify.label.yes").withStyle(style -> style.withColor(ChatFormatting.RED)):Component.translatable("gui.featurify.label.no").withStyle(style -> style.withColor(ChatFormatting.GREEN)))).build();

		disableAllPlacedFeaturesOption.addListener((opt, disableAllPlacedFeatures) -> {
			for (var placedFeaturesOption : placedFeaturesOptions) {
				placedFeaturesOption.requestSet(!disableAllPlacedFeatures);
				placedFeaturesOption.applyValue();
				placedFeaturesOption.setAvailable(!disableAllPlacedFeatures);
			}
		});

		globalPlacedFeaturesGroupBuilder.option(disableAllPlacedFeaturesOption);
		placedFeaturesCategoryBuilder.group(globalPlacedFeaturesGroupBuilder.build());
	}

	private static void addPlacedFeatures(ConfigCategory.Builder placedFeaturesCategoryBuilder, FeaturifyConfig config) {
		var placedFeatures = WorldgenDataProvider.getPlacedFeatures();
		var placedFeatureGroups = new TreeMap<String, TreeMap<Identifier, PlacedFeatureData>>(Comparators.ALPHABETICALL_NAMESPACE_COMPARATOR);
		var biomeRegistry = RegistryManagerProvider.getBiomeRegistry();

		for (Map.Entry<String, PlacedFeatureData> entry : placedFeatures.entrySet()) {
			String placedFeatureStringId = entry.getKey();
			Identifier placedFeatureId = Featurify.makeNamespacedId(placedFeatureStringId);
			String placedFeatureNamespace = placedFeatureId.getNamespace();
			PlacedFeatureData placedFeatureData = entry.getValue();
			placedFeatureGroups
				.computeIfAbsent(placedFeatureNamespace, namespace -> new TreeMap<>(Comparator.comparing(Identifier::getPath)))
				.put(placedFeatureId, placedFeatureData);
		}

		for (var placedFeatureGroup : placedFeatureGroups.entrySet()) {
			String placedFeatureNamespace = placedFeatureGroup.getKey();
			var namespacePlacedFeatures = placedFeatureGroup.getValue();

			var invisibleGroup = new InvisibleOptionGroup.Builder().name(Component.literal(""));
			invisibleGroup.option(YACLUtil.createEmptyLabelOption());
			placedFeaturesCategoryBuilder.group(invisibleGroup.build());

			OptionGroup.Builder namespaceGroupBuilder = OptionGroup.createBuilder()
				.name(Component.translatable("gui.featurify.placed_features.placed_feature_group.title", LanguageUtil.translateId(null, placedFeatureNamespace).getString()).withStyle(style -> style.withUnderlined(true)))
				.description(OptionDescription.of(Component.translatable("gui.featurify.placed_features.placed_feature_group.description", placedFeatureNamespace)));

			for (var namespacePlacedFeature : namespacePlacedFeatures.entrySet()) {
				var placedFeatureData = namespacePlacedFeature.getValue();
				var placedFeatureStringId = namespacePlacedFeature.getKey().toString();
				var placedFeatureOption = addPlacedFeature(placedFeatureData, placedFeatureStringId, config, biomeRegistry);
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
		FeaturifyConfig config,
		HolderLookup.RegistryLookup<Biome> biomeRegistry
	) {
		var placedFeatureName = LanguageUtil.translatePlacedFeatureId(placedFeatureId);

		var placedFeatureOptionBuilder = Option.<Boolean>createBuilder()
			.name(placedFeatureName)
			.binding(
				true,
				() -> !placedFeatureData.isDisabled(),
				isEnabled -> placedFeatureData.setDisabled(!isEnabled)
			)
			.available(!config.disableAllPlacedFeatures)
			.controller(opt -> ButtonControllerBuilder.create(opt, placedFeatureId)
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

		placedFeatureOptionBuilder.description(v -> {
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
							descriptionBuilder.text(Component.literal(" - ").append(LanguageUtil.translateId("biome", biomeHolder.unwrap().left().get()/*? if >= 1.21.11 {*/.identifier()/*?} else {*//*.location()*//*?}*/.toLanguageKey())));
						}
					} else {
						descriptionBuilder.text(Component.literal(" - ").append(LanguageUtil.translateId("biome", biome)));
					}
				}
			}

			return descriptionBuilder.build();
		});

		return placedFeatureOptionBuilder.build();
	}
}
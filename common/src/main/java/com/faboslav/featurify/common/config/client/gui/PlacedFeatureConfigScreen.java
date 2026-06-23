package com.faboslav.featurify.common.config.client.gui;

import com.faboslav.featurify.common.config.FeaturifyConfig;
import com.faboslav.featurify.common.config.client.api.controller.builder.BiomeStringControllerBuilder;
import com.faboslav.featurify.common.config.client.api.option.InvisibleOptionGroup;
import com.faboslav.featurify.common.config.data.PlacedFeatureData;
import com.faboslav.featurify.common.util.LanguageUtil;
import com.faboslav.featurify.common.util.YACLUtil;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.Locale;

@SuppressWarnings({"all", "deprecated", "removal"})
public final class PlacedFeatureConfigScreen
{
	public static YACLScreen create(FeaturifyConfig config, String placedFeatureId, Screen parent) {
		var yacl = YetAnotherConfigLib.createBuilder()
			.title(Component.literal(placedFeatureId))
			.save(config::save);

		var placedFeatureData = config.getPlacedFeatureData().get(placedFeatureId);
		var translatedPlacedFeatureName = LanguageUtil.translatePlacedFeatureId(placedFeatureId);
		var placedFeatureCategoryBuilder = ConfigCategory.createBuilder()
			.name(Component.translatable("gui.featurify.placed_features.placed_feature.title", translatedPlacedFeatureName))
			.tooltip(Component.translatable("gui.featurify.placed_features.placed_feature.description", translatedPlacedFeatureName));

		var placedFeatureSettingsGroup = new InvisibleOptionGroup.Builder().name(Component.literal(""));

		placedFeatureSettingsGroup.option(LabelOption.create(Component.translatable("gui.featurify.placed_features.placed_feature.settings.title").withStyle(style -> style.withBold(true))));

		var isDisabledOption = Option.<Boolean>createBuilder()
			.name(Component.translatable("gui.featurify.placed_features.placed_feature.is_disabled.title"))
			.description(OptionDescription.of(Component.translatable("gui.featurify.placed_features.placed_feature.is_disabled.description")))
			.binding(
				PlacedFeatureData.IS_DISABLED_DEFAULT_VALUE,
				placedFeatureData::isDisabled,
				placedFeatureData::setDisabled
			)
			.controller(opt -> BooleanControllerBuilder.create(opt).formatValue(val -> val ? Component.translatable("gui.featurify.label.yes").withStyle(style -> style.withColor(ChatFormatting.RED)):Component.translatable("gui.featurify.label.no").withStyle(style -> style.withColor(ChatFormatting.GREEN)))).build();

		placedFeatureSettingsGroup.option(isDisabledOption);

		placedFeatureCategoryBuilder.group(placedFeatureSettingsGroup.build());

		if(!placedFeatureData.getDefaultBiomes().isEmpty()) {
			var biomesOption = ListOption.<String>createBuilder()
				.name(Component.translatable("gui.featurify.placed_features.placed_feature.biomes.title"))
				.description(OptionDescription.of(Component.translatable("gui.featurify.placed_features.placed_feature.biomes.description", translatedPlacedFeatureName)))
				.insertEntriesAtEnd(false)
				.binding(
					placedFeatureData.getDefaultBiomes(),
					placedFeatureData::getBiomes,
					placedFeatureData::setBiomes
				)
				.controller(BiomeStringControllerBuilder::create)
				.initial("").build();

			placedFeatureCategoryBuilder.group(biomesOption);
		}

		var defaultWeightedPlacedFeatures = placedFeatureData.getDefaultWeightedPlacedFeatures();
		var weightedPlacedFeatures = placedFeatureData.getWeightedPlacedFeatures();

		if(!weightedPlacedFeatures.isEmpty()) {
			var subFeaturesGroup = new InvisibleOptionGroup.Builder().name(Component.literal("subfeatures"));
			subFeaturesGroup.option(YACLUtil.createEmptySmallLabelOption());
			subFeaturesGroup.option(LabelOption.create(Component.translatable("gui.featurify.placed_features.placed_feature.subfeatures.title").withStyle(style -> style.withBold(true))));

			for (var weightedPlacedFeature : weightedPlacedFeatures.entrySet()) {
				var weightedPlacedFeatureId = weightedPlacedFeature.getKey();
				var translatedSubFeatureName = LanguageUtil.translateId("configured_feature", weightedPlacedFeatureId);

				var chanceOption = Option.<Float>createBuilder()
					.name(translatedSubFeatureName)
					.description(OptionDescription.of(Component.translatable("gui.featurify.placed_features.placed_feature.subfeatures.chance.description")))
					.binding(
						defaultWeightedPlacedFeatures.get(weightedPlacedFeatureId),
						weightedPlacedFeature::getValue,
						weightedPlacedFeature::setValue
					)
					.controller(opt -> FloatSliderControllerBuilder.create(opt).range(PlacedFeatureData.MIN_CHANCE, PlacedFeatureData.MAX_CHANCE).step(0.001F).formatValue(value -> Component.literal(String.format(Locale.ROOT, "%.2f%%", value * 100.0F)))).build();

				subFeaturesGroup.option(chanceOption);
			}

			placedFeatureCategoryBuilder.group(subFeaturesGroup.build());
		}

		yacl.category(placedFeatureCategoryBuilder.build());

		return (YACLScreen) yacl.build().generateScreen(parent);
	}
}
package com.faboslav.featurify.common.config.client.gui;

import com.faboslav.featurify.common.config.FeaturifyConfig;
import com.faboslav.featurify.common.config.client.api.controller.builder.BiomeStringControllerBuilder;
import com.faboslav.featurify.common.config.client.api.option.InvisibleOptionGroup;
import com.faboslav.featurify.common.config.data.BiomeData;
import com.faboslav.featurify.common.util.LanguageUtil;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@SuppressWarnings({"all", "deprecated", "removal"})
public final class BiomeConfigScreen
{
	public static YACLScreen create(FeaturifyConfig config, String biomeId, Screen parent) {
		var yacl = YetAnotherConfigLib.createBuilder()
			.title(Component.literal(biomeId))
			.save(config::save);

		var biomeData = config.getBiomeData().get(biomeId);
		var translatedBiomeName = LanguageUtil.translateId("biome", biomeId);
		var biomeCategoryBuilder = ConfigCategory.createBuilder()
			.name(Component.translatable("gui.featurify.biomes.biome.title", translatedBiomeName))
			.tooltip(Component.translatable("gui.featurify.biomes.biome.description", translatedBiomeName));

		var biomeSettingsGroup = new InvisibleOptionGroup.Builder().name(Component.literal(""));

		biomeSettingsGroup.option(LabelOption.create(Component.translatable("gui.featurify.biomes.biome.settings.title").withStyle(style -> style.withBold(true))));

		var isDisabledOption = Option.<Boolean>createBuilder()
			.name(Component.translatable("gui.featurify.biomes.biome.is_disabled.title"))
			.description(OptionDescription.of(Component.translatable("gui.featurify.biomes.biome.is_disabled.description")))
			.binding(
				BiomeData.IS_DISABLED_DEFAULT_VALUE,
				biomeData::isDisabled,
				biomeData::setDisabled
			)
			.controller(opt -> BooleanControllerBuilder.create(opt).formatValue(val -> val ? Component.translatable("gui.featurify.label.yes").withStyle(style -> style.withColor(ChatFormatting.RED)) : Component.translatable("gui.featurify.label.no").withStyle(style -> style.withColor(ChatFormatting.GREEN)))).build();

		biomeSettingsGroup.option(isDisabledOption);

		var replacementBiomeOption = Option.<String>createBuilder()
			.name(Component.translatable("gui.featurify.biomes.biome.replacement_biome.title"))
			.description(OptionDescription.of(Component.translatable("gui.featurify.biomes.biome.replacement_biome.description")))
			.binding(
				BiomeData.REPLACEMENT_BIOME_DEFAULT_VALUE,
				biomeData::getReplacementBiome,
				biomeData::setReplacementBiome
			)
			.controller(opt -> BiomeStringControllerBuilder.create(opt).allowEmpty()).build();

		biomeSettingsGroup.option(replacementBiomeOption);

		biomeCategoryBuilder.group(biomeSettingsGroup.build());
		yacl.category(biomeCategoryBuilder.build());

		return (YACLScreen) yacl.build().generateScreen(parent);
	}
}
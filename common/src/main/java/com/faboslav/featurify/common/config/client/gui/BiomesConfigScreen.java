package com.faboslav.featurify.common.config.client.gui;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.FeaturifyClient;
import com.faboslav.featurify.common.config.FeaturifyConfig;
import com.faboslav.featurify.common.config.client.api.controller.builder.ButtonControllerBuilder;
import com.faboslav.featurify.common.config.client.api.option.InvisibleOptionGroup;
import com.faboslav.featurify.common.config.data.BiomeData;
import com.faboslav.featurify.common.worldgen.WorldgenDataProvider;
import com.faboslav.featurify.common.registry.RegistryManagerProvider;
import com.faboslav.featurify.common.util.Comparators;
import com.faboslav.featurify.common.util.LanguageUtil;
import com.faboslav.featurify.common.util.YACLUtil;
import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import java.util.*;

@SuppressWarnings({"unchecked"})
public final class BiomesConfigScreen
{
	private final static List<Option<Boolean>> biomesOptions = new ArrayList<>();

	public static void createBiomesTab(YetAnotherConfigLib.Builder yacl, FeaturifyConfig config) {
		biomesOptions.clear();

		var biomesCategoryBuilder = ConfigCategory.createBuilder()
			.name(Component.translatable("gui.featurify.biomes_category.title"))
			.tooltip(Component.translatable("gui.featurify.biomes_category.description"));

		addBiomes(biomesCategoryBuilder, config);

		yacl.category(biomesCategoryBuilder.build());
	}

	private static void addBiomes(ConfigCategory.Builder biomesCategoryBuilder, FeaturifyConfig config) {
		var biomes = WorldgenDataProvider.getBiomes();
		var biomeGroups = new TreeMap<String, TreeMap<Identifier, BiomeData>>(Comparators.ALPHABETICALL_NAMESPACE_COMPARATOR);

		for (Map.Entry<String, BiomeData> entry : biomes.entrySet()) {
			String biomeStringId = entry.getKey();
			Identifier biomeId = Featurify.makeNamespacedId(biomeStringId);
			String biomeNamespace = biomeId.getNamespace();
			BiomeData biomeData = entry.getValue();
			biomeGroups
				.computeIfAbsent(biomeNamespace, namespace -> new TreeMap<>(Comparator.comparing(Identifier::getPath)))
				.put(biomeId, biomeData);
		}

		for (var biomeGroup : biomeGroups.entrySet()) {
			String biomeNamespace = biomeGroup.getKey();
			var namespaceBiomes = biomeGroup.getValue();

			var invisibleGroup = new InvisibleOptionGroup.Builder().name(Component.literal(""));
			invisibleGroup.option(YACLUtil.createEmptyLabelOption());
			biomesCategoryBuilder.group(invisibleGroup.build());

			OptionGroup.Builder namespaceGroupBuilder = OptionGroup.createBuilder()
				.name(Component.translatable("gui.featurify.biomes.biome_group.title", LanguageUtil.translateId(null, biomeNamespace).getString()).withStyle(style -> style.withUnderlined(true)))
				.description(OptionDescription.of(Component.translatable("gui.featurify.placed_features.placed_feature_group.description", biomeNamespace)));

			for (var namespacebiome : namespaceBiomes.entrySet()) {
				var biomeData = namespacebiome.getValue();
				var biomeStringId = namespacebiome.getKey().toString();
				var biomeOption = addBiome(biomeData, biomeStringId);
				namespaceGroupBuilder.option(biomeOption);
				biomesOptions.add(biomeOption);
			}

			biomesCategoryBuilder.group(namespaceGroupBuilder.build());
		}

		var invisibleGroup = new InvisibleOptionGroup.Builder().name(Component.literal(""));
		invisibleGroup.option(YACLUtil.createEmptyLabelOption());
		biomesCategoryBuilder.group(invisibleGroup.build());
	}

	private static Option<Boolean> addBiome(
		BiomeData biomeData,
		String biomeId
	) {
		var biomeName = LanguageUtil.translateId("biome", biomeId);
		Component name;

		if(biomeData.isUsingDefaultReplacementBiome()) {
			name = biomeName;
		} else {
			var replacementBiomeName = LanguageUtil.translateId("biome", biomeData.getReplacementBiome());
			name = Component.translatable("gui.featurify.biomes.biome.detail_button.name", biomeName, replacementBiomeName);
		}

		var biomeOptionBuilder = Option.<Boolean>createBuilder()
			.name(name)
			.binding(
				true,
				() -> !biomeData.isDisabled(),
				isEnabled -> biomeData.setDisabled(!isEnabled)
			)
			.controller(opt -> ButtonControllerBuilder.create(opt, biomeId)
				.formatValue(val -> val ? Component.translatable("gui.featurify.label.enabled"):Component.translatable("gui.featurify.label.disabled"))
				.coloured(true)
				.openConfigCallback((screen, id) -> {
					var configScreen = FeaturifyClient.getConfigScreen();

					configScreen.savePendingChanges(screen);

					YACLScreen biomeScreen = BiomeConfigScreen.create(Featurify.getConfig(), id, screen);

					configScreen.switchScreen(screen, biomeScreen);
				}).buttonTooltip("gui.featurify.biomes.biome.detail_button.tooltip")
			);

		return biomeOptionBuilder.build();
	}
}
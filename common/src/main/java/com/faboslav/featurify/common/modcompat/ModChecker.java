package com.faboslav.featurify.common.modcompat;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.platform.PlatformHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Related code is based on The Bumblezone/Resourceful Lib mods with permissions from the authors
 *
 * @author TelepathicGrunt
 * <a href="https://github.com/TelepathicGrunt/Bumblezone">https://github.com/TelepathicGrunt/Bumblezone</a>
 * @author ThatGravyBoat
 * <a href="https://github.com/Team-Resourceful/ResourcefulLib">https://github.com/Team-Resourceful/ResourcefulLib</a>
 */
public final class ModChecker
{
	public static final List<ModCompat> CUSTOM_RESOURCE_PACK_PROVIDER_COMPATS = new ArrayList<>();

	public static void setupModCompat() {
		try {
			//? if global_packs {
			loadModCompat("globalpacks", () -> new GlobalPacksCompat());
			 //?}

			//? if open_loader {
			loadModCompat("openloader", () -> new OpenLoaderCompat());
			 //?}

			PlatformHooks.PLATFORM_COMPAT.setupPlatformModCompat();
		} catch (Throwable e) {
			Featurify.getLogger().error("Failed to setup mod compats");
			e.printStackTrace();
		}
	}

	public static void loadModCompat(String modId, Supplier<ModCompat> loader) {
		try {
			if (PlatformHooks.PLATFORM_HELPER.isModLoaded(modId)) {
				ModCompat compat = loader.get();
				if (compat.compatTypes().contains(ModCompat.Type.CUSTOM_RESOURCE_PACK_PROVIDERS)) {
					CUSTOM_RESOURCE_PACK_PROVIDER_COMPATS.add(compat);
				}
			}
		} catch (Throwable e) {
			Featurify.getLogger().error("Failed to load compat with " + modId);
			e.printStackTrace();
		}
	}
}

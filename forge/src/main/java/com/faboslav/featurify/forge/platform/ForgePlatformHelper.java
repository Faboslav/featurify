package com.faboslav.featurify.forge.platform;

import com.faboslav.featurify.common.Featurify;
import com.faboslav.featurify.common.platform.ModIconInfo;
import com.faboslav.featurify.common.platform.PlatformHelper;
import com.faboslav.featurify.common.util.FileUtil;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Optional;

public final class ForgePlatformHelper implements PlatformHelper
{
	public boolean isModLoaded(String modId) {
		return ModList.get().isLoaded(modId);
	}

	@Override
	public Optional<ModIconInfo> getModIconInfo(String modId) {
		return PlatformHelper.MOD_ICON_INFO_CACHE.computeIfAbsent(modId, id -> {
			var modContainer = ModList.get().getModContainerById(id);

			if(modContainer.isEmpty()) {
				return Optional.empty();
			}

			var iconPath = modContainer.get().getModInfo().getLogoFile();

			if(iconPath.isEmpty()) {
				return Optional.empty();
			}

			return FileUtil.getModIconInfo(id, iconPath, Optional.of(modContainer.get().getModInfo().getOwningFile().getFile().findResource(iconPath.get())));
		});
	}

	@Override
	@Nullable
	public String getModVersion() {
		return ModList.get().getModContainerById(Featurify.MOD_ID).map(modContainer -> modContainer.getModInfo().getVersion().toString()).orElse(null);
	}

	@Override
	public Path getConfigDirectory() {
		return FMLPaths.CONFIGDIR.get();
	}
}

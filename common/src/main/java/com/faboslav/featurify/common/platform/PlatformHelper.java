package com.faboslav.featurify.common.platform;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface PlatformHelper
{
	Map<String, Optional<ModIconInfo>> MOD_ICON_INFO_CACHE = new HashMap<>();

	boolean isModLoaded(String modId);

	String getModVersion();

	Optional<ModIconInfo> getModIconInfo(String modId);

	Path getConfigDirectory();
}


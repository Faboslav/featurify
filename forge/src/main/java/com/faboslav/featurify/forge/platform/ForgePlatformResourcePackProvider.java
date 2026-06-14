package com.faboslav.featurify.forge.platform;

import com.faboslav.featurify.common.platform.PlatformResourcePackProvider;
import net.minecraft.server.packs.repository.RepositorySource;

import java.util.ArrayList;

/**
 * Forge injects stuff into the vanilla registry
 */
public final class ForgePlatformResourcePackProvider implements PlatformResourcePackProvider
{
	@Override
	public ArrayList<RepositorySource> getPlatformResourcePackProviders() {
		ArrayList<RepositorySource> platformResourcePackProviders = new ArrayList<>();
		return platformResourcePackProviders;
	}
}

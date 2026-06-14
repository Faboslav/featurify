package com.faboslav.featurify.neoforge.platform;

import com.faboslav.featurify.common.platform.PlatformResourcePackProvider;
import net.minecraft.server.packs.repository.RepositorySource;

import java.util.ArrayList;

/**
 * NeoForge injects stuff into the vanilla registry
 */
public final class NeoForgePlatformResourcePackProvider implements PlatformResourcePackProvider
{
	@Override
	public ArrayList<RepositorySource> getPlatformResourcePackProviders() {
		ArrayList<RepositorySource> platformResourcePackProviders = new ArrayList<>();
		return platformResourcePackProviders;
	}
}

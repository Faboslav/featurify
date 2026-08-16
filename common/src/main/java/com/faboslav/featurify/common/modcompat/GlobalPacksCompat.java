package com.faboslav.featurify.common.modcompat;

//? if global_packs {

/*import com.faboslav.featurify.common.Featurify;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.RepositorySource;

import java.util.ArrayList;
import java.util.EnumSet;

public final class GlobalPacksCompat implements ModCompat
{
	@Override
	public EnumSet<Type> compatTypes() {
		return EnumSet.of(Type.CUSTOM_RESOURCE_PACK_PROVIDERS);
	}

	@Override
	public ArrayList<RepositorySource> getResourcePackProviders() {
		var resourcePackProviders = new ArrayList<RepositorySource>();

		try {
			Class<?> commonClass;
			try {
				// New versions
				commonClass = Class.forName("net.brazier_modding.gdarp.CommonClass");
			} catch (ClassNotFoundException e) {
				// Old versions
				commonClass = Class.forName("net.dark_roleplay.gdarp.CommonClass");
			}

			var method = commonClass.getMethod("getRepositorySource", PackType.class, boolean.class);

			resourcePackProviders.add((RepositorySource) method.invoke(null, PackType.SERVER_DATA, true));
			resourcePackProviders.add((RepositorySource) method.invoke(null, PackType.SERVER_DATA, false));

		} catch (Exception e) {
			Featurify.getLogger().error("Failed to load Global Packs repository source");
			e.printStackTrace();
		}

		return resourcePackProviders;
	}
}
*///?}

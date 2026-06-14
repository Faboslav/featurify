package com.faboslav.featurify.common.versions;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;

public class VersionedId
{
	public static ResourceLocation GetId(ResourceKey<?> resourceKey) {
		return resourceKey/*? if >= 1.21.11 {*//*.identifier()*//*?} else {*/.location()/*?}*/;
	}
}

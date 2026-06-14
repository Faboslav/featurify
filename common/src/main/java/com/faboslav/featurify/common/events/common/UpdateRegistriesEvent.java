package com.faboslav.featurify.common.events.common;

import com.faboslav.featurify.common.events.base.EventHandler;
import net.minecraft.core.HolderLookup;

public record UpdateRegistriesEvent(HolderLookup.Provider registryManager)
{
	public static final EventHandler<UpdateRegistriesEvent> EVENT = new EventHandler<>();
}

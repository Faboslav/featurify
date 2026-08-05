package com.faboslav.featurify.common.events.common;

import com.faboslav.featurify.common.events.base.EventHandler;
import net.minecraft.core.HolderLookup;

public record UpdateWorldgenDataEvent(HolderLookup.Provider registryManager)
{
	public static final EventHandler<UpdateWorldgenDataEvent> EVENT = new EventHandler<>();
}

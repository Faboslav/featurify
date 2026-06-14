package com.faboslav.featurify.common.events.common;

import com.faboslav.featurify.common.events.base.EventHandler;

public record LoadConfigEvent()
{
	public static final EventHandler<LoadConfigEvent> EVENT = new EventHandler<>();
}
package com.faboslav.featurify.common.events.common;

import com.faboslav.featurify.common.events.base.EventHandler;

public class LoadConfigEvent
{
	public static final EventHandler<LoadConfigEvent> EVENT = new EventHandler<>();

	private final boolean updateRegistries;

	public LoadConfigEvent()
	{
		this(false);
	}

	public LoadConfigEvent(boolean updateRegistries)
	{
		this.updateRegistries = updateRegistries;
	}

	public boolean updateRegistries()
	{
		return this.updateRegistries;
	}
}
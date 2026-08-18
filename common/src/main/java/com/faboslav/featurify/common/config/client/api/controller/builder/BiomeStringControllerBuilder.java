package com.faboslav.featurify.common.config.client.api.controller.builder;

import com.faboslav.featurify.common.config.client.api.controller.BiomeStringController;
import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.impl.controller.AbstractControllerBuilderImpl;

public final class BiomeStringControllerBuilder extends AbstractControllerBuilderImpl<String>
{
	private boolean allowEmpty = false;
	private boolean allowTags = false;

	private BiomeStringControllerBuilder(Option<String> option) {
		super(option);
	}

	@Override
	public Controller<String> build() {
		return new BiomeStringController(option, this.allowEmpty, this.allowTags);
	}

	public static BiomeStringControllerBuilder create(Option<String> option) {
		return new BiomeStringControllerBuilder(option);
	}

	public BiomeStringControllerBuilder allowEmpty() {
		this.allowEmpty = true;
		return this;
	}

	public BiomeStringControllerBuilder allowTags() {
		this.allowTags = false;
		return this;
	}
}
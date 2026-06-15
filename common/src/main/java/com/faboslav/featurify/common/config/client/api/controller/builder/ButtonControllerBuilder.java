package com.faboslav.featurify.common.config.client.api.controller.builder;

import com.faboslav.featurify.common.config.client.api.controller.ButtonController;
import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ValueFormatter;
import dev.isxander.yacl3.gui.controllers.BooleanController;
import dev.isxander.yacl3.impl.controller.BooleanControllerBuilderImpl;
import org.apache.commons.lang3.Validate;

public class ButtonControllerBuilder extends BooleanControllerBuilderImpl
{
	private final String id;
	private boolean coloured = false;
	private ValueFormatter<Boolean> formatter = BooleanController.ON_OFF_FORMATTER::apply;
	private ButtonController.OpenConfigCallback openConfigCallback;
	private String buttonTooltip;

	public ButtonControllerBuilder(Option<Boolean> option, String id) {
		super(option);

		this.id = id;
	}

	public ButtonControllerBuilder coloured(boolean coloured) {
		this.coloured = coloured;
		return this;
	}

	public ButtonControllerBuilder formatValue(ValueFormatter<Boolean> formatter) {
		Validate.notNull(formatter, "formatter cannot be null");

		this.formatter = formatter;
		return this;
	}

	public ButtonControllerBuilder onOffFormatter() {
		this.formatter = BooleanController.ON_OFF_FORMATTER::apply;
		return this;
	}

	public ButtonControllerBuilder yesNoFormatter() {
		this.formatter = BooleanController.YES_NO_FORMATTER::apply;
		return this;
	}

	public ButtonControllerBuilder trueFalseFormatter() {
		this.formatter = BooleanController.TRUE_FALSE_FORMATTER::apply;
		return this;
	}

	public ButtonControllerBuilder openConfigCallback(ButtonController.OpenConfigCallback openConfigCallback) {
		this.openConfigCallback = openConfigCallback;
		return this;
	}

	public ButtonControllerBuilder buttonTooltip(String buttonTooltip) {
		this.buttonTooltip = buttonTooltip;
		return this;
	}

	@Override
	public Controller<Boolean> build() {
		return new ButtonController(option, this.id, this.formatter::format, this.coloured, this.openConfigCallback, this.buttonTooltip);
	}

	public static ButtonControllerBuilder create(Option<Boolean> option, String id) {
		return new ButtonControllerBuilder(option, id);
	}
}

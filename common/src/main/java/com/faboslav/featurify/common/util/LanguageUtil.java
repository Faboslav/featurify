package com.faboslav.featurify.common.util;

import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Pattern;

public final class LanguageUtil
{
	private static final String FIRST_LETTER_REGEX = "\\b(.)(.*?)\\b";
	private static final Pattern FIRST_LETTER_PATTERN = Pattern.compile(FIRST_LETTER_REGEX);
	private static final Pattern NUMBER_PART_PATTERN = Pattern.compile("(.+?)_(\\d+)(?:_(.+))?$");

	public static MutableComponent translatePlacedFeatureId(String id) {
		String path = id.contains(":") ? id.split(":", 2)[1] : id;
		String suffix = "";

		var numberPartMatcher = NUMBER_PART_PATTERN.matcher(path);

		if (numberPartMatcher.matches()) {
			path = numberPartMatcher.group(1);

			if (numberPartMatcher.group(3) != null) {
				path += "_" + numberPartMatcher.group(3);
			}

			suffix = " (" + parseFeatureChance(numberPartMatcher.group(2)) + "%)";
		}

		if (path.endsWith("_checked")) {
			//path = path.substring(0, path.length() - "_checked".length());
		}

		return Component.literal(translateId(null, path).getString() + suffix);
	}

	private static String parseFeatureChance(String number) {
		if (number.length() > 2 && number.startsWith("0")) {
			return "0." + Integer.parseInt(number);
		}

		return String.valueOf(Integer.parseInt(number));
	}

	public static MutableComponent translateId(@Nullable String prefix, String id) {
		String langKey = transformToLangKey(prefix, id);
		Language language = Language.getInstance();

		if (!language.has(langKey)) {
			if (prefix == null) {
				langKey = id;
			} else if (id.contains(":")) {
				langKey = id.split(":")[1];
			}

			langKey = langKey.replace("_", " ").replace("/", " ");

			langKey = FIRST_LETTER_PATTERN
				.matcher(langKey)
				.replaceAll(matchResult -> matchResult.group(1).toUpperCase() + matchResult.group(2));
		}

		return Component.translatable(langKey);
	}

	private static String transformToLangKey(@Nullable String prefix, String identifier) {
		if (prefix == null) {
			return identifier.replace(":", ".");
		}

		return prefix + "." + identifier.replace(":", ".");
	}
}

package com.faboslav.featurify.common.util;

import com.faboslav.featurify.common.Featurify;
import net.minecraft.resources.Identifier;

import java.util.Comparator;

public final class Comparators
{
	public static final Comparator<String> ALPHABETICALL_ID_COMPARATOR = (firstId, secondId) -> {
		Identifier firstLocation = Featurify.makeNamespacedId(firstId);
		Identifier secondLocation = Featurify.makeNamespacedId(secondId);

		String firstNamespace = firstLocation.getNamespace();
		String secondNamespace = secondLocation.getNamespace();

		boolean firstIsMinecraft = firstNamespace.equals("minecraft");
		boolean secondIsMinecraft = secondNamespace.equals("minecraft");

		if (firstIsMinecraft && !secondIsMinecraft) {
			return -1;
		}

		if (!firstIsMinecraft && secondIsMinecraft) {
			return 1;
		}

		int namespaceComparison = firstNamespace.compareTo(secondNamespace);
		if (namespaceComparison != 0) {
			return namespaceComparison;
		}

		return firstLocation.getPath().compareTo(secondLocation.getPath());
	};

	public static final Comparator<String> ALPHABETICALL_NAMESPACE_COMPARATOR = (firstNamespace, secondNamespace) -> {
		boolean firstIsMinecraft = firstNamespace.equals("minecraft");
		boolean secondIsMinecraft = secondNamespace.equals("minecraft");

		if (firstIsMinecraft && !secondIsMinecraft) {
			return -1;
		}

		if (!firstIsMinecraft && secondIsMinecraft) {
			return 1;
		}

		return firstNamespace.compareTo(secondNamespace);
	};
}

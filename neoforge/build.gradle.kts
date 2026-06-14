plugins {
	`multiloader-loader`
	id("net.neoforged.moddev")
	id("dev.kikugie.fletching-table.neoforge") version "0.1.0-alpha.22"
}

neoForge {
	enable {
		version = commonMod.dep("neoforge")
	}
}

dependencies {
	// Required dependencies
	implementation("dev.isxander:yet-another-config-lib:${commonMod.dep("yacl")}-neoforge")

	// Global Packs
	commonMod.depOrNull("global_packs")?.let { globalPacksVersion ->
		implementation(commonMod.modrinth("globalpacks", globalPacksVersion)) { isTransitive = false }
	}

	// Open Loader
	commonMod.depOrNull("open_loader")?.let { openLoaderVersion ->
		if (commonMod.mc == "1.21.1") {
			implementation(
				group = "net.darkhax.openloader",
				name = "openloader-neoforge-${commonMod.mc}",
				version = openLoaderVersion
			)
		} else {
			implementation(
				group = "net.darkhax.openloader",
				name = "OpenLoader-NeoForge-${commonMod.mc}",
				version = openLoaderVersion
			)
		}
	}
}

neoForge {
	runs {
		register("client") {
			client()
			ideFolderName = "NeoForge"
			ideName = "NeoForge Client (${project.path})"
		}
		register("server") {
			server()
			ideFolderName = "NeoForge"
			ideName = "NeoForge Server (${project.path})"
		}
	}

	parchment {
		commonMod.depOrNull("parchment")?.let {
			mappingsVersion = it
			minecraftVersion = commonMod.mc
		}
	}

	mods {
		register(commonMod.id) {
			sourceSet(sourceSets.main.get())
		}
	}
}

sourceSets.main {
	resources.srcDir("src/generated/resources")
}

if (stonecutter.current.isActive) tasks.register("buildActive") {
	dependsOn("build")
}
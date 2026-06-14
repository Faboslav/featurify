plugins {
	id("multiloader-common")
	id("fabric-loom-compat")
	id("dev.kikugie.fletching-table.fabric") version "0.1.0-alpha.22"
}

stonecutter {
	constants["global_packs"] = rootProject.project(stonecutter.current.project).property("deps.global_packs").toString() != ""
	constants["open_loader"] = rootProject.project(stonecutter.current.project).property("deps.open_loader").toString() != ""
}

fletchingTable {
	j52j.register("main") {
		extension("json", "**/*.json5")
	}
}

if (stonecutter.eval(commonMod.mc, "<=1.21.11")) {
	loom {
		mixin {
			useLegacyMixinAp = false
		}
	}
}

dependencies {
	minecraft("com.mojang:minecraft:${commonMod.mc}")

	if (stonecutter.eval(commonMod.mc, "<=1.21.11")) {
		mappings(loom.layered {
			officialMojangMappings()
			commonMod.depOrNull("parchment")?.let { parchmentVersion ->
				parchment("org.parchmentmc.data:parchment-${commonMod.mc}:$parchmentVersion@zip")
			}
		})
	}

	compileOnly("org.spongepowered:mixin:0.8.5")

	"io.github.llamalad7:mixinextras-common:0.4.1".let {
		compileOnly(it)
		annotationProcessor(it)
	}

	modCompileOnly("net.fabricmc:fabric-loader:${commonMod.dep("fabric_loader")}")
	modCompileOnly("dev.isxander:yet-another-config-lib:${commonMod.dep("yacl")}-fabric")

	// Global Packs
	commonMod.depOrNull("global_packs")?.let { globalPacksVersion ->
		modImplementation(commonMod.modrinth("globalpacks", globalPacksVersion))
	}

	// Open Loader
	commonMod.depOrNull("open_loader")?.let { openLoaderVersion ->
		if (commonMod.mc == "1.21.1") {
			modImplementation(
				group = "net.darkhax.openloader",
				name = "openloader-common-${commonMod.mc}",
				version = openLoaderVersion
			)
		} else {
			modImplementation(
				group = "net.darkhax.openloader",
				name = "OpenLoader-Common-${commonMod.mc}",
				version = openLoaderVersion
			)
		}
	}
}

val commonJava: Configuration by configurations.creating {
	isCanBeResolved = false
	isCanBeConsumed = true
}

val commonResources: Configuration by configurations.creating {
	isCanBeResolved = false
	isCanBeConsumed = true
}

artifacts {
	afterEvaluate {
		val mainSourceSet = sourceSets.main.get()

		mainSourceSet.java.sourceDirectories.files.forEach {
			add(commonJava.name, it)
		}

		mainSourceSet.resources.sourceDirectories.files.forEach {
			add(commonResources.name, it)
		}
	}
}
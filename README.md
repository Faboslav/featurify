<br>

<center>
	<img title="Featurify" src="https://raw.githubusercontent.com/Faboslav/featurify/master/.github/assets/images/logo/logo.png" alt="Featurify" width="743">
</center>

<br>

<center>
	<a style="text-decoration: none;" href="https://ko-fi.com/faboslav">
		<img src="https://img.shields.io/static/v1?label=&message=Buy Me a Coffee&color=5b9c51&labelColor=5b9c51&logoColor=ffffff&style=for-the-badge&logo=ko-fi" alt="Buy Me a Coffee">
	</a>
	<a style="text-decoration: none;" href="https://www.patreon.com/Faboslav">
		<img src="https://img.shields.io/static/v1?label=&message=Become a Patron&color=5b9c51&labelColor=5b9c51&logoColor=ffffff&style=for-the-badge&logo=patreon" alt="Become a Patreon">
	</a>
	<a style="text-decoration: none;" href="https://discord.com/invite/QGwFvvMQCn">
		<img src="https://img.shields.io/discord/924964658169913404?style=for-the-badge&logo=discord&logoColor=ffffff&label=Join Our Discord&labelColor=5b9c51&color=5b9c51" alt="Join Our Discord">
	</a>
</center>

<br>

Featurify is a worldgen feature configuration mod that eliminates the need for datapacks. Add, remove, and tweak features in any biome.

<p>
	<img src="https://raw.githubusercontent.com/Faboslav/featurify/master/.github/assets/images/wiki/placed_features/placed_features_settings.webp" title="Placed Features Settings" alt="Placed Features Settings">
</p>

**Currently, it is possible to:**

* **Globally disable all placed features:** Easily disable all placed features across your world with a single setting, simplifying your world generation process.
* **Disable individual placed features:** Disable specific features individually, giving you precise control over which ones generate in your world.
* **Manage biomes for specific features:** Customize the list of biomes for individual features, add new.

Since this is the initial update, you can expect a lot of more features in the future.
<br>

**Available Commands:**

* **/featurify dump:** Dumps complete config file with default settings to the file.

<br>

# 💡 How to use the mod

This mod is designed for in-game configuration, so it requires the **[YACL (YetAnotherConfigLib)](https://modrinth.com/mod/yacl)** mod.

## Fabric/Quilt

If you’re using Fabric or Quilt, you’ll also need the **[Mod Menu](https://modrinth.com/mod/modmenu)**
mod or another mod that provides access to mod configs.

## NeoForge/Forge

No extra mods are needed.

<br>

# 🖥️ In-Game configuration

All placed feature settings can be configured directly in game through intuitive configuration screens. Changes are applied immediately, making it easy to add, remove, and modify placed features without editing files or creating datapacks.

![Placed features settings](https://raw.githubusercontent.com/Faboslav/featurify/master/.github/assets/images/wiki/placed_features/placed_feature_settings.webp)

<br>

# 📝 JSON file configuration

All configurations mentioned in the `In-Game` section of this guide are mirrored and saved in a JSON file located at
`config/featurify.json`.
This file is particularly useful for managing configurations on the server side. For that case it is recommended to
configure everything based on the `In-Game` section of this guide.

# ⚙️ Compatibility

Featurify is designed to be fully compatible with most of the worldgen mods and datapacks, and it currently
offers enhanced compatibility with the following:

### Global datapack loaders:

* [Paxi](https://modrinth.com/mod/paxi)
* [Open Loader](https://modrinth.com/mod/open-loader)
* [Global Packs](https://modrinth.com/mod/globalpacks)
* [Global Datapacks](https://modrinth.com/mod/datapacks)

Since feature generation is a complex system, things can occasionally break, especially with mods that implement their own custom feature generation logic or heavily alter vanilla generation behavior.

<br>

# 💬 Community

Feel free to <a href="https://discord.com/invite/QGwFvvMQCn">join our community at the discord server</a> to chat, share your
creations, ask any question or to simply be updated about the latest development of the mod and notified when the new
release is out. Also don't hesitate to <a href="https://github.com/Faboslav/featurify/issues">report any crash or bug
via GitHub issues</a>.

<br>

# 👋 Support

I will continue developing my mods as a hobby because I truly enjoy it. If you'd like to support me, you can do so
on [Patreon](https://www.patreon.com/Faboslav) or [Ko-fi](https://ko-fi.com/faboslav). Your support is greatly
appreciated.

<br>

# 📜 License

The mod is licensed with [CC BY-NC-ND 4.0](https://raw.githubusercontent.com/Faboslav/featurify/master/LICENSE.txt)
license.

Please feel free to explore my code for examples of how I've tackled and solved various challenges while developing this
mod. You're welcome to incorporate code snippets into your own projects. Also feel free to use this mod in any modpack (
although credit/link back to this page will be greatly appreciated).
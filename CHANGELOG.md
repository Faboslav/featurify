## 2.0.8

- Fixed TerraBlender compat (again, it's hard :/)

## 2.0.7

- A lot of other quality of life fixes both in the UI and config serialization
- Fixed "Oh The Biomes We've Gone" compat

## 2.0.6

- Fixed crash that occurred when mod ran for the first time without an existing config file and attempted to sync all things to the registries

## 2.0.5

- Added Blueprint compat

## 2.0.4

- Fixed bug when modded biomes (like Biomes O' Plenty biomes) generated as plain vanilla biomes

## 2.0.3

- Fixed bug when modded biomes (like Enderscape biomes) generated as plain vanilla biomes

## 2.0.2

- Added lithostitched compat
- Fixed TerraBlender compat (again)

## 2.0.1

- Fixed TerraBlender compat mixin related crash

## 2.0.0

- Added "/featurify config sync toServer" command
- Added "/featurify config sync fromServer" command
- Added "/featurify config status" command
- Added new biome config screen, which currently allows:
  - disable specific biome
  - replace specific biome with another specific biome (even from different dimension)

TerraBlender compatibility is already in the mod (but it's experimental)

## 1.0.4

- Fixed crash caused by loading non-existing or invalid world generation features

## 1.0.3

- Fixed crash caused by cyclic world generation features
- Improved feature order in UI

## 1.0.2

- Added configuration of weighted (sub)features
- Fixed load/save
- Removed more debug messages

## 1.0.1

- Fixed crash related to other mods using custom generation steps
- Removed debug messages

## 1.0.0

- Initial release
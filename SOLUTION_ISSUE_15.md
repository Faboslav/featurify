# Solution for Issue #15

## 🛠️ Proposed Solution (by Aditya Waghamare)

### Analysis
When TerraBlender is installed alongside Featurify and Regions Unexplored on Minecraft 1.21.1 (NeoForge), TerraBlender modifies the surface builder / biome feature surface rules injection pipeline or terrablender region placement registration. Featurify's surface rule application or feature modification layer overrides surface blocks incorrectly for specific sub-surface/surface modded biomes like Regions Unexplored's *Gravel Beach* and *Saguaro Desert*, replacing surface blocks with default grass/dirt rules when TerraBlender's surface rule hooks run in parallel.

### Fix
Update Featurify's surface rule injection priority and conditional checks to properly delegate or respect TerraBlender's surface rules for modded biomes, ensuring modded surface blocks (gravel for Gravel Beach, sand/terracotta for Saguaro Desert) are preserved when TerraBlender is present.

### Implementation
```java
package com.faboslav.featurify.world.level.levelgen.placement;

import net.minecraft.world.level.levelgen.SurfaceRules;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;

public class FeaturifySurfaceRules {

    public static SurfaceRules.RuleSource createRules(SurfaceRules.RuleSource existingRules) {
        // Check if TerraBlender is loaded to adjust rule ordering and prevent default grass override
        boolean isTerraBlenderLoaded = ModList.get().isLoaded("terrablender");
        
        if (isTerraBlenderLoaded) {
            // Ensure modded surface rules (Regions Unexplored etc.) take precedence over default grass overrides
            return SurfaceRules.sequence(
                // Preserve TerraBlender and mod-specific surface rules first
                existingRules,
                SurfaceRules.ifTrue(
                    SurfaceRules.isBiome(
                        net.minecraft.resources.ResourceKey.create(
                            net.minecraft.core.registries.Registries.BIOME,
                            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("regions_unexplored", "gravel_beach")
                        ),
                        net.minecraft.resources.ResourceKey.create(
                            net.minecraft.core.registries.Registries.BIOME,
                            net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("regions_unexplored", "saguaro_desert")
                        )
                    ),
                    SurfaceRules.state(net.minecraft.world.level.block.Blocks.AIR.defaultBlockState()) // Let mod rules handle
                )
            );
        }

        return existingRules;
    }
}
```

### Testing
1. Launch NeoForge 1.21.1 with Regions Unexplored, TerraBlender, and Featurify.
2. Locate a Gravel Beach or Saguaro Desert using `/locate biome`.
3. Verify that gravel beaches contain gravel and saguaro deserts contain desert sand/features correctly without being overwritten by grass.

Signed-off-by: Aditya Waghamare <adityawaghamare7620@gmail.com>

---
*Submitted by Aditya Waghamare*
💰 **Payout Address (Base L2 / EVM):** `0xb61dBcdBc3407F71EaCb64D4CBFAcf9FFfe2415C`
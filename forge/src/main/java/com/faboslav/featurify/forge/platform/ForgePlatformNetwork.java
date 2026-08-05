package com.faboslav.featurify.forge.platform;

import com.faboslav.featurify.common.network.base.Network;
import com.faboslav.featurify.common.network.platform.NetworkPlatform;
import com.faboslav.featurify.forge.network.ForgeNetwork;
import net.minecraft.resources.ResourceLocation;

public final class ForgePlatformNetwork implements NetworkPlatform {
    @Override
    public Network create(ResourceLocation channel, int protocolVersion) {
        return new ForgeNetwork(channel, protocolVersion);
    }
}

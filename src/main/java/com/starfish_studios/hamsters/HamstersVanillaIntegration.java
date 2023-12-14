package com.starfish_studios.hamsters;

import com.starfish_studios.hamsters.client.renderer.*;
import com.starfish_studios.hamsters.registry.HamstersBlocks;
import com.starfish_studios.hamsters.registry.HamstersEntityType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;

public class HamstersVanillaIntegration {


    public static void serverInit() {
    }

    @Environment(EnvType.CLIENT)
    public static class Client {

        public static void clientInit() {
            registerModelLayers();
            registerBlockRenderLayers();
        }

        private static void registerModelLayers() {
            EntityRendererRegistry.register(HamstersEntityType.HAMSTER, HamsterRenderer::new);
        }

        private static void registerBlockRenderLayers() {
            BlockRenderLayerMap.INSTANCE.putBlock(HamstersBlocks.TUNNEL, RenderType.translucent());
        }
    }
}

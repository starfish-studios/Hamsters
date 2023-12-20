package com.starfish_studios.hamsters;

import com.starfish_studios.hamsters.client.renderer.*;
import com.starfish_studios.hamsters.registry.HamstersBlocks;
import com.starfish_studios.hamsters.registry.HamstersEntityType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public class HamstersVanillaIntegration {


    public static void serverInit() {
    }

    @Environment(EnvType.CLIENT)
    public static class Client {

        public static void clientInit() {
            registerModelLayers();
            registerBlockRenderLayers();
            registerRenderers();
        }

        private static void registerRenderers() {
            registerEntityRenderers(HamstersEntityType.SEAT, SeatRenderer::new);
        }

        private static void registerModelLayers() {
            EntityRendererRegistry.register(HamstersEntityType.HAMSTER, HamsterRenderer::new);
        }

        private static void registerBlockRenderLayers() {
            // BlockRenderLayerMap.INSTANCE.putBlock(HamstersBlocks.TUNNEL, RenderType.translucent());
        }
    }

    public static <T extends Entity> void registerEntityRenderers(Supplier<EntityType<T>> type, EntityRendererProvider<T> renderProvider) {
        EntityRendererRegistry.register(type.get(), renderProvider);
    }
}

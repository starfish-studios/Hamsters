package com.starfish_studios.hamsters;

import com.starfish_studios.hamsters.client.renderer.*;
import com.starfish_studios.hamsters.registry.HamstersBlocks;
import com.starfish_studios.hamsters.registry.HamstersEntityType;
import com.starfish_studios.hamsters.registry.HamstersMenuType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

//This class is used to prevent clutter from the main classes for the Client and Server of the mod

public class HamstersVanillaIntegration {


    public static void serverInit() {
    }

    @Environment(EnvType.CLIENT)
    public static class Client {

        public static void clientInit() {
            registerModelLayers();
            registerScreens();
            registerBlockRenderLayers();
        }

        //client methods
        private static void registerModelLayers() {
            EntityRendererRegistry.register(HamstersEntityType.HAMSTER, HamsterRenderer::new);
        }

        private static void registerScreens() {
        }

        private static void registerBlockRenderLayers() {
            BlockRenderLayerMap.INSTANCE.putBlock(HamstersBlocks.TUNNEL, RenderType.translucent());
        }
    }
}

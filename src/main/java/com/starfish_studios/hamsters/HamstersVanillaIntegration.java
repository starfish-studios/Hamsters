package com.starfish_studios.hamsters;

import com.starfish_studios.hamsters.client.model.shoulder.LeftSittingHamsterModel;
import com.starfish_studios.hamsters.client.renderer.*;
import com.starfish_studios.hamsters.registry.HamstersBlocks;
import com.starfish_studios.hamsters.registry.HamstersEntityType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.function.Supplier;

public class HamstersVanillaIntegration {
    public static final ModelLayerLocation HAMSTER_LAYER = new ModelLayerLocation(new ResourceLocation(Hamsters.MOD_ID, "sitting_hamster"), "main");


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
            EntityRendererRegistry.register(HamstersEntityType.HAMSTER_NEW, HamsterNewRenderer::new);
            EntityRendererRegistry.register(HamstersEntityType.HAMSTER_BALL, HamsterBallRenderer::new);

            EntityModelLayerRegistry.registerModelLayer(HAMSTER_LAYER, LeftSittingHamsterModel::createBodyLayer);
        }

        private static void registerBlockRenderLayers() {
            BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(),
                    HamstersBlocks.CAGE_PANEL,
                    HamstersBlocks.RED_CAGE_PANEL,
                    HamstersBlocks.ORANGE_CAGE_PANEL,
                    HamstersBlocks.YELLOW_CAGE_PANEL,
                    HamstersBlocks.LIME_CAGE_PANEL,
                    HamstersBlocks.GREEN_CAGE_PANEL,
                    HamstersBlocks.CYAN_CAGE_PANEL,
                    HamstersBlocks.BLUE_CAGE_PANEL,
                    HamstersBlocks.LIGHT_BLUE_CAGE_PANEL,
                    HamstersBlocks.PINK_CAGE_PANEL,
                    HamstersBlocks.MAGENTA_CAGE_PANEL,
                    HamstersBlocks.PURPLE_CAGE_PANEL,
                    HamstersBlocks.WHITE_CAGE_PANEL,
                    HamstersBlocks.LIGHT_GRAY_CAGE_PANEL,
                    HamstersBlocks.GRAY_CAGE_PANEL,
                    HamstersBlocks.BLACK_CAGE_PANEL,
                    HamstersBlocks.BROWN_CAGE_PANEL,
                    HamstersBlocks.RED_HAMSTER_BOWL,
                    HamstersBlocks.ORANGE_HAMSTER_BOWL,
                    HamstersBlocks.YELLOW_HAMSTER_BOWL,
                    HamstersBlocks.LIME_HAMSTER_BOWL,
                    HamstersBlocks.GREEN_HAMSTER_BOWL,
                    HamstersBlocks.CYAN_HAMSTER_BOWL,
                    HamstersBlocks.BLUE_HAMSTER_BOWL,
                    HamstersBlocks.LIGHT_BLUE_HAMSTER_BOWL,
                    HamstersBlocks.PINK_HAMSTER_BOWL,
                    HamstersBlocks.MAGENTA_HAMSTER_BOWL,
                    HamstersBlocks.PURPLE_HAMSTER_BOWL,
                    HamstersBlocks.WHITE_HAMSTER_BOWL,
                    HamstersBlocks.LIGHT_GRAY_HAMSTER_BOWL,
                    HamstersBlocks.GRAY_HAMSTER_BOWL,
                    HamstersBlocks.BLACK_HAMSTER_BOWL,
                    HamstersBlocks.BROWN_HAMSTER_BOWL
            );
        }
    }

    public static <T extends Entity> void registerEntityRenderers(Supplier<EntityType<T>> type, EntityRendererProvider<T> renderProvider) {
        EntityRendererRegistry.register(type.get(), renderProvider);
    }
}

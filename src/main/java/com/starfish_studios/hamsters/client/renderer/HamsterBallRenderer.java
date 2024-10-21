package com.starfish_studios.hamsters.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.starfish_studios.hamsters.client.model.HamsterBallModel;
import com.starfish_studios.hamsters.entity.Hamster;
import com.starfish_studios.hamsters.entity.HamsterBall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Quaternionf;
import software.bernie.example.entity.DynamicExampleEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HamsterBallRenderer extends GeoEntityRenderer<HamsterBall> {

    public HamsterBallRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HamsterBallModel());
    }

    @Override
    public void preRender(PoseStack poseStack, HamsterBall animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, OverlayTexture.NO_OVERLAY, red, green, blue, alpha);
    }

    @Override
    public void render(HamsterBall animatable, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        super.render(animatable, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }
}

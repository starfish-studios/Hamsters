package com.starfish_studios.hamsters.client.renderer.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.hamsters.entity.Hamster;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.Map;

import static com.starfish_studios.hamsters.Hamsters.MOD_ID;

public class HamsterMarkingLayer extends GeoRenderLayer<Hamster> {

    public HamsterMarkingLayer(GeoRenderer<Hamster> entityRendererIn) {
        super(entityRendererIn);
    }


    private static final Map<Hamster.Marking, ResourceLocation> TEXTURES = Util.make(Maps.newHashMap(), hashMap -> {
        hashMap.put(Hamster.Marking.BLANK, new ResourceLocation(MOD_ID, "textures/entity/hamster/blank.png"));
        hashMap.put(Hamster.Marking.BANDED, new ResourceLocation(MOD_ID, "textures/entity/hamster/banded.png"));
        hashMap.put(Hamster.Marking.DOMINANT_SPOTS, new ResourceLocation(MOD_ID, "textures/entity/hamster/dominant_spots.png"));
        hashMap.put(Hamster.Marking.ROAN, new ResourceLocation(MOD_ID, "textures/entity/hamster/roan.png"));
        hashMap.put(Hamster.Marking.BELLY, new ResourceLocation(MOD_ID, "textures/entity/hamster/belly.png"));
    });

    public ResourceLocation getTextureResource(Hamster animatable) {
        if (Hamster.Marking.byId(animatable.getMarking()) != Hamster.Marking.BLANK) {
            return TEXTURES.get(Hamster.Marking.byId(animatable.getMarking()));
        }
        return new ResourceLocation(MOD_ID, "textures/entity/hamster/blank.png");
    }

    @Override
    public void render(PoseStack poseStack, Hamster animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (Hamster.Marking.byId(animatable.getMarking()) != Hamster.Marking.BLANK && !animatable.isBaby()) {
            RenderType renderType1 = RenderType.entityTranslucent(getTextureResource(animatable));
            this.getRenderer().actuallyRender(poseStack, animatable, bakedModel, renderType, bufferSource, bufferSource.getBuffer(renderType1), true, partialTick, packedLight, packedOverlay, 1f, 1f, 1f, 1f);
        }
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }

}

package com.starfish_studios.hamsters.client.renderer.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.hamsters.entity.HamsterNew;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

import java.util.Map;

import static com.starfish_studios.hamsters.Hamsters.MOD_ID;

public class HamsterNewMarkingLayer extends GeoRenderLayer<HamsterNew> {

    public HamsterNewMarkingLayer(GeoRenderer<HamsterNew> entityRendererIn) {
        super(entityRendererIn);
    }


    private static final Map<HamsterNew.Marking, ResourceLocation> TEXTURES = Util.make(Maps.newHashMap(), hashMap -> {
        hashMap.put(HamsterNew.Marking.BLANK, new ResourceLocation(MOD_ID, "textures/entity/hamster/blank.png"));
        hashMap.put(HamsterNew.Marking.BANDED, new ResourceLocation(MOD_ID, "textures/entity/hamster/banded.png"));
        hashMap.put(HamsterNew.Marking.DOMINANT_SPOTS, new ResourceLocation(MOD_ID, "textures/entity/hamster/dominant_spots.png"));
        hashMap.put(HamsterNew.Marking.ROAN, new ResourceLocation(MOD_ID, "textures/entity/hamster/roan.png"));
        hashMap.put(HamsterNew.Marking.BELLY, new ResourceLocation(MOD_ID, "textures/entity/hamster/belly.png"));
    });

    public ResourceLocation getTextureResource(HamsterNew animatable) {
        if (HamsterNew.Marking.byId(animatable.getMarking()) != HamsterNew.Marking.BLANK) {
            return TEXTURES.get(HamsterNew.Marking.byId(animatable.getMarking()));
        }
        return new ResourceLocation(MOD_ID, "textures/entity/hamster/blank.png");
    }

    @Override
    public void render(PoseStack poseStack, HamsterNew animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (HamsterNew.Marking.byId(animatable.getMarking()) != HamsterNew.Marking.BLANK && !animatable.isBaby()) {
            RenderType renderType1 = RenderType.entityTranslucent(getTextureResource(animatable));
            this.getRenderer().actuallyRender(poseStack, animatable, bakedModel, renderType, bufferSource, bufferSource.getBuffer(renderType1), true, partialTick, packedLight, packedOverlay, 1f, 1f, 1f, 1f);
        }
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
    }

}

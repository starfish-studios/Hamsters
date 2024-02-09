package com.starfish_studios.hamsters.client.renderer;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.starfish_studios.hamsters.client.model.HamsterModel;
import com.starfish_studios.hamsters.entity.Hamster;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import java.util.Map;

import static com.starfish_studios.hamsters.Hamsters.MOD_ID;

public class HamsterRenderer extends GeoEntityRenderer<Hamster> {

    public HamsterRenderer(EntityRendererProvider.Context context) {
        super(context, new HamsterModel());
        this.shadowRadius = 0.3F;
    }

    private static final Map<Hamster.Variant, ResourceLocation> TEXTURES = Util.make(Maps.newHashMap(), hashMap -> {
        hashMap.put(Hamster.Variant.WHITE, new ResourceLocation(MOD_ID, "textures/entity/hamster/white.png"));
        hashMap.put(Hamster.Variant.PEACHES_AND_CREAM, new ResourceLocation(MOD_ID, "textures/entity/hamster/peaches_and_cream.png"));
        hashMap.put(Hamster.Variant.ORANGE, new ResourceLocation(MOD_ID, "textures/entity/hamster/orange.png"));
        hashMap.put(Hamster.Variant.GREY_WHITE, new ResourceLocation(MOD_ID, "textures/entity/hamster/grey_white.png"));
        hashMap.put(Hamster.Variant.BROWN, new ResourceLocation(MOD_ID, "textures/entity/hamster/brown.png"));
        hashMap.put(Hamster.Variant.BLACK_WHITE, new ResourceLocation(MOD_ID, "textures/entity/hamster/black_white.png"));
        hashMap.put(Hamster.Variant.BLACK, new ResourceLocation(MOD_ID, "textures/entity/hamster/black.png"));
    });

    @Override
    public @NotNull ResourceLocation getTextureLocation(Hamster entity) {
        return TEXTURES.get(entity.getVariant());
    }

    @Override
    public void render(Hamster animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
            if (animatable.isBaby()) {
                poseStack.scale(0.6F, 0.6F, 0.6F);
            } else {
                poseStack.scale(1F, 1F, 1F);
            }
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

}

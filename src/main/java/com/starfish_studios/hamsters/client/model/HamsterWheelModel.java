package com.starfish_studios.hamsters.client.model;

import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.block.entity.HamsterWheelBlockEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HamsterWheelModel extends AnimatedGeoModel<HamsterWheelBlockEntity> {
    @Override
    public ResourceLocation getAnimationResource(HamsterWheelBlockEntity hamsterWheel) {
        return new ResourceLocation(Hamsters.MOD_ID, "animations/hamster_wheel.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(HamsterWheelBlockEntity object) {
        return new ResourceLocation(Hamsters.MOD_ID, "geo/block/hamster_wheel.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HamsterWheelBlockEntity object) {
        return new ResourceLocation(Hamsters.MOD_ID, "");
    }
}
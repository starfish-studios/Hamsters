package com.starfish_studios.hamsters.client.model.item;

import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.item.HamsterWheelItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HamsterWheelItemModel extends AnimatedGeoModel<HamsterWheelItem> {
    @Override
    public ResourceLocation getAnimationResource(HamsterWheelItem hamsterWheel) {
        return new ResourceLocation(Hamsters.MOD_ID, "animations/hamster_wheel.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(HamsterWheelItem object) {
        return new ResourceLocation(Hamsters.MOD_ID, "geo/block/hamster_wheel.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HamsterWheelItem object) {
        return new ResourceLocation(Hamsters.MOD_ID, "textures/block/hamster_wheel.png");
    }
}

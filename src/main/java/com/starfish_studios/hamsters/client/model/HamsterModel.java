package com.starfish_studios.hamsters.client.model;

import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.entity.Hamster;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import static com.starfish_studios.hamsters.Hamsters.MOD_ID;

public class HamsterModel extends AnimatedGeoModel<Hamster> {
    @Override
    public ResourceLocation getModelResource(Hamster object) {
        return new ResourceLocation(MOD_ID, "geo/entity/hamster.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Hamster animatable) {
        return new ResourceLocation(MOD_ID, "textures/entity/hamster/orange.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Hamster animatable) {
        return new ResourceLocation(Hamsters.MOD_ID, "animations/hamster.animation.json");
    }

    @Override
    public void setCustomAnimations(Hamster animatable, int instanceId, AnimationEvent animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;
        IBone head = this.getAnimationProcessor().getBone("head");
        IBone sleep = this.getAnimationProcessor().getBone("sleep");
        IBone cheeks = this.getAnimationProcessor().getBone("cheeks");

        cheeks.setHidden(animatable.getMainHandItem().isEmpty());


        // Ensures there are no strange eye glitches when the hamster is sleeping or awake.
        if (animatable.isSleeping()) {
            sleep.setHidden(false);
        } else {
            sleep.setHidden(true);
        }
        if (animatable.isBaby()) {
            head.setScaleX(1.4F);
            head.setScaleY(1.4F);
            head.setScaleZ(1.4F);
        } else {
            // Setting values to 1.0F here prevents conflicts with GeckoLib and optimization mods.
            // Without this, adult heads will also size up. It's a bug :(
            head.setPositionY(0F);
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }
    }


}

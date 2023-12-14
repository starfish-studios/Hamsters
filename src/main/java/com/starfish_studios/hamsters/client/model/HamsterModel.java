package com.starfish_studios.hamsters.client.model;

import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.block.entity.HamsterWheelBlockEntity;
import com.starfish_studios.hamsters.entity.Hamster;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import static com.starfish_studios.hamsters.Hamsters.MOD_ID;

public class HamsterModel extends DefaultedEntityGeoModel<Hamster> {

    public HamsterModel() {
        super(new ResourceLocation(MOD_ID, "hamster"), true);
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
    public void setCustomAnimations(Hamster animatable, long instanceId, AnimationState<Hamster> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone sleep = this.getAnimationProcessor().getBone("sleep");
        CoreGeoBone cheeks = this.getAnimationProcessor().getBone("cheeks");

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
            head.setPosY(0F);
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }
    }


}

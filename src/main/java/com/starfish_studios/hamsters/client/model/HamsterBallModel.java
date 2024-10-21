package com.starfish_studios.hamsters.client.model;

import com.mojang.math.Axis;
import com.starfish_studios.hamsters.entity.Hamster;
import com.starfish_studios.hamsters.entity.HamsterBall;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import static com.starfish_studios.hamsters.Hamsters.MOD_ID;

public class HamsterBallModel extends DefaultedEntityGeoModel<HamsterBall> {

    public HamsterBallModel() {
        super(new ResourceLocation(MOD_ID, "hamster_ball"), true);
    }

    @Override
    public ResourceLocation getModelResource(HamsterBall animatable) {
        return new ResourceLocation(MOD_ID, "geo/entity/hamster_ball.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HamsterBall animatable) {
        return new ResourceLocation(MOD_ID, "textures/entity/ball/blue.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HamsterBall animatable) {
        return new ResourceLocation(MOD_ID, "animations/hamster_ball.animation.json");
    }


    @Override
    public RenderType getRenderType(HamsterBall animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void setCustomAnimations(HamsterBall animatable, long instanceId, AnimationState<HamsterBall> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);



        if (animationState == null) return;

        CoreGeoBone root = this.getAnimationProcessor().getBone("root");

//        if (animatable.getDeltaMovement().x() != 0 || animatable.getDeltaMovement().z() != 0) {
//            root.setRotX(15);
//        }
    }


}

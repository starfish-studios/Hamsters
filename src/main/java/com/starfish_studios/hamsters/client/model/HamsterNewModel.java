package com.starfish_studios.hamsters.client.model;

import com.starfish_studios.hamsters.entity.HamsterNew;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

import static com.starfish_studios.hamsters.Hamsters.MOD_ID;

public class HamsterNewModel extends DefaultedEntityGeoModel<HamsterNew> {

    public HamsterNewModel() {
        super(new ResourceLocation(MOD_ID, "hamster"), true);
    }

    @Override
    public ResourceLocation getModelResource(HamsterNew animatable) {
        return animatable.isBaby() ? new ResourceLocation(MOD_ID, "geo/entity/pinkie.geo.json") : new ResourceLocation(MOD_ID, "geo/entity/hamster.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HamsterNew animatable) {
        if (animatable.isBaby()) {
            return new ResourceLocation(MOD_ID, "textures/entity/hamster/pinkie.png");
        }
        return switch (animatable.getVariant()) {
            case 0 -> new ResourceLocation(MOD_ID, "textures/entity/hamster/white.png");
            case 1 -> new ResourceLocation(MOD_ID, "textures/entity/hamster/cream.png");
            case 2 -> new ResourceLocation(MOD_ID, "textures/entity/hamster/champagne.png");
            case 3 -> new ResourceLocation(MOD_ID, "textures/entity/hamster/silver_dove.png");
            case 4 -> new ResourceLocation(MOD_ID, "textures/entity/hamster/dove.png");
            case 5 -> new ResourceLocation(MOD_ID, "textures/entity/hamster/chocolate.png");
            case 6 -> new ResourceLocation(MOD_ID, "textures/entity/hamster/black.png");
            default -> throw new IllegalStateException("Unexpected value: " + animatable.getVariant());
        };
    }

    @Override
    public ResourceLocation getAnimationResource(HamsterNew animatable) {
        return new ResourceLocation(MOD_ID, "animations/hamster.animation.json");
    }

    @Override
    public RenderType getRenderType(HamsterNew animatable, ResourceLocation texture) {
        return RenderType.entitySolid(texture);
    }

    @Override
    public void setCustomAnimations(HamsterNew animatable, long instanceId, AnimationState<HamsterNew> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        if (animationState == null) return;
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        CoreGeoBone sleep = this.getAnimationProcessor().getBone("sleep");
        CoreGeoBone cheeks = this.getAnimationProcessor().getBone("cheeks");

        cheeks.setHidden(animatable.getMainHandItem().isEmpty());


        // Ensures there are no strange eye glitches when the hamster is sleeping or awake.
        if (animatable.isSleeping() && !animatable.isBaby()) {
            sleep.setHidden(false);
        } else if (!animatable.isSleeping() && !animatable.isBaby()) {
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

package com.starfish_studios.hamsters.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.starfish_studios.hamsters.client.model.HamsterNewModel;
import com.starfish_studios.hamsters.client.renderer.layers.HamsterNewMarkingLayer;
import com.starfish_studios.hamsters.entity.HamsterNew;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.ItemArmorGeoLayer;

public class HamsterNewRenderer extends GeoEntityRenderer<HamsterNew> {
    private static final String HELMET = "armorBipedHead";

    public HamsterNewRenderer(EntityRendererProvider.Context context) {
        super(context, new HamsterNewModel());
        this.shadowRadius = 0.3F;

        addRenderLayer(new ItemArmorGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getArmorItemForBone(GeoBone bone, HamsterNew animatable) {
                if (bone.getName().equals(HELMET)) {
                    return this.helmetStack;
                }
                return null;
            }

            @Override
            protected @NotNull EquipmentSlot getEquipmentSlotForBone(GeoBone bone, ItemStack stack, HamsterNew animatable) {
                if (bone.getName().equals(HELMET)) {
                    return EquipmentSlot.HEAD;
                }
                return super.getEquipmentSlotForBone(bone, stack, animatable);
            }

            @NotNull
            @Override
            protected ModelPart getModelPartForBone(GeoBone bone, EquipmentSlot slot, ItemStack stack, HamsterNew animatable, HumanoidModel<?> baseModel) {
                if (bone.getName().equals(HELMET)) {
                    return baseModel.head;
                }
                return super.getModelPartForBone(bone, slot, stack, animatable, baseModel);
            }
        });

//        addRenderLayer(new BlockAndItemGeoLayer<>(this) {
//            @Override
//            protected @NotNull ItemStack getStackForBone(GeoBone bone, HamsterNew animatable) {
//                if (bone.getName().equals(HELMET)) {
//                    return animatable.getItemBySlot(EquipmentSlot.HEAD);
//                }
//                return ItemStack.EMPTY;
//            }
//
//            @Override
//            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, HamsterNew animatable) {
//                if (bone.getName().equals(HELMET)) {
//                    return ItemDisplayContext.HEAD;
//                }
//                return super.getTransformTypeForStack(bone, stack, animatable);
//            }
//        });

        this.addRenderLayer(new HamsterNewMarkingLayer(this));
    }


    @Override
    public float getMotionAnimThreshold(HamsterNew animatable) {
        return 0.001f;
    }

    @Override
    public void render(HamsterNew animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
            if (animatable.isBaby()) {
                poseStack.scale(0.6F, 0.6F, 0.6F);
            } else {
                poseStack.scale(1F, 1F, 1F);
            }
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

}

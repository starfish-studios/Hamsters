package com.starfish_studios.hamsters.client.renderer.layers.player;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.client.model.HamsterModel;
import com.starfish_studios.hamsters.client.model.shoulder.LeftSittingHamsterModel;
import com.starfish_studios.hamsters.entity.HamsterNew;
import com.starfish_studios.hamsters.registry.HamstersEntityType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class PlayerHamsterOnRightShoulderLayer<T extends Player> extends RenderLayer<T, PlayerModel<T>> {
    private final LeftSittingHamsterModel model;
    public static final ModelLayerLocation HAMSTER_LAYER = new ModelLayerLocation(new ResourceLocation(Hamsters.MOD_ID, "left_sitting_hamster"), "main");

    public PlayerHamsterOnRightShoulderLayer(RenderLayerParent<T, PlayerModel<T>> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.model = new LeftSittingHamsterModel<>(entityModelSet.bakeLayer(HAMSTER_LAYER));
    }

    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T player, float f, float g, float h, float j, float k, float l) {
        this.render(poseStack, multiBufferSource, i, player, f, g, k, l, true);
    }

    private void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T player, float f, float g, float h, float j, boolean bl) {
        CompoundTag compoundTag = bl ? player.getShoulderEntityLeft() : player.getShoulderEntityRight();
        EntityType.byString(compoundTag.getString("id")).filter((entityType) -> entityType == HamstersEntityType.HAMSTER).ifPresent((entityType) -> {
            poseStack.pushPose();
            poseStack.scale(1.0F, 1.0F, 1.0F);
            poseStack.translate(-0.425F, player.isCrouching() ? 0.2F : 0.0F, 0.25F);
            HamsterNew.Variant variant = HamsterNew.Variant.getTypeById(compoundTag.getInt("Variant"));
//            HamsterNew.Marking marking = HamsterNew.Marking.byId(compoundTag.getInt("Marking"));
            VertexConsumer vertexConsumer = multiBufferSource.getBuffer(this.model.renderType(HamsterModel.getVariantTexture2(variant)));
            this.model.renderOnShoulder(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, f, g, h, j, player.tickCount);
            poseStack.popPose();
        });
    }
}

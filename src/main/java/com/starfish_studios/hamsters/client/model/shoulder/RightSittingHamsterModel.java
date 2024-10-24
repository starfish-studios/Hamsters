package com.starfish_studios.hamsters.client.model.shoulder;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.starfish_studios.hamsters.entity.HamsterNew;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class RightSittingHamsterModel<T extends HamsterNew> extends EntityModel<T> {
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart headRot;
    private final ModelPart head;
    private final ModelPart armorBipedHead;
    private final ModelPart rightEar;
    private final ModelPart leftEar;
    private final ModelPart cheeks;
    private final ModelPart sleep;
    private final ModelPart tail;
    private final ModelPart rightHand;
    private final ModelPart leftHand;
    private final ModelPart legs;
    private final ModelPart leftFoot;
    private final ModelPart rightFoot;

	public RightSittingHamsterModel(ModelPart modelPart) {
        this.root = modelPart;
        this.body = modelPart.getChild("body");
        this.headRot = this.body.getChild("headRot");
        this.head = this.headRot.getChild("head");
        this.armorBipedHead = this.head.getChild("armorBipedHead");
        this.rightEar = this.head.getChild("rightEar");
        this.leftEar = this.head.getChild("leftEar");
        this.cheeks = this.head.getChild("cheeks");
        this.sleep = this.head.getChild("sleep");
        this.tail = this.body.getChild("tail");
        this.rightHand = this.body.getChild("rightHand");
        this.leftHand = this.body.getChild("leftHand");
        this.legs = modelPart.getChild("legs");
        this.leftFoot = this.legs.getChild("leftFoot");
        this.rightFoot = this.legs.getChild("rightFoot");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, -2.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -3.0F, -5.0F, 6.0F, 4.0F, 6.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, -0.5F, 3.0F, -0.9599F, 0.0F, 0.0F));

        PartDefinition headRot = body.addOrReplaceChild("headRot", CubeListBuilder.create(), PartPose.offsetAndRotation(-0.5F, -2.0F, -4.5F, 0.9599F, 0.0F, 0.0F));

        PartDefinition head = headRot.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition armorBipedHead = head.addOrReplaceChild("armorBipedHead", CubeListBuilder.create().texOffs(1, 11).addBox(-2.5F, -4.0F, -2.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 1.5F, -2.0F));

        PartDefinition rightEar = head.addOrReplaceChild("rightEar", CubeListBuilder.create().texOffs(0, 11).mirror().addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.01F)).mirror(false), PartPose.offset(-1.0F, -1.5F, -2.0F));

        PartDefinition leftEar = head.addOrReplaceChild("leftEar", CubeListBuilder.create().texOffs(0, 11).addBox(0.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offset(2.0F, -1.5F, -2.0F));

        PartDefinition cheeks = head.addOrReplaceChild("cheeks", CubeListBuilder.create().texOffs(20, 13).addBox(7.0F, -1.5F, -2.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F))
                .texOffs(20, 13).mirror().addBox(0.0F, -1.5F, -2.0F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-4.0F, 0.0F, -2.0F));

        PartDefinition sleep = head.addOrReplaceChild("sleep", CubeListBuilder.create(), PartPose.offset(0.5F, 2.5F, 0.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(19, 0).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -3.0F, 1.0F));

        PartDefinition rightHand = body.addOrReplaceChild("rightHand", CubeListBuilder.create().texOffs(24, 2).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(-1.5F, 1.5F, -3.5F, 2.3126F, 0.2618F, 0.0565F));

        PartDefinition leftHand = body.addOrReplaceChild("leftHand", CubeListBuilder.create().texOffs(24, 2).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 1.5F, -3.5F, 2.3126F, -0.2618F, -0.0698F));

        PartDefinition legs = root.addOrReplaceChild("legs", CubeListBuilder.create(), PartPose.offset(1.5F, -1.0F, -1.0F));

        PartDefinition leftFoot = legs.addOrReplaceChild("leftFoot", CubeListBuilder.create().texOffs(24, 2).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 3.0F));

        PartDefinition rightFoot = legs.addOrReplaceChild("rightFoot", CubeListBuilder.create().texOffs(24, 2).mirror().addBox(-1.0F, 0.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-3.0F, 0.0F, 3.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    public void renderOnShoulder(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j, float f, float g, float h, float k, int l) {
        this.setupAnim(State.ON_SHOULDER, l, f, g, 0.0F, h, k);
        this.root.render(poseStack, vertexConsumer, i, j);
    }

    private void setupAnim(State state, int i, float f, float g, float h, float j, float k) {
        this.head.xRot = k * 0.017453292F;
        this.head.yRot = j * 0.017453292F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(T entity, float f, float g, float h, float i, float j) {
        this.setupAnim(State.ON_SHOULDER, entity.tickCount, f, g, h, i, j);
    }

    @Environment(EnvType.CLIENT)
    public enum State {
        ON_SHOULDER;
    }
}
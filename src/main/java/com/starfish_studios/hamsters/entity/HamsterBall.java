package com.starfish_studios.hamsters.entity;

import com.starfish_studios.hamsters.entity.common.HamstersGeoEntity;
import com.starfish_studios.hamsters.registry.HamstersEntityType;
import com.starfish_studios.hamsters.registry.HamstersItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;

public class HamsterBall extends PathfinderMob implements HamstersGeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private final NonNullList<ItemStack> armorItems = NonNullList.withSize(0, ItemStack.EMPTY);

    protected static final RawAnimation ROLL = RawAnimation.begin().thenLoop("animation.sf_nba.hamster_ball.roll");


    public HamsterBall(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 500.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25);
    }


    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SHULKER_HURT_CLOSED;
    }

    protected void positionRider(Entity entity, Entity.MoveFunction moveFunction) {
        super.positionRider(entity, moveFunction);

        Hamster hamster = (Hamster) entity;
        if (entity.is(hamster)) {
            entity.setPos(this.getX(), this.getY() + 0.125f, this.getZ());
        }

    }

    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity var2 = this.getFirstPassenger();
        if (var2 instanceof Hamster hamster) {
            return hamster;
        }
        return null;
    }

    @Override
    public final @NotNull InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            if (!this.level().isClientSide) {
                this.remove(RemovalReason.DISCARDED);
                this.spawnAtLocation(Objects.requireNonNull(this.getPickResult()));
                level().playSound(null, blockPosition(), SoundEvents.CHICKEN_EGG, SoundSource.NEUTRAL, 0.6F, ((level().random.nextFloat() - level().random.nextFloat()) * 0.2F + 1.0F));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public void travel(Vec3 vec3) {
        float f;
        float p = 0.91F;
        f = this.onGround() ? p * 0.91F : 0.91F;
        Vec3 vec37 = this.handleRelativeFrictionAndCalculateMovement(vec3, p);
        double q = vec37.y;

        this.setDeltaMovement(vec37.x * (double)f, q * 0.9800000190734863, vec37.z * (double)f);
    }


    protected void tickRidden(Player player, Vec3 vec3) {
        LivingEntity livingEntity = this.getControllingPassenger();
        assert livingEntity != null;
        if (livingEntity.getType() == HamstersEntityType.HAMSTER && livingEntity != player) {
            this.setRot(livingEntity.getYRot(), livingEntity.getXRot() * 0.5F);
            this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        }
        super.tickRidden(player, vec3);
    }

    protected @NotNull Vec3 getRiddenInput(Player player, Vec3 vec3) {
        return new Vec3(0.0, 0.0, 1.0);
    }

    protected float getRiddenSpeed(Player player) {
        return (float)(this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 1.25);
    }

    // region MISC

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return this.armorItems;
    }

    @Override
    public @NotNull ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        this.verifyEquippedItem(itemStack);
    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    // endregion

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(HamstersItems.BLUE_HAMSTER_BALL);
    }



    // region GECKOLIB

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::animController));
    }



    protected <E extends HamsterBall> PlayState animController(final AnimationState<E> event) {
        if (event.isMoving() && this.getDeltaMovement().x() != 0 || this.getDeltaMovement().z() != 0) {
            event.setAnimation(ROLL);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    // endregion
}

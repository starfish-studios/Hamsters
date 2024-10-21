package com.starfish_studios.hamsters.block.entity;

import com.simibubi.create.content.kinetics.KineticNetwork;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.crank.HandCrankBlockEntity;
import com.starfish_studios.hamsters.block.HamsterWheelBlock;
import com.starfish_studios.hamsters.entity.Hamster;
import com.starfish_studios.hamsters.registry.HamstersBlockEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class HamsterWheelBlockEntity extends GeneratingKineticBlockEntity implements GeoBlockEntity {
    protected static final RawAnimation SPIN = RawAnimation.begin().thenLoop("animation.sf_nba.hamster_wheel.spin");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public HamsterWheelBlockEntity(BlockPos pos, BlockState state) {
        super(HamstersBlockEntities.HAMSTER_WHEEL, pos, state);
    }

    @Override
    public float getGeneratedSpeed() {
        Block block = getBlockState().getBlock();
        if (!(block instanceof HamsterWheelBlock wheel))
            return 0;
        assert level != null;
        if (HamsterWheelBlock.isOccupied(level, getBlockPos())) {
            return -16;
        } else {
            return 0;
        }
    }

    @Override
    public void lazyTick() {
        super.lazyTick();
    }


    @Override
    public void tick() {
        super.tick();

        assert level != null;
        if (HamsterWheelBlock.isOccupied(level, getBlockPos())) {
            updateGeneratedRotation();
        } else if (getGeneratedSpeed() == 0) {
                updateGeneratedRotation();
        }
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::controller));
    }

    private <E extends HamsterWheelBlockEntity> PlayState controller(final AnimationState<E> event) {
        assert level != null;
        BlockPos blockPos = this.getBlockPos();
        if (HamsterWheelBlock.isOccupied(level, blockPos)) {
            event.getController().setAnimation(SPIN);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}

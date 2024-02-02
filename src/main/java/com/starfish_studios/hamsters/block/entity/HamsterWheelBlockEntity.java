package com.starfish_studios.hamsters.block.entity;

import com.starfish_studios.hamsters.block.HamsterWheelBlock;
import com.starfish_studios.hamsters.registry.HamstersBlockEntities;
import net.minecraft.core.BlockPos;
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

public class HamsterWheelBlockEntity extends BlockEntity implements GeoBlockEntity {
    protected static final RawAnimation SPIN = RawAnimation.begin().thenLoop("animation.sf_nba.hamster_wheel.spin");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public HamsterWheelBlockEntity(BlockPos pos, BlockState state) {
        super(HamstersBlockEntities.HAMSTER_WHEEL.get(), pos, state);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::controller));
    }

    // TODO: The wheel currently just spins 24/7. We need to make it spin when a hamster is inside.
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

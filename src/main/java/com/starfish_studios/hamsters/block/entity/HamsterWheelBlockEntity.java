package com.starfish_studios.hamsters.block.entity;

import com.starfish_studios.hamsters.block.HamsterWheelBlock;
import com.starfish_studios.hamsters.registry.HamstersBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class HamsterWheelBlockEntity extends BlockEntity implements IAnimatable {
    public AnimationFactory factory = GeckoLibUtil.createFactory(this);
    protected static final AnimationBuilder SPIN = new AnimationBuilder().addAnimation("animation.sf_nba.hamster_wheel.spin", ILoopType.EDefaultLoopTypes.LOOP);
    public HamsterWheelBlockEntity(BlockPos pos, BlockState state) {
        super(HamstersBlockEntities.HAMSTER_WHEEL.get(), pos, state);
    }

    // TODO: The wheel currently just spins 24/7. We need to make it spin when a hamster is inside.
    private <E extends IAnimatable> PlayState controller(final AnimationEvent<E> event) {
        assert level != null;
        BlockPos blockPos = this.getBlockPos();
        if (HamsterWheelBlock.isOccupied(level, blockPos)) {
            event.getController().setAnimation(SPIN);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController(this, "controller", 0, this::controller));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}

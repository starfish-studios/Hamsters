package com.starfish_studios.hamsters.block.entity;

import com.starfish_studios.hamsters.registry.HamstersBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BottleBlockEntity extends BlockEntity {
    public BottleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(HamstersBlockEntities.BLUE_HAMSTER_BOTTLE, blockPos, blockState);
    }

}

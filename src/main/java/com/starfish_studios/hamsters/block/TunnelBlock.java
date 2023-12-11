package com.starfish_studios.hamsters.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TunnelBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    protected static final VoxelShape SOLID_LEFT = Block.box(0, 0, 0, 2, 16, 16);
    protected static final VoxelShape SOLID_RIGHT = Block.box(14, 0, 0, 16, 16, 16);
    protected static final VoxelShape SOLID_FRONT = Block.box(0, 0, 0, 16, 16, 2);
    protected static final VoxelShape SOLID_BACK = Block.box(0, 0, 14, 16, 16, 16);
    protected static final VoxelShape SOLID_TOP = Block.box(0, 14, 0, 16, 16, 16);
    protected static final VoxelShape SOLID_BOTTOM = Block.box(0, 0, 0, 16, 2, 16);

    protected static final VoxelShape SINGLE_Y = Shapes.or(SOLID_LEFT, SOLID_RIGHT, SOLID_FRONT, SOLID_BACK);
    protected static final VoxelShape SINGLE_X = Shapes.or(SOLID_FRONT, SOLID_BACK, SOLID_TOP, SOLID_BOTTOM);
    protected static final VoxelShape SINGLE_Z = Shapes.or(SOLID_LEFT, SOLID_RIGHT, SOLID_TOP, SOLID_BOTTOM);


    public TunnelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP));
    }

    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return switch (blockState.getValue(FACING).getAxis()) {
            default -> SINGLE_Y;
            case X -> SINGLE_X;
            case Z -> SINGLE_Z;
        };
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        Direction direction = blockPlaceContext.getClickedFace();
        BlockState blockState = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos().relative(direction.getOpposite()));
        return blockState.is(this) && blockState.getValue(FACING) == direction ?
                this.defaultBlockState().setValue(FACING, direction.getOpposite()) : this.defaultBlockState().setValue(FACING, direction);
    }

    public boolean skipRendering(BlockState blockState, BlockState blockState2, Direction direction) {
        return blockState2.is(this) || super.skipRendering(blockState, blockState2, direction);
    }

    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.setValue(FACING, mirror.mirror(blockState.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

}

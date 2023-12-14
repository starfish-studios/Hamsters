package com.starfish_studios.hamsters.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.Map;

public class TunnelBlock extends Block {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    // region SHAPES
    public static final BooleanProperty NORTH = BlockStateProperties.NORTH;
    public static final BooleanProperty EAST = BlockStateProperties.EAST;
    public static final BooleanProperty SOUTH = BlockStateProperties.SOUTH;
    public static final BooleanProperty WEST = BlockStateProperties.WEST;
    public static final BooleanProperty UP = BlockStateProperties.UP;
    public static final BooleanProperty DOWN = BlockStateProperties.DOWN;
    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = ImmutableMap.copyOf((Map) Util.make(Maps.newEnumMap(Direction.class), (enumMap) -> {
        enumMap.put(Direction.NORTH, NORTH);
        enumMap.put(Direction.EAST, EAST);
        enumMap.put(Direction.SOUTH, SOUTH);
        enumMap.put(Direction.WEST, WEST);
        enumMap.put(Direction.UP, UP);
        enumMap.put(Direction.DOWN, DOWN);
    }));

    protected static final VoxelShape SOLID_LEFT = Block.box(0, 0, 0, 2, 16, 16);
    protected static final VoxelShape SOLID_RIGHT = Block.box(14, 0, 0, 16, 16, 16);
    protected static final VoxelShape SOLID_FRONT = Block.box(0, 0, 0, 16, 16, 2);
    protected static final VoxelShape SOLID_BACK = Block.box(0, 0, 14, 16, 16, 16);
    protected static final VoxelShape SOLID_TOP = Block.box(0, 14, 0, 16, 16, 16);
    protected static final VoxelShape SOLID_BOTTOM = Block.box(0, 0, 0, 16, 2, 16);

    protected static final VoxelShape SINGLE_Y = Shapes.or(SOLID_LEFT, SOLID_RIGHT, SOLID_FRONT, SOLID_BACK);
    protected static final VoxelShape SINGLE_X = Shapes.or(SOLID_FRONT, SOLID_BACK, SOLID_TOP, SOLID_BOTTOM);
    protected static final VoxelShape SINGLE_Z = Shapes.or(SOLID_LEFT, SOLID_RIGHT, SOLID_TOP, SOLID_BOTTOM);

    // endregion

    public TunnelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(EAST, false)
                .setValue(SOUTH, false)
                .setValue(WEST, false)
                .setValue(UP, false)
                .setValue(DOWN, false)
                .setValue(WATERLOGGED, false));
    }

    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.getItemInHand(interactionHand).isEmpty() && !player.isShiftKeyDown()) {
            player.teleportTo(blockPos.getX() + 0.5, blockPos.getY() + 0.2, blockPos.getZ() + 0.5);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, net.minecraft.world.entity.Entity entity) {
        // TODO: Having a hard time getting this to work without launching the entity while they're in a corner.
        // entity.setDeltaMovement(entity.getDeltaMovement().multiply(1.2F, 1.0F, 1.2F));
    }

    public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        VoxelShape shape = Shapes.empty();
        if (blockState.getValue(SOUTH) || blockState.getValue(NORTH)) {
            shape = Shapes.or(shape, SOLID_LEFT, SOLID_RIGHT);
        }
        if (blockState.getValue(EAST) || blockState.getValue(WEST)) {
            shape = Shapes.or(shape, SOLID_FRONT, SOLID_BACK);
        }
        if (!blockState.getValue(UP) || !blockState.getValue(DOWN)) {
            shape = Shapes.or(shape, SOLID_TOP, SOLID_BOTTOM);
        }
        return shape;
        // TODO: This should be a modular system that can handle any combination of tunnels, but it's not working properly.
    }

    // This makes the block's interaction shape still solid, while the above lets the player pass through.
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.block();
    }

    // This makes sure the player's camera does not collide with the block.
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return Shapes.empty();
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.getStateForPlacement(blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos());
    }

    // This determines if it is next to another tunnel block.
    public BlockState getStateForPlacement(BlockGetter blockGetter, BlockPos blockPos) {
        BlockState blockState = blockGetter.getBlockState(blockPos.below());
        BlockState blockState2 = blockGetter.getBlockState(blockPos.above());
        BlockState blockState3 = blockGetter.getBlockState(blockPos.north());
        BlockState blockState4 = blockGetter.getBlockState(blockPos.east());
        BlockState blockState5 = blockGetter.getBlockState(blockPos.south());
        BlockState blockState6 = blockGetter.getBlockState(blockPos.west());
        return this.defaultBlockState()
                .setValue(DOWN, blockState.is(this))
                .setValue(UP, blockState2.is(this))
                .setValue(NORTH, blockState3.is(this))
                .setValue(EAST, blockState4.is(this))
                .setValue(SOUTH, blockState5.is(this))
                .setValue(WEST, blockState6.is(this));
    }

    // TODO: This may be why the collisions aren't working properly, but I haven't played with this part yet.
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
        return blockState.setValue(PROPERTY_BY_DIRECTION.get(direction), blockState2.is(this));
    }

    // This is for their final glassy look and making their model culling work properly.
    public boolean skipRendering(BlockState blockState, BlockState blockState2, Direction direction) {
        return blockState2.is(this) || super.skipRendering(blockState, blockState2, direction);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN, WATERLOGGED);
    }

}

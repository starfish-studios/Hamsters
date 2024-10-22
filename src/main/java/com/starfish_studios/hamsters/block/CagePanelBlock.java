package com.starfish_studios.hamsters.block;

import com.starfish_studios.hamsters.block.properties.CageType;
import com.starfish_studios.hamsters.registry.HamstersTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CagePanelBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<CageType> TYPE = EnumProperty.create("type", CageType.class);

    public static final VoxelShape NORTH_AABB = Block.box(0, 0, 15, 16, 16, 16);
    public static final VoxelShape EAST_AABB = Block.box(0, 0, 0, 1, 16, 16);
    public static final VoxelShape SOUTH_AABB = Block.box(0, 0, 0, 16, 16, 1);
    public static final VoxelShape WEST_AABB = Block.box(15, 0, 0, 16, 16, 16);

    public static final VoxelShape NORTH_COLLISION_AABB = Block.box(0, 0, 15, 16, 24, 16);
    public static final VoxelShape EAST_COLLISION_AABB = Block.box(0, 0, 0, 1, 24, 16);
    public static final VoxelShape SOUTH_COLLISION_AABB = Block.box(0, 0, 0, 16, 24, 1);
    public static final VoxelShape WEST_COLLISION_AABB = Block.box(15, 0, 0, 16, 24, 16);

    public CagePanelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(TYPE, CageType.NONE));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return switch (direction) {
            case EAST -> EAST_AABB;
            case SOUTH -> SOUTH_AABB;
            case WEST -> WEST_AABB;
            default -> NORTH_AABB;
        };
    }

    //getCollisionShape
    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return switch (direction) {
            case EAST -> EAST_COLLISION_AABB;
            case SOUTH -> SOUTH_COLLISION_AABB;
            case WEST -> WEST_COLLISION_AABB;
            default -> NORTH_COLLISION_AABB;
        };
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (player.getItemInHand(hand).is(HamstersTags.CAGE_PANELS)) {
            BlockPos above = pos.above();
            if (level.isEmptyBlock(above)) {
                BlockItem blockItem = (BlockItem) player.getItemInHand(hand).getItem();
//                level.setBlock(above, state, 3);
                level.setBlock(above, blockItem.getBlock().defaultBlockState().setValue(FACING, state.getValue(FACING)), 3);
                level.playSound(null, pos, this.getSoundType(state).getPlaceSound(), SoundSource.BLOCKS, level.random.nextFloat() * 0.25F + 0.7F, level.random.nextFloat() * 0.1F + 0.9F);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }


    public float getShadeBrightness(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return 1.0F;
    }

    public boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return true;
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction direction = context.getHorizontalDirection().getOpposite();

        BlockState state = this.defaultBlockState().setValue(FACING, direction);
        state = state.setValue(TYPE, getType(state, getRelativeTop(level, pos, direction), getRelativeBottom(level, pos, direction)));
        return state;
    }


    public boolean skipRendering(BlockState blockState, BlockState blockState2, Direction direction) {
//        return blockState2.is(this) && blockState2.getValue(FACING) == blockState.getValue(FACING);
        return blockState2.getBlock() instanceof CagePanelBlock && blockState2.getValue(FACING) == blockState.getValue(FACING);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.isClientSide) return;

        Direction direction = state.getValue(FACING);
        CageType type = getType(state, getRelativeTop(level, pos, direction), getRelativeBottom(level, pos, direction));
        if (state.getValue(TYPE) == type) return;

        state = state.setValue(TYPE, type);
        level.setBlock(pos, state, 3);
    }

    public BlockState getRelativeTop(Level level, BlockPos pos, Direction direction) {
        return level.getBlockState(pos.above());
    }

    public BlockState getRelativeBottom(Level level, BlockPos pos, Direction direction) {
        return level.getBlockState(pos.below());
    }

    public CageType getType(BlockState state, BlockState above, BlockState below) {
//        boolean shape_above_same = above.is(state.getBlock()) && state.getValue(FACING) == above.getValue(FACING);
//        boolean shape_below_same = below.is(state.getBlock()) && state.getValue(FACING) == below.getValue(FACING);

        // Return the booleans the same as above but instead of state.getBlock, do it if they're an instanceof CagePanelBlock
        boolean shape_above_same = above.getBlock() instanceof CagePanelBlock && state.getValue(FACING) == above.getValue(FACING);
        boolean shape_below_same = below.getBlock() instanceof CagePanelBlock && state.getValue(FACING) == below.getValue(FACING);

        if (shape_above_same && !shape_below_same) return CageType.BOTTOM;
        else if (!shape_above_same && shape_below_same) return CageType.TOP;
        else if (shape_above_same) return CageType.MIDDLE;
        return CageType.NONE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TYPE, FACING);
    }
}

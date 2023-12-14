package com.starfish_studios.hamsters.block;

import com.starfish_studios.hamsters.block.entity.HamsterWheelBlockEntity;
import com.starfish_studios.hamsters.registry.HamstersBlockEntities;
import com.starfish_studios.hamsters.registry.HamstersEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import software.bernie.example.registry.BlockEntityRegistry;

public class HamsterWheelBlock extends BaseEntityBlock implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public HamsterWheelBlock(Properties properties) {
        super(properties);
    }

    protected static final VoxelShape NORTH = Block.box(1, 0, 3, 15, 16, 16);
    protected static final VoxelShape SOUTH = Block.box(1, 0, 0, 15, 16, 13);
    protected static final VoxelShape EAST = Block.box(0, 0, 1, 13, 16, 15);
    protected static final VoxelShape WEST = Block.box(3, 0, 1, 16, 16, 15);

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING,
                context.getHorizontalDirection().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return HamstersBlockEntities.HAMSTER_WHEEL.create(blockPos, blockState);
    }


    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.getItemInHand(interactionHand).isEmpty() || (!player.getItemInHand(interactionHand).isEmpty() && !player.isShiftKeyDown())) {

            // TODO: If the wheel is right-clicked, a Hamster is spawned in front of the wheel. This is to replicate how it could be forcibly ejected.
            // Perhaps if it is ejected, it would search for the hamster inside and just teleport it instead of spawning a new one.

            if (!level.isClientSide) {
                blockPos = blockPos.relative(blockState.getValue(FACING));
                HamstersEntityType.HAMSTER.spawn((ServerLevel) level, player.getItemInHand(interactionHand), null, blockPos, MobSpawnType.EVENT, true, false);
            }
            return InteractionResult.SUCCESS;

        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SOUTH;
            case EAST -> EAST;
            case WEST -> WEST;
            default -> NORTH;
        };
    }

    // Can only survive on top of solid blocks.
    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).isFaceSturdy(world, pos.below(), net.minecraft.core.Direction.UP);
    }
}

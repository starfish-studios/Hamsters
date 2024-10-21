package com.starfish_studios.hamsters.entity;

import com.google.common.collect.Lists;
import com.starfish_studios.hamsters.block.HamsterWheelBlock;
import com.starfish_studios.hamsters.block.entity.HamsterWheelBlockEntity;
import com.starfish_studios.hamsters.entity.common.MMPathNavigatorGround;
import com.starfish_studios.hamsters.entity.common.SearchForItemsGoal;
import com.starfish_studios.hamsters.entity.common.SmartBodyHelper;
import com.starfish_studios.hamsters.registry.HamstersEntityType;
import com.starfish_studios.hamsters.registry.HamstersPoiTypes;
import com.starfish_studios.hamsters.registry.HamstersTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HamsterNew extends TamableAnimal implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    
    private static final int TICKS_BEFORE_GOING_TO_WHEEL = 2400;
    public static final String TAG_WHEEL_POS = "WheelPos";
    private static final int COOLDOWN_BEFORE_LOCATING_NEW_WHEEL = 200;
    int remainingCooldownBeforeLocatingNewWheel;


    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(HamsterNew.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MARKING = SynchedEntityData.defineId(HamsterNew.class, EntityDataSerializers.INT);


    @Nullable
    BlockPos wheelPos;

    public HamsterNew(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new MMPathNavigatorGround(this, level);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new SmartBodyHelper(this);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.3));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(4, new HamsterEnterWheelGoal());
        this.goalSelector.addGoal(5, new HamsterLocateWheelGoal());
        this.goalSelector.addGoal(5, new HamsterGoToWheelGoal());
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return new HamsterNew(HamstersEntityType.HAMSTER, serverLevel);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    void pathfindTowards(BlockPos blockPos) {
        Vec3 vec3 = Vec3.atBottomCenterOf(blockPos);
        this.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, 1.0);
    }



    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 2);
        this.entityData.define(MARKING, 0);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.wheelPos = null;
        if (compoundTag.contains("WheelPos")) {
            this.wheelPos = NbtUtils.readBlockPos(compoundTag.getCompound("WheelPos"));
        }
        this.setVariant(HamsterNew.Variant.BY_ID[compoundTag.getInt("Variant")]);
        this.setMarking(compoundTag.getInt("Marking"));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if (this.hasWheel()) {
            assert this.getWheelPos() != null;
            compoundTag.put("HivePos", NbtUtils.writeBlockPos(this.getWheelPos()));
        }
        compoundTag.putInt("Variant", this.getVariant());
        compoundTag.putInt("Marking", this.getMarking());
    }

    @VisibleForDebug
    public boolean hasWheel() {
        return this.wheelPos != null;
    }

    @Nullable
    @VisibleForDebug
    public BlockPos getWheelPos() {
        return this.wheelPos;
    }

    boolean isWheelValid(BlockPos blockPos) {
        return this.level().isLoaded(blockPos) && this.level().getBlockState(blockPos).is(HamstersTags.HAMSTER_WHEELS);
    }

    boolean closerThan(BlockPos blockPos, int i) {
        return !blockPos.closerThan(this.blockPosition(), i);
    }
    
    boolean isTooFarAway(BlockPos blockPos) {
        return this.closerThan(blockPos, 32);
    }

    public int getMarking() {
        return this.entityData.get(MARKING);
    }

    public void setMarking(int i) {
        this.entityData.set(MARKING, i);
    }


    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(HamsterNew.Variant variant) {
        this.entityData.set(VARIANT, variant.getId());
    }

    public enum Marking {
        BLANK (0, "blank"),
        BANDED (1, "banded"),
        DOMINANT_SPOTS (2, "dominant_spots"),
        ROAN (3, "roan"),
        BELLY (4, "belly");

        private static final IntFunction<HamsterNew.Marking> BY_ID = ByIdMap.continuous(HamsterNew.Marking::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

        private final int id;
        private final String name;

        private Marking(int j, String string2) {
            this.id = j;
            this.name = string2;
        }

        public int getId() {
            return this.id;
        }

        public static HamsterNew.Marking byId(int i) {
            return BY_ID.apply(i);
        }

        public String getName() {
            return this.name;
        }

    }

    public enum Variant {
        WHITE (0, "white"),
        CREAM (1, "cream"),
        CHAMPAGNE (2, "champagne"),
        SILVER_DOVE (3, "silver_dove"),
        DOVE (4, "dove"),
        CHOCOLATE (5, "chocolate"),
        BLACK (6, "black");

        public static final HamsterNew.Variant[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(HamsterNew.Variant::getId)).toArray(HamsterNew.Variant[]::new);
        private final int id;
        private final String name;

        private Variant(int j, String string2) {
            this.id = j;
            this.name = string2;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public static HamsterNew.Variant getTypeById(int id) {
            for (HamsterNew.Variant type : values()) {
                if (type.id == id) return type;
            }
            return HamsterNew.Variant.CHAMPAGNE;
        }
    }



    abstract static class BaseHamsterGoal extends Goal {
        BaseHamsterGoal() {
        }

        public abstract boolean canHamsterUse();

        public abstract boolean canBeeContinueToUse();

        public boolean canUse() {
            return this.canHamsterUse();
        }

        public boolean canContinueToUse() {
            return this.canBeeContinueToUse();
        }
    }

    class HamsterEnterWheelGoal extends BaseHamsterGoal {
        HamsterEnterWheelGoal() {
            super();
        }

        public boolean canHamsterUse() {
            return HamsterNew.this.wheelPos != null && HamsterNew.this.closerThan(HamsterNew.this.wheelPos, 2) && HamsterNew.this.isWheelValid(HamsterNew.this.wheelPos);
        }

        public boolean canBeeContinueToUse() {
            return false;
        }

        public void start() {
            assert HamsterNew.this.wheelPos != null;
            Block block = HamsterNew.this.level().getBlockState(HamsterNew.this.wheelPos).getBlock();
            BlockEntity blockEntity = HamsterNew.this.level().getBlockEntity(HamsterNew.this.wheelPos);
            if (block instanceof HamsterWheelBlock && blockEntity instanceof HamsterWheelBlockEntity) {
                if (HamsterWheelBlock.isOccupied(HamsterNew.this.level(), HamsterNew.this.wheelPos)) {
                    HamsterNew.this.wheelPos = null;
                } else {
                    HamsterWheelBlock.sitDown(HamsterNew.this.level(), HamsterNew.this.wheelPos, HamsterNew.this);
                }
            }
        }
    }

    class HamsterLocateWheelGoal extends BaseHamsterGoal {
        HamsterLocateWheelGoal() {
            super();
        }

        public boolean canHamsterUse() {
            return HamsterNew.this.remainingCooldownBeforeLocatingNewWheel == 0 && HamsterNew.this.wheelPos == null;
        }

        public boolean canBeeContinueToUse() {
            return false;
        }

        public void start() {
            HamsterNew.this.remainingCooldownBeforeLocatingNewWheel = 200;
            BlockPos blockPos = findNearbyWheel();
            if (blockPos != null) {
                HamsterNew.this.wheelPos = blockPos;
            }
        }

        @Nullable
        private BlockPos findNearbyWheel() {
            BlockPos blockPos = HamsterNew.this.blockPosition();
            PoiManager poiManager = ((ServerLevel)HamsterNew.this.level()).getPoiManager();
            Stream<PoiRecord> stream = poiManager.getInRange((holder) -> holder.is(HamstersPoiTypes.HAMSTER_WHEEL), blockPos, 20, PoiManager.Occupancy.ANY);
            return stream.map(PoiRecord::getPos).filter(HamsterNew.this::isWheelValid).min(Comparator.comparingDouble((blockPos2) -> blockPos2.distSqr(blockPos))).orElse(null);
        }
    }

    @VisibleForDebug
    public class HamsterGoToWheelGoal extends BaseHamsterGoal {
        public static final int MAX_TRAVELLING_TICKS = 600;
        int travellingTicks;
        private static final int MAX_BLACKLISTED_TARGETS = 3;
        final List<BlockPos> blacklistedTargets;
        @Nullable
        private Path lastPath;
        private static final int TICKS_BEFORE_WHEEL_DROP = 60;
        private int ticksStuck;

        HamsterGoToWheelGoal() {
            super();
            this.travellingTicks = HamsterNew.this.level().random.nextInt(10);
            this.blacklistedTargets = Lists.newArrayList();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canHamsterUse() {
            return HamsterNew.this.wheelPos != null && !HamsterNew.this.hasRestriction() && !this.hasReachedTarget(HamsterNew.this.wheelPos) && HamsterNew.this.isWheelValid(HamsterNew.this.wheelPos);
        }

        public boolean canBeeContinueToUse() {
            return this.canHamsterUse();
        }

        public void start() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            super.start();
        }

        public void stop() {
            this.travellingTicks = 0;
            this.ticksStuck = 0;
            HamsterNew.this.getNavigation().stop();
            HamsterNew.this.getNavigation().resetMaxVisitedNodesMultiplier();
        }

        public void tick() {
            if (HamsterNew.this.wheelPos != null) {
                ++this.travellingTicks;
                if (this.travellingTicks > this.adjustedTickDelay(MAX_TRAVELLING_TICKS)) {
                    this.dropAndBlacklistWheel();
                } else if (!HamsterNew.this.getNavigation().isInProgress()) {
                    if (!HamsterNew.this.closerThan(HamsterNew.this.wheelPos, 16)) {
                        if (HamsterNew.this.isTooFarAway(HamsterNew.this.wheelPos)) {
                            this.dropWheel();
                        } else {
                            HamsterNew.this.pathfindTowards(HamsterNew.this.wheelPos);
                        }
                    } else {
                        boolean bl = this.pathfindDirectlyTowards(HamsterNew.this.wheelPos);
                        if (!bl) {
                            this.dropAndBlacklistWheel();
                        } else if (this.lastPath != null && Objects.requireNonNull(HamsterNew.this.getNavigation().getPath()).sameAs(this.lastPath)) {
                            ++this.ticksStuck;
                            if (this.ticksStuck > TICKS_BEFORE_WHEEL_DROP) {
                                this.dropWheel();
                                this.ticksStuck = 0;
                            }
                        } else {
                            this.lastPath = HamsterNew.this.getNavigation().getPath();
                        }
                    }
                }
            }
        }
            private boolean pathfindDirectlyTowards(BlockPos blockPos) {
                HamsterNew.this.getNavigation().setMaxVisitedNodesMultiplier(10.0F);
                HamsterNew.this.getNavigation().moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0);
                return HamsterNew.this.getNavigation().getPath() != null && HamsterNew.this.getNavigation().getPath().canReach();
            }

            boolean isTargetBlacklisted(BlockPos blockPos) {
                return this.blacklistedTargets.contains(blockPos);
            }

            private void blacklistTarget(BlockPos blockPos) {
                this.blacklistedTargets.add(blockPos);

                while(this.blacklistedTargets.size() > 3) {
                    this.blacklistedTargets.remove(0);
                }
            }

            void clearBlacklist() {
                this.blacklistedTargets.clear();
            }

            private void dropAndBlacklistWheel() {
                if (HamsterNew.this.wheelPos != null) {
                    this.blacklistTarget(HamsterNew.this.wheelPos);
                }

                this.dropWheel();
            }

            private void dropWheel() {
                HamsterNew.this.wheelPos = null;
                HamsterNew.this.remainingCooldownBeforeLocatingNewWheel = 200;
            }

            private boolean hasReachedTarget(BlockPos blockPos) {
                if (HamsterNew.this.closerThan(blockPos, 2)) {
                    return true;
                } else {
                    Path path = HamsterNew.this.getNavigation().getPath();
                    return path != null && path.getTarget().equals(blockPos) && path.canReach() && path.isDone();
                }
            }
    }
}

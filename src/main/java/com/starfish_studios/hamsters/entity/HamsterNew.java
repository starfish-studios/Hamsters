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
import net.minecraft.util.RandomSource;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
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
import net.minecraft.world.level.ServerLevelAccessor;
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
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HamsterNew extends TamableAnimal implements GeoEntity {
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.run");
    protected static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.sleep");
    protected static final RawAnimation STANDING = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.standing");

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(HamsterNew.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MARKING = SynchedEntityData.defineId(HamsterNew.class, EntityDataSerializers.INT);

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
//        this.goalSelector.addGoal(4, new HamsterEnterWheelGoal());
//        this.goalSelector.addGoal(5, new HamsterLocateWheelGoal());
//        this.goalSelector.addGoal(5, new HamsterGoToWheelGoal());
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }


    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.populateDefaultEquipmentSlots(random, pDifficulty);
        if (pSpawnData == null) {
            RandomSource randomSource = pLevel.getRandom();
            this.setVariant(HamsterNew.Variant.values()[randomSource.nextInt(HamsterNew.Variant.values().length)]);
            this.setMarking(HamsterNew.Marking.values()[randomSource.nextInt(HamsterNew.Marking.values().length)].getId());
        }
        return pSpawnData;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return new HamsterNew(HamstersEntityType.HAMSTER, serverLevel);
    }

    void pathfindTowards(BlockPos blockPos) {
        Vec3 vec3 = Vec3.atBottomCenterOf(blockPos);
        this.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, 1.0);
    }


    // region DATA

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 2);
        this.entityData.define(MARKING, 0);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setVariant(HamsterNew.Variant.BY_ID[compoundTag.getInt("Variant")]);
        this.setMarking(compoundTag.getInt("Marking"));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Variant", this.getVariant());
        compoundTag.putInt("Marking", this.getMarking());
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

    // endregion

    // region GECKOLIB

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::animController));
    }

    protected <E extends HamsterNew> PlayState animController(final AnimationState<E> event) {
        if (this.isSleeping()) {
            event.setAnimation(SLEEP);
        } else if (this.isInSittingPose()) {
            event.setAnimation(STANDING);
        }  else if (event.isMoving()) {
            if (this.isSprinting()) {
                event.setControllerSpeed(1.3F);
                event.setAnimation(RUN);
            } else {
                event.setControllerSpeed(1.1F);
                event.setAnimation(WALK);
            }
        }  else if (this.isPassenger() && this.getVehicle() instanceof SeatEntity ) {
            event.setControllerSpeed(1.4F);
            event.setAnimation(WALK);
        } else {
            event.setAnimation(IDLE);
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    // endregion
}

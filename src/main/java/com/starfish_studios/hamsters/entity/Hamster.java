package com.starfish_studios.hamsters.entity;

import com.google.common.collect.Lists;
import com.starfish_studios.hamsters.block.BowlBlock;
import com.starfish_studios.hamsters.block.HamsterWheelBlock;
import com.starfish_studios.hamsters.block.entity.HamsterWheelBlockEntity;
import com.starfish_studios.hamsters.entity.common.MMPathNavigatorGround;
import com.starfish_studios.hamsters.entity.common.SmartBodyHelper;
import com.starfish_studios.hamsters.registry.*;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
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
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.starfish_studios.hamsters.block.HamsterWheelBlock.FACING;

public class Hamster extends TamableAnimal implements GeoEntity {
    // region
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.walk");
    protected static final RawAnimation PINKIE_WALK = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.pinkie_walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.run");
    protected static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.sleep");
    protected static final RawAnimation DANCE = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.dance2");
    protected static final RawAnimation STANDING = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.standing");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Integer> EAT_COUNTER = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.INT);

    private static final Ingredient FOOD_ITEMS = Ingredient.of(HamstersTags.HAMSTER_FOOD);
    private static final EntityDataAccessor<Boolean> DATA_INTERESTED = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MARKING = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_HAND = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> WAIT_TIME_BEFORE_RUN = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> WAIT_TIME_WHEN_RUNNING = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> INTERESTED_TICKS = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.INT);

    Hamster.HamsterGoToWheelGoal hamsterGoToWheelGoal;


    @Nullable
    private BlockPos jukebox;
    private boolean hamsterDance;

    protected int interestedTicks;

    // endregion
    private static final int TICKS_BEFORE_GOING_TO_WHEEL = 2400;
    public static final String TAG_WHEEL_POS = "WheelPos";
    private static final int COOLDOWN_BEFORE_LOCATING_NEW_WHEEL = 200;
    int remainingCooldownBeforeLocatingNewWheel;

    int ticksWithoutWaterSinceExitingWheel;
    @Nullable
    BlockPos wheelPos;


    public Hamster(EntityType<? extends Hamster> entityType, Level level) {
        super(entityType, level);
        if (!this.isSleeping()) {
            this.setCanPickUpLoot(true);
        }
        this.lookControl = new Hamster.HamsterLookControl();
        // region PATHFINDING
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, 1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_FIRE, 1.0F);
        this.setPathfindingMalus(BlockPathTypes.DAMAGE_CAUTIOUS, 1.0F);
        this.setPathfindingMalus((BlockPathTypes.DANGER_POWDER_SNOW), 1.0F);
        this.setPathfindingMalus((BlockPathTypes.DANGER_OTHER), 1.0F);
        this.setPathfindingMalus((BlockPathTypes.DAMAGE_OTHER), 1.0F);
        this.setPathfindingMalus((BlockPathTypes.WATER_BORDER), 1.0F);
        // endregion
    }


    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new MMPathNavigatorGround(this, level);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new SmartBodyHelper(this);
    }

    public void rideTick() {
        super.rideTick();
        Entity var2 = this.getControlledVehicle();
        if (var2 instanceof HamsterBall hamsterBall) {
            this.yBodyRot = hamsterBall.yBodyRot;
        }

    }

    // region BASIC ENTITY

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.3));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(4, new Hamster.HamsterEnterWheelGoal());
        this.goalSelector.addGoal(5, new Hamster.HamsterLocateWheelGoal());
        this.goalSelector.addGoal(5, new Hamster.HamsterGoToWheelGoal());
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

//    protected void registerGoals() {
//        this.goalSelector.addGoal(0, new FloatGoal(this));
//        this.goalSelector.addGoal(0, new PanicGoal(this, 1.3));
//        this.goalSelector.addGoal(1, new HamsterGoToBlockGoal());
//        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
//        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0));
//        this.goalSelector.addGoal(3, new SleepGoal());
//        this.hamsterGoToWheelGoal = new HamsterGoToWheelGoal();
//        this.goalSelector.addGoal(4, this.hamsterGoToWheelGoal);
//        this.goalSelector.addGoal(4, new SearchForItemsGoal(this, 1.25F, FOOD_ITEMS, 8.0D, 8.0D));
//        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25, FOOD_ITEMS, false));
//        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25));
//        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
//        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
//        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
//    }

    @Override
    public void customServerAiStep() {
        if (!this.isBaby()) {
            if (this.getMoveControl().hasWanted()) {
                this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.3D);
            } else {
                this.setSprinting(false);
            }
        }
        super.customServerAiStep();
    }

    @Override
    public void travel(@NotNull Vec3 vec3) {
        if (this.isBaby()) {
            vec3 = vec3.scale(0.2D);
        }
        if (this.isHamsterDance()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(0, 1, 0));
            vec3 = vec3.multiply(0, 1, 0);
        }
        super.travel(vec3);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 5.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 1.5);
    }

    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);


        if (itemStack.is(HamstersItems.HAMSTER_BALL)) {
            if (!this.level().isClientSide) {
                HamsterBall hamsterBall = new HamsterBall(HamstersEntityType.HAMSTER_BALL, this.level());
                hamsterBall.setPos(this.getX(), this.getY(), this.getZ());
                this.level().addFreshEntity(hamsterBall);
                this.startRiding(hamsterBall);
                this.setPersistenceRequired();
                if (!player.isCreative()) {
                    itemStack.shrink(1);
                }

                this.playSound(SoundEvents.ITEM_PICKUP, 0.4F, ((level().random.nextFloat() - level().random.nextFloat()) * 0.7F + 1.0F) * 1.5F);


            }
            return InteractionResult.SUCCESS;
        }

        if (!this.isBaby()) {
            if (this.level().isClientSide) {
                if (this.isTame() && this.isOwnedBy(player)) {
                    return InteractionResult.SUCCESS;
                } else {
                    return !this.isFood(itemStack) || !(this.getHealth() < this.getMaxHealth()) && this.isTame() ? InteractionResult.PASS : InteractionResult.SUCCESS;
                }
            } else {
                InteractionResult interactionResult;
                if (this.isTame()) {
                    if (this.isOwnedBy(player) && this.isFood(itemStack) && (this.getHealth() < this.getMaxHealth())) {
                        this.usePlayerItem(player, interactionHand, itemStack);
                        this.heal(2.0F);
                        return InteractionResult.CONSUME;
                    } else if (this.isOwnedBy(player)) {
                        interactionResult = super.mobInteract(player, interactionHand);
                        if (!interactionResult.consumesAction() || this.isBaby()) {
                            this.setOrderedToSit(!this.isOrderedToSit());
                        }
                        if (this.isOwnedBy(player) && player.isShiftKeyDown()) {
                            this.catchHamster(player);
                        }
                        return interactionResult;
                    }
                } else if (this.isFood(itemStack)) {
                    this.usePlayerItem(player, interactionHand, itemStack);
                    if (this.random.nextInt(3) == 0) {
                        this.tame(player);
                        this.setOrderedToSit(true);
                        this.level().broadcastEntityEvent(this, (byte) 7);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte) 6);
                    }
                    this.setPersistenceRequired();
                    return InteractionResult.CONSUME;
                }
            }
        }
        return super.mobInteract(player, interactionHand);
    }

    protected float getStandingEyeHeight(@NotNull Pose pose, @NotNull EntityDimensions entityDimensions) {
        return this.isBaby() ? 0.2F : 0.3F;
    }

    public boolean isFood(@NotNull ItemStack itemStack) {
        return FOOD_ITEMS.test(itemStack);
    }

    // endregion

    // region CATCHING

    public void catchHamster(Player player) {
        ItemStack output = this.getCaughtItemStack();
        saveDefaultDataToItemTag(this, output);
        if (!player.getInventory().add(output)) {
            ItemEntity itemEntity = new ItemEntity(level(), this.getX(), this.getY() + 0.5, this.getZ(), output);
            itemEntity.setPickUpDelay(0);
            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().multiply(0, 1, 0));
            level().addFreshEntity(itemEntity);
        }
        this.discard();
        player.getInventory().add(output);
    }

    private static void saveDefaultDataToItemTag(Hamster mob, ItemStack itemStack) {
        CompoundTag compoundTag = itemStack.getOrCreateTag();
        if (mob.hasCustomName()) {
            itemStack.setHoverName(mob.getCustomName());
        }
        try {
            compoundTag.putShort("Air", (short)mob.getAirSupply());
            compoundTag.putBoolean("Invulnerable", mob.isInvulnerable());
            if (mob.isCustomNameVisible()) compoundTag.putBoolean("CustomNameVisible", mob.isCustomNameVisible());
            if (mob.isSilent()) compoundTag.putBoolean("Silent", mob.isSilent());
            if (mob.isNoGravity()) compoundTag.putBoolean("NoGravity", mob.isNoGravity());
            if (mob.hasGlowingTag()) compoundTag.putBoolean("Glowing", true);
            mob.addAdditionalSaveData(compoundTag);
        }
        catch (Throwable var9) {
            CrashReport crashReport = CrashReport.forThrowable(var9, "Saving entity NBT");
            CrashReportCategory crashReportCategory = crashReport.addCategory("Entity being saved");
            mob.fillCrashReportCategory(crashReportCategory);
            throw new ReportedException(crashReport);
        }
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromHand();
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public ItemStack getCaughtItemStack() {
        return new ItemStack(HamstersItems.HAMSTER);
    }
    // endregion

    // region SOUNDS

    protected SoundEvent getAmbientSound() {
        return this.isSleeping() ? HamstersSoundEvents.HAMSTER_SLEEP : HamstersSoundEvents.HAMSTER_AMBIENT;
    }

    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return HamstersSoundEvents.HAMSTER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return HamstersSoundEvents.HAMSTER_DEATH;
    }

    protected void playStepSound(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        this.playSound(SoundEvents.WOLF_STEP, 0.15F, 3.0F);
    }

    protected void playBegSound() {
        this.playSound(HamstersSoundEvents.HAMSTER_BEG, this.getSoundVolume(), this.getVoicePitch());
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    // endregion

    // region DATA

    void clearStates() {
        this.setIsInterested(false);
        this.setInSittingPose(false);
        this.setSleeping(false);
    }

    public void setIsInterested(boolean bl) {
        this.entityData.set(DATA_INTERESTED, bl);
    }
    public boolean isInterested() {
        return this.entityData.get(DATA_INTERESTED);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 2);
        this.entityData.define(MARKING, 0);
        this.entityData.define(FROM_HAND, false);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.wheelPos = null;
        if (compoundTag.contains("WheelPos")) {
            this.wheelPos = NbtUtils.readBlockPos(compoundTag.getCompound("WheelPos"));
        }
        this.setVariant(Hamster.Variant.BY_ID[compoundTag.getInt("Variant")]);
        this.setMarking(compoundTag.getInt("Marking"));

        this.setFromHand(compoundTag.getBoolean("FromHand"));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);

        if (this.hasWheel()) {
            assert this.getHivePos() != null;
            compoundTag.put("HivePos", NbtUtils.writeBlockPos(this.getHivePos()));
        }
        compoundTag.putInt("Variant", this.getVariant());
        compoundTag.putInt("Marking", this.getMarking());
        compoundTag.putBoolean("FromHand", this.fromHand());
    }


    @VisibleForDebug
    public boolean hasWheel() {
        return this.wheelPos != null;
    }

    @Nullable
    @VisibleForDebug
    public BlockPos getHivePos() {
        return this.wheelPos;
    }

    public int getInterestedTicks() {
        return this.entityData.get(INTERESTED_TICKS);
    }

    public void setInterestedTicks(int ticks) {
        this.entityData.set(INTERESTED_TICKS, ticks);
    }

    public int getWaitTimeBeforeRunTicks() {
        return this.entityData.get(WAIT_TIME_BEFORE_RUN);
    }
    
    public void setWaitTimeBeforeRunTicks(int ticks) {
        this.entityData.set(WAIT_TIME_BEFORE_RUN, ticks);
    }

    public int getWaitTimeWhenRunningTicks() {
        return this.entityData.get(WAIT_TIME_WHEN_RUNNING);
    }
    public void setWaitTimeWhenRunningTicks(int ticks) {
        this.entityData.set(WAIT_TIME_WHEN_RUNNING, ticks);
    }


    public boolean isSleeping() {
        return this.getFlag();
    }

    public void setSleeping(boolean bl) {
        this.setFlag(bl);
    }

    private void setFlag(boolean bl) {
        if (bl) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) | 32));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) & ~32));
        }

    }

    private boolean getFlag() {
        return (this.entityData.get(DATA_FLAGS_ID) & 32) != 0;
    }

    void wakeUp() {
        this.setSleeping(false);
    }

    public void tick() {
        super.tick();

        if (Hamster.this.getInterestedTicks() < 400) {
            Hamster.this.setInterestedTicks(Hamster.this.getInterestedTicks() + 1);
        } else if (Hamster.this.getInterestedTicks() >= 400) {
            Hamster.this.setInterestedTicks(0);
        }

        if (this.isEffectiveAi()) {
            if (this.isInWater() || this.getTarget() != null || this.level().isThundering()) {
                this.wakeUp();
            }

            if (this.isInWater() || this.isSleeping()) {
                this.setInSittingPose(false);
            }
        }

    }

    public void setRecordPlayingNearby(@NotNull BlockPos blockPos, boolean bl) {
        this.jukebox = blockPos;
        this.hamsterDance = bl;
        this.setSleeping(false);
    }

    public boolean isHamsterDance() {
        return this.hamsterDance;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.level();

        if (this.jukebox == null || !this.jukebox.closerToCenterThan(this.position(), 3.46) || !this.level().getBlockState(this.jukebox).is(Blocks.JUKEBOX)) {
            this.hamsterDance = false;
            this.jukebox = null;
        }

        this.blockPosition();
        if (this.level().getBlockState(this.blockPosition()).is(HamstersBlocks.HAMSTER_WHEEL)) {
            this.setDeltaMovement(0, 0, 0);
        }

        // System.out.println(getWaitTimeWhenRunningTicks());

        if (this.getWaitTimeWhenRunningTicks() > 0) {
            this.setWaitTimeWhenRunningTicks(this.getWaitTimeWhenRunningTicks() - 1);
        }
        if (this.isPassenger() && this.getVehicle() instanceof SeatEntity && this.getWaitTimeWhenRunningTicks() == 0) {
            this.setWaitTimeBeforeRunTicks(this.random.nextInt(400) + 1200);

            this.stopRiding();
            clearStates();
        }

        if (this.isInterested()) {
            if (this.tickCount % 40 == 0) {
                this.playBegSound();
            }
        }

        if (this.isAlive() && !this.isInterested() && !this.isSleeping() && !this.isImmobile() && this.getTarget() == null) {
            List<Player> list = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
            for (Player player : list) {
                if (!player.isSpectator() && player.isHolding(FOOD_ITEMS) && distanceToSqr(player) < 2.0D) {
                    this.setIsInterested(true);
                    this.getNavigation().stop();
                }
            }
        }

        if (this.isInterested() && (this.getTarget() == null)) {
            List<Player> list = this.level().getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
            for (Player player : list) {
                if (!player.isSpectator() && player.isHolding(FOOD_ITEMS) && distanceToSqr(player) > 2.0D) {
                    this.setIsInterested(false);
                }
            }
        }

        if (this.isSleeping() || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }
    }

    // endregion

    // region BREEDING / VARIANTS / MIXING

    public boolean canMate(@NotNull Animal animal) {
        if (!this.isTame()) {
            return false;
        } else if (!(animal instanceof Hamster hamster)) {
            return false;
        } else {
            return hamster.isTame() && super.canMate(animal);
        }
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

    public void setVariant(Hamster.Variant variant) {
        this.entityData.set(VARIANT, variant.getId());
    }


    public boolean fromHand() {
        return this.entityData.get(FROM_HAND);
    }

    public void setFromHand(boolean fromHand) {
        this.entityData.set(FROM_HAND, fromHand);
    }

    public enum Marking {
        BLANK (0, "blank"),
        BANDED (1, "banded"),
        DOMINANT_SPOTS (2, "dominant_spots"),
        ROAN (3, "roan"),
        BELLY (4, "belly");

        private static final IntFunction<Marking> BY_ID = ByIdMap.continuous(Marking::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

        private final int id;
        private final String name;

        private Marking(int j, String string2) {
            this.id = j;
            this.name = string2;
        }

        public int getId() {
            return this.id;
        }

        public static Marking byId(int i) {
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

        public static final Hamster.Variant[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(Variant::getId)).toArray(Variant[]::new);
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

        public static Variant getTypeById(int id) {
            for (Variant type : values()) {
                if (type.id == id) return type;
            }
            return Variant.CHAMPAGNE;
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        Hamster hamster = HamstersEntityType.HAMSTER.create(serverLevel);
        assert hamster != null;
        if (ageableMob instanceof Hamster hamsterParent) {
            hamster.setVariant(this.getOffspringVariant(this, hamsterParent));
            hamster.setMarking(this.getOffspringPattern(this, hamsterParent).getId());
        }
        return hamster;
    }

    private Marking getOffspringPattern(Hamster hamster, Hamster otherParent) {
        Marking marking = Marking.byId(hamster.getMarking());
        Marking otherMarking = Marking.byId(otherParent.getMarking());

        return this.random.nextBoolean() ? marking : otherMarking;
    }

    private Variant getOffspringVariant(Hamster hamster, Hamster otherParent) {
        Variant variant = Variant.getTypeById(hamster.getVariant());
        Variant otherVariant = Variant.getTypeById(otherParent.getVariant());

        return this.random.nextBoolean() ? variant : otherVariant;
    }

    // endregion


    // region PICK UP ITEMS

    @Override
    public boolean canTakeItem(@NotNull ItemStack pItemstack) {
        EquipmentSlot slot = getEquipmentSlotForItem(pItemstack);
        if (!this.getItemBySlot(slot).isEmpty()) {
            return false;
        } else {
            return slot == EquipmentSlot.MAINHAND && super.canTakeItem(pItemstack);
        }
    }

    @Override
    protected void pickUpItem(ItemEntity pItemEntity) {
        ItemStack stack = pItemEntity.getItem();
        if (!this.isSleeping()) {
            if (this.getMainHandItem().isEmpty() && FOOD_ITEMS.test(stack)) {
                this.onItemPickup(pItemEntity);
                this.setItemSlot(EquipmentSlot.MAINHAND, stack);
                this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0F;
                this.take(pItemEntity, stack.getCount());
                pItemEntity.discard();
            }
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        if (!this.getMainHandItem().isEmpty() && !this.level().isClientSide) {
            ItemEntity itemEntity = new ItemEntity(this.level(), this.getX() + this.getLookAngle().x, this.getY() + 1.0D, this.getZ() + this.getLookAngle().z, this.getMainHandItem());
            itemEntity.setPickUpDelay(40);
            itemEntity.setThrower(this.getUUID());
            this.playSound(SoundEvents.FOX_SPIT, 1.0F, 1.0F);
            this.level().addFreshEntity(itemEntity);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        return super.hurt(pSource, pAmount);
    }

    // endregion

    // region SPAWNING

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.populateDefaultEquipmentSlots(random, pDifficulty);
        if (pSpawnData == null) {
            RandomSource randomSource = pLevel.getRandom();
            this.setVariant(Variant.values()[randomSource.nextInt(Variant.values().length)]);
            this.setMarking(Marking.values()[randomSource.nextInt(Marking.values().length)].getId());
        }
        return pSpawnData;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, @NotNull DifficultyInstance pDifficulty) {
        if (random.nextFloat() < 0.2F) {
            float chance = random.nextFloat();
            ItemStack stack;
            if (chance < 0.1F) {
                stack = new ItemStack(Items.BEETROOT_SEEDS);
            } else if (chance < 0.15F) {
                stack = new ItemStack(Items.PUMPKIN_SEEDS);
            } else if (chance < 0.3F) {
                stack = new ItemStack(Items.MELON_SEEDS);
            } else {
                stack = new ItemStack(Items.WHEAT_SEEDS);
            }

            this.setItemSlot(EquipmentSlot.MAINHAND, stack);
        }
    }

    // endregion

    // region GECKOLIB

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::animController));
    }

    protected <E extends Hamster> PlayState animController(final AnimationState<E> event) {
        if (this.isPassenger() && this.getVehicle() instanceof HamsterBall hamsterBall && hamsterBall.getDeltaMovement().length() > 0.1) {
            event.setAnimation(WALK);
            return PlayState.CONTINUE;
        } else

        if (this.isHamsterDance()) {
            event.setAnimation(DANCE);
            return PlayState.CONTINUE;
        } else if (this.isSleeping()) {
            event.setAnimation(SLEEP);
        } else if (this.isInterested() || this.isInSittingPose()) {
            event.setAnimation(STANDING);
        }  else if (event.isMoving()) {
            if (this.isSprinting()) {
                event.setControllerSpeed(1.3F);
                event.setAnimation(RUN);
            } else {
                event.setControllerSpeed(1.1F);
                if (this.isBaby()) {
                    event.setAnimation(PINKIE_WALK);
                } else {
                    event.setAnimation(WALK);
                }
            }
        }  else if (this.isPassenger() && this.getVehicle() instanceof SeatEntity) {
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

    // region GOALS

    public class HamsterLookControl extends LookControl {
        public HamsterLookControl() {
            super(Hamster.this);
        }

        public void tick() {
            if (!(Hamster.this.getVehicle() instanceof SeatEntity)) {
                super.tick();
            } else {
                BlockState state = Hamster.this.level().getBlockState(Hamster.this.blockPosition());
                if (state.is(HamstersBlocks.HAMSTER_WHEEL)) {
                    BlockPos pos1;

                    if (state.getValue(FACING) == Direction.SOUTH) {
                        pos1 = Hamster.this.blockPosition().east(1);
                    } else if (state.getValue(FACING) == Direction.NORTH) {
                        pos1 = Hamster.this.blockPosition().west(1);
                    } else if (state.getValue(FACING) == Direction.EAST) {
                        pos1 = Hamster.this.blockPosition().north(1);
                    } else {
                        pos1 = Hamster.this.blockPosition().south(1);
                    }
                    Hamster.this.setSleeping(false);
                    Hamster.this.setInSittingPose(false);
                    Hamster.this.lookAt(EntityAnchorArgument.Anchor.FEET, new Vec3(pos1.getX() + 0.5f, pos1.getY() + 0.5f, pos1.getZ() + 0.5f));
                }
            }
        }

        protected boolean resetXRotOnTick() {
            return !(Hamster.this.getVehicle() instanceof SeatEntity);
        }
    }

    class HamsterGoToBlockGoal extends Goal {

        private final Predicate<BlockState> VALID_GATHERING_BLOCKS;
        @Nullable
        private Vec3 blockPos;

        HamsterGoToBlockGoal() {
            this.VALID_GATHERING_BLOCKS = blockState -> {
                if (blockState.is(HamstersTags.HAMSTER_BLOCKS)) {
                    if (blockState.getBlock() instanceof BowlBlock) {
                        return blockState.getBlock().defaultBlockState().hasProperty(BowlBlock.SEEDS) && (blockState.getValue(BowlBlock.SEEDS) > 0);
                    }
                    return !blockState.hasProperty(BlockStateProperties.WATERLOGGED) || !blockState.getValue(BlockStateProperties.WATERLOGGED);
                }
                return false;
            };
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            Optional<BlockPos> optional = this.findNearbyResource();
            if (optional.isPresent() && Hamster.this.getInterestedTicks() > 100) {
                Hamster.this.navigation.moveTo((double) optional.get().getX() + 0.5, optional.get().getY(), (double) optional.get().getZ() + 0.5, 1.2f);
                return !Hamster.this.level().isRaining() && !Hamster.this.isSleeping() && !Hamster.this.isInSittingPose();
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            Optional<BlockPos> optional = this.findNearbyResource();
            if (optional.isPresent()) {
                return !Hamster.this.isSleeping() && !Hamster.this.isInSittingPose() && getInterestedTicks() < 200;
            }
            return false;
        }

        @Override
        public void stop() {
            Hamster.this.navigation.stop();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            Optional<BlockPos> optional = this.findNearbyResource();

//            if (block.getBlock() instanceof BottleBlock) {
//                if (Hamster.this.position().distanceTo(Vec3.atBottomCenterOf(optional.get())) <= 1.4) {
//                    if (Hamster.this.tickCount % 20 == 0) {
//                        Hamster.this.playSound(SoundEvents.GENERIC_DRINK, 0.5F, 1.3F);
//                    }
//                }
//            }
            if (optional.isPresent()) {
                BlockState block = Hamster.this.level().getBlockState(optional.get());

                if (block.getBlock() instanceof BowlBlock) {

                    if (block.getBlock().defaultBlockState().hasProperty(BowlBlock.SEEDS) && block.getValue(BowlBlock.SEEDS) > 0) {
                        if (Hamster.this.position().distanceTo(Vec3.atBottomCenterOf(optional.get())) <= 1.0) {
                            if (Hamster.this.getMainHandItem().isEmpty()) {
                                Hamster.this.level().setBlockAndUpdate(optional.get(), block.setValue(BowlBlock.SEEDS, block.getValue(BowlBlock.SEEDS) - 1));
                                Hamster.this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WHEAT_SEEDS));
                                Hamster.this.playSound(SoundEvents.PARROT_EAT, 1.0F, 1.0F);
                            }
                        }
                    }
                }

                Vec3 vec3 = Vec3.atBottomCenterOf(optional.get());
                if (vec3.distanceTo(Hamster.this.position()) > 0.7) {
                    blockPos = vec3;
                    this.setWantedPos();
                    return;
                }
                if (blockPos == null) {
                    this.blockPos = vec3;
                }
            }

            super.tick();

        }

        private void setWantedPos() {
            assert this.blockPos != null;
            if (Hamster.this.level().getBlockState(Hamster.this.blockPosition()).getBlock() instanceof BowlBlock && (Hamster.this.level().getBlockState(Hamster.this.blockPosition()).getValue(BowlBlock.SEEDS) == 0)) {
                return;
            } else {
            Hamster.this.getMoveControl().setWantedPosition(this.blockPos.x(), this.blockPos.y(), this.blockPos.z(), 0.7f);
            }
        }


        private Optional<BlockPos> findNearbyResource() {
            return this.findNearestBlock(this.VALID_GATHERING_BLOCKS);
        }

        private Optional<BlockPos> findNearestBlock(Predicate<BlockState> predicate) {
            BlockPos blockPos = Hamster.this.blockPosition();
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            int i = 0;
            while ((double) i <= 5.0) {
                int j = 0;
                while ((double) j < 5.0) {
                    int k = 0;
                    while (k <= j) {
                        int l = k < j && k > -j ? j : 0;
                        while (l <= j) {
                            mutableBlockPos.setWithOffset(blockPos, k, i - 1, l);
                            if (blockPos.closerThan(mutableBlockPos, 5.0) && predicate.test(Hamster.this.level().getBlockState(mutableBlockPos))) {
                                return Optional.of(mutableBlockPos);
                            }
                            l = l > 0 ? -l : 1 - l;
                        }
                        k = k > 0 ? -k : 1 - k;
                    }
                    ++j;
                }
                i = i > 0 ? -i : 1 - i;
            }
            return Optional.empty();


        }
    }

    private class SleepGoal extends Goal {
        private final TargetingConditions alertableTargeting = TargetingConditions.forNonCombat().range(6.0).ignoreLineOfSight().selector(new HamsterAlertableEntitiesSelector());
        private final int WAIT_TIME_BEFORE_SLEEP = random.nextInt(100) + 100;
        private int countdown;

        public SleepGoal() {
            super();
            this.countdown = Hamster.this.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        public boolean canUse() {
            if (!isHamsterDance()) {
                return false;
            } else if (Hamster.this.xxa == 0.0F && Hamster.this.yya == 0.0F && Hamster.this.zza == 0.0F) {
                return this.canSleep() || Hamster.this.isSleeping();
            } else {
                return false;
            }
        }

        public boolean canContinueToUse() {
            return this.canSleep();
        }

        private boolean canSleep() {
            if (this.countdown > 0) {
                --this.countdown;
                return false;
            } else {
                return ((Hamster.this.level().getDayTime() >= 1000 && Hamster.this.level().getDayTime() <= 10000) || (Hamster.this.level().getDayTime() >= 16000 && Hamster.this.level().getDayTime() <= 21000)) && !Hamster.this.isInPowderSnow && !this.alertable() && !Hamster.this.isPassenger();
            }
        }

        public void stop() {
            this.countdown = Hamster.this.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
            clearStates();
        }

        public void start() {
            Hamster.this.setInSittingPose(false);
            Hamster.this.setIsInterested(false);
            Hamster.this.setJumping(false);
            Hamster.this.setSleeping(true);
            Hamster.this.getNavigation().stop();
            Hamster.this.getMoveControl().setWantedPosition(Hamster.this.getX(), Hamster.this.getY(), Hamster.this.getZ(), 0.0);
        }


        protected boolean alertable() {
            return !Hamster.this.level().getNearbyEntities(LivingEntity.class, this.alertableTargeting, Hamster.this, Hamster.this.getBoundingBox().inflate(12.0, 6.0, 12.0)).isEmpty();
        }
    }

    public static class HamsterAlertableEntitiesSelector implements Predicate<LivingEntity> {
        public HamsterAlertableEntitiesSelector() {
        }

        public boolean test(LivingEntity livingEntity) {
            if (livingEntity instanceof Hamster) {
                return false;
            } else if (livingEntity instanceof Player && (livingEntity.isSpectator() || ((Player)livingEntity).isCreative())) {
                return false;
            } else {
                return !livingEntity.isSleeping() && !livingEntity.isDiscrete();
            }
        }
    }
    // endregion

    void pathfindTowards(BlockPos blockPos) {
        Vec3 vec3 = Vec3.atBottomCenterOf(blockPos);
        this.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, 1.0);
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



    private abstract static class BaseHamsterGoal extends Goal {
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

    class HamsterEnterWheelGoal extends Hamster.BaseHamsterGoal {
        HamsterEnterWheelGoal() {
            super();
        }

        public boolean canHamsterUse() {
            return Hamster.this.wheelPos != null && Hamster.this.closerThan(Hamster.this.wheelPos, 2) && Hamster.this.isWheelValid(Hamster.this.wheelPos);
        }

        public boolean canBeeContinueToUse() {
            return false;
        }

        public void start() {
            assert Hamster.this.wheelPos != null;
            Block block = Hamster.this.level().getBlockState(Hamster.this.wheelPos).getBlock();
            BlockEntity blockEntity = Hamster.this.level().getBlockEntity(Hamster.this.wheelPos);
            if (block instanceof HamsterWheelBlock && blockEntity instanceof HamsterWheelBlockEntity) {
                if (HamsterWheelBlock.isOccupied(Hamster.this.level(), Hamster.this.wheelPos)) {
                    Hamster.this.wheelPos = null;
                } else {
                    HamsterWheelBlock.sitDown(Hamster.this.level(), Hamster.this.wheelPos, Hamster.this);
                }
            }
        }
    }

    class HamsterLocateWheelGoal extends Hamster.BaseHamsterGoal {
        HamsterLocateWheelGoal() {
            super();
        }

        public boolean canHamsterUse() {
            return Hamster.this.remainingCooldownBeforeLocatingNewWheel == 0 && Hamster.this.wheelPos == null;
        }

        public boolean canBeeContinueToUse() {
            return false;
        }

        public void start() {
            Hamster.this.remainingCooldownBeforeLocatingNewWheel = 200;
            BlockPos blockPos = findNearbyWheel();
            if (blockPos != null) {
                Hamster.this.wheelPos = blockPos;
            }
        }

        @Nullable
        private BlockPos findNearbyWheel() {
            BlockPos blockPos = Hamster.this.blockPosition();
            PoiManager poiManager = ((ServerLevel)Hamster.this.level()).getPoiManager();
            Stream<PoiRecord> stream = poiManager.getInRange((holder) -> holder.is(HamstersPoiTypes.HAMSTER_WHEEL), blockPos, 20, PoiManager.Occupancy.ANY);
            return stream.map(PoiRecord::getPos).filter(Hamster.this::isWheelValid).min(Comparator.comparingDouble((blockPos2) -> blockPos2.distSqr(blockPos))).orElse(null);
        }
    }

    @VisibleForDebug
    public class HamsterGoToWheelGoal extends Hamster.BaseHamsterGoal {
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
            this.travellingTicks = Hamster.this.level().random.nextInt(10);
            this.blacklistedTargets = Lists.newArrayList();
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canHamsterUse() {
            return Hamster.this.wheelPos != null && !Hamster.this.hasRestriction() && !this.hasReachedTarget(Hamster.this.wheelPos) && Hamster.this.isWheelValid(Hamster.this.wheelPos);
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
            Hamster.this.getNavigation().stop();
            Hamster.this.getNavigation().resetMaxVisitedNodesMultiplier();
        }

        public void tick() {
            if (Hamster.this.wheelPos != null) {
                ++this.travellingTicks;
                if (this.travellingTicks > this.adjustedTickDelay(600)) {
                    this.dropAndBlacklistWheel();
                } else if (!Hamster.this.getNavigation().isInProgress()) {
                    if (!Hamster.this.closerThan(Hamster.this.wheelPos, 16)) {
                        if (Hamster.this.isTooFarAway(Hamster.this.wheelPos)) {
                            this.dropWheel();
                        } else {
                            Hamster.this.pathfindTowards(Hamster.this.wheelPos);
                        }
                    } else {
                        boolean bl = this.pathfindDirectlyTowards(Hamster.this.wheelPos);
                        if (!bl) {
                            this.dropAndBlacklistWheel();
                        } else if (this.lastPath != null && Hamster.this.getNavigation().getPath().sameAs(this.lastPath)) {
                            ++this.ticksStuck;
                            if (this.ticksStuck > 60) {
                                this.dropWheel();
                                this.ticksStuck = 0;
                            }
                        } else {
                            this.lastPath = Hamster.this.getNavigation().getPath();
                        }
                    }
                }
            }
        }
        private boolean pathfindDirectlyTowards(BlockPos blockPos) {
            Hamster.this.getNavigation().setMaxVisitedNodesMultiplier(10.0F);
            Hamster.this.getNavigation().moveTo(blockPos.getX(), blockPos.getY(), blockPos.getZ(), 1.0);
            return Hamster.this.getNavigation().getPath() != null && Hamster.this.getNavigation().getPath().canReach();
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
            if (Hamster.this.wheelPos != null) {
                this.blacklistTarget(Hamster.this.wheelPos);
            }

            this.dropWheel();
        }

        private void dropWheel() {
            Hamster.this.wheelPos = null;
            Hamster.this.remainingCooldownBeforeLocatingNewWheel = 200;
        }

        private boolean hasReachedTarget(BlockPos blockPos) {
            if (Hamster.this.closerThan(blockPos, 2)) {
                return true;
            } else {
                Path path = Hamster.this.getNavigation().getPath();
                return path != null && path.getTarget().equals(blockPos) && path.canReach() && path.isDone();
            }
        }
    }
}

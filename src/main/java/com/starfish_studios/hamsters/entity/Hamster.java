//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.starfish_studios.hamsters.entity;

import com.starfish_studios.hamsters.entity.common.Catchable;
import com.starfish_studios.hamsters.entity.common.SearchForItemsGoal;
import com.starfish_studios.hamsters.registry.HamstersBlocks;
import com.starfish_studios.hamsters.registry.HamstersEntityType;
import com.starfish_studios.hamsters.registry.HamstersItems;
import com.starfish_studios.hamsters.registry.HamstersSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
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

import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class Hamster extends TamableAnimal implements GeoEntity, Catchable {
    // region
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.run");
    protected static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.sleep");
    protected static final RawAnimation STANDING = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.standing");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Integer> EAT_COUNTER = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.INT);

    private static final Ingredient FOOD_ITEMS = Ingredient.of(ItemTags.VILLAGER_PLANTABLE_SEEDS);
    private static final EntityDataAccessor<Boolean> DATA_INTERESTED = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_HAND = SynchedEntityData.defineId(Hamster.class, EntityDataSerializers.BOOLEAN);
    private int sleepTime;

    // endregion

    public Hamster(EntityType<? extends Hamster> entityType, Level level) {
        super(entityType, level);
        if (!this.isSleeping()) {
            this.setCanPickUpLoot(true);
        }
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

    // region BASIC ENTITY

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.3));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new SleepGoal());
        this.goalSelector.addGoal(4, new SearchForItemsGoal(this, 1.25F, FOOD_ITEMS, 8.0D, 8.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.25, FOOD_ITEMS, false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }

    @Override
    public void customServerAiStep() {
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.3D);
        } else {
            this.setSprinting(false);
        }
        super.customServerAiStep();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 5.0).add(Attributes.MOVEMENT_SPEED, 0.25).add(Attributes.ATTACK_DAMAGE, 1.5);
    }

    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {

        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (this.level().isClientSide) {
            if (this.isTame() && this.isOwnedBy(player)) {
                if (!player.isShiftKeyDown()) {
                    return InteractionResult.SUCCESS;
                }
                return Catchable.catchAnimal(player, interactionHand, this).orElse(super.mobInteract(player, interactionHand));
            } else {
                return !this.isFood(itemStack) || !(this.getHealth() < this.getMaxHealth()) && this.isTame() ? InteractionResult.PASS : InteractionResult.SUCCESS;
            }
        } else {
            InteractionResult interactionResult;
            if (this.isTame()) {
                if (this.isOwnedBy(player)) {
                    if (this.getHealth() < this.getMaxHealth()) {
                        this.usePlayerItem(player, interactionHand, itemStack);
                        this.heal(2.0F);
                        return InteractionResult.CONSUME;
                    }

                    interactionResult = super.mobInteract(player, interactionHand);
                    if (!interactionResult.consumesAction() || this.isBaby()) {
                        this.setOrderedToSit(!this.isOrderedToSit());
                    }

                    return interactionResult;
                }
            } else if (this.isFood(itemStack)) {
                this.usePlayerItem(player, interactionHand, itemStack);
                if (this.random.nextInt(3) == 0) {
                    this.tame(player);
                    this.setOrderedToSit(true);
                    this.level().broadcastEntityEvent(this, (byte)7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte)6);
                }

                this.setPersistenceRequired();
                return InteractionResult.CONSUME;
            }
        }
        return Catchable.catchAnimal(player, interactionHand, this).orElse(super.mobInteract(player, interactionHand));
    }

    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return this.isBaby() ? 0.2F : 0.3F;
    }

    public boolean isFood(ItemStack itemStack) {
        return FOOD_ITEMS.test(itemStack);
    }

    // endregion

    // region CATCHING

    public void saveToHandTag(ItemStack stack) {
        Catchable.saveDefaultDataToHandTag(this, stack);
        CompoundTag compoundTag = stack.getOrCreateTag();
        compoundTag.putInt("Variant", this.getVariant().getId());
        compoundTag.putInt("Age", this.getAge());

    }

    public void loadFromHandTag(CompoundTag tag) {
        Catchable.loadDefaultDataFromHandTag(this, tag);
        int i = tag.getInt("Variant");
        if (i >= 0 && i < Variant.BY_ID.length) {
            this.setVariant(Variant.BY_ID[i]);
        }

        if (tag.contains("Age")) {
            this.setAge(tag.getInt("Age"));
        }
    }

    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromHand();
    }

    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    public ItemStack getCaughtItemStack() {
        return new ItemStack(HamstersItems.HAMSTER.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return null;
    }

    // endregion

    // region SOUNDS



    protected SoundEvent getAmbientSound() {
        return HamstersSoundEvents.HAMSTER_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return HamstersSoundEvents.HAMSTER_HURT;
    }

    protected SoundEvent getDeathSound() {
        return HamstersSoundEvents.HAMSTER_DEATH;
    }

    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
        this.playSound(SoundEvents.COW_STEP, 0.15F, 1.0F);
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
        this.entityData.define(EAT_COUNTER, 0);
        this.entityData.define(DATA_INTERESTED, false);
        this.entityData.define(DATA_VARIANT, 2);
        this.entityData.define(FROM_HAND, false);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setVariant(Hamster.Variant.BY_ID[compoundTag.getInt("Variant")]);
        this.setFromHand(compoundTag.getBoolean("FromHand"));
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Variant", getVariant().getId());
        compoundTag.putBoolean("FromHand", this.fromHand());
    }

    public boolean isSleeping() {
        return this.getFlag(32);
    }

    public void setSleeping(boolean bl) {
        this.setFlag(32, bl);
    }

    private void setFlag(int i, boolean bl) {
        if (bl) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) | i));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(this.entityData.get(DATA_FLAGS_ID) & ~i));
        }

    }

    private boolean getFlag(int i) {
        return (this.entityData.get(DATA_FLAGS_ID) & i) != 0;
    }

    void wakeUp() {
        this.setSleeping(false);
    }

    public void tick() {
        super.tick();
        if (this.isEffectiveAi()) {
            if (this.isInWater() || this.getTarget() != null || this.level().isThundering()) {
                this.wakeUp();
            }

            if (this.isInWater() || this.isSleeping()) {
                this.setInSittingPose(false);
            }
        }

    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.level();

        this.blockPosition();
        if (this.level().getBlockState(this.blockPosition()).is(HamstersBlocks.HAMSTER_WHEEL)) {
            this.setDeltaMovement(0, 0, 0);
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

    public boolean canMate(Animal animal) {
        if (!this.isTame()) {
            return false;
        } else if (!(animal instanceof Hamster)) {
            return false;
        } else {
            Hamster hamster = (Hamster) animal;
            return hamster.isTame() && super.canMate(animal);
        }
    }


    public Hamster.Variant getVariant() {
        return Hamster.Variant.BY_ID[this.entityData.get(DATA_VARIANT)];
    }

    public void setVariant(Hamster.Variant variant) {
        this.entityData.set(DATA_VARIANT, variant.getId());
    }


    public boolean fromHand() {
        return this.entityData.get(FROM_HAND);
    }

    public void setFromHand(boolean fromHand) {
        this.entityData.set(FROM_HAND, fromHand);
    }

    public enum Variant {
        WHITE (0, "white"),
        PEACHES_AND_CREAM (1, "peaches_and_cream"),
        ORANGE (2, "orange"),
        GREY_WHITE (3, "grey_white"),
        BROWN (4, "brown"),
        BLACK_WHITE (5, "black_white"),
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
            return Variant.ORANGE;
        }
    }

    @Nullable
    public Hamster getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob entity) {
        Hamster hamster = HamstersEntityType.HAMSTER.create(level);
        assert hamster != null;
        hamster.setVariant(Variant.values()[random.nextInt(Variant.values().length)]);
        return hamster;
    }
    
    // endregion


    // region PICK UP ITEMS

    @Override
    public boolean canTakeItem(ItemStack pItemstack) {
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
    public boolean hurt(DamageSource pSource, float pAmount) {
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
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.populateDefaultEquipmentSlots(random, pDifficulty);
        if (pSpawnData == null) {
            RandomSource randomSource = pLevel.getRandom();
            this.setVariant(Variant.values()[randomSource.nextInt(Variant.values().length)]);
        }
        return pSpawnData;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance pDifficulty) {
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
        if (this.isSleeping()) {
            event.setAnimation(SLEEP);
        } else if (this.isInterested() || this.isInSittingPose()) {
            event.setAnimation(STANDING);
        }  else if (event.isMoving()) {
            if (this.isSprinting()) {
                event.setControllerSpeed(1.3F);
                event.setAnimation(RUN);
            } else {
                event.setControllerSpeed(1.1F);
                event.setAnimation(WALK);
            }
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



    private class SleepGoal extends HamsterBehaviorGoal {
        private final int WAIT_TIME_BEFORE_SLEEP = random.nextInt(100) + 100;
        private int countdown;

        public SleepGoal() {
            super();
            this.countdown = Hamster.this.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        }

        public boolean canUse() {
            if (Hamster.this.xxa == 0.0F && Hamster.this.yya == 0.0F && Hamster.this.zza == 0.0F) {
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
                return Hamster.this.level().isDay() && !Hamster.this.isInPowderSnow && !this.alertable();
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
    }

    private abstract class HamsterBehaviorGoal extends Goal {
        private final TargetingConditions alertableTargeting = TargetingConditions.forNonCombat().range(6.0).ignoreLineOfSight().selector(new HamsterAlertableEntitiesSelector());

        HamsterBehaviorGoal() {
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
}

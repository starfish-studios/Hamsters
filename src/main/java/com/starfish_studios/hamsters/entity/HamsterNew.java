package com.starfish_studios.hamsters.entity;

import com.google.common.collect.Lists;
import com.starfish_studios.hamsters.entity.common.MMPathNavigatorGround;
import com.starfish_studios.hamsters.entity.common.SmartBodyHelper;
import com.starfish_studios.hamsters.registry.HamstersEntityType;
import com.starfish_studios.hamsters.registry.HamstersSoundEvents;
import com.starfish_studios.hamsters.registry.HamstersTags;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FireworkParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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

public class HamsterNew extends ShoulderRidingEntity implements GeoEntity {
    // region VARIABLES
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.walk");
    protected static final RawAnimation PINKIE_WALK = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.pinkie_walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.run");
    protected static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.sleep");
    protected static final RawAnimation STANDING = RawAnimation.begin().thenLoop("animation.sf_nba.hamster.standing");
    protected static final RawAnimation SQUISH = RawAnimation.begin().thenPlay("animation.sf_nba.hamster.squish")
            .thenPlayXTimes("animation.sf_nba.hamster.squished", 10)
            .thenPlay("animation.sf_nba.hamster.unsquish");
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(HamsterNew.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MARKING = SynchedEntityData.defineId(HamsterNew.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_BOW_COLOR = SynchedEntityData.defineId(HamsterNew.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> CHEEK_LEVEL = SynchedEntityData.defineId(HamsterNew.class, EntityDataSerializers.INT);

    private int squishedTicks;

    private static final Ingredient FOOD_ITEMS = Ingredient.of(HamstersTags.HAMSTER_FOOD);


    // endregion

    public HamsterNew(EntityType<? extends ShoulderRidingEntity> entityType, Level level) {
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
        this.goalSelector.addGoal(5, new TemptGoal(this, 1.0, FOOD_ITEMS, false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F) {
            @Override
            public void tick() {
                if (squishedTicks > 0) {
                    return;
                }
                super.tick();
            }
        });
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this) {
            @Override
            public void tick() {
                if (squishedTicks > 0) {
                    return;
                }
                super.tick();
            }
        });
//        this.goalSelector.addGoal(10, new GetOnOwnersShoulderGoal(this));

    }

    public static AttributeSupplier.Builder createAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(HamstersTags.HAMSTER_FOOD);
    }

    protected void customServerAiStep() {
        super.customServerAiStep();
    }

    // TODO : Make this only happen when it's fall damage
    public boolean hurt(DamageSource damageSource, float f) {
        boolean bl = super.hurt(damageSource, f);
        this.setSquishedTicks(120);
        this.playSound(SoundEvents.SLIME_HURT, 1.0F, 1.0F);
        return bl;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.squishedTicks > 0) {
            this.setDeltaMovement(0, 0, 0);
            --this.squishedTicks;
            if (this.squishedTicks == 10) {
                this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, 1.0F);
            }
        }

        if (this.level().isClientSide) {
            if (this.getCheekLevel() == 2) {
                if (this.tickCount % 10 == 0) {
                    this.level().addParticle(ParticleTypes.SPLASH, this.getX(), this.getY(1.2), this.getZ(), 0.0D, 0.2D, 0.0D);
                    this.playSound(HamstersSoundEvents.HAMSTER_BEG, 1.0F, 1.0F);
                }
            } else if (this.getCheekLevel() == 3) {
                if (this.tickCount % 5 == 0) {
                    this.level().addParticle(ParticleTypes.SPLASH, this.getX(), this.getY(1.2), this.getZ(), 0.0D, 0.2D, 0.0D);
                }
            }
        }

        if (!this.level().isClientSide && this.isAlive() && this.getCheekLevel() >= 2 && this.tickCount % (80 - (this.getCheekLevel() * 10)) == 0) {
            this.playSound(HamstersSoundEvents.HAMSTER_BEG, 1.0F, 1.0F);
        }
    }

    private ItemStack getFirework(DyeColor dyeColor, int i) {
        ItemStack itemStack = new ItemStack(Items.FIREWORK_ROCKET, 1);
        ItemStack itemStack2 = new ItemStack(Items.FIREWORK_STAR);
        CompoundTag compoundTag = itemStack2.getOrCreateTagElement("Explosion");
        List<Integer> list = Lists.newArrayList();
        // Red
        list.add(16711680);
        // Orange
        list.add(16753920);
        // Yellow
        list.add(16776960);
        // Green
        list.add(65280);
        // Blue
        list.add(255);
        // Purple
        list.add(16711935);
        compoundTag.putIntArray("Colors", list);
        compoundTag.putByte("Type", (byte) FireworkRocketItem.Shape.SMALL_BALL.getId());
        CompoundTag compoundTag2 = itemStack.getOrCreateTagElement("Fireworks");
        ListTag listTag = new ListTag();
        CompoundTag compoundTag3 = itemStack2.getTagElement("Explosion");
        if (compoundTag3 != null) {
            listTag.add(compoundTag3);
        }
        CompoundTag compoundTag4 = itemStack2.getOrCreateTagElement("LifeTime");
        compoundTag4.putInt("LifeTime", 0);

        compoundTag2.putInt("Flight", -128);
        if (!listTag.isEmpty()) {
            compoundTag2.put("Explosions", listTag);
            compoundTag4.putInt("LifeTime", 0);
        }
        return itemStack;
    }


    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        Item item = itemStack.getItem();

        RandomSource randomSource = this.getRandom();

        if (this.level().isClientSide) {
            boolean bl = this.isOwnedBy(player) || this.isTame() || itemStack.is(HamstersTags.HAMSTER_FOOD) && !this.isTame();
            return bl ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else {
            // HAMSTER OVER-FEEDING EXPLOSIONS
//            if (this.isFood(itemStack)) {
//                if (this.getCheekLevel() < 3) {
//                    if (player.getCooldowns().isOnCooldown(itemStack.getItem())) {
//                        return InteractionResult.FAIL;
//                    }
//                    this.setCheekLevel(this.getCheekLevel() + 1);
//                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PARROT_EAT, SoundSource.NEUTRAL, 1.0F, 1.0F);
//                    player.getCooldowns().addCooldown(itemStack.getItem(), 20);
//                } else if (this.getCheekLevel() >= 3) {
//                    if (player.getCooldowns().isOnCooldown(itemStack.getItem())) {
//                        return InteractionResult.FAIL;
//                    }
//                    this.remove(RemovalReason.KILLED);
//                    this.playSound(HamstersSoundEvents.HAMSTER_ExPLODE, 1.0F, 1.0F);
//
//                    DyeColor dyeColor = Util.getRandom(DyeColor.values(), randomSource);
//                    int i = randomSource.nextInt(3);
//                    ItemStack fireworkStack = this.getFirework(dyeColor, i);
//                    FireworkRocketEntity fireworkRocketEntity = new FireworkRocketEntity(this.level(), this, this.getX(), this.getEyeY(), this.getZ(), fireworkStack);
//                    fireworkRocketEntity.setSilent(true);
//                    fireworkRocketEntity.setInvisible(true);
//                    this.level().addFreshEntity(fireworkRocketEntity);
//
//                    fireworkRocketEntity.setDeltaMovement(0, 0, 0);
//
//
//
//
//                    if (this.level() instanceof ServerLevel serverLevel) {
////                        serverLevel.sendParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0.0D, 0.0D, 0.0D, 0.0D);
//                    }
//                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.FIREWORK_ROCKET_BLAST, SoundSource.NEUTRAL, 1.0F, 1.0F);
//                }
//                return InteractionResult.SUCCESS;
//            }

            label90: {
                if (this.isTame()) {
                    if (this.isFood(itemStack) && this.getHealth() < this.getMaxHealth()) {
                        if (!player.getAbilities().instabuild) {
                            itemStack.shrink(1);
                        }

                        this.heal((float)item.getFoodProperties().getNutrition());
                        return InteractionResult.SUCCESS;
                    }

                    if (!(item instanceof DyeItem)) {
                        break label90;
                    }

                    DyeItem dyeItem = (DyeItem)item;
                    if (!this.isOwnedBy(player)) {
                        break label90;
                    }

                    DyeColor dyeColor = dyeItem.getDyeColor();
                    if (dyeColor != this.getCollarColor()) {
                        this.setCollarColor(dyeColor);
                        if (!player.getAbilities().instabuild) {
                            itemStack.shrink(1);
                        }

                        return InteractionResult.SUCCESS;
                    }
                } else if (itemStack.is(HamstersTags.HAMSTER_FOOD)) {
                    if (!player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }

                    if (this.random.nextInt(3) == 0) {
                        this.tame(player);
                        this.navigation.stop();
                        this.setTarget(null);
                        this.setOrderedToSit(true);
                        this.level().broadcastEntityEvent(this, (byte)7);
                    } else {
                        this.level().broadcastEntityEvent(this, (byte)6);
                    }

                    return InteractionResult.SUCCESS;
                }

                return super.mobInteract(player, interactionHand);
            }

            InteractionResult interactionResult = super.mobInteract(player, interactionHand);
            if ((!interactionResult.consumesAction() || this.isBaby()) && this.isOwnedBy(player)) {
                this.setOrderedToSit(!this.isOrderedToSit());
                this.jumping = false;
                this.navigation.stop();
                this.setTarget(null);
                return InteractionResult.SUCCESS;
            } else {
                return interactionResult;
            }
        }
    }


    // Spawns a "Wild" HamsterNew with no markings. These cannot be obtained from breeding.
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.populateDefaultEquipmentSlots(random, pDifficulty);
        if (pSpawnData == null) {
//            this.setVariant(HamsterNew.Variant.WILD);
//            this.setMarking(HamsterNew.Marking.BLANK.getId());
            // Make them have any variant except for wild
            this.setVariant(Variant.BY_ID[this.random.nextInt(Variant.BY_ID.length - 1)]);
            // Give them random variants
            this.setMarking(this.random.nextInt(Marking.values().length));
        }
        return pSpawnData;
    }

    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        HamsterNew hamster = HamstersEntityType.HAMSTER_NEW.create(serverLevel);
        assert hamster != null;
        // If both parents are Wild variant with no markings, the offspring will have a random color and marking.
        if (ageableMob instanceof HamsterNew hamsterParent && hamsterParent.getVariant() == Variant.WILD.getId() && hamsterParent.getMarking() == Marking.BLANK.getId()) {
            hamster.setVariant(Variant.BY_ID[this.random.nextInt(Variant.BY_ID.length)]);
            hamster.setMarking(Marking.byId(this.random.nextInt(Marking.values().length)).getId());
        }

        // If one parent is Wild, and one parent has color/marking, the offspring will have a 50% chance
        // of a random color/marking or will have the same as the parent with color/marking.
        else if (ageableMob instanceof HamsterNew hamsterParent) {
            if (hamsterParent.getVariant() == Variant.WILD.getId() && hamsterParent.getMarking() == Marking.BLANK.getId()) {
                hamster.setVariant(Variant.BY_ID[this.random.nextInt(Variant.BY_ID.length)]);
                hamster.setMarking(Marking.byId(this.random.nextInt(Marking.values().length)).getId());
            } else {
                hamster.setVariant(Variant.getTypeById(hamsterParent.getVariant()));
                hamster.setMarking(hamsterParent.getMarking());
            }
        }

        // If neither parent is Wild, and they have their own color and marking, the offspring will pick a color
        // and marking from one of the parents.
        else if (ageableMob instanceof HamsterNew hamsterParent) {
            hamster.setVariant(this.getOffspringVariant(this, hamsterParent));
            hamster.setMarking(this.getOffspringPattern(this, hamsterParent).getId());
        }
        return hamster;
    }

    private Marking getOffspringPattern(HamsterNew hamster, HamsterNew otherParent) {
        Marking marking = Marking.byId(hamster.getMarking());
        Marking otherMarking = Marking.byId(otherParent.getMarking());

        return this.random.nextBoolean() ? marking : otherMarking;
    }

    private Variant getOffspringVariant(HamsterNew hamster, HamsterNew otherParent) {
        Variant variant = Variant.getTypeById(hamster.getVariant());
        Variant otherVariant = Variant.getTypeById(otherParent.getVariant());

        return this.random.nextBoolean() ? variant : otherVariant;
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
        this.entityData.define(DATA_BOW_COLOR, 0);
        this.entityData.define(CHEEK_LEVEL, 0);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setVariant(Variant.BY_ID[compoundTag.getInt("Variant")]);
        this.setMarking(compoundTag.getInt("Marking"));
        this.setCheekLevel(compoundTag.getInt("CheekLevel"));
        this.setSquishedTicks(compoundTag.getInt("SquishedTicks"));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Variant", this.getVariant());
        compoundTag.putInt("Marking", this.getMarking());
        compoundTag.putInt("CheekLevel", this.getCheekLevel());
        compoundTag.putInt("SquishedTicks", this.getSquishedTicks());
    }

    public int getSquishedTicks() {
        return squishedTicks;
    }

    public void setSquishedTicks(int squishedTicks) {
        this.squishedTicks = squishedTicks;
    }

    public DyeColor getCollarColor() {
        return DyeColor.byId(this.entityData.get(DATA_BOW_COLOR));
    }

    public void setCollarColor(DyeColor dyeColor) {
        this.entityData.set(DATA_BOW_COLOR, dyeColor.getId());
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

    public void setVariant(Variant variant) {
        this.entityData.set(VARIANT, variant.getId());
    }

    public int getCheekLevel() {
        return this.entityData.get(CHEEK_LEVEL);
    }

    public void setCheekLevel(int cheekLevel) {
        this.entityData.set(CHEEK_LEVEL, cheekLevel);
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
        BLACK (6, "black"),
        WILD (7, "wild");

        public static final Variant[] BY_ID = Arrays.stream(values()).sorted(Comparator.comparingInt(Variant::getId)).toArray(Variant[]::new);
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

    // endregion

    // region GECKOLIB

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 0, this::animController));
    }

    protected <E extends HamsterNew> PlayState animController(final AnimationState<E> event) {
        if (this.squishedTicks > 0) {
            event.setAnimation(SQUISH);
        } else if (this.isSleeping()) {
            event.setAnimation(SLEEP);
        } else if (this.isInSittingPose()) {
            event.setAnimation(STANDING);
        }  else if (event.isMoving()) {
            if (this.isSprinting()) {
                event.setControllerSpeed(1.3F);
                event.setAnimation(RUN);
            } else {
                if (this.isBaby()) {
                    event.setControllerSpeed(1.1F);
                    event.setAnimation(PINKIE_WALK);
                } else {
                    event.setControllerSpeed(1.1F);
                    event.setAnimation(WALK);
                }
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

    public class GetOnOwnersShoulderGoal extends Goal {
        private final ShoulderRidingEntity entity;
        private ServerPlayer owner;
        private boolean isSittingOnShoulder;

        public GetOnOwnersShoulderGoal(ShoulderRidingEntity shoulderRidingEntity) {
            this.entity = shoulderRidingEntity;
        }

        public boolean canUse() {
            ServerPlayer serverPlayer = (ServerPlayer)this.entity.getOwner();
            boolean bl = serverPlayer != null && !serverPlayer.isSpectator() && !serverPlayer.isInWater() && !serverPlayer.isInPowderSnow;
            return !this.entity.isOrderedToSit() && bl && this.entity.canSitOnShoulder();
        }

        public boolean isInterruptable() {
            return !this.isSittingOnShoulder;
        }

        public void start() {
            this.owner = (ServerPlayer)this.entity.getOwner();
            this.isSittingOnShoulder = false;
        }

        public void tick() {
            if (!this.isSittingOnShoulder && !this.entity.isInSittingPose() && !this.entity.isLeashed()) {
                if (this.entity.getBoundingBox().intersects(this.owner.getBoundingBox())) {
                    this.isSittingOnShoulder = this.entity.setEntityOnShoulder(this.owner);
                }

            }
        }
    }
}

package com.starfish_studios.hamsters.entity.common;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FleeSunGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

//TODO for some reason there's a de-sync issue between client and server when it comes to sleeping
public class HamstersAnimal extends Animal {
    private static final EntityDataAccessor<Boolean> IS_SLEEPING = SynchedEntityData.defineId(HamstersAnimal.class, EntityDataSerializers.BOOLEAN);
    private int cantSleepCooldown;

    protected HamstersAnimal(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        //this.goalSelector.addGoal(4, new SeekShelterGoal(1.25));
        //this.goalSelector.addGoal(3, new SleepGoal());
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() && this.isSleeping();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_SLEEPING, this.random.nextFloat() > 0.9);
    }

    @Override
    public void aiStep() {
        if (cantSleepCooldown > 0) cantSleepCooldown--;
        super.aiStep();
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {

        setIsSleeping(false);
        cantSleepCooldown = 200;

        return super.hurt(damageSource, f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("IsSleeping", this.getIsSleeping());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setIsSleeping(compoundTag.getBoolean("IsSleeping"));
    }

    public boolean getIsSleeping() {
        return this.entityData.get(IS_SLEEPING);
    }

    public void setIsSleeping(boolean bl) {
        this.entityData.set(IS_SLEEPING, bl);
    }

    
    class SleepGoal extends Goal {
        private static final int WAIT_TIME_BEFORE_SLEEP = HamstersAnimal.SleepGoal.reducedTickDelay(140);
        private int countdown;

        public SleepGoal() {
            this.countdown = HamstersAnimal.this.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        }

        @Override
        public boolean canUse() {
            if (HamstersAnimal.this.xxa != 0.0f || HamstersAnimal.this.yya != 0.0f || HamstersAnimal.this.zza != 0.0f) {
                return false;
            }
            return this.canSleep() || HamstersAnimal.this.isSleeping();
        }

        @Override
        public boolean canContinueToUse() {
            return this.canSleep();
        }


        protected boolean hasShelter() {
            BlockPos blockPos = BlockPos.containing(HamstersAnimal.this.getX(), HamstersAnimal.this.getBoundingBox().maxY, HamstersAnimal.this.getZ());
            return HamstersAnimal.this.level().getBrightness(LightLayer.SKY, blockPos) < 15 && HamstersAnimal.this.getWalkTargetValue(blockPos) >= 0.0f;
        }
        
        private boolean canSleep() {
            if (this.countdown > 0) {
                --this.countdown;
                return false;
            }
            return HamstersAnimal.this.level().isNight() && this.hasShelter() && !HamstersAnimal.this.isInPowderSnow && HamstersAnimal.this.cantSleepCooldown == 0;
        }

        @Override
        public void stop() {
            this.countdown = HamstersAnimal.this.random.nextInt(WAIT_TIME_BEFORE_SLEEP);
            HamstersAnimal.this.setIsSleeping(false);
        }

        @Override
        public void start() {
            HamstersAnimal.this.setJumping(false);
            HamstersAnimal.this.setIsSleeping(true);
            HamstersAnimal.this.getNavigation().stop();
            HamstersAnimal.this.getMoveControl().setWantedPosition(HamstersAnimal.this.getX(), HamstersAnimal.this.getY(), HamstersAnimal.this.getZ(), 0.0);
        }
    }
}

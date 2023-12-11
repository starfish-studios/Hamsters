package com.starfish_studios.hamsters.entity.ai.goals;

import com.starfish_studios.hamsters.entity.common.HamstersAnimalBehavior;
import com.starfish_studios.hamsters.entity.common.BehaviorHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import software.bernie.geckolib.animatable.GeoEntity;

//This goal is the base of animal idle animations and behaviors, it gets information about the current existent behaviors in HamstersAnimalBehavior and executes it accordingly

public class AbstractAnimalBehaviorGoal extends Goal {
    HamstersAnimalBehavior behavior;
    LivingEntity mob;
    int timer;

    public AbstractAnimalBehaviorGoal(LivingEntity mob, HamstersAnimalBehavior behavior) {
        this.mob = mob;
        this.behavior = behavior;
    }


    @Override
    public boolean canUse() {
        if (mob instanceof BehaviorHolder holder) return holder.getBehavior() == HamstersAnimalBehavior.IDLE.getName() && !mob.isInWater() && mob.onGround();
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return timer > 0 && !mob.isInWater() && mob.onGround();
    }

    @Override
    public void start() {
        super.start();
        if (mob instanceof BehaviorHolder holder) {
            timer = behavior.getLength();
            mob.playSound(behavior.getSound(), 1.0f, mob.getVoicePitch());
            holder.setBehavior(behavior.getName());
            if (mob instanceof GeoEntity geoEntity) {
                geoEntity.triggerAnim(behavior.getControllerName(), behavior.getAnimationName());
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        timer--;
    }

    @Override
    public void stop() {
        super.stop();

        if (mob instanceof BehaviorHolder holder) {
            holder.setBehavior(HamstersAnimalBehavior.IDLE.getName());
        }
    }
}
package com.starfish_studios.hamsters.entity.common;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public enum HamstersAnimalBehavior {
    IDLE("Idling",SoundEvents.EMPTY, 0, "controller", "idle"),
    PECK("Pecking", SoundEvents.EMPTY, 22, "controller", "peck");
    private final String name;
    private final SoundEvent sound;
    private final int length;
    private final String controllerName;
    private final String animationName;

    HamstersAnimalBehavior(String name, SoundEvent sound, int length, String controllerName, String animationName) {
        this.name = name;
        this.sound = sound;
        this.length = length;
        this.controllerName = controllerName;
        this.animationName = animationName;
    }

    public String getName() {
        return name;
    }
    public SoundEvent getSound() {
        return sound;
    }
    public int getLength() {
        return length;
    }
    public String getControllerName() {
        return controllerName;
    }
    public String getAnimationName() {
        return animationName;
    }
}

package com.starfish_studios.hamsters.registry;

import com.starfish_studios.hamsters.Hamsters;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.SoundType;

public interface HamstersSoundEvents {



    SoundEvent HAMSTER_AMBIENT = register("entity.hamster.ambient");
    SoundEvent HAMSTER_HURT = register("entity.hamster.hurt");
    SoundEvent HAMSTER_DEATH = register("entity.hamster.death");
    SoundEvent HAMSTER_BEG = register("entity.hamster.beg");
    SoundEvent HAMSTER_SLEEP = register("entity.hamster.sleep");


    private static SoundType register(String name, float volume, float pitch) {
        return new SoundType(volume, pitch, register("block." + name + ".break"), register("block." + name + ".step"), register("block." + name + ".place"), register("block." + name + ".hit"), register("block." + name + ".fall"));
    }

    static SoundEvent register(String name) {
        ResourceLocation id = new ResourceLocation(Hamsters.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }
}

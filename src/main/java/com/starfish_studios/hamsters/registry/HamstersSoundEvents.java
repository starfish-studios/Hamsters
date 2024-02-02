package com.starfish_studios.hamsters.registry;

import com.starfish_studios.hamsters.Hamsters;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Hamsters.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HamstersSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Hamsters.MOD_ID);

    public static final RegistryObject<SoundEvent> HAMSTER_AMBIENT = register("entity.hamster.ambient");
    public static final RegistryObject<SoundEvent> HAMSTER_HURT = register("entity.hamster.hurt");
    public static final RegistryObject<SoundEvent> HAMSTER_DEATH = register("entity.hamster.death");
    public static final RegistryObject<SoundEvent> HAMSTER_BEG = register("entity.hamster.beg");
    public static final RegistryObject<SoundEvent> HAMSTER_SLEEP = register("entity.hamster.sleep");

    private static RegistryObject<SoundEvent> register(String id) {
        return SOUND_EVENTS.register(id, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Hamsters.MOD_ID, id)));
    }
}

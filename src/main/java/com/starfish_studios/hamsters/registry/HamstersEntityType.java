package com.starfish_studios.hamsters.registry;

import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.entity.Hamster;
import com.starfish_studios.hamsters.entity.SeatEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Hamsters.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HamstersEntityType {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Hamsters.MOD_ID);

    public static final RegistryObject<EntityType<Hamster>> HAMSTER = ENTITY_TYPES.register(
            "hamster", () ->
            EntityType.Builder.of(Hamster::new, MobCategory.CREATURE)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(Hamsters.MOD_ID, "hamster").toString())
    );

    public static final RegistryObject<EntityType<SeatEntity>> SEAT = ENTITY_TYPES.register(
            "seat", () ->
                    EntityType.Builder.<SeatEntity>of(SeatEntity::new, MobCategory.MISC)
                            .sized(0.0F, 0.0F)
                            .build(new ResourceLocation(Hamsters.MOD_ID, "seat").toString()));

}

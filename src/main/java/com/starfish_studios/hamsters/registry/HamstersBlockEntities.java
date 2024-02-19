package com.starfish_studios.hamsters.registry;

import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.block.entity.HamsterWheelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.starfish_studios.hamsters.Hamsters.MOD_ID;

@Mod.EventBusSubscriber(modid = Hamsters.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HamstersBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID);

    public static final RegistryObject<BlockEntityType<HamsterWheelBlockEntity>> HAMSTER_WHEEL = BLOCK_ENTITY_TYPES.register(
            "hamsters",
            () -> BlockEntityType.Builder.of(HamsterWheelBlockEntity::new, HamstersBlocks.HAMSTER_WHEEL.get()).build(null)
    );

}

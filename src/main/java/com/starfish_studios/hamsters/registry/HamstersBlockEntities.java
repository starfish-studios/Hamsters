package com.starfish_studios.hamsters.registry;

import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.block.entity.HamsterWheelBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

import static com.starfish_studios.hamsters.Hamsters.MOD_ID;

public class HamstersBlockEntities {

    public static final BlockEntityType<HamsterWheelBlockEntity> HAMSTER_WHEEL = Registry.register
            (BuiltInRegistries.BLOCK_ENTITY_TYPE, Hamsters.MOD_ID + ":hamsters",
            FabricBlockEntityTypeBuilder.create(HamsterWheelBlockEntity::new, HamstersBlocks.HAMSTER_WHEEL)
                    .build(null)
            );

}

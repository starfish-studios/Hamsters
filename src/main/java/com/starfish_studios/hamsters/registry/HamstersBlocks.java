package com.starfish_studios.hamsters.registry;

import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.block.HamsterWheelBlock;
import com.starfish_studios.hamsters.block.TunnelBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class HamstersBlocks {

    // public static final Block TUNNEL = register("tunnel", new TunnelBlock(FabricBlockSettings.copyOf(Blocks.GREEN_STAINED_GLASS)));

    public static final Block HAMSTER_WHEEL = register("hamster_wheel", new HamsterWheelBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false)));


    private static Block register(String id, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Hamsters.MOD_ID, id), block);
    }
}

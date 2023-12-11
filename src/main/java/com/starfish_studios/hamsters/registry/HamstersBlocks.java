package com.starfish_studios.hamsters.registry;

import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.block.TunnelBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;

public class HamstersBlocks {

    public static final Block TUNNEL = register("tunnel", new TunnelBlock(FabricBlockSettings.copyOf(Blocks.GREEN_STAINED_GLASS)));


    private static Block register(String id, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Hamsters.MOD_ID, id), block);
    }
}

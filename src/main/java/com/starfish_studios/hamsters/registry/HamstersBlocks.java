package com.starfish_studios.hamsters.registry;

import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.block.HamsterWheelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Hamsters.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class HamstersBlocks {

    // public static final Block TUNNEL = register("tunnel", new TunnelBlock(FabricBlockSettings.copyOf(Blocks.GREEN_STAINED_GLASS)));
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Hamsters.MOD_ID);

    public static final RegistryObject<Block> HAMSTER_WHEEL = BLOCKS.register("hamster_wheel", () -> new HamsterWheelBlock(BlockBehaviour.Properties.of(Material.METAL).strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false)));

}

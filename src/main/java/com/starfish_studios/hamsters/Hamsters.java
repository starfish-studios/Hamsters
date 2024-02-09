package com.starfish_studios.hamsters;

import com.starfish_studios.hamsters.registry.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib3.GeckoLib;


@Mod(Hamsters.MOD_ID)
public class Hamsters {
	public static final String MOD_ID = "hamsters";

	public static HamstersCreativeModeTab hamsterCreativeTab;

	public Hamsters() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus eventBus = MinecraftForge.EVENT_BUS;

		HamstersBlocks.BLOCKS.register(modEventBus);
		HamstersBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
		HamstersItems.ITEMS.register(modEventBus);
		HamstersEntityType.ENTITY_TYPES.register(modEventBus);
		HamstersSoundEvents.SOUND_EVENTS.register(modEventBus);

		GeckoLib.initialize();

		modEventBus.addListener(this::commonSetup);
		eventBus.register(this);

		hamsterCreativeTab = new HamstersCreativeModeTab("hamsters.tab");
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(HamstersVanillaIntegration::serverInit);
	}
}
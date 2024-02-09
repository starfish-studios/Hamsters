package com.starfish_studios.hamsters;

import com.starfish_studios.hamsters.registry.HamstersBlockEntities;
import com.starfish_studios.hamsters.registry.HamstersBlocks;
import com.starfish_studios.hamsters.registry.HamstersEntityType;
import com.starfish_studios.hamsters.registry.HamstersItems;
import com.starfish_studios.hamsters.registry.HamstersSoundEvents;
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

	public static CreativeModeTab hamsterCreativeTab;

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

		hamsterCreativeTab = new CreativeModeTab(CreativeModeTab.getGroupCountSafe(), "hamsters") {
			@Override
			public ItemStack makeIcon() {
				return new ItemStack(HamstersItems.HAMSTER_SPAWN_EGG.get());
			}
		};
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(HamstersVanillaIntegration::serverInit);
	}

}
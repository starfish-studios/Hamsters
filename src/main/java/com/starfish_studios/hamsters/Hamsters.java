package com.starfish_studios.hamsters;

import com.google.common.reflect.Reflection;
import com.starfish_studios.hamsters.registry.*;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;


public class Hamsters implements ModInitializer {
	public static final String MOD_ID = "hamsters";

	@Override
	public void onInitialize() {
		FabricLoader.getInstance().getModContainer("create"	).ifPresent(modContainer -> CreateCompat.setup());

		Reflection.initialize(
				HamstersCreativeModeTab.class,
				HamstersSoundEvents.class,
				HamstersItems.class,
				HamstersBlocks.class,
				HamstersEntityType.class,
				HamstersBlockEntities.class
		);
		HamstersVanillaIntegration.serverInit();
	}
}
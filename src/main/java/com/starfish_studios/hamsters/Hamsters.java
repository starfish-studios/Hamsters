package com.starfish_studios.hamsters;

import com.google.common.reflect.Reflection;
import com.starfish_studios.hamsters.registry.*;
import net.fabricmc.api.ModInitializer;


public class Hamsters implements ModInitializer {
	public static final String MOD_ID = "hamsters";

	@Override
	public void onInitialize() {
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
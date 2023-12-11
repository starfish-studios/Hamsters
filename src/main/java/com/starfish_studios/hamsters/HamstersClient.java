package com.starfish_studios.hamsters;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class HamstersClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		HamstersVanillaIntegration.Client.clientInit();
	}
}
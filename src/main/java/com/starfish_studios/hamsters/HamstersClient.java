package com.starfish_studios.hamsters;

import com.starfish_studios.hamsters.registry.HamstersItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class HamstersClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		HamstersVanillaIntegration.Client.clientInit();

		ItemProperties.register(HamstersItems.HAMSTER.get(), new ResourceLocation("variant"), (stack, world, entity, num) -> {
			CompoundTag compoundTag = stack.getTag();
			if (compoundTag != null && compoundTag.contains("Variant")) {
				return (float)compoundTag.getInt("Variant") / 7;
			}
			return 0.333F;
		});
	}
}
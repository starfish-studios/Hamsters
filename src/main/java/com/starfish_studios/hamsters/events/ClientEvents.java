package com.starfish_studios.hamsters.events;

import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.client.renderer.HamsterRenderer;
import com.starfish_studios.hamsters.client.renderer.HamsterWheelRenderer;
import com.starfish_studios.hamsters.client.renderer.SeatRenderer;
import com.starfish_studios.hamsters.registry.HamstersBlockEntities;
import com.starfish_studios.hamsters.registry.HamstersEntityType;
import com.starfish_studios.hamsters.registry.HamstersItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib.GeckoLib;

@Mod.EventBusSubscriber(modid = Hamsters.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void onClientSetup(final FMLClientSetupEvent event) {
        ItemProperties.register(HamstersItems.HAMSTER.get(), new ResourceLocation("variant"), (stack, world, entity, num) -> {
            CompoundTag compoundTag = stack.getTag();
            if (compoundTag != null && compoundTag.contains("Variant")) {
                return (float)compoundTag.getInt("Variant") / 7;
            }
            return 0.333F;
        });
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(HamstersBlockEntities.HAMSTER_WHEEL.get(), context -> new HamsterWheelRenderer());
        event.registerEntityRenderer(HamstersEntityType.HAMSTER.get(), HamsterRenderer::new);
        event.registerEntityRenderer(HamstersEntityType.SEAT.get(), SeatRenderer::new);
    }

}

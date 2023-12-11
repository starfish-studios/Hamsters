package com.starfish_studios.hamsters.registry;

import com.starfish_studios.hamsters.Hamsters;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;

import static com.starfish_studios.hamsters.registry.HamstersItems.*;

public class HamstersCreativeModeTab {

    @SuppressWarnings("unused")
    public static final CreativeModeTab ITEM_GROUP = register("item_group", FabricItemGroup.builder().icon(HAMSTER_SPAWN_EGG::getDefaultInstance).title(Component.translatable("itemGroup.hamsters.tab")).displayItems((featureFlagSet, output) -> {


                output.accept(HAMSTER_SPAWN_EGG);
                output.accept(TUNNEL);

            }).build()
    );

    private static CreativeModeTab register(String id, CreativeModeTab tab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(Hamsters.MOD_ID, id), tab);
    }
}

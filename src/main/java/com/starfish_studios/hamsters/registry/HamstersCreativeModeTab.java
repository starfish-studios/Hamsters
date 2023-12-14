package com.starfish_studios.hamsters.registry;

import com.starfish_studios.hamsters.Hamsters;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import static com.starfish_studios.hamsters.registry.HamstersBlocks.HAMSTER_WHEEL;
import static com.starfish_studios.hamsters.registry.HamstersItems.*;

public class HamstersCreativeModeTab {

    @SuppressWarnings("unused")
    public static final CreativeModeTab ITEM_GROUP = register("item_group", FabricItemGroup.builder().icon(HAMSTER_SPAWN_EGG::getDefaultInstance).title(Component.translatable("itemGroup.hamsters.tab")).displayItems((featureFlagSet, output) -> {

        output.accept(TUNNEL);
        output.accept(HamstersItems.HAMSTER_WHEEL);


        output.accept(HAMSTER_SPAWN_EGG);

        // TODO: Not very pretty right now. Maybe there's a better way to do this?
        
        ItemStack item0 = new ItemStack(HAMSTER.get());
        item0.getOrCreateTag().putInt("Variant", 0);
        output.accept(item0);

        ItemStack item1 = new ItemStack(HAMSTER.get());
        item1.getOrCreateTag().putInt("Variant", 1);
        output.accept(item1);
        
        ItemStack item2 = new ItemStack(HAMSTER.get());
        item2.getOrCreateTag().putInt("Variant", 2);
        output.accept(item2);
        
        ItemStack item3 = new ItemStack(HAMSTER.get());
        item3.getOrCreateTag().putInt("Variant", 3);
        output.accept(item3);

        ItemStack item4 = new ItemStack(HAMSTER.get());
        item4.getOrCreateTag().putInt("Variant", 4);
        output.accept(item4);

        ItemStack item5 = new ItemStack(HAMSTER.get());
        item5.getOrCreateTag().putInt("Variant", 5);
        output.accept(item5);

        ItemStack item6 = new ItemStack(HAMSTER.get());
        item6.getOrCreateTag().putInt("Variant", 6);
        output.accept(item6);



        }).build()
    );

    private static CreativeModeTab register(String id, CreativeModeTab tab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(Hamsters.MOD_ID, id), tab);
    }
}

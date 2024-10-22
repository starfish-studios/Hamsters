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

import static com.starfish_studios.hamsters.registry.HamstersBlocks.CAGE_PANEL;
import static com.starfish_studios.hamsters.registry.HamstersBlocks.HAMSTER_WHEEL;
import static com.starfish_studios.hamsters.registry.HamstersItems.*;

public class HamstersCreativeModeTab {

    @SuppressWarnings("unused")
    public static final CreativeModeTab ITEM_GROUP = register("item_group", FabricItemGroup.builder().icon(HAMSTER_SPAWN_EGG::getDefaultInstance).title(Component.translatable("itemGroup.hamsters.tab")).displayItems((featureFlagSet, output) -> {

//        output.accept(TUNNEL);
        output.accept(HAMSTER_WHEEL);

//        // RED, ORANGE, YELLOW, LIME, GREEN, CYAN, BLUE, LIGHT BLUE, PINK, MAGENTA, PURPLE, WHITE, LIGHT GRAY, GRAY, BLACK, BROWN
        output.accept(CAGE_PANEL);
        output.accept(RED_CAGE_PANEL);
        output.accept(ORANGE_CAGE_PANEL);
        output.accept(YELLOW_CAGE_PANEL);
        output.accept(LIME_CAGE_PANEL);
        output.accept(GREEN_CAGE_PANEL);
        output.accept(CYAN_CAGE_PANEL);
        output.accept(BLUE_CAGE_PANEL);
        output.accept(LIGHT_BLUE_CAGE_PANEL);
        output.accept(PINK_CAGE_PANEL);
        output.accept(MAGENTA_CAGE_PANEL);
        output.accept(PURPLE_CAGE_PANEL);
        output.accept(WHITE_CAGE_PANEL);
        output.accept(LIGHT_GRAY_CAGE_PANEL);
        output.accept(GRAY_CAGE_PANEL);
        output.accept(BLACK_CAGE_PANEL);
        output.accept(BROWN_CAGE_PANEL);

        output.accept(RED_HAMSTER_BOWL);
        output.accept(ORANGE_HAMSTER_BOWL);
        output.accept(YELLOW_HAMSTER_BOWL);
        output.accept(LIME_HAMSTER_BOWL);
        output.accept(GREEN_HAMSTER_BOWL);
        output.accept(CYAN_HAMSTER_BOWL);
        output.accept(BLUE_HAMSTER_BOWL);
        output.accept(LIGHT_BLUE_HAMSTER_BOWL);
        output.accept(PINK_HAMSTER_BOWL);
        output.accept(MAGENTA_HAMSTER_BOWL);
        output.accept(PURPLE_HAMSTER_BOWL);
        output.accept(WHITE_HAMSTER_BOWL);
        output.accept(LIGHT_GRAY_HAMSTER_BOWL);
        output.accept(GRAY_HAMSTER_BOWL);
        output.accept(BLACK_HAMSTER_BOWL);
        output.accept(BROWN_HAMSTER_BOWL);

        output.accept(RED_HAMSTER_BOTTLE);
        output.accept(ORANGE_HAMSTER_BOTTLE);
        output.accept(YELLOW_HAMSTER_BOTTLE);
        output.accept(LIME_HAMSTER_BOTTLE);
        output.accept(GREEN_HAMSTER_BOTTLE);
        output.accept(CYAN_HAMSTER_BOTTLE);
        output.accept(BLUE_HAMSTER_BOTTLE);
        output.accept(LIGHT_BLUE_HAMSTER_BOTTLE);
        output.accept(PINK_HAMSTER_BOTTLE);
        output.accept(MAGENTA_HAMSTER_BOTTLE);
        output.accept(PURPLE_HAMSTER_BOTTLE);
        output.accept(WHITE_HAMSTER_BOTTLE);
        output.accept(LIGHT_GRAY_HAMSTER_BOTTLE);
        output.accept(GRAY_HAMSTER_BOTTLE);
        output.accept(BLACK_HAMSTER_BOTTLE);
        output.accept(BROWN_HAMSTER_BOTTLE);

        output.accept(RED_HAMSTER_BALL);
        output.accept(ORANGE_HAMSTER_BALL);
        output.accept(YELLOW_HAMSTER_BALL);
        output.accept(LIME_HAMSTER_BALL);
        output.accept(GREEN_HAMSTER_BALL);
        output.accept(CYAN_HAMSTER_BALL);
        output.accept(BLUE_HAMSTER_BALL);
        output.accept(LIGHT_BLUE_HAMSTER_BALL);
        output.accept(PINK_HAMSTER_BALL);
        output.accept(MAGENTA_HAMSTER_BALL);
        output.accept(PURPLE_HAMSTER_BALL);
        output.accept(WHITE_HAMSTER_BALL);
        output.accept(LIGHT_GRAY_HAMSTER_BALL);
        output.accept(GRAY_HAMSTER_BALL);
        output.accept(BLACK_HAMSTER_BALL);
        output.accept(BROWN_HAMSTER_BALL);


        output.accept(HAMSTER_SPAWN_EGG);
        output.accept(HAMSTER_NEW_SPAWN_EGG);
        }).build()
    );

    private static CreativeModeTab register(String id, CreativeModeTab tab) {
        return Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(Hamsters.MOD_ID, id), tab);
    }
}

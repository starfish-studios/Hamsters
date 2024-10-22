package com.starfish_studios.hamsters.registry;

import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.entity.Hamster;
import com.starfish_studios.hamsters.item.HamsterBallItem;
import com.starfish_studios.hamsters.item.HamsterItem;
import com.starfish_studios.hamsters.item.HamsterWheelItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

public class HamstersItems {

    public static class ItemProperties {
        public static FabricItemSettings hamsterBallItem = (FabricItemSettings) new FabricItemSettings().stacksTo(1);
        public static FabricItemSettings hamsterBottleItem = new FabricItemSettings();
        public static FabricItemSettings hamsterBowlItem = new FabricItemSettings();
    }

    public static final Item HAMSTER_SPAWN_EGG = register("hamster_spawn_egg", new SpawnEggItem(HamstersEntityType.HAMSTER, 16747824, 16775119, new FabricItemSettings()));
    public static final Item HAMSTER_NEW_SPAWN_EGG = register("hamster_new_spawn_egg", new SpawnEggItem(HamstersEntityType.HAMSTER_NEW, 16747824, 16775119, new FabricItemSettings()));
//    public static final Item TUNNEL = register("tunnel", new BlockItem(HamstersBlocks.TUNNEL, new FabricItemSettings()));

    public static final Item HAMSTER = register("hamster", new HamsterItem(new FabricItemSettings().stacksTo(1)));

    // RED, ORANGE, YELLOW, LIME, GREEN, CYAN, BLUE, LIGHT BLUE, PINK, MAGENTA, PURPLE, WHITE, LIGHT GRAY, GRAY, BLACK, BROWN

    public static final Item CAGE_PANEL = register("cage_panel", new BlockItem(HamstersBlocks.CAGE_PANEL, new FabricItemSettings()));
    public static final Item RED_CAGE_PANEL = register("red_cage_panel", new BlockItem(HamstersBlocks.RED_CAGE_PANEL, new FabricItemSettings()));
    public static final Item ORANGE_CAGE_PANEL = register("orange_cage_panel", new BlockItem(HamstersBlocks.ORANGE_CAGE_PANEL, new FabricItemSettings()));
    public static final Item YELLOW_CAGE_PANEL = register("yellow_cage_panel", new BlockItem(HamstersBlocks.YELLOW_CAGE_PANEL, new FabricItemSettings()));
    public static final Item LIME_CAGE_PANEL = register("lime_cage_panel", new BlockItem(HamstersBlocks.LIME_CAGE_PANEL, new FabricItemSettings()));
    public static final Item GREEN_CAGE_PANEL = register("green_cage_panel", new BlockItem(HamstersBlocks.GREEN_CAGE_PANEL, new FabricItemSettings()));
    public static final Item CYAN_CAGE_PANEL = register("cyan_cage_panel", new BlockItem(HamstersBlocks.CYAN_CAGE_PANEL, new FabricItemSettings()));
    public static final Item BLUE_CAGE_PANEL = register("blue_cage_panel", new BlockItem(HamstersBlocks.BLUE_CAGE_PANEL, new FabricItemSettings()));
    public static final Item LIGHT_BLUE_CAGE_PANEL = register("light_blue_cage_panel", new BlockItem(HamstersBlocks.LIGHT_BLUE_CAGE_PANEL, new FabricItemSettings()));
    public static final Item PINK_CAGE_PANEL = register("pink_cage_panel", new BlockItem(HamstersBlocks.PINK_CAGE_PANEL, new FabricItemSettings()));
    public static final Item MAGENTA_CAGE_PANEL = register("magenta_cage_panel", new BlockItem(HamstersBlocks.MAGENTA_CAGE_PANEL, new FabricItemSettings()));
    public static final Item PURPLE_CAGE_PANEL = register("purple_cage_panel", new BlockItem(HamstersBlocks.PURPLE_CAGE_PANEL, new FabricItemSettings()));
    public static final Item WHITE_CAGE_PANEL = register("white_cage_panel", new BlockItem(HamstersBlocks.WHITE_CAGE_PANEL, new FabricItemSettings()));
    public static final Item LIGHT_GRAY_CAGE_PANEL = register("light_gray_cage_panel", new BlockItem(HamstersBlocks.LIGHT_GRAY_CAGE_PANEL, new FabricItemSettings()));
    public static final Item GRAY_CAGE_PANEL = register("gray_cage_panel", new BlockItem(HamstersBlocks.GRAY_CAGE_PANEL, new FabricItemSettings()));
    public static final Item BLACK_CAGE_PANEL = register("black_cage_panel", new BlockItem(HamstersBlocks.BLACK_CAGE_PANEL, new FabricItemSettings()));
    public static final Item BROWN_CAGE_PANEL = register("brown_cage_panel", new BlockItem(HamstersBlocks.BROWN_CAGE_PANEL, new FabricItemSettings()));

    public static final Item RED_HAMSTER_BALL = register("red_hamster_ball", new HamsterBallItem(ItemProperties.hamsterBallItem));
    public static final Item ORANGE_HAMSTER_BALL = register("orange_hamster_ball", new HamsterBallItem(ItemProperties.hamsterBallItem));
    public static final Item YELLOW_HAMSTER_BALL = register("yellow_hamster_ball", new HamsterBallItem(ItemProperties.hamsterBallItem));
    public static final Item LIME_HAMSTER_BALL = register("lime_hamster_ball", new HamsterBallItem(ItemProperties.hamsterBallItem));
    public static final Item GREEN_HAMSTER_BALL = register("green_hamster_ball", new HamsterBallItem(ItemProperties.hamsterBallItem));
    public static final Item CYAN_HAMSTER_BALL = register("cyan_hamster_ball", new HamsterBallItem(ItemProperties.hamsterBallItem));
    public static final Item BLUE_HAMSTER_BALL = register("blue_hamster_ball", new HamsterBallItem(ItemProperties.hamsterBallItem));
    public static final Item LIGHT_BLUE_HAMSTER_BALL = register("light_blue_hamster_ball", new HamsterBallItem(ItemProperties.hamsterBallItem));
    public static final Item PINK_HAMSTER_BALL = register("pink_hamster_ball", new HamsterBallItem(ItemProperties.hamsterBallItem));
    public static final Item MAGENTA_HAMSTER_BALL = register("magenta_hamster_ball", new HamsterBallItem(ItemProperties.hamsterBallItem));
    public static final Item PURPLE_HAMSTER_BALL = register("purple_hamster_ball", new HamsterBallItem(ItemProperties.hamsterBallItem));
    public static final Item WHITE_HAMSTER_BALL = register("white_hamster_ball", new HamsterBallItem(ItemProperties.hamsterBallItem));
    public static final Item LIGHT_GRAY_HAMSTER_BALL = register("light_gray_hamster_ball", new HamsterBallItem(ItemProperties.hamsterBallItem));
    public static final Item GRAY_HAMSTER_BALL = register("gray_hamster_ball", new HamsterBallItem(ItemProperties.hamsterBallItem));
    public static final Item BLACK_HAMSTER_BALL = register("black_hamster_ball", new HamsterBallItem(ItemProperties.hamsterBallItem));
    public static final Item BROWN_HAMSTER_BALL = register("brown_hamster_ball", new HamsterBallItem(ItemProperties.hamsterBallItem));

    public static final BlockItem HAMSTER_WHEEL = (BlockItem) register("hamster_wheel", new HamsterWheelItem(HamstersBlocks.HAMSTER_WHEEL, new FabricItemSettings()));

    public static final Item RED_HAMSTER_BOTTLE = register("red_hamster_bottle", new BlockItem(HamstersBlocks.RED_HAMSTER_BOTTLE, ItemProperties.hamsterBottleItem));
    public static final Item ORANGE_HAMSTER_BOTTLE = register("orange_hamster_bottle", new BlockItem(HamstersBlocks.ORANGE_HAMSTER_BOTTLE, ItemProperties.hamsterBottleItem));
    public static final Item YELLOW_HAMSTER_BOTTLE = register("yellow_hamster_bottle", new BlockItem(HamstersBlocks.YELLOW_HAMSTER_BOTTLE, ItemProperties.hamsterBottleItem));
    public static final Item LIME_HAMSTER_BOTTLE = register("lime_hamster_bottle", new BlockItem(HamstersBlocks.LIME_HAMSTER_BOTTLE, ItemProperties.hamsterBottleItem));
    public static final Item GREEN_HAMSTER_BOTTLE = register("green_hamster_bottle", new BlockItem(HamstersBlocks.GREEN_HAMSTER_BOTTLE, ItemProperties.hamsterBottleItem));
    public static final Item CYAN_HAMSTER_BOTTLE = register("cyan_hamster_bottle", new BlockItem(HamstersBlocks.CYAN_HAMSTER_BOTTLE, ItemProperties.hamsterBottleItem));
    public static final Item BLUE_HAMSTER_BOTTLE = register("blue_hamster_bottle", new BlockItem(HamstersBlocks.BLUE_HAMSTER_BOTTLE, ItemProperties.hamsterBottleItem));
    public static final Item LIGHT_BLUE_HAMSTER_BOTTLE = register("light_blue_hamster_bottle", new BlockItem(HamstersBlocks.LIGHT_BLUE_HAMSTER_BOTTLE, ItemProperties.hamsterBottleItem));
    public static final Item PINK_HAMSTER_BOTTLE = register("pink_hamster_bottle", new BlockItem(HamstersBlocks.PINK_HAMSTER_BOTTLE, ItemProperties.hamsterBottleItem));
    public static final Item MAGENTA_HAMSTER_BOTTLE = register("magenta_hamster_bottle", new BlockItem(HamstersBlocks.MAGENTA_HAMSTER_BOTTLE, ItemProperties.hamsterBottleItem));
    public static final Item PURPLE_HAMSTER_BOTTLE = register("purple_hamster_bottle", new BlockItem(HamstersBlocks.PURPLE_HAMSTER_BOTTLE, ItemProperties.hamsterBottleItem));
    public static final Item WHITE_HAMSTER_BOTTLE = register("white_hamster_bottle", new BlockItem(HamstersBlocks.WHITE_HAMSTER_BOTTLE, ItemProperties.hamsterBottleItem));
    public static final Item LIGHT_GRAY_HAMSTER_BOTTLE = register("light_gray_hamster_bottle", new BlockItem(HamstersBlocks.LIGHT_GRAY_HAMSTER_BOTTLE, ItemProperties.hamsterBottleItem));
    public static final Item GRAY_HAMSTER_BOTTLE = register("gray_hamster_bottle", new BlockItem(HamstersBlocks.GRAY_HAMSTER_BOTTLE, ItemProperties.hamsterBottleItem));
    public static final Item BLACK_HAMSTER_BOTTLE = register("black_hamster_bottle", new BlockItem(HamstersBlocks.BLACK_HAMSTER_BOTTLE, ItemProperties.hamsterBottleItem));
    public static final Item BROWN_HAMSTER_BOTTLE = register("brown_hamster_bottle", new BlockItem(HamstersBlocks.BROWN_HAMSTER_BOTTLE, ItemProperties.hamsterBottleItem));

    public static final Item RED_HAMSTER_BOWL = register("red_hamster_bowl", new BlockItem(HamstersBlocks.RED_HAMSTER_BOWL, ItemProperties.hamsterBowlItem));
    public static final Item ORANGE_HAMSTER_BOWL = register("orange_hamster_bowl", new BlockItem(HamstersBlocks.ORANGE_HAMSTER_BOWL, ItemProperties.hamsterBowlItem));
    public static final Item YELLOW_HAMSTER_BOWL = register("yellow_hamster_bowl", new BlockItem(HamstersBlocks.YELLOW_HAMSTER_BOWL, ItemProperties.hamsterBowlItem));
    public static final Item LIME_HAMSTER_BOWL = register("lime_hamster_bowl", new BlockItem(HamstersBlocks.LIME_HAMSTER_BOWL, ItemProperties.hamsterBowlItem));
    public static final Item GREEN_HAMSTER_BOWL = register("green_hamster_bowl", new BlockItem(HamstersBlocks.GREEN_HAMSTER_BOWL, ItemProperties.hamsterBowlItem));
    public static final Item CYAN_HAMSTER_BOWL = register("cyan_hamster_bowl", new BlockItem(HamstersBlocks.CYAN_HAMSTER_BOWL, ItemProperties.hamsterBowlItem));
    public static final Item BLUE_HAMSTER_BOWL = register("blue_hamster_bowl", new BlockItem(HamstersBlocks.BLUE_HAMSTER_BOWL, ItemProperties.hamsterBowlItem));
    public static final Item LIGHT_BLUE_HAMSTER_BOWL = register("light_blue_hamster_bowl", new BlockItem(HamstersBlocks.LIGHT_BLUE_HAMSTER_BOWL, ItemProperties.hamsterBowlItem));
    public static final Item PINK_HAMSTER_BOWL = register("pink_hamster_bowl", new BlockItem(HamstersBlocks.PINK_HAMSTER_BOWL, ItemProperties.hamsterBowlItem));
    public static final Item MAGENTA_HAMSTER_BOWL = register("magenta_hamster_bowl", new BlockItem(HamstersBlocks.MAGENTA_HAMSTER_BOWL, ItemProperties.hamsterBowlItem));
    public static final Item PURPLE_HAMSTER_BOWL = register("purple_hamster_bowl", new BlockItem(HamstersBlocks.PURPLE_HAMSTER_BOWL, ItemProperties.hamsterBowlItem));
    public static final Item WHITE_HAMSTER_BOWL = register("white_hamster_bowl", new BlockItem(HamstersBlocks.WHITE_HAMSTER_BOWL, ItemProperties.hamsterBowlItem));
    public static final Item LIGHT_GRAY_HAMSTER_BOWL = register("light_gray_hamster_bowl", new BlockItem(HamstersBlocks.LIGHT_GRAY_HAMSTER_BOWL, ItemProperties.hamsterBowlItem));
    public static final Item GRAY_HAMSTER_BOWL = register("gray_hamster_bowl", new BlockItem(HamstersBlocks.GRAY_HAMSTER_BOWL, ItemProperties.hamsterBowlItem));
    public static final Item BLACK_HAMSTER_BOWL = register("black_hamster_bowl", new BlockItem(HamstersBlocks.BLACK_HAMSTER_BOWL, ItemProperties.hamsterBowlItem));
    public static final Item BROWN_HAMSTER_BOWL = register("brown_hamster_bowl", new BlockItem(HamstersBlocks.BROWN_HAMSTER_BOWL, ItemProperties.hamsterBowlItem));

    //public static Supplier<Item> registerCaughtMobItem(String name, EntityType entitySupplier, Supplier<? extends Fluid> fluidSupplier, SoundEvent soundSupplier, int variantAmount) {
    //    return registerItem(name, () ->  new HamsterItem(entitySupplier, fluidSupplier.get(), soundSupplier, variantAmount, new Item.Properties().stacksTo(1)));
    //}

    public static <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        T registry = Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Hamsters.MOD_ID, name), item.get());
        return () -> registry;
    }

    private static Item register(String id, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Hamsters.MOD_ID, id), item);
    }
}

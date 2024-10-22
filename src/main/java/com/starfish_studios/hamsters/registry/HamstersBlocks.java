package com.starfish_studios.hamsters.registry;

import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.material.PushReaction;

public class HamstersBlocks {

    public static class BlockProperties {
        public static FabricBlockSettings cagePanel = (FabricBlockSettings) FabricBlockSettings.create().strength(0.3F).noOcclusion().isSuffocating((state, world, pos) -> false);
    }

//     public static final Block TUNNEL = register("tunnel", new TunnelBlock(BlockProperties.cagePanel));

    public static final Block HAMSTER_WHEEL = register("hamster_wheel", new HamsterWheelBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.IGNORE)));

    // RED, ORANGE, YELLOW, LIME, GREEN, CYAN, BLUE, LIGHT BLUE, PINK, MAGENTA, PURPLE, WHITE, LIGHT GRAY, GRAY, BLACK, BROWN
    public static final Block CAGE_PANEL = register("cage_panel", new CagePanelBlock(BlockProperties.cagePanel));
    public static final Block RED_CAGE_PANEL = register("red_cage_panel", new CagePanelBlock(BlockProperties.cagePanel));
    public static final Block ORANGE_CAGE_PANEL = register("orange_cage_panel", new CagePanelBlock(BlockProperties.cagePanel));
    public static final Block YELLOW_CAGE_PANEL = register("yellow_cage_panel", new CagePanelBlock(BlockProperties.cagePanel));
    public static final Block LIME_CAGE_PANEL = register("lime_cage_panel", new CagePanelBlock(BlockProperties.cagePanel));
    public static final Block GREEN_CAGE_PANEL = register("green_cage_panel", new CagePanelBlock(BlockProperties.cagePanel));
    public static final Block CYAN_CAGE_PANEL = register("cyan_cage_panel", new CagePanelBlock(BlockProperties.cagePanel));
    public static final Block BLUE_CAGE_PANEL = register("blue_cage_panel", new CagePanelBlock(BlockProperties.cagePanel));
    public static final Block LIGHT_BLUE_CAGE_PANEL = register("light_blue_cage_panel", new CagePanelBlock(BlockProperties.cagePanel));
    public static final Block PINK_CAGE_PANEL = register("pink_cage_panel", new CagePanelBlock(BlockProperties.cagePanel));
    public static final Block MAGENTA_CAGE_PANEL = register("magenta_cage_panel", new CagePanelBlock(BlockProperties.cagePanel));
    public static final Block PURPLE_CAGE_PANEL = register("purple_cage_panel", new CagePanelBlock(BlockProperties.cagePanel));
    public static final Block WHITE_CAGE_PANEL = register("white_cage_panel", new CagePanelBlock(BlockProperties.cagePanel));
    public static final Block LIGHT_GRAY_CAGE_PANEL = register("light_gray_cage_panel", new CagePanelBlock(BlockProperties.cagePanel));
    public static final Block GRAY_CAGE_PANEL = register("gray_cage_panel", new CagePanelBlock(BlockProperties.cagePanel));
    public static final Block BLACK_CAGE_PANEL = register("black_cage_panel", new CagePanelBlock(BlockProperties.cagePanel));
    public static final Block BROWN_CAGE_PANEL = register("brown_cage_panel", new CagePanelBlock(BlockProperties.cagePanel));

    public static final Block RED_HAMSTER_BOWL = register("red_hamster_bowl", new BowlBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block ORANGE_HAMSTER_BOWL = register("orange_hamster_bowl", new BowlBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block YELLOW_HAMSTER_BOWL = register("yellow_hamster_bowl", new BowlBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block LIME_HAMSTER_BOWL = register("lime_hamster_bowl", new BowlBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block GREEN_HAMSTER_BOWL = register("green_hamster_bowl", new BowlBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block CYAN_HAMSTER_BOWL = register("cyan_hamster_bowl", new BowlBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block BLUE_HAMSTER_BOWL = register("blue_hamster_bowl", new BowlBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block LIGHT_BLUE_HAMSTER_BOWL = register("light_blue_hamster_bowl", new BowlBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block PINK_HAMSTER_BOWL = register("pink_hamster_bowl", new BowlBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block MAGENTA_HAMSTER_BOWL = register("magenta_hamster_bowl", new BowlBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block PURPLE_HAMSTER_BOWL = register("purple_hamster_bowl", new BowlBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block WHITE_HAMSTER_BOWL = register("white_hamster_bowl", new BowlBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block LIGHT_GRAY_HAMSTER_BOWL = register("light_gray_hamster_bowl", new BowlBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block GRAY_HAMSTER_BOWL = register("gray_hamster_bowl", new BowlBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block BLACK_HAMSTER_BOWL = register("black_hamster_bowl", new BowlBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block BROWN_HAMSTER_BOWL = register("brown_hamster_bowl", new BowlBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));

    public static final Block RED_HAMSTER_BOTTLE = register("red_hamster_bottle", new BottleBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block ORANGE_HAMSTER_BOTTLE = register("orange_hamster_bottle", new BottleBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block YELLOW_HAMSTER_BOTTLE = register("yellow_hamster_bottle", new BottleBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block LIME_HAMSTER_BOTTLE = register("lime_hamster_bottle", new BottleBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block GREEN_HAMSTER_BOTTLE = register("green_hamster_bottle", new BottleBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block CYAN_HAMSTER_BOTTLE = register("cyan_hamster_bottle", new BottleBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block BLUE_HAMSTER_BOTTLE = register("blue_hamster_bottle", new BottleBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block LIGHT_BLUE_HAMSTER_BOTTLE = register("light_blue_hamster_bottle", new BottleBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block PINK_HAMSTER_BOTTLE = register("pink_hamster_bottle", new BottleBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block MAGENTA_HAMSTER_BOTTLE = register("magenta_hamster_bottle", new BottleBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block PURPLE_HAMSTER_BOTTLE = register("purple_hamster_bottle", new BottleBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block WHITE_HAMSTER_BOTTLE = register("white_hamster_bottle", new BottleBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block LIGHT_GRAY_HAMSTER_BOTTLE = register("light_gray_hamster_bottle", new BottleBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block GRAY_HAMSTER_BOTTLE = register("gray_hamster_bottle", new BottleBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block BLACK_HAMSTER_BOTTLE = register("black_hamster_bottle", new BottleBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));
    public static final Block BROWN_HAMSTER_BOTTLE = register("brown_hamster_bottle", new BottleBlock(FabricBlockSettings.create().strength(0.6F).noOcclusion().isSuffocating((state, world, pos) -> false).pushReaction(PushReaction.DESTROY)));


    private static Block register(String id, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(Hamsters.MOD_ID, id), block);
    }
}

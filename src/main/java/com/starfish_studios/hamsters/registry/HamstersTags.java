package com.starfish_studios.hamsters.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import static com.starfish_studios.hamsters.Hamsters.MOD_ID;

public interface HamstersTags {

    TagKey<Block> HAMSTER_WHEELS = TagKey.create(Registries.BLOCK, new ResourceLocation(MOD_ID, "hamster_wheels"));

    TagKey<Block> HAMSTER_BLOCKS = TagKey.create(Registries.BLOCK, new ResourceLocation(MOD_ID, "hamster_blocks"));

    TagKey<Item> HAMSTER_FOOD = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "hamster_food"));

    TagKey<Biome> HAS_HAMSTER = TagKey.create(Registries.BIOME, new ResourceLocation(MOD_ID, "has_hamster"));
}

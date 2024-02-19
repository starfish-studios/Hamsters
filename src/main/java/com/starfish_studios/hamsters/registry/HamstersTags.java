package com.starfish_studios.hamsters.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;

import static com.starfish_studios.hamsters.Hamsters.MOD_ID;

public interface HamstersTags {

    TagKey<Item> HAMSTER_FOOD = TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(MOD_ID, "hamster_food"));

    TagKey<Biome> HAS_HAMSTER = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MOD_ID, "has_hamster"));
}

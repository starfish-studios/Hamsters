package com.starfish_studios.hamsters.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static com.starfish_studios.hamsters.Hamsters.MOD_ID;

public interface HamstersTags {

    //Item tags
    TagKey<Item> CHICKEN_TEMPT_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "chicken_tempt_items"));
    TagKey<Item> CHICKEN_BREED_ITEMS = TagKey.create(Registries.ITEM, new ResourceLocation(MOD_ID, "chicken_breed_items"));

    //Block tags
    //TODO make so animals prefer to sleep on this all the time
    TagKey<Block> ANIMALS_SLEEP_ON = TagKey.create(Registries.BLOCK, new ResourceLocation(MOD_ID, "animals_sleep_on"));

    //Biome tags

    //EntityType tags
    TagKey<EntityType<?>> CHICKEN_PREY = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MOD_ID, "chicken_prey"));

    //pointOfInterestType tags
    TagKey<PoiType> COOP = TagKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(MOD_ID, "coop"));

}

package com.starfish_studios.hamsters.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;

public class HamstersPoiTypes {


    public static final TagKey<PoiType> HAMSTER_WHEEL = create("hamster_wheel");

    private HamstersPoiTypes() {
    }

    private static TagKey<PoiType> create(String string) {
        return TagKey.create(Registries.POINT_OF_INTEREST_TYPE, new ResourceLocation(string));
    }
}

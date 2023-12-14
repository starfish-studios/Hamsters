package com.starfish_studios.hamsters.client.renderer;

import com.starfish_studios.hamsters.Hamsters;
import com.starfish_studios.hamsters.block.entity.HamsterWheelBlockEntity;
import com.starfish_studios.hamsters.client.model.HamsterWheelModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.example.block.entity.GeckoHabitatBlockEntity;
import software.bernie.example.client.model.block.GeckoHabitatModel;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class HamsterWheelRenderer extends GeoBlockRenderer<HamsterWheelBlockEntity> {
    public HamsterWheelRenderer() {
        super(new HamsterWheelModel());
    }
}
